package com.library.domain.entity;

import com.library.domain.enums.PayStatus;
import com.library.domain.exception.LoanNotFoundException;
import com.library.domain.exception.ValidationException;
import com.sun.source.tree.BreakTree;

import java.time.LocalDate;

public class LoanPenaltyEntity {
    private int loanId;
    private LocalDate returnDate;
    private int lateDays;
    private double penaltyCost;
    private PayStatus payStatus;

    public LoanPenaltyEntity(){}

    public LoanPenaltyEntity(int loanId, LocalDate returnDate,int lateDays,
                             double penaltyCost)
    {
        validation(loanId, returnDate, lateDays, penaltyCost);
        this.loanId = loanId;
        this.returnDate = returnDate;
        this.lateDays = lateDays;
        this.penaltyCost = penaltyCost;
    }

    //getters

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

    public PayStatus getPayStatus() {
        return payStatus;
    }


    //setters
    public void setLoanId(int loanId) {
        if(loanId <= 0)
            throw new LoanNotFoundException();
        this.loanId = loanId;
    }

    public void setReturnDate(LocalDate returnDate) {
        if(returnDate == null)
            throw new ValidationException("Invalid return date !!!");
        this.returnDate = returnDate;
    }

    public void setLateDays(int lateDays) {
        if(lateDays <= 0)
            throw new ValidationException("Late days can't be 0!!!");
        this.lateDays = lateDays;
    }

    public void setPenaltyCost(double penaltyCost) {
        if(penaltyCost <= 0)
            throw new ValidationException("no penalty for this loan!!!");
        this.penaltyCost = penaltyCost;
    }

    public void setPayStatus(PayStatus payStatus) {
        if(payStatus == null)
            throw new ValidationException("Pay Status Can't be null");
        this.payStatus = payStatus;
    }

    private void validation(int loanId, LocalDate returnDate, int lateDays,
                            double penaltyCost){
        if(loanId <= 0)
            throw new LoanNotFoundException();
        if(returnDate == null)
            throw new ValidationException("Invalid return date !!!");
        if(lateDays <= 0)
            throw new ValidationException("Late days can't be 0!!!");
        if(penaltyCost <= 0)
            throw new ValidationException("no penalty for this loan!!!");
    }
}
