package com.uniClub.member.memberDto;

import com.uniClub.baseEntity.DtoBase;
import com.uniClub.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse extends DtoBase {

    private Long id;
    private StatusEnum status;
    private String name;
    private String surname;
    private Integer age;
    private String studentNumber;
    private String faculty;
    private String department;
    private String level;
    private String university;
    private String phone;

    private String profilePhotoPath;
    private String about;

    private String instagram;
    private String linkedIn;
    private String xAccount;
    private String github;
    private String websiteUrl;

    private List<String> skills;
    private List<String> interests;
    private List<String> certificates;
    private List<String> languages;
    private List<String> projects;

    // 🔥 ENTITY YOK
    private List<String> clubNames;
    private List<String> participatedEventTitles;
}
