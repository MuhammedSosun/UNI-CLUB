package com.uniClub.mail.mailRepository;

import com.uniClub.mail.mailEntity.PasswordResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PasswordResetCodeRepository extends JpaRepository<PasswordResetCode, Long> {

    Optional<PasswordResetCode> findByEmailAndCodeAndUsedFalse(String email,String code);

    //normalde Query sadece Select sorgularında çalışır fakat bu anatasyon sayesinde update gibi işlemlerde de çalışır
    @Modifying
    @Transactional
    @Query("UPDATE PasswordResetCode p SET p.used = true WHERE p.email = :email")
    void invalidateAllCodes(@Param("email") String email);

}
