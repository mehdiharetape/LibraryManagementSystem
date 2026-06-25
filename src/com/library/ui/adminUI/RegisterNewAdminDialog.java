package com.library.ui.adminUI;

import com.library.conroller.AdminController;
import com.library.domain.entity.AdminUpdateEntity;
import com.library.domain.enums.AdminRole;
import com.library.domain.exception.LibraryException;
import com.library.services.DTOs.AdminDTO;
import com.library.services.DTOs.AdminListDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import java.awt.*;

public class RegisterNewAdminDialog extends JDialog {
    //edits
    private boolean isEdit;
    private AdminDTO adminToEdit;
    //------
    private AdminController adminController;
    //text fields
    private JTextField txtName, txtUsername;
    private JPasswordField txtPassword, txtRepassword;
    //buttons
    private JButton btnRegister, btnCancel;
    //admin role
    private JComboBox<String> adminRolesCombo;

    public RegisterNewAdminDialog(Frame parent, AdminController adminController){
        this(parent, adminController, null);
    }

    public RegisterNewAdminDialog(Frame parent ,AdminController adminController,
                                  AdminDTO admin)
    {
        super(parent, admin == null ? "Register Admin" : "Edit ("+admin.getFullName()+")",
                true);
        this.adminController = adminController;

        //--------------------------
        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        //form
        JPanel formPanel = new JPanel(new GridBagLayout());
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //labels
        var gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(5,5,5,5);
        gbc_label.fill = GridBagConstraints.NONE;

        //name
        gbc_label.gridx=0; gbc_label.gridy=0;
        formPanel.add(new JLabel("Admin Full Name : "), gbc_label);
        gbc.gridx=1; gbc.gridy=0;
        txtName = new JTextField(30);
        formPanel.add(txtName, gbc);

        //username
        gbc_label.gridx=0; gbc_label.gridy=1;
        formPanel.add(new JLabel("username : "), gbc_label);
        gbc.gridx=1; gbc.gridy=1;
        txtUsername = new JTextField(30);
        formPanel.add(txtUsername, gbc);

        //pass
        gbc_label.gridx=0; gbc_label.gridy=2;
        formPanel.add(new JLabel("password : "), gbc_label);
        gbc.gridx=1; gbc.gridy=2;
        txtPassword = new JPasswordField(30);
        formPanel.add(txtPassword, gbc);

        //re-pass
        gbc_label.gridx=0; gbc_label.gridy=3;
        formPanel.add(new JLabel("Re-Password : "), gbc_label);
        gbc.gridx=1; gbc.gridy=3;
        txtRepassword = new JPasswordField(30);
        formPanel.add(txtRepassword, gbc);

        //admin roles
        gbc_label.gridx=0; gbc_label.gridy=4;
        formPanel.add(new JLabel("Admin Role : "), gbc_label);
        gbc.gridx=1; gbc.gridy=4;
        adminRolesCombo = new JComboBox<>();
        adminRolesCombo.addItem(AdminRole.LIBRARIAN.toString());
        adminRolesCombo.addItem(AdminRole.SUPER_ADMIN.toString());
        formPanel.add(adminRolesCombo, gbc);

        formPanel.setBorder(BorderCreator.createBorder("Admin Info"));
        add(formPanel, BorderLayout.CENTER);

        //buttons------------------
        var btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        btnRegister = new JButton("Save");
        btnCancel = new JButton("Cancel");
        btnPanel.add(btnRegister);
        btnPanel.add(btnCancel);

        //edit mode
        if(admin != null){
            this.isEdit = true;
            this.adminToEdit = admin;

            //fill fields
            txtName.setText(admin.getFullName());
            txtUsername.setText(admin.getUsername());
            txtPassword.setEditable(false);
            txtRepassword.setEditable(false);
            adminRolesCombo.setSelectedItem(admin.getAdminRole());
        }

        btnRegister.addActionListener(e -> {
            if(isEdit) update();
            else register();
        });
        btnCancel.addActionListener(e -> dispose());
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    private void register(){
        try{
            String fullName = txtName.getText();
            String username = txtUsername.getText();
            String password = String.valueOf(txtPassword.getPassword());
            String rePassword = String.valueOf(txtRepassword.getPassword());
            String adminRole = adminRolesCombo.getSelectedItem().toString();

            if(fullName == null || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Empty full name!!!");
                return;
            }
            if(username == null || username.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Empty username!!!");
                return;
            }
            if(password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Empty password!!!");
                return;
            }
            if(rePassword.isEmpty() || !rePassword.equals(password)){

                JOptionPane.showMessageDialog(this,
                        "Re-Password not equals to Password!!!");
                return;
            }
            if(adminRole == null || adminRole.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Admin must have role!!!");
                return;
            }
            var newAdmin = new AdminDTO(null, fullName, username, password,
                    adminRole);
            boolean success = adminController.handlerRegisterAdmin(newAdmin);
            if(success) {
                JOptionPane.showMessageDialog(this,
                        "Admin Registered Successfully");
                dispose();
            }
            else
                JOptionPane.showMessageDialog(this,
                        "Fail to register !!!");
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void update(){
        try{
            String fullName = txtName.getText();
            String username = txtUsername.getText();
            String adminRole = adminRolesCombo.getSelectedItem().toString();

            if(fullName == null || fullName.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Empty full name!!!");
                return;
            }
            if(username == null || username.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Empty username!!!");
                return;
            }
            if(adminRole == null || adminRole.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Admin must have role!!!");
                return;
            }
            var updatedAdmin = new AdminDTO(adminToEdit.getAdminId(), fullName, username,"",
                    adminRole);
            boolean success = adminController.handleUpdateAdmin(updatedAdmin);
            if(success) {
                JOptionPane.showMessageDialog(this,
                        "Admin Updated Successfully");
                dispose();
            }
            else
                JOptionPane.showMessageDialog(this,
                        "Fail to Updated failed!!!");
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
