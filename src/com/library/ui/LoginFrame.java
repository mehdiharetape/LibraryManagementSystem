package com.library.ui;

import com.library.conroller.AdminController;
import com.library.domain.exception.LibraryException;
import com.library.infrastructure.persistance.CreateConnection;
import com.library.repository.jdbc.AdminRepository;
import com.library.services.AdminService;
import com.library.utils.Session;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LoginFrame extends JFrame {
    private CreateConnection connection;
    private AdminRepository adminRepository;
    private AdminService adminService;
    private AdminController adminController;

    private JTextField username;
    private JPasswordField password;

    private JButton btnLogin, btnCancel;
    private JPanel topPanel, centerPanel;

    public LoginFrame(CreateConnection connection){
        this.connection = connection;
        adminRepository = new AdminRepository(connection);
        adminService = new AdminService(adminRepository);
        adminController = new AdminController(adminService);

        setTitle("Login to library management system");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450,160);
        setLayout(new BorderLayout(10,10));
        setResizable(false);

        //top panel
        topPanel = new JPanel(new BorderLayout(5,5));

        var usernamePanel = new JPanel();
        username = new JTextField(30);
        usernamePanel.add(new JLabel("User name : "), BorderLayout.WEST);
        usernamePanel.add(username, BorderLayout.CENTER);
        topPanel.add(usernamePanel, BorderLayout.NORTH);

        var passPanel = new JPanel();
        password = new JPasswordField(30);
        passPanel.add(new JLabel("Password : "), BorderLayout.WEST);
        passPanel.add(password, BorderLayout.CENTER);
        topPanel.add(passPanel, BorderLayout.CENTER);

        //center panel
        centerPanel = new JPanel(new FlowLayout(1,2,3));
        btnLogin = new JButton("Login");
        btnCancel = new JButton("Cancel");
        centerPanel.add(btnLogin);
        centerPanel.add(btnCancel);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        btnLogin.addActionListener(e -> login());
        btnCancel.addActionListener(e -> dispose());
        //pack();
        setLocationRelativeTo(null);
    }

    private void login(){
        try {
            String txtUserName = username.getText();
            char[] pssChar = password.getPassword();
            String txtPlainPass = String.valueOf(pssChar);

            if(txtUserName.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Empty User name!");
                return;
            }

            if(txtPlainPass.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Empty Password!");
                return;
            }

            boolean isSuccessLogin = adminController.handlerLoginAdmin(txtUserName, txtPlainPass);
            var admin = adminController.handleFindAdminByUsername(txtUserName);
            if(isSuccessLogin && admin != null){
                Session.set(admin);
                JOptionPane.showMessageDialog(null,
                        "----Successful Login----");
                var mainFrame = new MainFrame(connection);
                mainFrame.startBackgroundTask();
                mainFrame.setVisible(true);
                dispose();
            }
            else{
                JOptionPane.showMessageDialog( null,
                        "Wrong Username or Password");
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
