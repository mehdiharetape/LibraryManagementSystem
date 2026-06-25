package com.library.ui.bookUI;

import com.library.conroller.AuthorController;
import com.library.conroller.BookController;
import com.library.conroller.CategoryController;
import com.library.conroller.PublisherController;
import com.library.domain.enums.BookStatus;
import com.library.domain.exception.LibraryException;
import com.library.services.DTOs.AuthorDTO;
import com.library.services.DTOs.BookDTO;
import com.library.services.DTOs.CategoryDTO;
import com.library.services.DTOs.PublisherDTO;
import com.library.ui.common.BorderCreator;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AddEditBookDialog extends JDialog {
    //mode
    private boolean isEdit = false;
    private BookDTO bookToEdit = null;
    //text fields
    private JTextField txtIsbn, txtTitle, txtPrice, txtTotal;
    private JTextField categoryTxtSearch, publisherTxtSearch, authorTxtSearch;
    //category
    private JList<CategoryDTO> categoryList;
    private DefaultListModel<CategoryDTO> categoryListModel;
    private JScrollPane categoryListScrollPane;
    private List<CategoryDTO> allCategories;
    //publisher
    private JList<PublisherDTO> publisherList;
    private DefaultListModel<PublisherDTO> publisherListModel;
    private JScrollPane publisherListScrollPane;
    private List<PublisherDTO> allPublishers;
    //authors
    private JList<AuthorDTO> authorsList;
    private DefaultListModel<AuthorDTO> authorListModel;
    private JScrollPane authorScrollPane;
    private List<AuthorDTO> allAuthors;
    private JButton btnAddAuthor, btnRemoveAuthor;
    //selected authors
    private Set<AuthorDTO> selectedAuthors = new LinkedHashSet<>();
    private JList<AuthorDTO> selectedAuthorList;
    private JScrollPane selectedAuthorListPane;
    private DefaultListModel<AuthorDTO> selectedAuthorListModel;
    //buttons
    private JButton btnSave, btnCancel;
    private BookController bookController;
    //status combo box
    private JComboBox<String> bookStatusCombo;

    public AddEditBookDialog(Frame parent, BookController bookController,
                             AuthorController authorController, CategoryController categoryController,
                             PublisherController publisherController)
    {
        this(parent, bookController, authorController, categoryController,
                publisherController, null);
    }

    public AddEditBookDialog(Frame parent, BookController bookController,
                             AuthorController authorController, CategoryController categoryController,
                             PublisherController publisherController, BookDTO book)
    {
        super(parent, (book == null ? "Add new book" : "Edit Book ("+book.getTitle()+")"), true);

        //connection
        this.bookController = bookController;

        //----------------------------
        setLayout(new GridBagLayout());
        setResizable(false);

        var gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.VERTICAL;

        var gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(5,5,5,5);
        gbc_label.fill = GridBagConstraints.NONE;
        //form panel===============
        gbc.gridx=0; gbc.gridy=0;
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderCreator.createBorder("Info"));
        add(formPanel, gbc);

        //isbn
        gbc_label.gridx=0; gbc_label.gridy=0;
        formPanel.add(new JLabel("ISBN : "), gbc_label);
        gbc.gridx=1; gbc.gridy=0;
        txtIsbn = new JTextField(20);
        formPanel.add(txtIsbn, gbc);

        //title
        gbc_label.gridx=0; gbc_label.gridy=1;
        formPanel.add(new JLabel("Title : "), gbc_label);
        gbc.gridx=1; gbc.gridy=1;
        txtTitle = new JTextField(20);
        formPanel.add(txtTitle, gbc);

        //price
        gbc_label.gridx=0; gbc_label.gridy=2;
        formPanel.add(new JLabel("Price : "), gbc_label);
        txtPrice = new JTextField(20);
        gbc.gridx=1; gbc.gridy=2;
        formPanel.add(txtPrice, gbc);

        //total quantity
        gbc_label.gridx=0; gbc_label.gridy=3;
        formPanel.add(new JLabel("Quantity : "), gbc_label);
        gbc.gridx=1; gbc.gridy=3;
        txtTotal = new JTextField(20);
        formPanel.add(txtTotal, gbc);

        //book status
        gbc_label.gridx=0; gbc_label.gridy=4;
        formPanel.add(new JLabel("Status : "), gbc_label);
        gbc.gridx=1; gbc.gridy=4;
        bookStatusCombo = new JComboBox<>();
        bookStatusCombo.addItem(BookStatus.AVAILABLE.toString());
        bookStatusCombo.addItem(BookStatus.DAMAGED.toString());
        bookStatusCombo.addItem(BookStatus.UNAVAILABLE.toString());
        formPanel.add(bookStatusCombo, gbc);

        //category panel ==============
        gbc.gridx=1; gbc.gridy=0;
        var categoryPanel = new JPanel(new GridBagLayout());
        categoryPanel.setBorder(BorderCreator.createBorder("Category"));
        add(categoryPanel, gbc);
        categoryPanel.setBackground(new Color(244, 221, 221));

        gbc.gridx=0; gbc.gridy=0;
        categoryTxtSearch = new JTextField(20);
        categoryListModel = new DefaultListModel<>();
        categoryPanel.add(categoryTxtSearch, gbc);
        gbc.gridx=0; gbc.gridy=1;
        allCategories = categoryController.handleCategoryList();
        categoryList = new JList<>(categoryListModel);
        categoryListModel.clear();
        for(CategoryDTO c : allCategories)
            categoryListModel.addElement(c);

        categoryListScrollPane = new JScrollPane(categoryList);
        categoryListScrollPane.setPreferredSize(new Dimension(150, 100));
        categoryTxtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {filterCategory();}

            @Override
            public void removeUpdate(DocumentEvent e) {filterCategory();}

            @Override
            public void changedUpdate(DocumentEvent e) {filterCategory();}
        });
        categoryPanel.add(categoryListScrollPane, gbc);
        //publisher================
        gbc.gridx=2; gbc.gridy=0;
        var publisherPanel = new JPanel(new GridBagLayout());
        publisherPanel.setBorder(BorderCreator.createBorder("Publisher"));
        add(publisherPanel, gbc);
        publisherPanel.setBackground(new Color(228, 248, 215));

        gbc.gridx=0; gbc.gridy=0;
        publisherTxtSearch = new JTextField(20);
        publisherListModel = new DefaultListModel<>();
        publisherPanel.add(publisherTxtSearch, gbc);
        gbc.gridx=0; gbc.gridy=1;
        allPublishers = publisherController.handlePublisherList();
        publisherList = new JList<>(publisherListModel);
        publisherListModel.clear();
        for (PublisherDTO p : allPublishers){
            publisherListModel.addElement(p);
        }
        publisherListScrollPane = new JScrollPane(publisherList);
        publisherListScrollPane.setPreferredSize(new Dimension(150, 100));
        publisherTxtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {filterPublisher();}

            @Override
            public void removeUpdate(DocumentEvent e) {filterPublisher();}

            @Override
            public void changedUpdate(DocumentEvent e) {filterPublisher();}
        });
        publisherPanel.add(publisherListScrollPane, gbc);

        //authors=====================
        var authorPanel = new JPanel(new GridBagLayout());

        //all authors
        gbc.gridx=3; gbc.gridy=0;
        authorPanel.setBorder(BorderCreator.createBorder("Authors"));
        add(authorPanel, gbc);
        authorPanel.setBackground(new Color(221, 244, 237));

        //all authors
        gbc.gridx=0; gbc.gridy=0;
        authorTxtSearch = new JTextField(20);
        authorListModel = new DefaultListModel<>();
        authorPanel.add(authorTxtSearch, gbc);

        //selected authors
        gbc.gridx=1;gbc.gridy=1;gbc.gridwidth=1;
        selectedAuthorListModel = new DefaultListModel<>();
        selectedAuthorList = new JList<>(selectedAuthorListModel);
        selectedAuthorListPane = new JScrollPane(selectedAuthorList);
        selectedAuthorListPane.setBorder(BorderCreator.createBorder("Selected Authors"));
        authorPanel.add(selectedAuthorListPane, gbc);

        gbc.gridx=0; gbc.gridy=1;
        allAuthors = authorController.handleAuthorList();
        authorsList = new JList<>(authorListModel);
        authorsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        authorListModel.clear();
        for (AuthorDTO a : allAuthors){
            authorListModel.addElement(a);
        }
        authorScrollPane = new JScrollPane(authorsList);
        authorScrollPane.setPreferredSize(new Dimension(150, 100));
        authorTxtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {filterAuthor();}

            @Override
            public void removeUpdate(DocumentEvent e) {filterAuthor();}

            @Override
            public void changedUpdate(DocumentEvent e) {filterAuthor();}

        });
        authorPanel.add(authorScrollPane, gbc);

        gbc.gridx=0; gbc.gridy=2;
        btnAddAuthor = new JButton("Add Author");
        btnAddAuthor.addActionListener(e -> {
            AuthorDTO selected = authorsList.getSelectedValue();
            if(selected != null) {
                selectedAuthorListModel.addElement(selected);
            }
        });
        authorPanel.add(btnAddAuthor, gbc);

        gbc.gridx=1; gbc.gridy=2;
        btnRemoveAuthor = new JButton("Remove Author");
        btnRemoveAuthor.addActionListener(e -> {
            AuthorDTO selected = selectedAuthorList.getSelectedValue();
            if(selected != null){
                selectedAuthorListModel.removeElement(selected);
            }
        });
        authorPanel.add(btnRemoveAuthor, gbc);
        //buttons=====================
        gbc.gridx=0; gbc.gridy=1;
        var btnPanel = new JPanel(new GridBagLayout());
        add(btnPanel, gbc);
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        //btnPanel.add(selectedAuthorsLabel);

        //edit mode
        if(book != null){
            this.isEdit = true;
            this.bookToEdit = book;

            //fill field
            txtIsbn.setText(book.getIsbn());
            txtTitle.setText(book.getTitle());
            txtPrice.setText(book.getPrice()+"");
            txtTotal.setText(book.getTotaQuality()+"");
            bookStatusCombo.setSelectedItem(book.getBookStatus());

            //select category
            for (int i = 0; i < categoryListModel.size(); i++) {
                var c = categoryListModel.get(i);
                if(c.getCategoryId() == book.getCategory_id()){
                    categoryList.setSelectedIndex(i);
                    break;
                }
            }

            //select publisher
            for (int i = 0; i < publisherListModel.size(); i++){
                var p = publisherListModel.get(i);
                if(p.getPublisherId() == book.getPublisher_id()){
                    publisherList.setSelectedIndex(i);
                    break;
                }
            }

            //select authors
            List<Integer> selectedIndexes = new ArrayList<>();
            for (int i = 0; i < authorListModel.size(); i++){
                var a = authorListModel.get(i);
                if(book.getAuthorIds().contains(a.getAuthorId())) {
                    selectedIndexes.add(i);
                    selectedAuthorListModel.addElement(authorListModel.get(i));
                }
            }
            int[] indices = selectedIndexes.stream()
                            .mapToInt(Integer::intValue).toArray();

            authorsList.setSelectedIndices(indices);
        }

        btnSave.addActionListener(e -> saveBook());
        btnCancel.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(getParent());
    }//end of constructor

    //---------------------filter lists---------------
    //filter category list
    public void filterCategory(){
        String query = categoryTxtSearch.getText().toLowerCase();
        categoryListModel.clear();
        for(CategoryDTO c : allCategories){
            if(c.getCategoryName().toLowerCase().contains(query))
                categoryListModel.addElement(c);
        }
    }

    //filter publisher List
    public void filterPublisher(){
        String query = publisherTxtSearch.getText().toLowerCase();
        publisherListModel.clear();
        for (PublisherDTO p : allPublishers)
            if(p.getPublisherName().toLowerCase().contains(query)){
                publisherListModel.addElement(p);
            }
    }

    //filter author List
    public void filterAuthor(){
        String query = authorTxtSearch.getText().toLowerCase();
        authorListModel.clear();
        for (AuthorDTO a : allAuthors)
            if(a.getAuthorName().toLowerCase().contains(query)){
                authorListModel.addElement(a);
            }
    }

    //---------------------save ---------------------
    public void saveBook(){
        try {
            String isbn = txtIsbn.getText();
            String title = txtTitle.getText();
            String priceStr = txtPrice.getText();
            String quantityStr = txtTotal.getText();
            String bookStatus = bookStatusCombo.getSelectedItem().toString();

            if(isbn == null || isbn.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "ISBN Can't be empty!!");
                return;
            }
            if(title == null || title.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "Title can't be empty!!!!");
                return;
            }
            if (priceStr == null || priceStr.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "price can't be 0 or less !!!!");
                return;
            }
            if(quantityStr == null || quantityStr.isEmpty()){
                JOptionPane.showMessageDialog(this,
                        "quantity can't be 0 or less !!!!");
                return;
            }
            if(bookStatus == null){
                JOptionPane.showMessageDialog(this,
                        "Select Book Status !!!!");
                return;
            }

            CategoryDTO selectedCategory = categoryList.getSelectedValue();
            PublisherDTO selectedPublisher = publisherList.getSelectedValue();

            if (selectedCategory == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select a category!");
                return;
            }

            if (selectedPublisher == null) {
                JOptionPane.showMessageDialog(this,
                        "Please select a publisher!");
                return;
            }

            if (selectedAuthorListModel.isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "Select 1 or more authors!!!");
                return;
            }

            for(int i = 0; i < selectedAuthorListModel.size(); i++){
                selectedAuthors.add(selectedAuthorListModel.get(i));
            }

            int categoryId = selectedCategory.getCategoryId();
            int publisherId = selectedPublisher.getPublisherId();
            List<Integer> selectedAuthorIds = new ArrayList<>();
            for(AuthorDTO author : selectedAuthors)
                selectedAuthorIds.add(author.getAuthorId());

            double price = Double.parseDouble(priceStr);
            int quantity = Integer.parseInt(quantityStr);

            if(isEdit){
                bookToEdit.setIsbn(isbn);
                bookToEdit.setTitle(title);
                bookToEdit.setPrice(price);
                bookToEdit.setTotaQuality(quantity);
                bookToEdit.setPublisher_id(publisherId);
                bookToEdit.setCategory_id(categoryId);
                bookToEdit.setAuthorIds(selectedAuthorIds);
                bookToEdit.setBookStatus(bookStatus);
                boolean success = bookController.handleUpdateBook(bookToEdit);
                if(success){
                    JOptionPane.showMessageDialog(this,
                            "Book edited successfully");
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(this,
                            "Edit Operation Failed!!!\nOr A filed is Empty" +
                                    "Or Unselected");
            }
            else {
                var book = new BookDTO(isbn, title, price, publisherId, categoryId, quantity,
                        selectedAuthorIds, bookStatus);
                boolean success = bookController.handleAddBook(book);

                if(success){
                    JOptionPane.showMessageDialog(this,
                            "Book added successfully");
                    dispose();
                }
                else
                    JOptionPane.showMessageDialog(this,
                            "Add book error!!!");
            }
        }
        catch (LibraryException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}
