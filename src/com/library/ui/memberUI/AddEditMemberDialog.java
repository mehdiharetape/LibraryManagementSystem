package com.library.ui.memberUI;

import com.library.conroller.MemberController;
import com.library.domain.exception.LibraryException;
import com.library.domain.valueobject.Email;
import com.library.domain.valueobject.Phone;
import com.library.services.DTOs.MemberDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class AddEditMemberDialog extends JDialog {
    //mode
    private boolean isEdit;
    private MemberDTO memberToEdit;
    //field
    private JTextField txtFullName, txtNationalCode, txtAddress,
            txtEmail, txtPhoneNumber;
    private JFormattedTextField txtBirthDate;
    //button
    private JButton btnSave, btnCancel;
    //---------
    private MemberController memberController;

    public AddEditMemberDialog(Frame parent, MemberController memberController){
        this(parent, memberController, null);
    }

    public AddEditMemberDialog(Frame parent, MemberController memberController, MemberDTO memberToEdit){
        super(parent, (memberToEdit == null ? "Add Member" : "Edit ("+memberToEdit.getFullName()+")"),
                true);
        this.memberController = memberController;

        //----------------------
        setLayout(new BorderLayout(10,10));
        setResizable(false);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderCreator.createBorder("Member Info"));
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.VERTICAL;

        var gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(5,5,5,5);
        gbc_label.fill = GridBagConstraints.NONE;

        //form panel===============

        //full name
        gbc_label.gridx=0; gbc_label.gridy=0;
        formPanel.add(new JLabel("Full Name : "), gbc_label);
        gbc.gridx=1; gbc.gridy=0;
        txtFullName = new JTextField(20);
        formPanel.add(txtFullName, gbc);

        //national code
        gbc_label.gridx=2; gbc_label.gridy=0;
        formPanel.add(new JLabel("National Code : "), gbc_label);
        gbc.gridx=3; gbc.gridy=0;
        txtNationalCode = new JTextField(20);
        txtNationalCode.setToolTipText("National code can't be less than 10 numbers");
        formPanel.add(txtNationalCode, gbc);

        //birth date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        gbc_label.gridx=0; gbc_label.gridy=1;
        formPanel.add(new JLabel("Birth Date : "), gbc_label);
        txtBirthDate = new JFormattedTextField(new DateFormatter(dateFormat));
        txtBirthDate.setColumns(20);
        txtBirthDate.setToolTipText("format : yyyy-mm-dd");
        gbc.gridx=1; gbc.gridy=1;
        formPanel.add(txtBirthDate, gbc);

        //address
        gbc_label.gridx=2; gbc_label.gridy=1;
        formPanel.add(new JLabel("Address : "), gbc_label);
        gbc.gridx=3; gbc.gridy=1;
        txtAddress = new JTextField(20);
        formPanel.add(txtAddress, gbc);

        //email
        gbc_label.gridx=0; gbc_label.gridy=2;
        formPanel.add(new JLabel("Email : "), gbc_label);
        gbc.gridx=1; gbc.gridy=2;
        txtEmail = new JTextField(20);
        txtEmail.setToolTipText("www.example@gmail.com");
        formPanel.add(txtEmail, gbc);
        
        //phone number
        gbc_label.gridx=2; gbc_label.gridy=2;
        formPanel.add(new JLabel("Phone Number : "), gbc_label);
        gbc.gridx=3; gbc.gridy=2;
        txtPhoneNumber = new JTextField(20);
        txtPhoneNumber.setToolTipText("example : 09123456789");
        formPanel.add(txtPhoneNumber, gbc);

        add(formPanel, BorderLayout.CENTER);

        //buttons-----------------------
        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        if(memberToEdit != null){
            this.isEdit = true;
            this.memberToEdit = memberToEdit;

            txtFullName.setText(memberToEdit.getFullName());
            txtNationalCode.setText(memberToEdit.getNationalCode());
            txtBirthDate.setText(memberToEdit.getBirthDate().toString());
            txtAddress.setText(memberToEdit.getAddress());
            txtEmail.setText(memberToEdit.getEmail());
            txtPhoneNumber.setText(memberToEdit.getPhoneNumber());
        }

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
        
        pack();
        setLocationRelativeTo(null);
    }

    private void save(){
        try {
            String fullName = txtFullName.getText();
            String nationalCode = txtNationalCode.getText();
            String birthDate = txtBirthDate.getText();
            String address = txtAddress.getText();
            String email = txtEmail.getText();
            String phoneNumber = txtPhoneNumber.getText();

            if(fullName == null || fullName.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Empty Full name !!!");
                return;
            }
            if(nationalCode == null || nationalCode.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Empty nationalCode!!!");
                return;
            }
            if(birthDate == null || birthDate.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Empty birth date!!!");
                return;
            }
            if(address == null || address.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Empty address!!!");
                return;
            }
            if(email == null || email.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Empty email!!!");
                return;
            }
            if(phoneNumber == null || phoneNumber.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Empty phone number!!!");
                return;
            }

            if(isEdit){
                memberToEdit.setFullName(fullName);
                memberToEdit.setNationalCode(nationalCode);
                memberToEdit.setBirthDate(LocalDate.parse(birthDate));
                memberToEdit.setAddress(address);
                memberToEdit.setEmail(email);
                memberToEdit.setPhoneNumber(phoneNumber);

                boolean isSuccess = memberController.handleUpdateMember(memberToEdit);
                if(isSuccess) {
                    JOptionPane.showMessageDialog(this, "Member Edited Successfully !!!!");
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(this, "Member Edited Failed !!!!");
            }
            else {
                var member = new MemberDTO(null, fullName, nationalCode, LocalDate.parse(birthDate),
                        address, LocalDate.now(), email, phoneNumber);
                boolean success = memberController.handleRegisterMember(member);

                if(success){
                    JOptionPane.showMessageDialog(this,
                            "Member added successfully");
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(this,
                            "Add Member error!!!");
            }
        }
        catch (LibraryException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
