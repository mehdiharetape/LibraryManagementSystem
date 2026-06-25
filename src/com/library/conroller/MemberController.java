package com.library.conroller;

import com.library.domain.entity.MemberEntity;
import com.library.domain.valueobject.Email;
import com.library.domain.valueobject.Phone;
import com.library.mappers.MemberMapper;
import com.library.services.DTOs.BorrowedBook;
import com.library.services.DTOs.MemberDTO;
import com.library.services.MemberService;

import java.time.LocalDate;
import java.util.List;

public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    //add member
    public boolean handleRegisterMember(MemberDTO member)
    {
        var memberToAdd = MemberMapper.toEntity(member);
        return memberService.registerMember(memberToAdd);
    }

    //get all members
    public List<MemberDTO> handleMembersList(){
        List<MemberDTO> members = memberService.getAllMembers();
        return members;
    }

    //remove member
    public boolean handleRemoveMember(int id){
        return memberService.removeMember(id);
    }

    //update member
    public boolean handleUpdateMember(MemberDTO member)
    {
        var memberToUpdate = MemberMapper.toEntity(member);
        return memberService.updateMember(memberToUpdate);
    }

    //get member by id
    public MemberDTO handleGetMemberById(int id){
        return memberService.getMemberById(id);
    }

    //get member active Loans
    public List<BorrowedBook> handleGetMemberActiveLoanById(int memberId){
        return memberService.memberActiveLoanById(memberId);
    }
}
