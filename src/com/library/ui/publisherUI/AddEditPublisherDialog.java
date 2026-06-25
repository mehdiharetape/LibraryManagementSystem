package com.library.ui.publisherUI;

import com.library.conroller.PublisherController;
import com.library.domain.exception.LibraryException;
import com.library.services.DTOs.PublisherDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import java.awt.*;

public class AddEditPublisherDialog extends JDialog {
    //mode
    private boolean isEdit=false;
    private PublisherDTO publisher = null;
    //text fields
    private JTextField txtPublisherName, txtUrl;
    //buttons
    private JButton btnSave, btnCancel;
    private PublisherController publisherController;

    public AddEditPublisherDialog(Frame parent, PublisherController publisherController){
        this(parent, publisherController, null);
    }

    public AddEditPublisherDialog(Frame parent, PublisherController publisherController,
                                  PublisherDTO publisher)
    {
        super(parent, (publisher == null ? "Add Publisher" : "Edit ("+publisher.getPublisherName()+")"),
                true);
        this.publisherController = publisherController;


        //--------------------------
        setLayout(new BorderLayout(10, 10));
        setResizable(false);
        //form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderCreator.createBorder("Publisher Info"));
        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //labels
        var gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(5,5,5,5);
        gbc_label.fill = GridBagConstraints.NONE;

        //name
        gbc_label.gridx=0; gbc_label.gridy=0;
        formPanel.add(new JLabel("Publisher Name : "), gbc_label);
        gbc.gridx=1; gbc.gridy=0;
        txtPublisherName = new JTextField(30);
        formPanel.add(txtPublisherName, gbc);

        //url
        gbc_label.gridx=0; gbc_label.gridy=1;
        formPanel.add(new JLabel("url : "), gbc_label);
        gbc.gridx=1; gbc.gridy=1;
        txtUrl = new JTextField(30);
        txtUrl.setToolTipText("www.example.com");
        formPanel.add(txtUrl, gbc);

        add(formPanel, BorderLayout.CENTER);
        //buttons------------------
        var btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        if(publisher != null){
            isEdit = true;
            this.publisher = publisher;

            txtPublisherName.setText(publisher.getPublisherName());
            txtUrl.setText(publisher.getPublisherUrl());
        }


        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(null);
    }

    private void save(){
        try {
            String publisherName = txtPublisherName.getText();
            String url = txtUrl.getText();

            if(publisherName == null || publisherName.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Publisher name can't be Empty !!!");
                return;
            }

            if(isEdit){
                publisher.setPublisherName(publisherName);
                publisher.setPublisherUrl(url);
                boolean isSuccess = publisherController.handlePublisherUpdate(publisher);
                if(isSuccess){
                    JOptionPane.showMessageDialog(this,
                            "Publisher edited successfully");
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(this,
                            "Publisher Edit Operation Failed!!!\nOr A filed is Empty");
            }
            else {
                boolean success = publisherController.handleAddPublisher(
                        new PublisherDTO(null, publisherName, url)
                );

                if(success){
                    JOptionPane.showMessageDialog(this,
                            "Publisher added successfully");
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(this,
                            "Add Publisher error!!!");
            }
        }
        catch (LibraryException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
