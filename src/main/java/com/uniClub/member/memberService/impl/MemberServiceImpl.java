package com.uniClub.service.memberService.impl;

import com.uniClub.dto.memberDto.MemberRequest;
import com.uniClub.dto.memberDto.MemberResponse;
import com.uniClub.entity.memberEntity.Member;
import com.uniClub.entity.userEntity.UserEntity;
import com.uniClub.enums.StatusEnum;
import com.uniClub.exceptions.exception.BaseException;
import com.uniClub.exceptions.exception.ErrorMessage;
import com.uniClub.exceptions.exception.MessageType;
import com.uniClub.mapper.memberMapper.MemberMapper;
import com.uniClub.repository.memberRepository.MemberRepository;
import com.uniClub.service.memberService.IMemberService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements IMemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper  memberMapper;

    public MemberServiceImpl(MemberRepository memberRepository, MemberMapper memberMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
    }


    @Override
    public MemberResponse getMyProfile(UserEntity user) {
        Member member = memberRepository.findByUser(user)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(
                                MessageType.MEMBER_NOT_FOUND,
                                "Profil bulunamadı"
                        )
                ));
        String username = getUsername();
        member.setCreatedBy(username);
        member.setUpdatedBy(username);

        return memberMapper.toDto(member);
    }

    @Override
    public MemberResponse updateMyProfile(MemberRequest memberRequest, UserEntity user) {
        Member member = memberRepository.findByUser(user)
                .orElseThrow(() -> new BaseException(
                        new ErrorMessage(
                                MessageType.MEMBER_NOT_FOUND,
                                "Profil bulunamadı"
                        )
                ));

        memberMapper.updateEntityFromRequest(memberRequest, member);

        member.setStatus(StatusEnum.ACTIVE);

        String username = getUsername();
        member.setCreatedBy(username);
        member.setUpdatedBy(username);

        memberRepository.save(member);

        return memberMapper.toDto(member);
    }

    public String getUsername(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


}
