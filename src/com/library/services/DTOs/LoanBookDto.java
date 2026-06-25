package com.library.services.DTOs;

import com.library.domain.enums.LoanStatus;

import java.time.LocalDate;

public class LoanBookDto {
    private Integer loanId;
    private int memberId;
    private int bookId;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LoanStatus status;

    public LoanBookDto(Integer loanId, int memberId, int bookId, LocalDate fromDate,
                       LocalDate toDate, LoanStatus status) {
        this.loanId = loanId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
    }

    public Integer getLoanId() {
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

    //setters

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }
}
