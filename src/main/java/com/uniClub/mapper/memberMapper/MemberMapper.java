package com.uniClub.mapper.memberMapper;

import com.uniClub.dto.memberDto.MemberRequest;
import com.uniClub.dto.memberDto.MemberResponse;
import com.uniClub.entity.memberEntity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy =
                org.mapstruct.NullValuePropertyMappingStrategy.IGNORE
)
public interface MemberMapper {

    Member toEntity(MemberRequest memberRequest);

    void updateEntityFromRequest(
            MemberRequest request,
            @MappingTarget Member member
    );

    MemberResponse toDto(Member member);


}
