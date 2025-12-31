package com.uniClub.mapper.memberMapper;

import com.uniClub.dto.memberDto.MemberRequest;
import com.uniClub.dto.memberDto.MemberResponse;
import com.uniClub.entity.memberEntity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

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

    @Mapping(target = "clubNames", expression = "java(mapClubNames(member))")
    @Mapping(target = "participatedEventTitles", expression = "java(mapEventTitles(member))")
    MemberResponse toDto(Member member);

    default List<String> mapClubNames(Member member) {
        if (member.getClubMemberships() == null) return List.of();
        return member.getClubMemberships()
                .stream()
                .map(cm -> cm.getClub().getClubName())
                .toList();
    }

    default List<String> mapEventTitles(Member member) {
        if (member.getEventParticipations() == null) return List.of();
        return member.getEventParticipations()
                .stream()
                .map(ep -> ep.getEvent().getTitle())
                .toList();
    }


}
