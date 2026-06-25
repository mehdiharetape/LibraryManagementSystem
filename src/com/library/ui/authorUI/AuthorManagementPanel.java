package com.library.ui.authorUI;

import com.library.conroller.AuthorController;
import com.library.conroller.BookController;
import com.library.services.DTOs.AuthorDTO;
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

public class AuthorManagementPanel extends JPanel {
    //-----------------------------------------------
    private AuthorController authorController;
    private BookController bookController;
    //-----------------------------------------------
    private JTable authorTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    //buttons
    JButton btnAdd, btnEdit, btnAllBooks;
    //JButton btnDelete;

    public AuthorManagementPanel(AuthorController authorController,
                                 BookController bookController)
    {
        //connect to db
        this.authorController = authorController;
        this.bookController = bookController;

        setLayout(new BorderLayout(10,10));

        var topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        createTablePanel();
        createBottomPanel();

        Window parent = SwingUtilities.getWindowAncestor(this);
        //add
        btnAdd.addActionListener(e -> {
            var dialog = new AddEditAuthorDialog((Frame) parent, authorController);
            dialog.setVisible(true);
            refreshTable();
        });

        //edit
        btnEdit.addActionListener(e -> {
            int selectedRow = authorTable.getSelectedRow();
            if(selectedRow != -1){
                Object idObject = tableModel.getValueAt(selectedRow, 0);
                int authorId = (int) idObject;
                var author = authorController.handleGetAuthorById(authorId);
                var dialog = new AddEditAuthorDialog((Frame) parent, authorController, author);
                dialog.setVisible(true);
                refreshTable();
            }
        });

        btnAllBooks.addActionListener(e -> {
            int selectedAuthor = authorTable.getSelectedRow();
            if(selectedAuthor != -1){
                Object objectId = tableModel.getValueAt(selectedAuthor, 0);
                int id = (int) objectId;
                var books = new GetBooksCommon((Frame) parent,
                        bookController, new BookFilter(BookFilterType.AUTHOR, id));
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
                filterAuthor(query);
            }
        });
    }

    private void filterAuthor(String query){
        tableModel.setRowCount(0);
        List<AuthorDTO> authors = authorController.handleAuthorList();
        for(AuthorDTO a : authors){
            if(String.valueOf(a.getAuthorId()).contains(query) ||
              a.getAuthorName().toLowerCase().contains(query) ||
              a.getAuthorUrl().toLowerCase().contains(query))
            {
                Object[] row = {
                        a.getAuthorId(),
                        a.getAuthorName(),
                        a.getAuthorUrl()
                };
                tableModel.addRow(row);
            }
        }
    }

    //search panel
    private JPanel createTopPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("search (id | name | url) : "));
        searchField = new JTextField(30);
        panel.add(searchField);
        panel.setBorder(BorderCreator.createBorder(""));

        return panel;
    }

    private void createTablePanel(){
        String[] columns = {"ID", "Author Name", "Url Address"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        authorTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(authorTable);
        scrollPane.setBorder(BorderCreator.createBorder("Authors List"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createBottomPanel(){
        var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnAdd = new JButton("Add Author");
        btnEdit = new JButton("Edit");
        btnAllBooks = new JButton("All Books");
        btnAllBooks.setBackground(new Color(168, 241, 109));

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnAllBooks);
        panel.setBorder(BorderCreator.createBorder(
                "Actions(Click 'All Books' button to see all books of selected author)"));
        add(panel, BorderLayout.SOUTH);

        //show initial data
        refreshTable();
    }

    public void refreshTable(){
        List<AuthorDTO> authors = authorController.handleAuthorList();

        //clear table data
        tableModel.setRowCount(0);

        if(authors != null){
            for (AuthorDTO a : authors){
                Object[] row = {
                        a.getAuthorId(),
                        a.getAuthorName(),
                        a.getAuthorUrl()
                };
                tableModel.addRow(row);
            }
        }
    }
}