package com.uniClub.member.memberMapper;

import com.uniClub.enums.ClubRole;
import com.uniClub.member.memberDto.MemberRequest;
import com.uniClub.member.memberDto.MemberResponse;
import com.uniClub.member.memberEntity.Member;
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

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.username", target = "username")

    @Mapping(target = "canCreateEvent", expression = "java(checkAuthority(member))")

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

    default boolean checkAuthority(Member member) {
        if (member.getClubMemberships() == null) {
            return false;
        }

        return member.getClubMemberships().stream()
                .anyMatch(membership ->
                        membership.getRole() == ClubRole.PRESIDENT ||
                                membership.getRole() == ClubRole.VICE_PRESIDENT
                );
    }
}