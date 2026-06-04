package com.uniClub.member.memberRepository;

import com.uniClub.member.memberEntity.Member;
import com.uniClub.user.userEntity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUser(UserEntity user);

    Optional<Member> findByStudentNumber(String studentNumber);

    Optional<Member> findByUserUsername(String username);

    @Query("SELECT m FROM Member m WHERE " +
            "(:filter IS NULL OR :filter = '' OR " +
            "LOWER(m.name) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(m.surname) LIKE LOWER(CONCAT('%', :filter, '%')) OR " +
            "LOWER(m.studentNumber) LIKE LOWER(CONCAT('%', :filter, '%')))")
    Page<Member> searchMembers(@Param("filter") String filter, Pageable pageable);
}