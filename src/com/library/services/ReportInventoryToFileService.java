package com.library.services;

import com.library.repository.jdbc.ReportRepository;
import com.library.services.DTOs.InventoryReportDTO;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class ReportInventoryToFileService {
    private final ReportRepository reportRepository;

    public ReportInventoryToFileService(ReportRepository reportRepository)
    {
        this.reportRepository = reportRepository;
    }

    //Inventory Report
    public boolean inventoryReportToFileService(File filePath)
    {
        List<InventoryReportDTO> inventories = reportRepository.inventoryReport();
        try (var out = new PrintWriter(new BufferedWriter(new FileWriter(filePath))))
        {
            writeAllRecords(inventories, out);
            return true;
        }
        catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //........................................................
    //write one InventoryReportDTO record
    private void writeRecord(PrintWriter out, InventoryReportDTO inv){
        out.println("id:" + inv.getBookId() + " | title:" + inv.getBookName() +
                 " | total:" + inv.getTotal() + " | in shelve:" + inv.getInShelve() +
                 " | in loan:" + inv.getInLoan());
    }

    //write list of InventoryReportDTO
    private void writeAllRecords(List<InventoryReportDTO> i,
                        PrintWriter out) throws IOException
    {
        out.println("Report at " + LocalDateTime.now());
        for (InventoryReportDTO in : i)
            writeRecord(out, in);
    }
}
