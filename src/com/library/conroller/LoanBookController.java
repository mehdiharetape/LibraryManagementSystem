package com.library.conroller;

import com.library.domain.entity.LoanBookEntity;
import com.library.mappers.LoanMapper;
import com.library.services.DTOs.LoanBookDto;
import com.library.services.DTOs.LoanBookListDTO;
import com.library.services.LoanBookService;
import com.library.services.PenaltyCalculationResult;

import java.time.LocalDate;
import java.util.List;

public class LoanBookController {
    private final LoanBookService loanBookService;

    public LoanBookController(LoanBookService loanBookService){
        this.loanBookService = loanBookService;
    }

    //create loan book
    public boolean handleCreateLoan(LoanBookDto loanBook)
    {
        var bookAdd = LoanMapper.toEntity(loanBook);
        return loanBookService.createLoanBook(bookAdd);
    }

    //return book
    public boolean handleReturnBook(LoanBookDto loan){
        LoanBookEntity entity = LoanMapper.toEntity(loan);
        return loanBookService.returnLoan(entity);
    }

    //calculate penalty cost
    public PenaltyCalculationResult handleCalculatePenalty(LocalDate expireDate){
        return loanBookService.calculatePenaltyCost(expireDate);
    }

    //check loan expires
    public void handleCheckExpiredLoan(){
        loanBookService.checkAndExpireLoans();
    }

    //get all loans
    public List<LoanBookListDTO> handleGetAllLoans(){
        return loanBookService.getLoansService();
    }

    //get loan by id
    public LoanBookListDTO handleGetLoanById(int loanId){
        return loanBookService.getLoanByIdService(loanId);
    }
}
