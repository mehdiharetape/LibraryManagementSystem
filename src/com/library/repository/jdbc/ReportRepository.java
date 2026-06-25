package com.library.repository.jdbc;

import com.library.services.DTOs.BorrowedBook;
import com.library.services.DTOs.InfoActiveLoan;
import com.library.services.DTOs.InventoryReportDTO;
import com.library.infrastructure.persistance.CreateConnection;
import com.library.services.DTOs.PenaltyReportDTO;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportRepository {
    private final CreateConnection connection;

    public ReportRepository(CreateConnection connection){
        this.connection = connection;
    }

    //Inventory Report
    public List<InventoryReportDTO> inventoryReport(){
        String sql = """
                SELECT b.book_id,b.title,b.isbn,b.book_status,b.total_quantity AS total_copies,
                COALESCE(active_loans.borrowed_count, 0) AS currently_borrowed,
                (b.total_quantity - COALESCE(active_loans.borrowed_count,0)) AS available_in_shelf
                FROM books b
                LEFT JOIN(SELECT book_id_system, COUNT(*) AS borrowed_count
                FROM loanbooks WHERE (status='ACTIVE' OR status='OVERDUE')GROUP BY book_id_system
                ) AS active_loans ON b.book_id = active_loans.book_id_system;
                """;
        List<InventoryReportDTO> inventories = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
             ResultSet r = statement.executeQuery())
        {
            while (r.next())
                inventories.add(new InventoryReportDTO(r.getInt("book_id"),
                        r.getString("title"),
                        r.getString("isbn"),r.getString("book_status"),
                        r.getInt("available_in_shelf"),
                        r.getInt("currently_borrowed"), r.getInt("total_copies")));
            return inventories;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //get list of members that have loan (with their book list)
    public List<InfoActiveLoan> memberLoanList(){
        String sql = """
                SELECT 
                m.member_id,m.full_name,
                b.title, b.isbn,
                l.from_date, l.to_date, l.status
                FROM members m
                JOIN loanbooks l ON m.member_id = l.member_id
                JOIN books b ON l.book_id_system = b.book_id
                WHERE l.status IN ('ACTIVE', 'OVERDUE')
                ORDER BY m.member_id
                """;
        Map<Integer, InfoActiveLoan> map = new HashMap<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
             ResultSet r = statement.executeQuery())
        {
            while (r.next()){
                int memberId = r.getInt("member_id");
                InfoActiveLoan dto = map.get(memberId);
                if(dto == null){
                    dto = new InfoActiveLoan(memberId,r.getString("full_name"),
                            new ArrayList<>());
                    map.put(memberId, dto);
                }
                BorrowedBook book = new BorrowedBook(r.getString("title"),
                        r.getString("isbn"),
                        r.getString("from_date"), r.getString("to_date"));
                dto.getActiveLoans().add(book);
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
        return new ArrayList<>(map.values());
    }

    //Penalty Report
    public List<PenaltyReportDTO> getAllPenalties(LocalDate fromDate, LocalDate toDate) {
        String sqlNoFilter = """
                SELECT
                l.loan_id,
                m.full_name,
                b.title,
                l.to_date,
                p.return_date, p.late_days, p.penalty_cost, p.pay_status
                FROM penalty p
                JOIN loanbooks l ON p.loan_id = l.loan_id
                JOIN members m ON l.member_id = m.member_id
                JOIN books b ON l.book_id_system = b.book_id
                """;
        String sqlFilter = """
                SELECT
                l.loan_id,
                m.full_name,
                b.title,
                l.to_date,
                p.return_date, p.late_days, p.penalty_cost, p.pay_status
                FROM penalty p
                JOIN loanbooks l ON p.loan_id = l.loan_id
                JOIN members m ON l.member_id = m.member_id
                JOIN books b ON l.book_id_system = b.book_id
                WHERE p.return_date BETWEEN ? AND ?
                """;
        List<PenaltyReportDTO> penalties = new ArrayList<>();
        if (fromDate == null || toDate == null) {
            try (PreparedStatement noFilterSt = connection.getConnection().prepareStatement(sqlNoFilter);
                 ResultSet rs = noFilterSt.executeQuery();) {
                while (rs.next())
                    penalties.add(new PenaltyReportDTO(
                            rs.getInt("loan_id"), rs.getString("full_name"),
                            rs.getString("title"),
                            LocalDate.parse(rs.getString("to_date")),
                            LocalDate.parse(rs.getString("return_date")),
                            rs.getInt("late_days"),
                            rs.getDouble("penalty_cost"))
                    );
                return penalties;
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            try (PreparedStatement filterSt = connection.getConnection().prepareStatement(sqlFilter);)
            {
                filterSt.setDate(1, Date.valueOf(fromDate));
                filterSt.setDate(2, Date.valueOf(toDate));
                try (ResultSet rsf = filterSt.executeQuery()){
                    while (rsf.next()) {
                        penalties.add(new PenaltyReportDTO(
                                rsf.getInt("loan_id"), rsf.getString("full_name"),
                                rsf.getString("title"),
                                LocalDate.parse(rsf.getString("to_date")),
                                LocalDate.parse(rsf.getString("return_date")),
                                rsf.getInt("late_days"),
                                rsf.getDouble("penalty_cost"))
                        );
                    }
                    return penalties;
                }
            } catch (SQLException ex) {
                throw new RuntimeException(ex.getMessage());
            }
        }
    }

    public static void main(String[] args){
        var data = new ReportRepository(CreateConnection.getInstance());
        var all = data.getAllPenalties(null, null);
        for (PenaltyReportDTO pa : all)
            System.out.println(pa.getLoanId() + "-" + pa.getBookTitle() + "-" + pa.getExpireDate() +
                    "-return date : " + pa.getReturnDate() + "-" + pa.getMemberName() +
                    "-"+ pa.getLateDays());

        System.out.println("-------------------------");
        var filtered = data.getAllPenalties(LocalDate.of(2026,6,24),
                LocalDate.of(2026, 6, 25));
        for (PenaltyReportDTO pa : filtered)
            System.out.println(pa.getLoanId() + "-" + pa.getBookTitle() + "-" + pa.getExpireDate() +
                    "-return date : " + pa.getReturnDate() + "-" + pa.getMemberName() +
                    "-"+ pa.getLateDays());
    }
}
