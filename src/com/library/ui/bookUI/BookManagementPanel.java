package com.library.ui.bookUI;

import com.library.conroller.AuthorController;
import com.library.conroller.BookController;
import com.library.conroller.CategoryController;
import com.library.conroller.PublisherController;
import com.library.services.DTOs.BookDTO;
import com.library.services.DTOs.BooksListDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookManagementPanel extends JPanel
{
    //-----------------------------------------------
    private BookController bookController;
    //-----------------------------------------------
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    //buttons
    JButton btnAdd, btnEdit, btnDelete, btnRefresh, btnAllBooks;

    public BookManagementPanel(BookController bookController,
                               AuthorController authorController, CategoryController categoryController,
                               PublisherController publisherController)
    {
        //connect to db
        this.bookController = bookController;
        //--------------------
        setLayout(new BorderLayout(10,10));

        var topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        createTablePanel();
        createBottomPanel();

        btnAdd.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);

            var dialog = new AddEditBookDialog((Frame)parentWindow, bookController,
                    authorController, categoryController, publisherController);

            dialog.setVisible(true);
            refreshTable();
        });

        //edit book
        btnEdit.addActionListener(e -> {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            int selectedRow = bookTable.getSelectedRow();
            if(selectedRow != -1){
                Object idObject = tableModel.getValueAt(selectedRow, 0);
                int bookId = (int) idObject;

                BookDTO book = bookController.handleGetBookById(bookId);
                var dialog = new AddEditBookDialog((Frame)parentWindow, bookController,
                        authorController, categoryController, publisherController, book);

                dialog.setVisible(true);
                refreshTable();
            }
        });

        //delete book
        btnDelete.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if(selectedRow != -1){
                Object idObject = tableModel.getValueAt(selectedRow, 0);
                int bookId = (int) idObject;
                boolean isDelete;
                int confirm = JOptionPane.showConfirmDialog(this,
                        "are you sure to remove this book ? ",
                        "Remove Confirm", JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION){
                    isDelete = bookController.handleRemoveBook(bookId);
                    if (isDelete) {
                        JOptionPane.showMessageDialog(this, "Book Removed!!!");
                        refreshTable();
                    }
                    else
                        JOptionPane.showMessageDialog(this,"Fail To Remove!!!");
                }
            }
        });


        btnAllBooks.addActionListener(e -> {
            var showAuthorsPanel = new JDialog();
            var authorsArea = new JTextArea();
            authorsArea.setEditable(false);
            authorsArea.setSize(300, 150);
            authorsArea.setBorder(BorderCreator.createBorder("Authors"));
            JScrollPane scrollPane = new JScrollPane(authorsArea);
            showAuthorsPanel.add(scrollPane);

            int selectedRow = bookTable.getSelectedRow();
            if(selectedRow != -1){
                Object idObject = tableModel.getValueAt(selectedRow, 0);
                int bookId = (int) idObject;
                List<String> authors = bookController.handleGetAllAuthorsOfBook(bookId);
                for(String author : authors)
                    authorsArea.append(author + "\n");
            }
            showAuthorsPanel.setModal(true);
            showAuthorsPanel.setLocationRelativeTo(null);
            showAuthorsPanel.pack();
            showAuthorsPanel.setVisible(true);
        });

        btnRefresh.addActionListener(e -> refreshTable());
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
                filterBooks(query);
            }
        });
    }

    private void filterBooks(String query){
        tableModel.setRowCount(0);
        List<BooksListDTO> books = bookController.handleBookList();
        for (BooksListDTO b : books){
            if (String.valueOf(b.getBookId()).contains(query) ||
              b.getTitle().toLowerCase().contains(query) ||
              b.getIsbn().contains(query) || b.getCategoryName().toLowerCase().contains(query) ||
              b.getPublisherName().toLowerCase().contains(query))
            {
                Object[] row = {
                        b.getBookId(),
                        b.getTitle(),
                        b.getIsbn(),
                        b.getPrice(),
                        b.getCategoryName(),
                        b.getPublisherName(),
                        b.getTotalQuantity(),
                        b.getBookStatus()
                };
                tableModel.addRow(row);
            }
        }
    }

    private JPanel createTopPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("search (Id | Title | ISBN | Category | Publisher) : "));
        searchField = new JTextField(30);
        panel.add(searchField);
        panel.setBorder(BorderCreator.createBorder(""));

        return panel;
    }

    private void createTablePanel(){
        String[] columns =
                {"ID", "Title", "ISBN", "Price", "Category", "Publisher", "Total", "Status"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderCreator.createBorder("Books List"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createBottomPanel(){
        var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnAdd = new JButton("Add book");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        btnRefresh = new JButton("Refresh");
        btnAllBooks = new JButton("All Authors");
        btnRefresh.setBackground(new Color(182, 246, 235));
        btnAllBooks.setBackground(new Color(168, 241, 109));

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnRefresh);
        panel.add(btnAllBooks);

        panel.setBorder(BorderCreator.createBorder(
                "Actions(Click 'All Authors' button to see all author(s) of selected book)"));

        add(panel, BorderLayout.SOUTH);

        //show initial data
        refreshTable();
    }

    public void refreshTable(){
        List<BooksListDTO> books = bookController.handleBookList();

        //clear table data
        tableModel.setRowCount(0);

        for (BooksListDTO b : books){
            Object[] row = {
                b.getBookId(),
                b.getTitle(),
                b.getIsbn(),
                b.getPrice(),
                b.getCategoryName(),
                b.getPublisherName(),
                b.getTotalQuantity(),
                b.getBookStatus()
            };
            tableModel.addRow(row);
        }
    }
}
