package com.library.repository.jdbc;

import com.library.domain.entity.LoanBookEntity;
import com.library.domain.entity.LoanPenaltyEntity;
import com.library.domain.enums.LoanStatus;
import com.library.infrastructure.persistance.CreateConnection;
import com.library.services.DTOs.LoanBookDto;
import com.library.services.DTOs.LoanBookListDTO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanBookRepository {
    private final CreateConnection connection;

    public LoanBookRepository(CreateConnection connection){
        this.connection = connection;
    }

    //create new loan
    public boolean create(LoanBookEntity loanBook){
        String sql = """
                INSERT INTO loanbooks 
                (member_id,book_id_system,from_date,to_date,status)
                VALUES (?,?,?,?,?)
                """;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, loanBook.getMemberId());
            statement.setInt(2, loanBook.getBookId());
            statement.setObject(3, loanBook.getFromDate());
            statement.setObject(4, loanBook.getToDate());
            statement.setString(5, loanBook.getStatus().toString());

            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //book count increment and change loan status
    public void updateExpiredLoans(){
        String updateStatusQuery = """
                UPDATE loanbooks SET status = 'OVERDUE' 
                WHERE to_date < CURRENT_DATE AND status='ACTIVE'
                """;

        try (PreparedStatement psStatus = connection.getConnection().prepareStatement(updateStatusQuery))
        {
            int affectedLoanBooks = psStatus.executeUpdate();
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //active loan of a book that a member borrowed
    public boolean hasActiveLoanForBook(int memberId, int bookId){
        String sql = """
                SELECT COUNT(*) FROM loanbooks 
                WHERE member_id=? AND book_id_system=? 
                AND status='ACTIVE'
                """;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql))
        {
            statement.setInt(1, memberId);
            statement.setInt(2, bookId);
            ResultSet rs = statement.executeQuery();
            if(rs.next())
                return rs.getInt(1) > 0;
            return false;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //count number of books that available (not borrowed) now
    public int availableBookNow(int bookId){
        String sql = """
                SELECT 
                b.total_quantity - COUNT(CASE WHEN lb.status='ACTIVE' THEN 1 END)
                AS available_quantity
                FROM books b
                LEFT JOIN loanbooks lb ON b.book_id=lb.book_id_system
                WHERE b.book_id=? GROUP BY b.total_quantity
                """;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, bookId);
            try (ResultSet r = statement.executeQuery();){
                if(r.next())
                    return r.getInt("available_quantity");
                else
                    return -1;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //get all loans
    public List<LoanBookListDTO> getAllLoans(LocalDate fromDate, LocalDate toDate)
    {
        String noFilterSql = """
            SELECT 
            loanbooks.loan_id, loanbooks.from_date, loanbooks.to_date, loanbooks.status,
            members.member_id, members.full_name, 
            books.book_id ,books.title, books.book_status
            FROM loanbooks
            JOIN members ON loanbooks.member_id=members.member_id
            JOIN books ON loanbooks.book_id_system=books.book_id
            ORDER BY loanbooks.loan_id DESC
            """;

        String filterSql = """
            SELECT 
            loanbooks.loan_id, loanbooks.from_date, loanbooks.to_date, loanbooks.status,
            members.member_id, members.full_name, 
            books.book_id ,books.title, books.book_status
            FROM loanbooks
            JOIN members ON loanbooks.member_id=members.member_id
            JOIN books ON loanbooks.book_id_system=books.book_id
            WHERE loanbooks.to_date BETWEEN ? AND ?
            ORDER BY loanbooks.loan_id DESC
            """;

        List<LoanBookListDTO> loans = new ArrayList<>();
        if(fromDate == null || toDate == null){
            try (PreparedStatement statement = connection.getConnection().prepareStatement(noFilterSql);
                 ResultSet r = statement.executeQuery())
            {
                while (r.next()){
                    var loan = new LoanBookListDTO(r.getInt("loan_id"), r.getInt("member_id"),
                            r.getString("full_name"), r.getInt("book_id"),
                            r.getString("title"), r.getString("book_status"),
                            LocalDate.parse(r.getString("from_date")),
                            LocalDate.parse(r.getString("to_date")),
                            LoanStatus.valueOf(r.getString("status")));
                    loans.add(loan);
                }
                return loans;
            }
            catch (SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        else {
            try (PreparedStatement filterSt = connection.getConnection().prepareStatement(filterSql);)
            {
                filterSt.setDate(1, Date.valueOf(fromDate));
                filterSt.setDate(2, Date.valueOf(toDate));
                try (ResultSet rs = filterSt.executeQuery()){
                    while (rs.next()){
                        var loan = new LoanBookListDTO(rs.getInt("loan_id"),
                                rs.getInt("member_id"),
                                rs.getString("full_name"), rs.getInt("book_id"),
                                rs.getString("title"), rs.getString("book_status"),
                                LocalDate.parse(rs.getString("from_date")),
                                LocalDate.parse(rs.getString("to_date")),
                                LoanStatus.valueOf(rs.getString("status")));
                        loans.add(loan);
                    }
                    return loans;
                }
            }
            catch (SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    //get loan by id
    public LoanBookListDTO getLoanById(int loanId){
        String sql = """
            SELECT 
            loanbooks.loan_id, loanbooks.from_date, loanbooks.to_date, loanbooks.status,
            members.member_id, members.full_name, 
            books.book_id ,books.title, books.book_status
            FROM loanbooks
            JOIN members ON loanbooks.member_id=members.member_id
            JOIN books ON loanbooks.book_id_system=books.book_id
            WHERE loanbooks.loan_id=?
            """;
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1,loanId);
            try (ResultSet r = statement.executeQuery()){
                if(r.next())
                    return new LoanBookListDTO(r.getInt("loan_id"),
                            r.getInt("member_id"),
                            r.getString("full_name"), r.getInt("book_id"),
                            r.getString("title"), r.getString("book_status"),
                            LocalDate.parse(r.getString("from_date")),
                            LocalDate.parse(r.getString("to_date")),
                            LoanStatus.valueOf(r.getString("status")));
            }
            return null;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //count active loans for a member
    public int memberActiveLoans(int memberId){
        String sql = """
                SELECT COUNT(*) AS active_loans
                FROM loanbooks
                WHERE member_id=?
                AND status='ACTIVE'
                """;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, memberId);
            int count = 0;
            try (ResultSet rs = statement.executeQuery()){
                if(rs.next())
                    count = rs.getInt("active_loans");
            }
            return count;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //create penalty record
    public boolean addToPenalty(LoanPenaltyEntity entity){
        String sql = """
                INSERT INTO penalty
                (loan_id,return_date,late_days,penalty_cost)
                VALUES (?,?,?,?)
                """;
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, entity.getLoanId());
            statement.setObject(2, entity.getReturnDate());
            statement.setInt(3, entity.getLateDays());
            statement.setDouble(4, entity.getPenaltyCost());

            connection.getConnection().setAutoCommit(false);
            int rawAffected = statement.executeUpdate();
            boolean isReturned = changeLoanStatus(entity.getLoanId());
            connection.getConnection().commit();

            return rawAffected > 0 && isReturned;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //change loan state to RETURNED
    public boolean changeLoanStatus(int loanId){
        String sql = "UPDATE loanbooks SET status = 'RETURNED' WHERE loan_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, loanId);
            return statement.executeUpdate() > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

}
