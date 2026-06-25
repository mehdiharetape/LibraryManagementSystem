package com.library.services;

import com.library.domain.entity.LoanBookEntity;
import com.library.domain.entity.LoanPenaltyEntity;
import com.library.domain.enums.BookStatus;
import com.library.domain.enums.LoanStatus;
import com.library.domain.exception.*;
import com.library.repository.jdbc.BookRepository;
import com.library.repository.jdbc.LoanBookRepository;
import com.library.services.DTOs.BookDTO;
import com.library.services.DTOs.LoanBookListDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanBookService {

    private final LoanBookRepository loanBookRepository;
    private final BookRepository bookRepository;
    private final int MAX_LOAN_BOOK = 2;
    private static final double PENALTY_PER_DAY = 2; //dollars

    public LoanBookService(LoanBookRepository loanBookRepository, BookRepository bookRepository)
    {
        this.loanBookRepository = loanBookRepository;
        this.bookRepository = bookRepository;
    }

    //create loan book
    public boolean createLoanBook(LoanBookEntity loanBook){
        BookDTO book = bookRepository.getBookById(loanBook.getBookId());
        checkDate(loanBook.getFromDate(),loanBook.getToDate());
        if(book == null)
            throw new BookNotFoundException();
        if(loanBookRepository.memberActiveLoans(loanBook.getMemberId()) >= MAX_LOAN_BOOK)
            throw new LoanLimitException();
        if(loanBookRepository.hasActiveLoanForBook(loanBook.getMemberId(), loanBook.getBookId())
           || !book.getBookStatus().equals(BookStatus.AVAILABLE.toString()))
        {
            throw new BookNotAvailableException();
        }
        //check fo loan
        if(loanBookRepository.availableBookNow(book.getBookID()) > 0){
            return loanBookRepository.create(new LoanBookEntity(null ,loanBook.getMemberId(),
                    loanBook.getBookId(), loanBook.getFromDate(), loanBook.getToDate(),
                    LoanStatus.ACTIVE));
        }
        else return false;
    }

    //return loan
    public boolean returnLoan(LoanBookEntity loan){
        if(loan.getStatus().equals(LoanStatus.ACTIVE)) {
            return loanBookRepository.changeLoanStatus(loan.getLoanId());
        }
        else if (loan.getStatus().equals(LoanStatus.OVERDUE)){
            var calc = calculatePenaltyCost(loan.getToDate());
            return loanBookRepository.addToPenalty(new LoanPenaltyEntity(
                    loan.getLoanId(),
                    LocalDate.now(),
                    calc.getLateDays(),
                    calc.getPenaltyCost()
            ));
        }
        else return false;
    }

    //check for loan time ended
    public String checkAndExpireLoans(){
        return loanBookRepository.updateExpiredLoans();
    }

    //get loans
    public List<LoanBookListDTO> getLoansService(){
        var loans = loanBookRepository.getAllLoans();
        if(loans == null)
            return new ArrayList<>();
        return loans;
    }

    //get loan by
    public LoanBookListDTO getLoanByIdService(int loanID){
        var loan = loanBookRepository.getLoanById(loanID);
        if(loanID < 500 || loan == null)
            throw new LoanNotFoundException();
        return loan;
    }

    //--------------------------------
    //check dates
    private void checkDate(LocalDate fromDate, LocalDate toDate)
            throws IllegalArgumentException
    {
        if(toDate.isBefore(fromDate)){
            throw new ValidationException("Loan Date can't be greater than Expire date!");
        }
        if(fromDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Loan date can't be in future!");
        }
        if(toDate.isBefore(LocalDate.now()) || toDate.equals(LocalDate.now())) {
            throw new ValidationException("Expire date can't be in past or now!");
        }
    }

    //calculate penalty cost
    public PenaltyCalculationResult calculatePenaltyCost(LocalDate expireDate){
        if(expireDate == null)
            throw new ValidationException("Expire date can't be null !!!!");
        int lateDays = LocalDate.now().getDayOfMonth() - expireDate.getDayOfMonth();
        double penaltyCost = lateDays * PENALTY_PER_DAY;
        return new  PenaltyCalculationResult(lateDays, penaltyCost);
    }
}
