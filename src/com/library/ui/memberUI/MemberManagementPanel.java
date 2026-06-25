package com.library.ui.memberUI;

import com.library.conroller.MemberController;
import com.library.services.DTOs.BorrowedBook;
import com.library.services.DTOs.MemberDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MemberManagementPanel extends JPanel {
    //-------------------------------
    private MemberController memberController;
    //-------------------------------
    private JTextField searchMembers;
    private DefaultTableModel tableModel;
    private JTable memberTable;
    //-------------------------------
    private JButton btnAdd, btnEdit, btnLoanedBooks;

    public MemberManagementPanel(MemberController memberController){
        this.memberController = memberController;

        setLayout(new BorderLayout(10,10));

        var topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        createTablePanel();
        createBottomPanel();

        btnAdd.addActionListener(e -> {
            var window = SwingUtilities.getWindowAncestor(this);
            var dialog = new AddEditMemberDialog((Frame) window, memberController);
            dialog.setVisible(true);
            refreshTable();
        });

        btnEdit.addActionListener(e -> {
            var window = SwingUtilities.getWindowAncestor(this);
            int selectedRow = memberTable.getSelectedRow();
            if(selectedRow != -1){
                Object idObject = tableModel.getValueAt(selectedRow, 0);
                int memberId = (int) idObject;
                var member = memberController.handleGetMemberById(memberId);
                var dialog = new AddEditMemberDialog((Frame) window, memberController, member);
                dialog.setVisible(true);
                refreshTable();
            }
        });

        btnLoanedBooks.addActionListener(e -> {
            var panel = new JDialog();
            var area = new JTextArea();
            area.setEditable(false);
            area.setSize(600, 100);
            area.setBorder(BorderCreator.createBorder("Loaned Books"));
            JScrollPane scrollPane = new JScrollPane(area);
            panel.add(scrollPane);

            int selectedRow = memberTable.getSelectedRow();
            if(selectedRow != -1){
                Object idObject = tableModel.getValueAt(selectedRow, 0);
                int memberId = (int) idObject;
                var actives = memberController.handleGetMemberActiveLoanById(memberId);
                if (actives.isEmpty())
                    area.setText("No Loan Books!!");
                else {
                    for (BorrowedBook item : actives)
                        area.append(item.toString()+"\n---------------------------------\n");
                }
            }

            panel.setModal(true);
            panel.setLocationRelativeTo(null);
            panel.pack();
            panel.setVisible(true);
        });
        addFilterLogic(searchMembers);
    }

    //filters
    private void addFilterLogic(JTextField textField){
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {filter();}

            @Override
            public void removeUpdate(DocumentEvent e) {filter();}

            @Override
            public void changedUpdate(DocumentEvent e) {filter();}

            private void filter(){
                String query = textField.getText().toLowerCase();
                filterMember(query);
            }
        });
    }

    private void filterMember(String query){
        tableModel.setRowCount(0);
        List<MemberDTO> members = memberController.handleMembersList();
        for(MemberDTO m : members){
            if(String.valueOf(m.getId()).contains(query) ||
              m.getFullName().toLowerCase().contains(query) ||
              m.getNationalCode().toLowerCase().contains(query) ||
              m.getPhoneNumber().toLowerCase().contains(query))
            {
                Object[] row = {
                        m.getId(),
                        m.getFullName(),
                        m.getNationalCode(),
                        m.getBirthDate(),
                        m.getAddress(),
                        m.getRegisterDate(),
                        m.getEmail(),
                        m.getPhoneNumber()
                };
                tableModel.addRow(row);
            }
        }
    }

    //search panel
    private JPanel createTopPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("search (member id or name | national code | phone number) : "));
        searchMembers = new JTextField(30);
        panel.add(searchMembers);
        panel.setBorder(BorderCreator.createBorder(""));

        return panel;
    }

    private void createTablePanel(){
        String[] columns = {"ID", "Name", "National Code", "Birth Day", "Address",
                "Register Date", "Email", "Phone Number"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        memberTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBorder(BorderCreator.createBorder("Members List"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createBottomPanel(){
        var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnAdd = new JButton("Add New Member");
        btnEdit = new JButton("Edit");
        btnLoanedBooks = new JButton("Loaned Books");

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnLoanedBooks);

        panel.setBorder(BorderCreator.createBorder("Actions"));
        add(panel, BorderLayout.SOUTH);

        //show initial data
        refreshTable();
    }

    public void refreshTable(){
        List<MemberDTO> members = memberController.handleMembersList();

        //clear table data
        tableModel.setRowCount(0);

        if(members != null){
            for (MemberDTO m : members){
                Object[] row = {
                        m.getId(),
                        m.getFullName(),
                        m.getNationalCode(),
                        m.getBirthDate(),
                        m.getAddress(),
                        m.getRegisterDate(),
                        m.getEmail(),
                        m.getPhoneNumber()
                };
                tableModel.addRow(row);
            }
        }
    }
}
