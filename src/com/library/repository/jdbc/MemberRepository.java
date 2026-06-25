package com.library.repository.jdbc;

import com.library.domain.entity.MemberEntity;
import com.library.services.DTOs.BorrowedBook;
import com.library.services.DTOs.InfoActiveLoan;
import com.library.services.DTOs.MemberDTO;
import com.library.infrastructure.persistance.CreateConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberRepository {
    private final CreateConnection connection;

    public MemberRepository(CreateConnection connection){
        this.connection = connection;
    }

    //add member
    public boolean create(MemberEntity entity){
        String sql = """
                INSERT INTO members
                (full_name,national_code,birthdate,address,register_date,email,phone_number)
                VALUES (?,?,?,?,?,?,?)
                """;

        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setString(1,entity.getFullName());
            statement.setString(2, entity.getNationalCode());
            statement.setObject(3, entity.getBirthDate());
            statement.setString(4, entity.getAddress());
            statement.setObject(5, entity.getRegisterDate());
            statement.setString(6, entity.getEmail().getValue());
            statement.setString(7, entity.getPhoneNumber().getValue());

            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //get members
    public List<MemberDTO> retrieve(){
        String sql = "SELECT * FROM members";
        List<MemberDTO> members = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql);
             ResultSet r = statement.executeQuery())
        {
            while (r.next()) {
                members.add(new MemberDTO(r.getInt("member_id"),
                        r.getString("full_name"), r.getString("national_code"),
                        MemberDTO.setRegisterDate(r.getDate("birthdate")),
                        r.getString("address"),
                        MemberDTO.setRegisterDate(r.getDate("register_date")),
                        r.getString("email"), r.getString("phone_number")));
            }
            return members;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //Active Loan Report
    public List<BorrowedBook> getMemberLoanedBooks(int memberId){
        String sql = """
            SELECT b.isbn ,b.title, l.from_date, l.to_date
            FROM loanbooks l
            JOIN books b ON l.book_id_system=b.book_id
            WHERE l.member_id=? AND (l.status='ACTIVE' OR l.status='OVERDUE')
            ORDER BY l.to_date ASC
            """;
        List<BorrowedBook> list = new ArrayList<>();
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, memberId);
            try (ResultSet r = statement.executeQuery();){
                while (r.next())
                    list.add(new BorrowedBook(r.getString("title"),r.getString("isbn"),
                            r.getString("from_date"), r.getString("to_date")));
                return list;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //remove member
    public boolean remove(int memberId){
        String sql = "DELETE FROM members WHERE member_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, memberId);
            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //update member
    public boolean update(MemberEntity member)
    {
        String sql = """
                UPDATE members
                SET full_name=?, national_code=?, birthdate=?,
                address=?, email=?, phone_number=?
                WHERE member_id=?
                """;
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql))
        {
            statement.setString(1, member.getFullName());
            statement.setString(2, member.getNationalCode());
            statement.setObject(3, member.getBirthDate());
            statement.setString(4, member.getAddress());
            statement.setString(5, member.getEmail().getValue());
            statement.setString(6, member.getPhoneNumber().getValue());
            statement.setInt(7, member.getMemberId());

            int rawAffected = statement.executeUpdate();
            return rawAffected > 0;
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //get all member info by id
    public MemberDTO getMemberById(int memberId){
        String sql = "SELECT * FROM members WHERE member_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql))
        {
            statement.setInt(1, memberId);
            try (ResultSet r = statement.executeQuery();){
                if(r.next()) {
                    return new MemberDTO(r.getInt("member_id"),
                            r.getString("full_name"), r.getString("national_code"),
                            MemberDTO.setRegisterDate(r.getDate("birthdate")),
                            r.getString("address"),
                            MemberDTO.setRegisterDate(r.getDate("register_date")),
                            r.getString("email"), r.getString("phone_number"));
                }
                else return null;
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //get only member name by id
    public String getMemberNameById(int memberId){
        String sql = "SELECT full_name FROM members WHERE member_id=?";
        try (PreparedStatement statement = connection.getConnection().prepareStatement(sql)){
            statement.setInt(1, memberId);
            try(ResultSet r = statement.executeQuery()){
                if (r.next())
                    return r.getString("full_name");
                return "Unknown User";
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e.getMessage());
        }
    }
}
