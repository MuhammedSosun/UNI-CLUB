package com.uniClub.Club.clubRepository;

import com.uniClub.Club.clubEntity.ClubEntity;
import com.uniClub.enums.StatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClubRepository extends JpaRepository<ClubEntity, Long> {
    Boolean existsByClubName(String clubName);
    List<ClubEntity> findAllByStatus(StatusEnum status);

    Page<ClubEntity> findAllByStatus(StatusEnum status, Pageable pageable);

    boolean existsByIdAndStatus(Long id, StatusEnum status);
    @Query("""
    SELECT c FROM ClubEntity c 
    WHERE LOWER(c.clubName) LIKE LOWER(CONCAT('%', :filter, '%'))
       OR LOWER(c.shortName) LIKE LOWER(CONCAT('%', :filter, '%'))
""")
    Page<ClubEntity> searchByClubNameOrShortName(
            @Param("filter") String filter,
            Pageable pageable);

    long countByStatus(StatusEnum status);
    Optional<ClubEntity> findByPresidentId(UUID presidentId);
}
