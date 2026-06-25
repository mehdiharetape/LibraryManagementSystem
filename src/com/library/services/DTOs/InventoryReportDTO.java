package com.library.services.DTOs;

public class InventoryReportDTO {
    private int bookId;
    private String bookName;
    private String isbn;
    private String bookStatus;
    private int inShelve;
    private int inLoan;
    private int total;

    public InventoryReportDTO(int bookId, String bookName, String isbn,String bookStatus,int inShelve,
                              int inLoan, int total)
    {
        this.bookId = bookId;
        this.bookName = bookName;
        this.isbn = isbn;
        this.inShelve = inShelve;
        this.inLoan = inLoan;
        this.total = total;
        this.bookStatus = bookStatus;
    }

    public int getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public String getIsbn(){
        return isbn;
    }

    public int getInShelve() {
        return inShelve;
    }

    public int getInLoan() {
        return inLoan;
    }

    public int getTotal() {
        return total;
    }

    public String getBookStatus(){
        return bookStatus;
    }
}
