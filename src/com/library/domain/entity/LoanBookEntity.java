package com.library.domain.entity;

import com.library.domain.enums.LoanStatus;
import com.library.domain.exception.BookNotFoundException;
import com.library.domain.exception.MemberNotFoundException;
import com.library.domain.exception.ValidationException;

import java.time.LocalDate;

public class LoanBookEntity {
    private Integer loanId;
    private int memberId;
    private int bookId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LoanStatus status;

    public LoanBookEntity(){}

    public LoanBookEntity(Integer loanId ,int memberId, int bookId, LocalDate fromDate,
                          LocalDate toDate, LoanStatus status)
    {
        if(loanId != null && loanId < 500)
            throw new IllegalArgumentException("Invalid Loan Id !!!");
        this.loanId = loanId;
        validation(memberId, bookId, fromDate, toDate, status);
        this.memberId = memberId;
        this.bookId = bookId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
    }

    //--------getters------------
    public Integer getLoanId(){
        return loanId;
    }
    public int getMemberId() {
        return memberId;
    }

    public int getBookId() {
        return bookId;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public LoanStatus getStatus() {
        return status;
    }

    //----------getters---------------


    public void setLoanId(Integer loanId) {
        if(loanId != null && loanId < 500)
            throw new IllegalArgumentException("Invalid Loan Id !!!");
        this.loanId = loanId;
    }

    public void setMemberId(int memberId) {
        if(memberId < 3000)
            throw new MemberNotFoundException();
        this.memberId = memberId;
    }

    public void setBookId(int bookId) {
        if(bookId < 300)
            throw new BookNotFoundException();
        this.bookId = bookId;
    }

    public void setFromDate(LocalDate fromDate) {
        if(fromDate == null)
            throw new ValidationException("Invalid loan date !!!");
        this.fromDate = fromDate;
    }

    public void setToDate(LocalDate toDate) {
        if(toDate == null)
            throw new ValidationException("Invalid expire date !!!");
        this.toDate = toDate;
    }

    public void setStatus(LoanStatus status) {
        if(status == null)
            throw new ValidationException("Invalid status !!!");
        this.status = status;
    }

    public void validation(int memberId, int bookId, LocalDate fromDate,
                           LocalDate toDate, LoanStatus status)
    {
        if(memberId < 3000)
            throw new MemberNotFoundException();
        if(bookId < 300)
            throw new BookNotFoundException();
        if(fromDate == null)
            throw new ValidationException("Invalid loan date !!!");
        if(toDate == null)
            throw new ValidationException("Invalid expire date !!!");
        if(status == null)
            throw new ValidationException("Invalid status !!!");
    }
}
