package com.library.ui.publisherUI;

import com.library.conroller.BookController;
import com.library.conroller.PublisherController;
import com.library.services.DTOs.PublisherDTO;
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

public class PublisherManagementPanel extends JPanel {
    //-----------------------------------------------
    private PublisherController publisherController;
    private BookController bookController;
    //-----------------------------------------------
    private JTable publisherTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    //buttons
    JButton btnAdd, btnEdit, btnAllBooks;

    public PublisherManagementPanel(PublisherController publisherController,
                                    BookController bookController)
    {
        this.publisherController = publisherController;
        this.bookController = bookController;
        setLayout(new BorderLayout(10,10));

        var topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        createTablePanel();
        createBottomPanel();

        var window = SwingUtilities.getWindowAncestor(this);
        btnAdd.addActionListener(e -> {
            var dialog = new AddEditPublisherDialog((Frame) window, publisherController);
            dialog.setVisible(true);
            refreshTable();
        });

        btnEdit.addActionListener(e -> {
            int selectedRow = publisherTable.getSelectedRow();
            if(selectedRow != -1){
                Object idObject = tableModel.getValueAt(selectedRow, 0);
                int id = (int) idObject;
                var publisher = publisherController.handleGetPublisherById(id);
                var dialog = new AddEditPublisherDialog((Frame) window, publisherController, publisher);
                dialog.setVisible(true);
                refreshTable();
            }
        });

        btnAllBooks.addActionListener(e -> {
            int selectedPublisher = publisherTable.getSelectedRow();
            if(selectedPublisher != -1){
                Object objectId = tableModel.getValueAt(selectedPublisher, 0);
                int id = (int) objectId;
                var books = new GetBooksCommon((Frame) window ,bookController,
                        new BookFilter(BookFilterType.PUBLISHER, id));
                books.setVisible(true);
            }
        });

        addFilterLogic(searchField);
    }

    //filtet
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
                filterPublisher(query);
            }
        });
    }

    private void filterPublisher(String query){
        tableModel.setRowCount(0);
        List<PublisherDTO> publishers = publisherController.handlePublisherList();
        for(PublisherDTO p : publishers){
            if(String.valueOf(p.getPublisherId()).contains(query) ||
              p.getPublisherName().toLowerCase().contains(query) ||
              p.getPublisherUrl().toLowerCase().contains(query))
            {
                Object[] row = {
                        p.getPublisherId(),
                        p.getPublisherName(),
                        p.getPublisherUrl()
                };
                tableModel.addRow(row);
            }
        }
    }
    //search panel
    private JPanel createTopPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        panel.add(new JLabel("search (Id | Name | url) : "));
        searchField = new JTextField(30);
        panel.add(searchField);
        panel.setBorder(BorderCreator.createBorder(""));

        return panel;
    }

    private void createTablePanel(){
        String[] columns = {"ID", "Publisher Name", "Publisher Url"};
        tableModel = new DefaultTableModel(columns, 0){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        publisherTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(publisherTable);
        scrollPane.setBorder(BorderCreator.createBorder("Publishers List"));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void createBottomPanel(){
        var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        btnAdd = new JButton("Add Publisher");
        btnEdit = new JButton("Edit");
        btnAllBooks = new JButton("All Books");
        btnAllBooks.setBackground(new Color(168, 241, 109));

        panel.setBorder(BorderCreator.createBorder(
                "Actions(Click 'All Books' button to see all books of selected publisher)"));
        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnAllBooks);

        add(panel, BorderLayout.SOUTH);

        //show initial data
        refreshTable();
    }

    public void refreshTable(){
        List<PublisherDTO> publishers = publisherController.handlePublisherList();

        //clear table data
        tableModel.setRowCount(0);

        if(publishers != null){
            for (PublisherDTO p : publishers){
                Object[] row = {
                        p.getPublisherId(),
                        p.getPublisherName(),
                        p.getPublisherUrl()
                };
                tableModel.addRow(row);
            }
        }
    }
}
