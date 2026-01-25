package com.uniClub.member.memberDto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MemberRequest {

    // -------- TEMEL BİLGİLER --------

    @Size(min = 2, max = 50, message = "Name must be between 2-50 characters")
    private String name;

    @Size(min = 2, max = 50, message = "Surname must be between 2-50 characters")
    private String surname;

    @Min(value = 15, message = "Age must be at least 15")
    @Max(value = 80, message = "Age cannot exceed 80")
    private Integer age;

    @Pattern(
            regexp = "^[0-9]{3,15}$",
            message = "Student number must be 3-15 digits"
    )
    private String studentNumber;

    @Size(max = 100, message = "Faculty cannot exceed 100 characters")
    private String faculty;

    @Size(max = 100, message = "Department cannot exceed 100 characters")
    private String department;

    @Pattern(
            regexp = "^(Hazırlık|1|2|3|4)$",
            message = "Level must be Hazırlık, 1, 2, 3 or 4"
    )
    private String level;

    // -------- İLETİŞİM --------

    @Pattern(
            regexp = "^(\\+\\d{1,3})?\\s?\\d{10,12}$",
            message = "Phone number format is invalid"
    )
    private String phone;

    @Size(max = 500, message = "About section cannot exceed 500 characters")
    private String about;

    // -------- SOSYAL MEDYA --------

    @URL(message = "Instagram URL must be valid")
    private String instagram;

    @URL(message = "LinkedIn URL must be valid")
    private String linkedIn;

    @URL(message = "X (Twitter) URL must be valid")
    private String xAccount;

    @URL(message = "GitHub URL must be valid")
    private String github;

    @URL(message = "Website URL must be valid")
    private String websiteUrl;

    // -------- YETENEK / İLGİ --------

    private List<@Size(min = 1, max = 50) String> skills = new ArrayList<>();

    private List<@Size(min = 1, max = 50) String> interests = new ArrayList<>();

    private List<@Size(min = 1, max = 100) String> certificates = new ArrayList<>();

    private List<@Size(min = 1, max = 30) String> languages = new ArrayList<>();

    private List<@Size(min = 1, max = 100) String> projects = new ArrayList<>();
}
