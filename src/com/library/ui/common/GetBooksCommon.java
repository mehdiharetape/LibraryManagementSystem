package com.library.ui.common;

import com.library.conroller.BookController;
import com.library.services.DTOs.BookListMiniDTO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GetBooksCommon extends JDialog {
    private BookController bookController;
    private final BookFilter filter;

    private JTable table;
    private DefaultTableModel tableModel;

    private JTextField searchField;

    public GetBooksCommon(Frame parent ,BookController bookController,
                          BookFilter filter)
    {
        super(parent, "Books List", true);
        this.bookController = bookController;
        this.filter = filter;
        setLayout(new BorderLayout(10,10));

        //search
        var topPanel = new JPanel(new BorderLayout(5,5));
        searchField = new JTextField(20);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.setBorder(BorderCreator.createBorder("Search"));
        add(topPanel, BorderLayout.NORTH);

        //table--------------------
        String[] columns = {"Id", "Title", "ISBN", "Total"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        //fill table---------------------------------
        List<BookListMiniDTO> books = new ArrayList<>();
        switch (filter.type()){
            case AUTHOR ->
                books = bookController.handleGetAllBooksOfAuthor(filter.id());
            case CATEGORY ->
                books = bookController.handleGetAllCategoryBook(filter.id());
            case PUBLISHER ->
                books = bookController.handleGetAllPublisherBook(filter.id());
        }
        tableModel.setRowCount(0);
        for(BookListMiniDTO b : books){
            Object[] row = {
                    b.getBookId(),
                    b.getTitle(),
                    b.getIsbn(),
                    b.getTotalQuantity()
            };
            tableModel.addRow(row);
        }

        scrollPane.setBorder(BorderCreator.createBorder("Books"));
        setResizable(false);

        addFilterLogic(searchField, books);
        add(scrollPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }

    private void addFilterLogic(JTextField field, List<BookListMiniDTO> booksList) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String query = field.getText().toLowerCase();
                filterBooks(query, booksList);
            }
        });
    }

    private void filterBooks(String query, List<BookListMiniDTO> booksList){
        tableModel.setRowCount(0);
        for(BookListMiniDTO b : booksList){
            if(b.getTitle().toLowerCase().contains(query) ||
                    b.getIsbn().contains(query))
            {
                tableModel.addRow(new Object[]{
                        b.getBookId(),
                        b.getTitle(),
                        b.getIsbn(),
                        b.getTotalQuantity()
                });
            }
        }
    }
}
