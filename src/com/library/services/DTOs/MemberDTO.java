package com.library.services.DTOs;

import com.library.domain.entity.Model;

import java.sql.Date;
import java.time.LocalDate;

public class MemberDTO implements Model {
    private Integer memberId;
    private String fullName;
    private String nationalCode;
    private LocalDate birthDate;
    private String address;
    private LocalDate registerDate;
    private String email;
    private String phoneNumber;

    public MemberDTO(Integer memberId ,String fullName, String nationalCode, LocalDate birthDate,
                        String address, LocalDate registerDate, String email, String phoneNumber)
    {
        this.memberId = memberId;
        this.fullName = fullName;
        this.nationalCode = nationalCode;
        this.birthDate = birthDate;
        this.address = address;
        this.registerDate = registerDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public static LocalDate setRegisterDate(Date sqlDate){
        LocalDate registerDate = (sqlDate != null) ? sqlDate.toLocalDate() : null;
        return registerDate;
    }

    public Integer getId(){
        return memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getRegisterDate() {
        return registerDate;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public String getEmail(){
        return email;
    }

    public String getPhoneNumber(){return phoneNumber;}

    //setters

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}
}
