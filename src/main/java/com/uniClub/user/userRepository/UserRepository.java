package com.uniClub.repository.userRepository;

import com.uniClub.entity.userEntity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {


    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :filter, '%')) " +
            "OR LOWER(u.email) LIKE LOWER(CONCAT('%', :filter, '%'))")
    List<UserEntity> searchUsers(@Param("filter") String filter);

    @Query("""
    SELECT u FROM UserEntity u
    WHERE 
      LOWER(u.username) LIKE LOWER(CONCAT('%', :filter, '%'))
      OR LOWER(u.email) LIKE LOWER(CONCAT('%', :filter, '%'))
""")
    Page<UserEntity> searchUsersPaged(@Param("filter") String filter, Pageable pageable);


}
