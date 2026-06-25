package com.library.conroller;

import com.library.domain.enums.ReportType;
import com.library.services.DTOs.InfoActiveLoan;
import com.library.services.DTOs.InventoryReportDTO;
import com.library.services.DTOs.PenaltyReportDTO;
import com.library.services.ReportActiveLoanToFileService;
import com.library.services.ReportInventoryToFileService;
import com.library.services.ReportPenaltyToFileService;
import com.library.services.ReportShowService;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ReportController {
    private final ReportShowService reportShowService;
    private final ReportInventoryToFileService ritf;
    private final ReportActiveLoanToFileService ratf;
    private final ReportPenaltyToFileService rptf;

    public ReportController(ReportShowService reportShowService,
                            ReportInventoryToFileService ritf,
                            ReportActiveLoanToFileService ratf,
                            ReportPenaltyToFileService rptf)
    {
        this.reportShowService = reportShowService;
        this.ritf = ritf;
        this.ratf = ratf;
        this.rptf = rptf;
    }

    //---------------show reports------------------
    //Inventory Report
    public List<InventoryReportDTO> handleInventoryReport(){
        return reportShowService.inventoryReportShow();
    }

    //Active loans
    public List<InfoActiveLoan> handleActiveLoanReport(){
        return reportShowService.memberActiveLoanShow();
    }

    //penalty
    public List<PenaltyReportDTO> handlePenaltyReport(LocalDate fromDate, LocalDate toDate){
        return reportShowService.penaltyReportShow(fromDate, toDate);
    }

    //-----------------to file reports----------------
    public boolean handleInventoryToFile(File filePath){
        return ritf.inventoryReportToFileService(filePath);
    }

    public void handleActiveLoanToFile(File filePath){
        ratf.ActiveLoanToFileService(filePath);
    }
    public void handlePenaltyToFile(File filPath, LocalDate fromDate, LocalDate toDate){
        rptf.penaltyReportToFileService(filPath, fromDate, toDate);
    }
}
