package com.uniClub.repository.clubRepository;

import com.uniClub.entity.clubEntity.ClubEntity;
import com.uniClub.enums.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<ClubEntity, Long> {
    Boolean existsByClubName(String clubName);
    List<ClubEntity> findAllByStatus(StatusEnum status);

    Page<ClubEntity> findAllByStatus(StatusEnum status, Pageable pageable);

    boolean existsByIdAndStatus(Long id, StatusEnum status);


}
