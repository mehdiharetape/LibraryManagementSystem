package com.library.ui.reportUI;

import com.library.conroller.ReportController;
import com.library.services.DTOs.InfoActiveLoan;
import com.library.services.DTOs.InventoryReportDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ReportManagementPanel extends JPanel {
    private ReportController reportController;
    //table
    private JTable inventoryReportTable;
    private JTable activeLoanTable;
    private DefaultTableModel inventoryModel;
    private DefaultTableModel activeLoanModel;
    //fields
    private JTextField txtSearchField, txtMemberSearch;
    //panel
    private JPanel topPanel, bottomPanel;
    //buttons
    private JButton btnBookFile, btnMemberFile, btnRefreshInventory, btnRefreshActive;
    //file chooser
    private JFileChooser fileChooser;

    public ReportManagementPanel(ReportController reportController){
        this.reportController = reportController;

        setLayout(new GridLayout(2,1,5,5));

        //north panel-----------------------------------
        topPanel = new JPanel(new BorderLayout(5,5));
        var northPanel = new JPanel(new BorderLayout(5, 5));

        txtSearchField = new JTextField(20);
        var label_b = new JLabel("Search book by 'name' or 'ISBN'");
        northPanel.add(label_b, BorderLayout.NORTH);
        northPanel.add(txtSearchField, BorderLayout.CENTER);
        topPanel.add(northPanel, BorderLayout.NORTH);

        var btnBookPanel = new JPanel(new FlowLayout(5,5,5));
        btnBookFile = new JButton("Out To Text File");
        btnBookFile.setBackground(new Color(168, 241, 109));
        btnBookFile.addActionListener(e -> saveToTxtFile(false));

        btnRefreshInventory = new JButton("Refresh");
        btnRefreshInventory.addActionListener(e -> refreshBookTable());

        btnBookPanel.add(btnRefreshInventory);
        btnBookPanel.add(btnBookFile);
        topPanel.add(btnBookPanel, BorderLayout.SOUTH);

        //table
        createBookTablePanel();


        //south panel-------------------------------------
        bottomPanel = new JPanel(new BorderLayout(5,5));
        var southPanel = new JPanel(new BorderLayout(5, 5));

        txtMemberSearch = new JTextField(20);
        var label_a = new JLabel("Search member by 'ID' or 'Name'");
        southPanel.add(label_a, BorderLayout.NORTH);
        southPanel.add(txtMemberSearch, BorderLayout.CENTER);
        bottomPanel.add(southPanel, BorderLayout.NORTH);

        var btnMemberPanel = new JPanel(new FlowLayout(5,5,5));
        btnMemberFile = new JButton("Out To Text File");
        btnMemberFile.setBackground(new Color(168, 241, 109));
        btnMemberFile.addActionListener(e -> saveToTxtFile(true));

        btnRefreshActive = new JButton("Refresh");
        btnRefreshActive.addActionListener(e -> refreshActiveTable());

        btnMemberPanel.add(btnRefreshActive);
        btnMemberPanel.add(btnMemberFile);
        bottomPanel.add(btnMemberPanel, BorderLayout.SOUTH);


        //table
        createMemberTable();


        //add filter logic
        addFilterLogic(txtSearchField, true);
        addFilterLogic(txtMemberSearch, false);

        //save to file

        refreshBookTable();
        refreshActiveTable();


        topPanel.setBorder(BorderCreator.createBorder("Book Inventory Report"));
        bottomPanel.setBorder(BorderCreator.createBorder("Member Active Loans Report"));
        add(topPanel);
        add(bottomPanel);
    }

    //save to file
    private void saveToTxtFile(boolean isMember){
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(isMember ? "Save Active Loans" : "Save Book Inventory");

        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Text Files (*.txt)", "txt"));

        int result = fileChooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            if(!selectedFile.getName().toLowerCase().endsWith(".txt")){
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }
            if(isMember){
                reportController.handleActiveLoanToFile(selectedFile);
                JOptionPane.showMessageDialog(this,
                        "File Saved To "+selectedFile.getPath()+" Successfully");
            }
            else{
                reportController.handleInventoryToFile(selectedFile);
                JOptionPane.showMessageDialog(this,
                        "File Saved To "+selectedFile.getPath()+" Successfully");
            }
        }
    }

    //book table
    private void createBookTablePanel(){
        String[] columns = {"ID", "Book Title", "ISBN", "In shelve", "In loan", "Total", "Status"};
        inventoryModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        inventoryReportTable = new JTable(inventoryModel);
        JScrollPane scrollPane = new JScrollPane(inventoryReportTable);
        topPanel.add(scrollPane, BorderLayout.CENTER);
    }

    //member table
    private void createMemberTable(){
        String[] columns = {"ID", "Name", "Book List"};
        activeLoanModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        activeLoanTable = new JTable(activeLoanModel);
        activeLoanTable.getColumnModel().getColumn(2).
                setCellRenderer(new TextAreaRenderer());
        JScrollPane scrollPane = new JScrollPane(activeLoanTable);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);
    }

    //search
    private void addFilterLogic(JTextField field, boolean isBook){
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {filter();}

            @Override
            public void removeUpdate(DocumentEvent e) {filter();}

            @Override
            public void changedUpdate(DocumentEvent e) {filter();}

            private void filter(){
                String query = field.getText().toLowerCase();
                if(isBook) filterBooks(query);
                else filterMembers(query);
            }
        });
    }

    private void filterMembers(String query){
        List<InfoActiveLoan> activeLoans = reportController.handleActiveLoanReport();
        activeLoanModel.setRowCount(0);
        for(InfoActiveLoan i : activeLoans){
            if(String.valueOf(i.getMemberId()).contains(query)|| i.getMemberName().contains(query))
            {
                Object[] row = {
                        i.getMemberId(),
                        i.getMemberName(),
                        i.getActiveLoans()
                };
                activeLoanModel.addRow(row);
            }
        }
    }

    private void filterBooks(String query){
        inventoryModel.setRowCount(0);
        List<InventoryReportDTO> inventories = reportController.handleInventoryReport();
        for(InventoryReportDTO i : inventories)
        {
            if(i.getBookName().toLowerCase().contains(query) || i.getIsbn().contains(query))
            {
                Object[] row = {
                        i.getBookId(),
                        i.getBookName(),
                        i.getIsbn(),
                        i.getInShelve(),
                        i.getInLoan(),
                        i.getTotal(),
                        i.getBookStatus()
                };
                inventoryModel.addRow(row);
            }
        }
    }

    //load data
    private void refreshActiveTable(){
        List<InfoActiveLoan> activeLoans = reportController.handleActiveLoanReport();

        activeLoanModel.setRowCount(0);
        for(InfoActiveLoan i : activeLoans){
            Object[] row = {
                    i.getMemberId(),
                    i.getMemberName(),
                    i.getActiveLoans()
            };
            activeLoanModel.addRow(row);
        }
    }

    private void refreshBookTable(){
        List<InventoryReportDTO> inventories = reportController.handleInventoryReport();

        //clear table data
        inventoryModel.setRowCount(0);
        for (InventoryReportDTO i : inventories){
            Object[] row = {
                    i.getBookId(),
                    i.getBookName(),
                    i.getIsbn(),
                    i.getInShelve(),
                    i.getInLoan(),
                    i.getTotal(),
                    i.getBookStatus()
            };
            inventoryModel.addRow(row);
        }
    }
}
