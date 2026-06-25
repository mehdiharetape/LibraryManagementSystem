package com.library.domain.entity;

import com.library.domain.exception.*;
import com.library.domain.valueobject.Email;
import com.library.domain.valueobject.Phone;

import java.time.LocalDate;

public class MemberEntity implements Model {
    private Integer memberId;
    private String fullName;
    private String nationalCode;
    private LocalDate birthDate;
    private String address;
    private Email email;
    private Phone phoneNumber;

    public MemberEntity(){}

    public MemberEntity(Integer memberId ,String fullName, String nationalCode, LocalDate birthDate,
                        String address, Email email, Phone phoneNumber)
    {
        if(memberId != null && memberId < 3000)
            throw new MemberNotFoundException();
        this.memberId = memberId;
        validation(fullName, nationalCode, birthDate, address, email, phoneNumber);
        this.fullName = fullName;
        this.nationalCode = nationalCode;
        this.birthDate = birthDate;
        this.address = address;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Integer getMemberId(){
        return memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public LocalDate getRegisterDate() {
        return LocalDate.now();
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

    public Email getEmail(){
        return email;
    }

    public Phone getPhoneNumber(){return phoneNumber;}

    //----setters---------


    public void setMemberId(Integer memberId) {
        if(memberId != null && memberId < 3000)
            throw new MemberNotFoundException();
        this.memberId = memberId;
    }

    public void setFullName(String fullName) {
        if(fullName.length() < 5)
            throw new ValidationException("Full name can't be less than 5 characters!!!");
        this.fullName = fullName;
    }

    public void setNationalCode(String nationalCode) {
        if(nationalCode.length() != 10)
            throw new InvalidNationalCodeException();
        this.nationalCode = nationalCode;
    }

    public void setBirthDate(LocalDate birthDate) {
        if(birthDate == null)
            throw new ValidationException("Birth date can't be empty!!!");
        this.birthDate = birthDate;
    }

    public void setAddress(String address) {
        if(address.length() < 15)
            throw new ValidationException("Address can't be less than 15 characters!!!");
        this.address = address;
    }

    public void setEmail(Email email) {
        if(email == null)
            throw new InvalidEmailException();
        this.email = email;
    }

    public void setPhoneNumber(Phone phoneNumber) {
        if(phoneNumber == null)
            throw new InvalidPhoneException();
        this.phoneNumber = phoneNumber;
    }

    //validation
    private void validation(String fullName, String nationalCode, LocalDate birthDate,
                            String address, Email email, Phone phoneNumber)
    {
        if(fullName.length() < 5)
            throw new ValidationException("Full name can't be less than 5 characters!!!");
        if(nationalCode.length() != 10)
            throw new InvalidNationalCodeException();
        if(birthDate == null)
            throw new ValidationException("Birth date can't be empty!!!");
        if(address.length() < 15)
            throw new ValidationException("Address can't be less than 15 characters!!!");
        if(email == null)
            throw new InvalidEmailException();
        if(phoneNumber == null)
            throw new InvalidPhoneException();
    }
}
