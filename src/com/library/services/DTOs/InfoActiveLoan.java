package com.library.services.DTOs;

import java.util.List;

public class InfoActiveLoan {
    private int memberId;
    private String memberName;
    private List<BorrowedBook> activeLoans;

    public InfoActiveLoan(int memberId, String memberName, List<BorrowedBook> activeLoans){
        this.memberId = memberId;
        this.memberName = memberName;
        this.activeLoans = activeLoans;
    }

    public List<BorrowedBook> getActiveLoans() {
        return activeLoans;
    }

    public int getMemberId() {
        return memberId;
    }

    public String getMemberName(){
        return memberName;
    }
}
