package com.library.services;

import com.library.repository.jdbc.ReportRepository;
import com.library.services.DTOs.InfoActiveLoan;
import com.library.services.DTOs.InventoryReportDTO;
import com.library.services.DTOs.LoanBookDto;
import com.library.services.DTOs.PenaltyReportDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportShowService {
    private final ReportRepository reportRepository;

    public ReportShowService(ReportRepository reportRepository){
        this.reportRepository = reportRepository;
    }

    public List<InventoryReportDTO> inventoryReportShow(){
        var data = reportRepository.inventoryReport();
        if(data == null)
            return new ArrayList<>();
        return data;
    }

    public List<InfoActiveLoan> memberActiveLoanShow(){
        var data = reportRepository.memberLoanList();
        if(data == null)
            return new ArrayList<>();
        return data;
    }

    public List<PenaltyReportDTO> penaltyReportShow(LocalDate fromDate, LocalDate toDate){
        var data = reportRepository.getAllPenalties(fromDate, toDate);
        if(data == null)
            return new ArrayList<>();
        return data;
    }
}
