package com.library.ui;

import com.library.conroller.*;
import com.library.domain.enums.AdminRole;
import com.library.infrastructure.persistance.CreateConnection;
import com.library.repository.jdbc.*;
import com.library.services.*;
import com.library.ui.adminUI.AdminManagementPanel;
import com.library.ui.authorUI.AuthorManagementPanel;
import com.library.ui.bookUI.BookManagementPanel;
import com.library.ui.categoryUI.CategoryManagementPanel;
import com.library.ui.common.BorderCreator;
import com.library.ui.loanUI.LoanManagementPanel;
import com.library.ui.memberUI.MemberManagementPanel;
import com.library.ui.publisherUI.PublisherManagementPanel;
import com.library.ui.reportUI.ReportManagementPanel;
import com.library.utils.Session;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainFrame extends JFrame
{
    //-------------------------
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    private CreateConnection connection;
    //-------------------------
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    private CategoryRepository categoryRepository;
    private PublisherRepository publisherRepository;

    private MemberRepository memberRepository;
    private LoanBookRepository loanBookRepository;
    private AdminRepository adminRepository;
    private ReportRepository reportRepository;

    private BookService bookService;
    private AuthorService authorService;
    private CategoryService categoryService;
    private PublisherService publisherService;
    private MemberService memberService;
    private LoanBookService loanBookService;
    private AdminService adminService;
    private ReportShowService reportShowService;
    private ReportInventoryToFileService ritf;
    private ReportActiveLoanToFileService altf;
    private ReportPenaltyToFileService rptf;

    private BookController bookController;
    private AuthorController authorController;
    private CategoryController categoryController;
    private PublisherController publisherController;
    private MemberController memberController;
    private LoanBookController loanBookController;
    private AdminController adminController;
    private ReportController reportController;

    //private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel contentPanel;


    private JButton btnHome, btnCreateLoan, btnReports, btnMembers, btnBooks,
                   btnAuthor, btnPublisher, btnCategory, btnAdmin, btnLogout;

    public MainFrame(CreateConnection connection){

        this.connection = connection;

        setTitle("Library Management System");
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //services
        bookRepository = new BookRepository(connection);
        authorRepository = new AuthorRepository(connection);
        categoryRepository = new CategoryRepository(connection);
        publisherRepository = new PublisherRepository(connection);
        memberRepository = new MemberRepository(connection);
        loanBookRepository = new LoanBookRepository(connection);
        adminRepository = new AdminRepository(connection);
        reportRepository = new ReportRepository(connection);

        bookService = new BookService(bookRepository, publisherRepository, categoryRepository,
                authorRepository);
        authorService = new AuthorService(authorRepository);
        categoryService = new CategoryService(categoryRepository);
        publisherService = new PublisherService(publisherRepository);
        memberService = new MemberService(memberRepository);
        loanBookService = new LoanBookService(loanBookRepository, bookRepository);
        adminService = new AdminService(adminRepository);
        reportShowService = new ReportShowService(reportRepository);
        ritf = new ReportInventoryToFileService(reportRepository);
        altf = new ReportActiveLoanToFileService(reportRepository,  memberRepository);
        rptf = new ReportPenaltyToFileService(reportRepository);

        bookController = new BookController(bookService);
        authorController = new AuthorController(authorService);
        categoryController = new CategoryController(categoryService);
        publisherController = new PublisherController(publisherService);
        memberController = new MemberController(memberService);
        loanBookController = new LoanBookController(loanBookService);
        adminController = new AdminController(adminService);
        reportController = new ReportController(reportShowService, ritf, altf, rptf);

        //content panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        add(contentPanel, BorderLayout.CENTER);

        //side panel
        var sidebarPanel = createSidebar();
        add(sidebarPanel, BorderLayout.WEST);

        //home page
        var homePanel = new JPanel(new BorderLayout(1,1));
        var currentAdmin = Session.get();
        if(Session.isLoggedIn()){
            String txtGreet = "'"+currentAdmin.getAdminName() + "', Welcome to Library Management System";
            String txtInfo = "username : " + currentAdmin.getAdminUserName() +
                    "   |   Role : " + currentAdmin.getAdminRole();
            var formatted = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String txtTime = "Login Time : " + LocalDateTime.now().format(formatted);

            var northPanel = new JPanel(new BorderLayout(6,6));
            northPanel.setBorder(BorderCreator.createBorder("Login Info"));
            northPanel.add(new JLabel(txtGreet), BorderLayout.NORTH);
            northPanel.add(new JLabel(txtInfo), BorderLayout.CENTER);
            northPanel.add(new JLabel(txtTime), BorderLayout.SOUTH);

            var southPanel = new JPanel(new FlowLayout(10,10,7));
            btnLogout = new JButton("Logout");
            btnLogout.addActionListener(e -> logout());
            southPanel.add(btnLogout, BorderLayout.NORTH);

            homePanel.add(northPanel, BorderLayout.NORTH);
            homePanel.add(southPanel, BorderLayout.SOUTH);
        }
        homePanel.setBackground(new Color(205, 205, 205));
        //book panel
        var bookPanel = new BookManagementPanel(bookController, authorController,
                categoryController,publisherController);
        //author panel
        var authorPanel = new AuthorManagementPanel(authorController, bookController);
        //category panel
        var categoryPanel = new CategoryManagementPanel(categoryController,bookController);
        //publisher panel
        var publisherPanel = new PublisherManagementPanel(publisherController, bookController);
        //member panel
        var memberPanel = new MemberManagementPanel(memberController);
        //borrow panel
        var borrowPanel = new LoanManagementPanel(loanBookController, reportController,
                memberController, bookController);
        //admin panel
        var adminPanel = new AdminManagementPanel(adminController);
        //report panel
        var reportPanel = new ReportManagementPanel(reportController);

        //add
        if(Session.get().getAdminRole().equals(AdminRole.SUPER_ADMIN.toString())){
            contentPanel.add(homePanel, "HOME");
            contentPanel.add(bookPanel, "BOOKS");
            contentPanel.add(authorPanel, "AUTHORS");
            contentPanel.add(categoryPanel, "CATEGORY");
            contentPanel.add(publisherPanel, "PUBLISHER");
            contentPanel.add(memberPanel, "MEMBER");
            contentPanel.add(borrowPanel, "BORROW");
            contentPanel.add(adminPanel,"ADMIN");
            contentPanel.add(reportPanel, "REPORT");
        }
        else {
            contentPanel.add(homePanel, "HOME");
            contentPanel.add(bookPanel, "BOOKS");
            contentPanel.add(authorPanel, "AUTHORS");
            contentPanel.add(categoryPanel, "CATEGORY");
            contentPanel.add(publisherPanel, "PUBLISHER");
            contentPanel.add(memberPanel, "MEMBER");
            contentPanel.add(borrowPanel, "BORROW");
        }


        //center panel
        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar(){
        var sideBar = new JPanel();
        sideBar.setBorder(BorderCreator.createBorder("Management"));
        sideBar.setLayout(new GridLayout(10,1,10,0));
        sideBar.setBackground(Color.WHITE);
        sideBar.setPreferredSize(new Dimension(180, 0));

        btnHome = new JButton("Home");
        btnHome.setFocusPainted(false);

        btnCreateLoan = new JButton("Borrows");
        btnCreateLoan.setFocusPainted(false);

        btnReports = new JButton("Reports");
        btnReports.setFocusPainted(false);

        btnMembers = new JButton("Members");
        btnMembers.setFocusPainted(false);

        btnBooks = new JButton("Books");
        btnBooks.setFocusPainted(false);

        btnAuthor = new JButton("Authors");
        btnAuthor.setFocusPainted(false);

        btnPublisher = new JButton("Publishers");
        btnPublisher.setFocusPainted(false);

        btnCategory = new JButton("Categories");
        btnCategory.setFocusPainted(false);

        btnAdmin = new JButton("Admins");
        btnAdmin.setFocusPainted(false);


        //listeners
        btnHome.addActionListener(e -> cardLayout.show(contentPanel, "HOME"));
        btnBooks.addActionListener(e -> cardLayout.show(contentPanel, "BOOKS"));
        btnAuthor.addActionListener(e -> cardLayout.show(contentPanel, "AUTHORS"));
        btnCategory.addActionListener(e -> cardLayout.show(contentPanel, "CATEGORY"));
        btnPublisher.addActionListener(e -> cardLayout.show(contentPanel, "PUBLISHER"));
        btnMembers.addActionListener(e -> cardLayout.show(contentPanel, "MEMBER"));
        btnCreateLoan.addActionListener(e -> cardLayout.show(contentPanel, "BORROW"));
        btnAdmin.addActionListener(e -> cardLayout.show(contentPanel, "ADMIN"));
        btnReports.addActionListener(e -> cardLayout.show(contentPanel, "REPORT"));
        //add
        sideBar.add(btnHome);
        sideBar.add(btnCreateLoan);
        sideBar.add(btnMembers);
        sideBar.add(btnBooks);
        sideBar.add(btnAuthor);
        sideBar.add(btnPublisher);
        sideBar.add(btnCategory);
        if(Session.get().getAdminRole().equals(AdminRole.SUPER_ADMIN.toString())){
            sideBar.add(btnReports);
            sideBar.add(btnAdmin);
        }

        return sideBar;
    }

    //methods that helps to update status of expired loans in background
    public void startBackgroundTask(){
        Runnable task = () -> {
            try {
                loanBookController.handleCheckExpiredLoan();
                System.out.println("Start thread");
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        };
        scheduler.scheduleAtFixedRate(task, 0, 10, TimeUnit.MINUTES);
    }

    //logout
    private void logout(){
        int logConf =
                JOptionPane.showConfirmDialog(this, "Are you sure yo logging out?");
        if(logConf == JOptionPane.YES_OPTION){
            Session.clear();
            dispose();
            new LoginFrame(connection).setVisible(true);
        }
    }

    public static void main(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e) {e.printStackTrace();}

        CreateConnection conn = CreateConnection.getInstance();
        SwingUtilities.invokeLater(() -> {
            var frame = new LoginFrame(conn);
            frame.setVisible(true);
        });
    }
}
