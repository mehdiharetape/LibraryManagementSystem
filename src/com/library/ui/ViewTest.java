package com.library.ui;

import com.library.conroller.AuthorController;
import com.library.conroller.BookController;
import com.library.conroller.ReportController;
import com.library.conroller.SearchBookController;
import com.library.repository.jdbc.*;
import com.library.services.*;
import com.library.infrastructure.persistance.CreateConnection;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ViewTest {
    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    private static final CreateConnection connection =
            CreateConnection.getInstance();

    //publisher
    private static PublisherRepository publisherRepository;

    //category
    private static CategoryRepository categoryRepository;

    //search
    private static SearchBookRepository searchBookRepository;
    private static SearchBookService searchBookService;
    private static SearchBookController searchBookController;

    private static AuthorRepository authorRepository;
    private static AuthorService authorService;
    private static AuthorController authorController;

    //book
    private static BookRepository bookRepository;
    private static BookService bookService;
    private static BookController bookController;

    //report
    private ReportRepository reportRepository;
    private MemberRepository memberRepository;
    private ReportShowService reportShowService;
    private ReportInventoryToFileService reportToFileService;
    private ReportActiveLoanToFileService reportActiveLoanToFileService;
    private ReportController report;

    //loan
    private LoanBookRepository loanBookRepository;
    private LoanBookService loanBookService;

    public ViewTest(){
        searchBookRepository = new SearchBookRepository(connection);
        searchBookService = new SearchBookService(searchBookRepository);
        searchBookController = new SearchBookController(searchBookService);

        publisherRepository = new PublisherRepository(connection);
        categoryRepository = new CategoryRepository(connection);

        authorRepository = new AuthorRepository(connection);
        authorService = new AuthorService(authorRepository);
        authorController = new AuthorController(authorService);

        bookRepository = new BookRepository(connection);
        bookService = new BookService(bookRepository, publisherRepository, categoryRepository, authorRepository);
        bookController = new BookController(bookService);

        reportRepository = new ReportRepository(connection);
        memberRepository = new MemberRepository(connection);
        reportShowService = new ReportShowService(reportRepository);
        reportToFileService = new ReportInventoryToFileService(reportRepository);
        reportActiveLoanToFileService = new ReportActiveLoanToFileService(reportRepository,
                memberRepository);
//        report = new ReportController(reportShowService, reportToFileService,
//                reportActiveLoanToFileService);

        loanBookRepository = new LoanBookRepository(connection);
        loanBookService = new LoanBookService(loanBookRepository,
                bookRepository);
    }

//     public static void main() {
//        //status update
//        //startBackgroundTask(loanBookService);
//
//        //get books
//        System.out.println("...................................");
//        List<BookDTO> books = bookController.handleBookList();
//        for (BookDTO b : books)
//            System.out.println(b.getBookId() + " - " + b.getTitle() + " - " + b.getIsbn() +
//                    " - " + b.getPrice() + " - " + b.getPublisher_name() + " - " + b.getCategory_name() +
//                    " - " + b.getTotaQuality());
//
//
//        //report
//        System.out.println("--------report-----------------");
//        //List<InventoryReportDTO> inventories = report.handleInventoryReport();
////        for(InventoryReportDTO i : inventories)
////            System.out.println("id : " + i.getBookId() + " | name : " + i.getBookName() +
////                    " | total :  " + i.getTotal() +
////                    " | in shelve :  " + i.getInShelve() + " | in loan : " + i.getInLoan());
//
//         //all a publisher's book
//         System.out.println("--------publisher book-----------------");
//         List<BookListDTO> bookLists = bookController.handleGetAllPublisherBook(1006);
//         for (BookListDTO b : bookLists)
//             System.out.println("id : " + b.getBookId() + " | title : " + b.getTitle() +
//                     " | total :  " + b.getTotalQuantity() + " | isbn :  " + b.getIsbn());
//
//         //all a publisher's book
//         System.out.println("--------category book-----------------");
//         bookLists = bookController.handleGetAllCategoryBook(7);
//         for (BookListDTO b : bookLists)
//             System.out.println("id : " + b.getBookId() + " | title : " + b.getTitle() +
//                     " | total :  " + b.getTotalQuantity() + " | isbn :  " + b.getIsbn());
//
//         //all a publisher's book
//         System.out.println("--------book of an author-----------------");
//         bookLists = bookController.handleGetAllBooksOfAuthor(2006);
//         for (BookListDTO b : bookLists)
//             System.out.println("id : " + b.getBookId() + " | title : " + b.getTitle() +
//                     " | total :  " + b.getTotalQuantity() + " | isbn :  " + b.getIsbn());
//
//         System.out.println("--------authors of a book-----------------");
//         List<String> aus = bookController.handleGetAllAuthorsOfBook(301);
//         for (String a : aus)
//             System.out.println("author name : " + a);
//
//         //inventory report to file
//
//
//
//         //search
//         String keyWord = "prog";
//         System.out.println("=========result search for (" + keyWord + ") ========");
//         List<BookSearchDTO> foundedBooks = searchBookController.handleSearchBook(keyWord);
//         for(BookSearchDTO b : foundedBooks)
//             System.out.println(b.getBookId() + " || " + b.getTitle() + " || " + b.getIsbn() +
//                     " || " + b.getPrice() + " || " + b.getTotaQuality());
//
//        //close connection after work
//        connection.closeConnection();
//    }

    //methods that helps to update status of expired loans in backgroun
    private static void startBackgroundTask(LoanBookService loanBookService){
        Runnable task = () ->{
            try {
                //System.out.println("[System check] . Check expired loans");
                loanBookService.checkAndExpireLoans();
                //System.out.println("[System check] . Checked Successfully");
            }
            catch (Exception e){
                System.out.println("[System Error] " + e.getMessage());
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, 120, TimeUnit.SECONDS);
    }

//    private static void runMainMenu(){
//        var loanBookController = new LoanBookController();
//        var bookController = new BookController();
//        try (var in = new Scanner(System.in)){
//            int command = 0;
//            while (true){
//                System.out.println("Enter command\n1)loan book\n2)books list" +
//                        "\n3)inventory Report\n4)exit");
//                command = in.nextInt();
//                switch (command){
//                    case 1:{
//                        System.out.print("Enter member id : ");
//                        int member_id = in.nextInt();
//                        System.out.print("Enter book id : ");
//                        int book_id = in.nextInt();
//                        System.out.println("Enter (from date) year-month-day xxxx-xx-xx:");
//                        int year = in.nextInt();
//                        int month = in.nextInt();
//                        int day = in.nextInt();
//                        LocalDate fromDate = LocalDate.of(year, month, day);
//                        System.out.println("Enter (to date) year-month-day xxxx-xx-xx:");
//                        year = in.nextInt();
//                        month = in.nextInt();
//                        day = in.nextInt();
//                        LocalDate toDate = LocalDate.of(year, month, day);
//                        var loanBook = new LoanBookEntity(member_id, book_id, fromDate,
//                                toDate, LoanStatus.ACTIVE);
//                        loanBookController.handleCreateLoan(loanBook);
//                        break;
//                    }
//                    case 2 : {
//                        List<BookDTO> books = bookController.handleBookList();
//                        for (BookDTO b : books)
//                            System.out.println(b.getBookId() + " | " + b.getTitle() + " | " +
//                                    b.getIsbn() + " | " + b.getPrice() + " | " + b.getTotaQuality());
//                        break;
//                    }
//                    case 3:{
//                        List<InventoryReportDTO> ins = reportController.handleInventoryReport();
//                        for (InventoryReportDTO inc : ins)
//                            System.out.println("book id : " + inc.getBookId() + " | title : " +
//                                    inc.getBookName() +
//                                    " | in shelve : " + inc.getInShelve() + " | in loan : " + inc.getInLoan() +
//                                    " | total quantity : " + inc.getTotal());
//                        break;
//                    }
//                    case 4: {
//                        System.out.println("Bye !!!");
//                        System.exit(0);
//                    }
//                }
//            }
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
//    }
}
