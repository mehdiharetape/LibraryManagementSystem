package com.library.ui.authorUI;

import com.library.conroller.AuthorController;
import com.library.domain.entity.AuthorEntity;
import com.library.domain.exception.LibraryException;
import com.library.services.DTOs.AuthorDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import java.awt.*;

public class AddEditAuthorDialog extends JDialog{
    //mode
    private boolean isEdit=false;
    private AuthorDTO author = null;
    //text fields
    private JTextField txtName, txtUrl;
    //buttons
    private JButton btnSave, btnCancel;
    private AuthorController authorController;

    public AddEditAuthorDialog(Frame parent,AuthorController authorController)
    {
        this(parent, authorController, null);
    }

    public AddEditAuthorDialog(Frame parent,AuthorController authorController,
                               AuthorDTO author)
    {
        super(parent, (author == null ? "Add Author" : "Edit ("+author.getAuthorName() +")"),true);

        //connection
        this.authorController = authorController;

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
        formPanel.add(new JLabel("Author Name : "), gbc_label);
        gbc.gridx=1; gbc.gridy=0;
        txtName = new JTextField(30);
        formPanel.add(txtName, gbc);

        //url
        gbc_label.gridx=0; gbc_label.gridy=1;
        formPanel.add(new JLabel("url : "), gbc_label);
        gbc.gridx=1; gbc.gridy=1;
        txtUrl = new JTextField(30);
        txtUrl.setToolTipText("www.example.com");
        formPanel.add(txtUrl, gbc);
        formPanel.setBorder(BorderCreator.createBorder("Author Info"));

        add(formPanel, BorderLayout.CENTER);
        //buttons------------------
        var btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        //Edit
        if(author != null){
            this.isEdit = true;
            this.author = author;

            txtName.setText(author.getAuthorName());
            txtUrl.setText(author.getAuthorUrl());
        }

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }

    //save
    public void save(){
        try {
            String name = txtName.getText();
            String url = txtUrl.getText().trim();

            if(name == null || name.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Empty Author name!!!");
                return;
            }

            if(isEdit){
                author.setAuthorName(name);
                author.setAuthorUrl(url);
                boolean isSuccess = authorController.handleUpdateAuthor(author);
                if(isSuccess){
                    JOptionPane.showMessageDialog(this,
                            "Author edited successfully");
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(this,
                            "Author Edit Operation Failed!!!\nOr A filed is Empty");
            }
            else {
                var authorAdd = new AuthorDTO(null ,name, url);
                boolean success = authorController.handleCreateAuthor(authorAdd);

                if(success){
                    JOptionPane.showMessageDialog(this,
                            "Author added successfully");
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(this,
                            "Add Author error!!!");
            }
        }
        catch (LibraryException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
