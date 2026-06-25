package com.library.ui.categoryUI;

import com.library.conroller.CategoryController;
import com.library.domain.exception.LibraryException;
import com.library.services.DTOs.CategoryDTO;

import javax.swing.*;
import java.awt.*;

public class AddEditCategoryDialog extends JDialog {
    //mode
    private boolean isEdit = false;
    private CategoryDTO category = null;
    //text fields
    private JTextField txtCategoryName;
    //buttons
    private JButton btnSave, btnCancel;
    private CategoryController categoryController;

    public AddEditCategoryDialog (Frame parent, CategoryController categoryController){
        this(parent, categoryController, null);
    }

    public AddEditCategoryDialog(Frame parent, CategoryController categoryController,
                                 CategoryDTO category)
    {
        super(parent, (category == null ? "Add Category" : "Edit ("+category.getCategoryName()+")"),
                true);

        this.categoryController = categoryController;

        //--------------------------
        setLayout(new BorderLayout());
        setResizable(false);

        //form panel
        var formPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        formPanel.add(new JLabel("Category Name : "));
        txtCategoryName = new JTextField(20);
        formPanel.add(txtCategoryName);
        add(formPanel, BorderLayout.CENTER);

        //buttons
        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, BorderLayout.SOUTH);

        if(category != null){
            this.isEdit = true;
            this.category = category;

            txtCategoryName.setText(category.getCategoryName());
        }

        btnSave.addActionListener(e -> save());
        btnCancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(null);
    }

    private void save(){
        try {
            String categoryName = txtCategoryName.getText();

            if(categoryName == null || categoryName.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Empty CategoryName!!!");
                return;
            }

            if(isEdit){
                category.setCategoryName(txtCategoryName.getText());
                boolean isSuccess = categoryController.handleUpdateCategory(category);
                if(isSuccess){
                    JOptionPane.showMessageDialog(this,
                            "Category edited successfully");
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(this,
                            "Category Edit Operation Failed!!!\nOr A filed is Empty");
            }
            else {
                boolean success = categoryController.handleAddCategory(
                        new CategoryDTO(null, categoryName)
                );
                if(success){
                    JOptionPane.showMessageDialog(this,
                            "Category added successfully");
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(this,
                            "Add Category error!!!");
            }
        }
        catch (LibraryException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
