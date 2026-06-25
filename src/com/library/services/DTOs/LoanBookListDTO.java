package com.library.services.DTOs;

import com.library.domain.enums.LoanStatus;

import java.time.LocalDate;

public class LoanBookListDTO {
    private Integer loanId;
    private int memberId;
    private String memberName;
    private int bookId;
    private String title;
    private String bookStatus;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LoanStatus status;

    public LoanBookListDTO(Integer loanId, int memberId, String memberName, int bookId, String title,
                           String bookStatus, LocalDate fromDate, LocalDate toDate, LoanStatus status)
    {
        this.loanId = loanId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.bookId = bookId;
        this.title = title;
        this.bookStatus = bookStatus;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getMemberName() {
        return memberName;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public int getMemberId() {
        return memberId;
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

    public String getBookStatus(){return bookStatus;}

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
