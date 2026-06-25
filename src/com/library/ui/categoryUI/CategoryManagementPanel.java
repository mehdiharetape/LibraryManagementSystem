package com.library.ui.categoryUI;

import com.library.conroller.BookController;
import com.library.conroller.CategoryController;
import com.library.services.DTOs.CategoryDTO;
import com.library.ui.common.BookFilter;
import com.library.ui.common.BookFilterType;
import com.library.ui.common.BorderCreator;
import com.library.ui.common.GetBooksCommon;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoryManagementPanel extends JPanel {
    //-----------------------------------------------
    private CategoryController categoryController;
    private BookController bookController;
    //-----------------------------------------------
    private JTable categoryTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    //buttons
    JButton btnAdd, btnEdit, btnAllBooks;

    public CategoryManagementPanel(CategoryController categoryController,
                                   BookController bookController)
    {
        this.bookController =bookController;
        this.categoryController = categoryController;
        setLayout(new BorderLayout(10,10));

        var topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        createTablePanel();
        createBottomPanel();

        btnAdd.addActionListener(e -> {
            var window = SwingUtilities.getWindowAncestor(this);
            var dialog = new AddEditCategoryDialog((Frame) window, categoryController);
            dialog.setVisible(true);
            refreshTable();
        });

        btnEdit.addActionListener(e -> {
            var window = SwingUtilities.getWindowAncestor(this);
            int selectedCategory = categoryTable.getSelectedRow();
            if(selectedCategory != -1){
                Object objectId = tableModel.getValueAt(selectedCategory, 0);
                int id = (int) objectId;
                var category = categoryController.handleGetCategoryById(id);
                var dialog = new AddEditCategoryDialog((Frame) window, categoryController, category);
                dialog.setVisible(true);
                refreshTable();
            }
        });

        btnAllBooks.addActionListener(e -> {
            var window = SwingUtilities.getWindowAncestor(this);
            int selectedCategory = categoryTable.getSelectedRow();
            if(selectedCategory != -1){
                Object objectId = tableModel.getValueAt(selectedCategory, 0);
                int id = (int) objectId;
                var books = new GetBooksCommon((Frame) window ,bookController,
                        new BookFilter(BookFilterType.CATEGORY, id));
                books.setVisible(true);
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
                filterCategories(query);
            }
        });
    }

    private void filterCategories(String query){
        tableModel.setRowCount(0);
        List<CategoryDTO> categories = categoryController.handleCategoryList();
        for (CategoryDTO c : categories){
            if(c.getCategoryName().toLowerCase().contains(query)){
                Object[] row = {
                        c.getCategoryId(),
                        c.getCategoryName()
                };
                tableModel.addRow(row);
            }
        }
    }

    //search panel
    private JPanel createTopPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("search (Category Name): "));
        searchField = new JTextField(30);
        panel.add(searchField);
        panel.setBorder(BorderCreator.createBorder(""));

        return panel;
    }

    private void createTablePanel(){
        String[] columns = {"ID", "Category"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        categoryTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        scrollPane.setBorder(BorderCreator.createBorder("Categories List"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createBottomPanel(){
        var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnAdd = new JButton("Add Category");
        btnEdit = new JButton("Edit");
        btnAllBooks = new JButton("All Books");
        btnAllBooks.setBackground(new Color(168, 241, 109));

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnAllBooks);

        panel.setBorder(BorderCreator.createBorder(
                "Actions(Click 'All Books' button to see all books in selected category)"));
        add(panel, BorderLayout.SOUTH);

        //show initial data
        refreshTable();
    }

    public void refreshTable(){
        List<CategoryDTO> categories = categoryController.handleCategoryList();

        //clear table data
        tableModel.setRowCount(0);

        if(categories != null){
            for (CategoryDTO c : categories){
                Object[] row = {
                        c.getCategoryId(),
                        c.getCategoryName()
                };
                tableModel.addRow(row);
            }
        }
    }
}
