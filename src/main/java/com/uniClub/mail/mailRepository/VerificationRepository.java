package com.uniClub.repository.mailRepository;

import com.uniClub.entity.mailEntity.Verification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification,Long> {

    Optional<Verification> findByEmail(String email);

    void deleteByEmail(String email);
}
