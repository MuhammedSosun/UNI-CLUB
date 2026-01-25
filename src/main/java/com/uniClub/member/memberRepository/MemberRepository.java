package com.uniClub.repository.memberRepository;

import com.uniClub.entity.memberEntity.Member;
import com.uniClub.entity.userEntity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member> findByUser(UserEntity user);

    Optional<Member> findByStudentNumber(String studentNumber);

    Optional<Member> findByUserUsername(String username);


}
