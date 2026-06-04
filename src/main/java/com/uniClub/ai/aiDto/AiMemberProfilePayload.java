package com.uniClub.ai.aiDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiMemberProfilePayload {

    private Long memberId;

    private String name;
    private String surname;
    private Integer age;

    private String faculty;
    private String department;
    private String level;
    private String university;

    private String about;

    private List<String> skills;
    private List<String> interests;
    private List<String> certificates;
    private List<String> languages;
    private List<String> projects;
}