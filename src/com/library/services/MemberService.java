package com.library.services;

import com.library.domain.entity.MemberEntity;
import com.library.domain.exception.MemberNotFoundException;
import com.library.repository.jdbc.MemberRepository;
import com.library.services.DTOs.BorrowedBook;
import com.library.services.DTOs.MemberDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    //add new member
    public boolean registerMember(MemberEntity member){
        return memberRepository.create(member);
    }

    //get all members
    public List<MemberDTO> getAllMembers(){
        if(memberRepository.retrieve().isEmpty())
            return new ArrayList<>();
        return memberRepository.retrieve();
    }

    //remove member
    public boolean removeMember(int id){
        MemberDTO founded = memberRepository.getMemberById(id);
        if(founded == null)
            throw new MemberNotFoundException();
        return memberRepository.remove(id);
    }

    //update member
    public boolean updateMember(MemberEntity member)
    {
        MemberDTO founded = memberRepository.getMemberById(member.getMemberId());
        if(founded == null)
            throw new MemberNotFoundException();
        return memberRepository.update(member);
    }

    //get member by id
    public MemberDTO getMemberById(int id){
        MemberDTO founded = memberRepository.getMemberById(id);
        if(founded == null)
            throw new MemberNotFoundException();
        return founded;
    }

    //get member active loan
    public List<BorrowedBook> memberActiveLoanById(int memberId){
        var actives = memberRepository.getMemberLoanedBooks(memberId);
        if(actives == null)
            return new ArrayList<>();
        return actives;
    }
}
