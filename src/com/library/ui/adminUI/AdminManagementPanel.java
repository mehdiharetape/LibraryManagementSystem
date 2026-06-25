package com.library.ui.adminUI;

import com.library.conroller.AdminController;
import com.library.domain.exception.LibraryException;
import com.library.services.DTOs.AdminDTO;
import com.library.services.DTOs.AdminListDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminManagementPanel extends JPanel {
    private AdminController adminController;

    //-----------------------------------------------
    private JTable adminTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    //buttons
    JButton btnAdd, btnEdit;

    public AdminManagementPanel(AdminController adminController){
        this.adminController = adminController;
        setLayout(new BorderLayout(10,10));

        var topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        createTablePanel();
        createBottomPanel();

        btnAdd.addActionListener(e -> {
            var parent = SwingUtilities.getWindowAncestor(this);
            var dialog = new RegisterNewAdminDialog((Frame) parent, adminController);
            dialog.setVisible(true);
            refreshTable();
        });

        btnEdit.addActionListener(e -> {
            var parent = SwingUtilities.getWindowAncestor(this);
            int selectedRow = adminTable.getSelectedRow();
            if(selectedRow != -1){
                Object usernameObject = tableModel.getValueAt(selectedRow, 0);
                int username = (int) usernameObject;
                AdminDTO admin = adminController.handleGetAdminById(username);
                var dialog = new RegisterNewAdminDialog((Frame) parent, adminController, admin);
                dialog.setVisible(true);
                refreshTable();
            }
        });

        addFilterLogic(searchField);
    }


    //filter
    private void addFilterLogic(JTextField field){
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {filter();}

            @Override
            public void removeUpdate(DocumentEvent e) {filter();}

            @Override
            public void changedUpdate(DocumentEvent e) {filter();}

            private void filter(){
                String query = field.getText().toLowerCase();
                filterAdmins(query);
            }
        });
    }

    private void filterAdmins(String query){
        tableModel.setRowCount(0);
        List<AdminListDTO> admins = adminController.handleGetAllAdmins();
        for(AdminListDTO a : admins){
            if(String.valueOf(a.getAdminId()).contains(query) ||
              a.getAdminName().toLowerCase().contains(query) ||
              a.getAdminUserName().toLowerCase().contains(query))
            {
                Object[] row = {
                        a.getAdminId(),
                        a.getAdminName(),
                        a.getAdminUserName(),
                        a.getAdminRole()
                };
                tableModel.addRow(row);
            }
        }
    }
    private JPanel createTopPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("search (ID | Full Name | Username): "));
        searchField = new JTextField(30);
        panel.add(searchField);
        panel.setBorder(BorderCreator.createBorder(""));

        return panel;
    }

    private void createTablePanel(){
        String[] columns = {"ID", "Admin Full Name", "Admin username", "Role"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        adminTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(adminTable);
        scrollPane.setBorder(BorderCreator.createBorder("Admins List"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createBottomPanel(){
        var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnAdd = new JButton("Register Admin");
        btnEdit = new JButton("Edit");
        //btnDelete = new JButton("Delete");

        panel.add(btnAdd);
        panel.add(btnEdit);
        //panel.add(btnDelete);
        panel.setBorder(BorderCreator.createBorder("Actions"));
        add(panel, BorderLayout.SOUTH);

        //show initial data
        refreshTable();
    }

    public void refreshTable(){
        try {
            List<AdminListDTO> admins = adminController.handleGetAllAdmins();

            //clear table data
            tableModel.setRowCount(0);

            if(admins != null){
                for (AdminListDTO a : admins){
                    Object[] row = {
                            a.getAdminId(),
                            a.getAdminName(),
                            a.getAdminUserName(),
                            a.getAdminRole()
                    };
                    tableModel.addRow(row);
                }
            }
        }
        catch (LibraryException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
