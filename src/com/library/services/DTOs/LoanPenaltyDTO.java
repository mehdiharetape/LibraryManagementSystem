package com.library.services.DTOs;

import com.library.domain.enums.PayStatus;

import java.time.LocalDate;

public class LoanPenaltyDTO {
    private Integer id;
    private int loanId;
    private LocalDate returnDate;
    private int lateDays;
    private double penaltyCost;
    private String payStatus;

    public LoanPenaltyDTO(Integer id,int loanId, LocalDate returnDate, int penaltyCost, String payStatus)
    {
        this.id = id;
        this.loanId = loanId;
        this.returnDate = returnDate;
        this.penaltyCost = penaltyCost;
        this.payStatus = payStatus;
    }

    public Integer getId() {
        return id;
    }

    public int getLoanId() {
        return loanId;
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

    public String getPayStatus() {
        return payStatus;
    }
}
