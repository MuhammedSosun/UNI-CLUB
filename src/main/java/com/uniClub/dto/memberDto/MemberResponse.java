package com.uniClub.dto.memberDto;

import com.uniClub.entity.baseEntity.BaseEntity;
import com.uniClub.entity.eventEntity.EventParticipation;
import com.uniClub.entity.memberEntity.ClubMembership;
import com.uniClub.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse extends BaseEntity {

    private Long id;
    private StatusEnum status;
    private String name;
    private String surname;
    private Integer age;
    private String studentNumber;
    private String faculty;
    private String department;
    private String level; // Hazırlık, 1,2,3,4
    private String university = "Yalova University";
    private String phone;

    private String profilePhotoPath;
    private String about;

    private String instagram;
    private String linkedIn;
    private String xAccount;
    private String github;
    private String websiteUrl;
    private List<String> skills = new ArrayList<>();

    private List<String> interests = new ArrayList<>();
    private List<String> certificates = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private List<String> projects = new ArrayList<>();

    private List<ClubMembership> clubMemberships = new ArrayList<>();

    private List<EventParticipation> eventParticipations = new ArrayList<>();


}
