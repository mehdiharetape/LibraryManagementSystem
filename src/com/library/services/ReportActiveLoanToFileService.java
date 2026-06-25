package com.library.services;

import com.library.repository.jdbc.MemberRepository;
import com.library.repository.jdbc.ReportRepository;
import com.library.services.DTOs.BorrowedBook;
import com.library.services.DTOs.InfoActiveLoan;

import java.io.*;
import java.util.List;

public class ReportActiveLoanToFileService {

    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;

    public ReportActiveLoanToFileService(ReportRepository reportRepository,
                                        MemberRepository memberRepository)
    {
        this.reportRepository = reportRepository;
        this.memberRepository = memberRepository;
    }

    public void ActiveLoanToFileService(File filePath){
        try (var out = new PrintWriter(new BufferedWriter(new FileWriter(filePath)))){
            writeAllRecords(out);
        }
        catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }


    //write all records
    private void writeAllRecords(PrintWriter out) throws IOException {
        List<InfoActiveLoan> activeLoans = reportRepository.memberLoanList();
        for (InfoActiveLoan a : activeLoans){
            out.println("=============================");
            out.println("Member : '" + a.getMemberName() + "' With ID : " + a.getMemberId());
            if (a.getActiveLoans().isEmpty())
                out.println("There is no loaned book !!!");
            else {
                for (BorrowedBook b : a.getActiveLoans())
                    out.println(b.toString());
            }
        }
    }
}
