package com.library.services;

public class PenaltyCalculationResult {
    private final int lateDays;
    private final double penaltyCost;

    public PenaltyCalculationResult(int lateDays, double penaltyCost){
        this.lateDays = lateDays;
        this.penaltyCost = penaltyCost;
    }

    public int getLateDays() {
        return lateDays;
    }

    public double getPenaltyCost() {
        return penaltyCost;
    }
}
