package com.library.ui.loanUI;

import com.library.conroller.BookController;
import com.library.conroller.LoanBookController;
import com.library.conroller.MemberController;
import com.library.domain.entity.LoanBookEntity;
import com.library.domain.enums.LoanStatus;
import com.library.domain.exception.LibraryException;
import com.library.services.DTOs.BookDTO;
import com.library.services.DTOs.BooksListDTO;
import com.library.services.DTOs.LoanBookDto;
import com.library.services.DTOs.MemberDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class CreateLoanPanel extends JDialog {
    private Integer selectedMemberId = null;
    private Integer selectedBookId = null;

    private MemberController memberController;
    private BookController bookController;
    private LoanBookController loanBookController;

    //models & tables
    private DefaultTableModel memberTableModel;
    private DefaultTableModel bookTableModel;
    private JTable memberTable;
    private JTable bookTable;

    private JTextField txtLoanDate, txtStatus;
    private JFormattedTextField txtReturnLoanDate;

    public CreateLoanPanel(MemberController memberController, BookController bookController,
                           LoanBookController loanBookController)
    {
        this.memberController = memberController;
        this.bookController = bookController;
        this.loanBookController = loanBookController;

        setTitle("Loan Register Panel");
        setSize(700, 600);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        setModal(true);

        //search section (top)
        JPanel searchPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        JTextField searchMemberField = new JTextField();
        JTextField searchBookField = new JTextField();
        searchPanel.add(createSearchBox("Search Member(name or national code) : ", searchMemberField));
        searchPanel.add(createSearchBox("Search Book(title or ISBN)", searchBookField));

        //tables section (middle)
        JPanel tablesPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        //members table
        String[] memberCols = {"ID", "Name", "National Code"};
        memberTableModel = new DefaultTableModel(memberCols, 0);
        memberTable = new JTable(memberTableModel);
        setupSelectionListener(memberTable, true);
        var scrollMember = new JScrollPane(memberTable);
        scrollMember.setBorder(BorderCreator.createBorder("Members"));

        //books table
        String[] bookCols = {"ID", "Title", "ISBN", "Status"};
        bookTableModel = new DefaultTableModel(bookCols, 0);
        bookTable = new JTable(bookTableModel);
        setupSelectionListener(bookTable, true);
        var scrollBook = new JScrollPane(bookTable);
        scrollBook.setBorder(BorderCreator.createBorder("Books"));

        tablesPanel.add(scrollMember);
        tablesPanel.add(scrollBook);

        //--info and button section (bottom)
        JPanel infoPanel = new JPanel(new GridLayout(2, 4, 10, 10));

        txtLoanDate = new JTextField(LocalDate.now().toString());
        txtStatus = new JTextField(LoanStatus.ACTIVE.toString());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        txtReturnLoanDate = new JFormattedTextField(new DateFormatter(dateFormat));
        txtReturnLoanDate.setColumns(20);
        txtReturnLoanDate.setToolTipText("format : yyyy-mm-dd");
        JButton btnSubmit = new JButton("Register Loan");
        btnSubmit.setBackground(new Color(46, 239, 72));

        //read only fields
        txtLoanDate.setEditable(false);
        txtStatus.setEditable(false);

        infoPanel.setBorder(BorderCreator.createBorder("Loan Info"));
        infoPanel.add(new JLabel("Loan Date:"));
        infoPanel.add(txtLoanDate);
        infoPanel.add(new JLabel("Status:"));
        infoPanel.add(txtStatus);
        infoPanel.add(new JLabel("Return Date:"));
        infoPanel.add(txtReturnLoanDate);
        infoPanel.add(new JLabel("Operation:"));
        infoPanel.add(btnSubmit);

        //final layout
        add(searchPanel, BorderLayout.NORTH);
        add(tablesPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        //load data
        loadData();

        //add filter to fields
        addFilterLogic(searchMemberField, true);
        addFilterLogic(searchBookField, false);

        btnSubmit.addActionListener(e -> save());

        setLocationRelativeTo(null);
    }

    private void save(){
        try{
            //get selected member id
            int row = memberTable.getSelectedRow();
            if(row != -1){
                Object memberId = memberTableModel.getValueAt(row, 0);
                selectedMemberId = (Integer) memberId;
            }
            if(row < 0) {
                JOptionPane.showMessageDialog(this, "Select a member!!!");
                return;
            }

            //get selected book
            row = bookTable.getSelectedRow();
            if(row != -1){
                Object bookId = bookTableModel.getValueAt(row, 0);
                selectedBookId = (Integer) bookId;
            }
            if(row < 0){
                JOptionPane.showMessageDialog(this, "Select a book!!!");
                return;
            }

            String fromDate = txtLoanDate.getText();
            String toDate = txtReturnLoanDate.getText();
            String status = txtStatus.getText();

            //check date
            if(toDate == null || toDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Expire date can't be empty!!!.");
                return;
            }

            var loan = new LoanBookDto(null ,selectedMemberId, selectedBookId,
                    LocalDate.parse(fromDate),LocalDate.parse(toDate), LoanStatus.valueOf(status));

            boolean isSuccess = loanBookController.handleCreateLoan(loan);
            if(isSuccess)
                JOptionPane.showMessageDialog(this, "Loan Created Successfully.");
            else
                JOptionPane.showMessageDialog(this, "Loan Created Failed!!!!.");
        }
        catch (LibraryException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    //for create search box
    private JPanel createSearchBox(String label, JTextField field){
        var p = new JPanel(new BorderLayout(5,5));
        p.add(new JLabel(label), BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    //row selection management
    private void setupSelectionListener(JTable table, boolean isMember){
        table.getSelectionModel().addListSelectionListener(e -> {
            if(!e.getValueIsAdjusting()){
                int row = table.getSelectedRow();
                if(row != -1){
                    if(isMember) selectedMemberId = (Integer) table.getValueAt(row, 0);
                    else selectedBookId = (Integer) table.getValueAt(row, 0);
                }
            }
        });
    }

    //filter
    private void addFilterLogic(JTextField field, boolean isMemberTable) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String query = field.getText().toLowerCase();
                if(isMemberTable)
                    filterMembers(query);
                else
                    filterBooks(query);
            }
        });
    }

    private void filterBooks(String query){
        bookTableModel.setRowCount(0);
        List<BooksListDTO> books = bookController.handleBookList();
        for(BooksListDTO b : books){
            if(b.getTitle().toLowerCase().contains(query) ||
                    b.getIsbn().contains(query))
            {
                bookTableModel.addRow(new Object[]{b.getBookId(), b.getTitle(), b.getIsbn(),
                                      b.getBookStatus()});
            }
        }
    }

    private void filterMembers(String query){
        memberTableModel.setRowCount(0);
        List<MemberDTO> members = memberController.handleMembersList();
        for(MemberDTO m : members){
            if(m.getFullName().toLowerCase().contains(query) ||
                  m.getNationalCode().contains(query))
            {
                memberTableModel.addRow(new Object[]{m.getId(), m.getFullName(), m.getNationalCode()});
            }
        }
    }

    //load date
    private void loadData(){
        //members
        List<MemberDTO> members = memberController.handleMembersList();
        memberTableModel.setRowCount(0);
        for(MemberDTO m : members){
            Object[] row ={
                    m.getId(),
                    m.getFullName(),
                    m.getNationalCode()
            };
            memberTableModel.addRow(row);
        }

        //books
        List<BooksListDTO> books = bookController.handleBookList();
        bookTableModel.setRowCount(0);
        for(BooksListDTO b : books){
            Object[] row = {
                    b.getBookId(),
                    b.getTitle(),
                    b.getIsbn(),
                    b.getBookStatus()
            };
            bookTableModel.addRow(row);
        }
    }
}
