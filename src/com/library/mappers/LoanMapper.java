package com.library.mappers;

import com.library.domain.entity.LoanBookEntity;
import com.library.services.DTOs.LoanBookDto;

public class LoanMapper {
    public static LoanBookEntity toEntity(LoanBookDto dto){
        LoanBookEntity entity = new LoanBookEntity();
        entity.setLoanId(dto.getLoanId());
        entity.setMemberId(dto.getMemberId());
        entity.setBookId(dto.getBookId());
        entity.setFromDate(dto.getFromDate());
        entity.setToDate(dto.getToDate());
        entity.setStatus(dto.getStatus());
        return entity;
    }
}
