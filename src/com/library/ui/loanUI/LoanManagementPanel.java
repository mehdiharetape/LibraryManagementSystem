package com.library.ui.loanUI;

import com.library.conroller.BookController;
import com.library.conroller.LoanBookController;
import com.library.conroller.MemberController;
import com.library.conroller.ReportController;
import com.library.services.DTOs.LoanBookListDTO;
import com.library.services.DTOs.PenaltyReportDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class LoanManagementPanel extends JPanel {
    //-------------------------
    private LoanBookController loanBookController;
    private ReportController reportController;
    private MemberController memberController;
    private BookController bookController;
    //-------------------------
    private JTable loanTable, penaltyTable;
    private DefaultTableModel tableModel, penaltyTableModel;
    //-------------------------
    private JButton btnAddLoan, btnRefresh, btnDetails, btnToFile, btnRefreshPenalty,
            btnFilter, btnLoanFilter;
    //-------------------------
    private JTextField searchField, searchPenaltyField,
            fromDateField, toDateField, fromLoanField, toLoanField;
    //panels
    private JPanel northPanel, southPanel;

    private JFileChooser fileChooser;

    public LoanManagementPanel(LoanBookController loanBookController,
                               ReportController reportController, MemberController memberController,
                               BookController bookController)
    {
        this.loanBookController = loanBookController;
        this.reportController = reportController;
        this.memberController = memberController;
        this.bookController = bookController;
        setLayout(new GridLayout(2,1,5,5));

        //north panel (Loans)
        northPanel = new JPanel(new BorderLayout());
        northPanel.setBorder(BorderCreator.createBorder("Loans"));

        var topLoanPanel = createTopLoanPanel();
        northPanel.add(topLoanPanel, BorderLayout.NORTH);

        createLoanTablePanel();
        createBottomNorthPanel();

        btnAddLoan.addActionListener(e -> {
            var panel = new CreateLoanPanel(memberController, bookController, loanBookController);
            panel.setVisible(true);
        });

        btnRefresh.addActionListener(e -> refreshLoanTable());
        btnDetails.addActionListener(e -> {
            var window = SwingUtilities.getWindowAncestor(this);
            int selectedLoan = loanTable.getSelectedRow();
            if(selectedLoan != -1){
                Object objectId = tableModel.getValueAt(selectedLoan, 0);
                int loanId = (int) objectId;
                var loanDetailDialog = new ViewLoanDialog((Frame) window, loanBookController, loanId);
                loanDetailDialog.setVisible(true);
                refreshLoanTable();
            }
        });
        addFilterLogic(searchField, true);

        add(northPanel, BorderLayout.NORTH);


        //south panel (Penalties)
        southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(BorderCreator.createBorder("Penalties"));
        var topPenaltyPanel = createTopPenaltyPanel();
        southPanel.add(topPenaltyPanel, BorderLayout.NORTH);

        createPenaltyTablePanel();
        createBottomSouthPanel();

        btnRefreshPenalty.addActionListener(e -> refreshPenaltyTable());

        btnFilter.addActionListener(e -> filterPenaltyDate());
        btnLoanFilter.addActionListener(e -> filterLoanDate());
        btnToFile.addActionListener(e -> saveToFile(true));

        addFilterLogic(searchPenaltyField, false);
        add(southPanel, BorderLayout.CENTER);
    }

    //loan---------------
    private JPanel createTopLoanPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //left search panel
        JPanel lefPanel = new JPanel(new BorderLayout());
        lefPanel.add(new JLabel("search (member name | book title | status | loan ID) : ")
                ,BorderLayout.WEST);
        searchField = new JTextField(30);
        lefPanel.add(searchField);
        //date panel
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightPanel.setBorder(BorderCreator.createBorder("Date Filter(Expire date)"));
        rightPanel.add(new JLabel("From Date : "));
        fromLoanField = new JTextField(10);
        rightPanel.add(fromLoanField);
        rightPanel.add(new JLabel(" - To Date : "));
        toLoanField = new JTextField(10);
        rightPanel.add(toLoanField);
        btnLoanFilter = new JButton("Filter");
        rightPanel.add(btnLoanFilter);

        panel.add(lefPanel);
        panel.add(rightPanel);
        return panel;
    }

    private void addFilterLogic(JTextField field, boolean isLoan){
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {filter();}

            @Override
            public void removeUpdate(DocumentEvent e) {filter();}

            @Override
            public void changedUpdate(DocumentEvent e) {filter();}

            private void filter(){
                String query = field.getText().toLowerCase();
                if(isLoan) filterLoan(query);
                else filterPenalty(query);
            }
        });
    }

    private void filterLoan(String query){
        tableModel.setRowCount(0);
        List<LoanBookListDTO> loans = loanBookController.handleGetAllLoans(null, null);
        for(LoanBookListDTO l : loans){
            if(String.valueOf(l.getMemberId()).contains(query) ||
                    String.valueOf(l.getLoanId()).contains(query) ||
                    l.getMemberName().toLowerCase().contains(query) ||
                    l.getTitle().toLowerCase().contains(query) ||
                    l.getStatus().toString().toLowerCase().contains(query))
            {
                Object[] row = {
                        l.getLoanId(),
                        l.getMemberId(),//1
                        l.getMemberName(),
                        l.getBookId(),//3
                        l.getTitle(),
                        l.getFromDate(),
                        l.getToDate(),
                        l.getStatus()
                };
                tableModel.addRow(row);
                hideColumn(loanTable, 1);
                hideColumn(loanTable, 3);
            }
        }
    }

    private void createLoanTablePanel(){
        String[] columns = {"Loan ID", "member ID" ,"Member", "book ID" ,
                "Book Title", "Borrow Date", "Expire Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        loanTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(loanTable);
        northPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createBottomNorthPanel(){
        var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnAddLoan = new JButton("Add New Loan");
        btnRefresh = new JButton("Refresh");
        btnDetails = new JButton("Details");

        panel.add(btnAddLoan);
        panel.add(btnRefresh);
        panel.add(btnDetails);
        northPanel.add(panel, BorderLayout.SOUTH);

        //show initial data
        refreshLoanTable();
    }

    private void refreshLoanTable(){
        List<LoanBookListDTO> loans = loanBookController.handleGetAllLoans(null, null);

        //clear table data
        tableModel.setRowCount(0);

        if(loans != null){
            for (LoanBookListDTO l : loans){
                Object[] row = {
                        l.getLoanId(),
                        l.getMemberId(),//1
                        l.getMemberName(),
                        l.getBookId(),//3
                        l.getTitle(),
                        l.getFromDate(),
                        l.getToDate(),
                        l.getStatus()
                };
                tableModel.addRow(row);
            }
            hideColumn(loanTable, 1);
            hideColumn(loanTable, 3);
        }
    }

    private void filterLoanDate(){
        String txtLoanFrom = fromLoanField.getText();
        String txtLoanTo = toLoanField.getText();
        if(txtLoanFrom == null || txtLoanFrom.isEmpty() || txtLoanTo == null ||txtLoanTo.isEmpty()){
            JOptionPane.showMessageDialog(this, "Dates Can't be empty !!!");
        }
        else {
            LocalDate fromDate = LocalDate.parse(txtLoanFrom);
            LocalDate toDate = LocalDate.parse(txtLoanTo);
            tableModel.setRowCount(0);
            List<LoanBookListDTO> loans =
                    loanBookController.handleGetAllLoans(fromDate, toDate);
            for(LoanBookListDTO l : loans){
                Object[] row = {
                        l.getLoanId(),
                        l.getMemberId(),//1
                        l.getMemberName(),
                        l.getBookId(),//3
                        l.getTitle(),
                        l.getFromDate(),
                        l.getToDate(),
                        l.getStatus()
                };
                tableModel.addRow(row);
            }
            hideColumn(loanTable, 1);
            hideColumn(loanTable, 3);
        }
    }

    private static void hideColumn(JTable table, int index){
        table.getColumnModel().getColumn(index).setMinWidth(0);
        table.getColumnModel().getColumn(index).setMaxWidth(0);
    }

    //penalties-------------------
    private JPanel createTopPenaltyPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        //left search panel
        JPanel lefPanel = new JPanel(new BorderLayout());
        lefPanel.add(new JLabel("search (member name | book title | status | loan ID) : ")
                ,BorderLayout.WEST);
        searchPenaltyField = new JTextField(20);
        lefPanel.add(searchPenaltyField, BorderLayout.EAST);
        //date panel
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rightPanel.setBorder(BorderCreator.createBorder("Date Filter(Return date)"));
        rightPanel.add(new JLabel("From Date : "));
        fromDateField = new JTextField(10);
        rightPanel.add(fromDateField);
        rightPanel.add(new JLabel(" - To Date : "));
        toDateField = new JTextField(10);
        rightPanel.add(toDateField);
        btnFilter = new JButton("Filter");
        rightPanel.add(btnFilter);

        panel.add(lefPanel);
        panel.add(rightPanel);
        return panel;
    }

    private void createPenaltyTablePanel(){
        String[] columns = {"Loan id","Member", "Book", "Expire Date", "Return Date"
                ,"Late Days", "Penalty Cost", "Pay Status"};
        penaltyTableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){return false;}
        };
        penaltyTable = new JTable(penaltyTableModel);
        JScrollPane scrollPane = new JScrollPane(penaltyTable);
        southPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createBottomSouthPanel(){
        var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRefreshPenalty = new JButton("Refresh");
        btnToFile = new JButton("Write To File");

        panel.add(btnRefreshPenalty);
        panel.add(btnToFile);

        southPanel.add(panel, BorderLayout.SOUTH);

        //show initial
        refreshPenaltyTable();
    }

    private void refreshPenaltyTable(){
        List<PenaltyReportDTO> penalties = reportController.handlePenaltyReport(null, null);
        penaltyTableModel.setRowCount(0);
        if(penalties != null){
            for(PenaltyReportDTO p : penalties){
                Object[] row = {
                        p.getLoanId(),
                        p.getMemberName(),
                        p.getBookTitle(),
                        p.getExpireDate(),
                        p.getReturnDate(),
                        p.getLateDays(),
                        p.getPenaltyCost()+"$"
                };
                penaltyTableModel.addRow(row);
            }
        }
    }

    private void filterPenalty(String query){
        penaltyTableModel.setRowCount(0);
        List<PenaltyReportDTO> penalties =
                reportController.handlePenaltyReport(null, null);
        for(PenaltyReportDTO p : penalties){
            if(String.valueOf(p.getLoanId()).contains(query) ||
                    p.getBookTitle().toLowerCase().contains(query) ||
                    p.getMemberName().toLowerCase().contains(query))
            {
                Object[] row = {
                        p.getLoanId(),
                        p.getMemberName(),
                        p.getBookTitle(),
                        p.getExpireDate(),
                        p.getReturnDate(),
                        p.getLateDays(),
                        p.getPenaltyCost()+"$"
                };
                penaltyTableModel.addRow(row);
            }
        }
    }

    private void filterPenaltyDate(){
        String txtFrom = fromDateField.getText();
        String txtTo = toDateField.getText();
        if(txtFrom == null || txtFrom.isEmpty() || txtTo == null ||txtTo.isEmpty()){
            JOptionPane.showMessageDialog(this, "Dates Can't be empty !!!");
        }
        else {
            LocalDate fromDate = LocalDate.parse(txtFrom);
            LocalDate toDate = LocalDate.parse(txtTo);
            penaltyTableModel.setRowCount(0);
            List<PenaltyReportDTO> penalties =
                    reportController.handlePenaltyReport(fromDate, toDate);
            for(PenaltyReportDTO p : penalties){
                Object[] row = {
                        p.getLoanId(),
                        p.getMemberName(),
                        p.getBookTitle(),
                        p.getExpireDate(),
                        p.getReturnDate(),
                        p.getLateDays(),
                        p.getPenaltyCost()+"$"
                };
                penaltyTableModel.addRow(row);
            }
        }
    }

    //---------------------------
    private void saveToFile(boolean isPenalty){
        String txtFrom = fromDateField.getText();
        String txtTo = toDateField.getText();
        fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(isPenalty ? "Save Penalties" : "Save Borrows");

        fileChooser.setFileFilter(new FileNameExtensionFilter(
                "Text Files (*.txt)", "txt"));

        int result = fileChooser.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            if(!selectedFile.getName().toLowerCase().endsWith(".txt")){
                selectedFile = new File(selectedFile.getAbsolutePath() + ".txt");
            }
            if(isPenalty){
                if(txtFrom == null || txtFrom.isEmpty() || txtTo == null ||txtTo.isEmpty()){
                    reportController.handlePenaltyToFile(selectedFile, null, null);
                }
                else {
                    LocalDate fromDate = LocalDate.parse(txtFrom);
                    LocalDate toDate = LocalDate.parse(txtTo);
                    reportController.handlePenaltyToFile(selectedFile, fromDate, toDate);
                }

                JOptionPane.showMessageDialog(this,
                        "File Saved To "+selectedFile.getPath()+" Successfully");
            }
            else{
                JOptionPane.showMessageDialog(this,
                        "Borrows");
            }
        }
    }
}
