package com.uniClub.entity.memberEntity;

import com.uniClub.entity.baseEntity.BaseEntity;
import com.uniClub.entity.eventEntity.EventParticipation;
import com.uniClub.entity.userEntity.UserEntity;
import com.uniClub.enums.MemberStatus;
import com.uniClub.enums.StatusEnum;
import jakarta.persistence.*;
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
@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    // User profiline bağlı
    @OneToOne(fetch =  FetchType.LAZY,optional = false)
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;
    // Temel bilgiler
    private String name;
    private String surname;
    private Integer age;
    private String studentNumber;
    private String faculty;
    private String department;
    private String level; // Hazırlık, 1,2,3,4
    private String university = "Yalova University";

    // İletişim
    private String phone;

    private String profilePhotoPath;
    private String about;

    // Sosyal medya linkleri
    private String instagram;
    private String linkedIn;
    private String xAccount;
    private String github;
    private String websiteUrl;

    // Yetenekler
    @ElementCollection
    @CollectionTable(name = "member_skills", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    // İlgi alanları
    @ElementCollection
    @CollectionTable(name = "member_interests", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "interest")
    private List<String> interests = new ArrayList<>();

    // Sertifikalar
    @ElementCollection
    @CollectionTable(name = "member_certificates", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "certificate")
    private List<String> certificates = new ArrayList<>();

    // Opsiyonel:
    @ElementCollection
    @CollectionTable(name = "member_languages", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "language")
    private List<String> languages = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "member_projects", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "project")
    private List<String> projects = new ArrayList<>();

    @OneToMany(mappedBy = "member",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<ClubMembership> clubMemberships = new ArrayList<>();


    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventParticipation> eventParticipations = new ArrayList<>();

}