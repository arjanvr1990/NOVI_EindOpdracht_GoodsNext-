package com.arjanvanraamsdonk.goodsnext.repositories;

import com.arjanvanraamsdonk.goodsnext.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Vind een gebruiker op basis van de gebruikersnaam
    Optional<User> findByUsername(String username);

    // Controleer of een gebruiker bestaat op basis van de gebruikersnaam
    boolean existsByUsername(String username);
}
