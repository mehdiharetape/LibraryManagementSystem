package com.library.services.DTOs;

import java.time.LocalDate;

public class PenaltyReportDTO {
    private int loanId;
    private String memberName;
    private String bookTitle;
    private LocalDate expireDate;
    private LocalDate returnDate;
    private int lateDays;
    private double penaltyCost;

    public PenaltyReportDTO(int loanId, String memberName, String bookTitle,
                            LocalDate expireDate, LocalDate returnDate,
                            int lateDays, double penaltyCost)
    {
        this.loanId = loanId;
        this.memberName = memberName;
        this.bookTitle = bookTitle;
        this.expireDate = expireDate;
        this.returnDate = returnDate;
        this.lateDays = lateDays;
        this.penaltyCost = penaltyCost;
    }

    public String getMemberName() {
        return memberName;
    }

    public int getLoanId() {
        return loanId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getExpireDate() {
        return expireDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public int getLateDays() {
        return lateDays;
    }

    public double getPenaltyCost() {
        return penaltyCost;
    }
}
