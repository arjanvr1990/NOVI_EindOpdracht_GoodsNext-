package com.arjanvanraamsdonk.goodsnext.repository;

import com.arjanvanraamsdonk.goodsnext.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    // Eventueel kan je extra query-methodes toevoegen als dat nodig is, bijvoorbeeld:
    // Optional<Role> findByRoleName(String roleName);
}
