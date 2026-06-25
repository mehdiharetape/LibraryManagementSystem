package com.library.mappers;

import com.library.domain.entity.LoanPenaltyEntity;
import com.library.domain.enums.PayStatus;
import com.library.services.DTOs.LoanBookDto;
import com.library.services.DTOs.LoanPenaltyDTO;

public class PenaltyMapper {
    public static LoanPenaltyEntity toEntity(LoanPenaltyDTO dto){
        var entity = new LoanPenaltyEntity();
        entity.setLoanId(dto.getLoanId());
        entity.setReturnDate(dto.getReturnDate());
        entity.setLateDays(dto.getLateDays());
        entity.setPenaltyCost(dto.getPenaltyCost());
        entity.setPayStatus(PayStatus.valueOf(dto.getPayStatus()));

        return entity;
    }
}
