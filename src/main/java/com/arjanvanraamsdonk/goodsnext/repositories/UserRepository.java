package com.arjanvanraamsdonk.goodsnext.repositories;

import com.arjanvanraamsdonk.goodsnext.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.authorities WHERE u.username = :username")
    Optional<User> findByUsernameWithAuthorities(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.contactInfo.id = :contactInfoId")
    boolean existsByContactInfoId(@Param("contactInfoId") Long contactInfoId);

    @Query("SELECT u FROM User u WHERE u.contactInfo.id = :contactInfoId")
    User findByContactInfoId(@Param("contactInfoId") Long contactInfoId);
}


