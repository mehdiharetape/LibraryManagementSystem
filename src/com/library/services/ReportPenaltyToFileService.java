package com.library.services;

import com.library.repository.jdbc.ReportRepository;
import com.library.services.DTOs.PenaltyReportDTO;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class ReportPenaltyToFileService {
    private final ReportRepository reportRepository;
    public ReportPenaltyToFileService(ReportRepository reportRepository){
        this.reportRepository = reportRepository;
    }

    public boolean penaltyReportToFileService(File filePath,
                                              LocalDate fromDate, LocalDate toDate)
    {
        List<PenaltyReportDTO> penalties = reportRepository.getAllPenalties(fromDate, toDate);
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))){
            writeAllRecords(out, penalties);
            return true;
        }
        catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    private void writeRecord(PrintWriter out, PenaltyReportDTO penalty){
        out.println("loan id : " + penalty.getLoanId() + " | member : " + penalty.getMemberName()+
                " | book : " + penalty.getBookTitle() + " | expire date : " + penalty.getExpireDate()+
                " | returned at : " + penalty.getReturnDate() + " | late days : " + penalty.getLateDays()+
                " | penalty cost " + penalty.getPenaltyCost() + "$");
    }

    private void writeAllRecords(PrintWriter out, List<PenaltyReportDTO> penalties){
        out.println("Report Time : " + LocalDateTime.now());
        out.println("Number Of Records : " + penalties.size());
        out.println("--------------------------------");
        for (PenaltyReportDTO p : penalties)
            writeRecord(out, p);
    }
}
