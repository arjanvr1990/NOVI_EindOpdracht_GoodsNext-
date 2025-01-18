package com.arjanvanraamsdonk.goodsnext.service;

import com.arjanvanraamsdonk.goodsnext.dto.RoleDto;
import com.arjanvanraamsdonk.goodsnext.dto.RoleInputDto;
import com.arjanvanraamsdonk.goodsnext.exception.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.Role;
import com.arjanvanraamsdonk.goodsnext.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Haal alle rollen op
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Haal een specifieke rol op
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Role not found with id: " + id));
        return convertToDto(role);
    }

    // Voeg een nieuwe rol toe
    public RoleDto addRole(RoleInputDto roleInputDto) {
        Role role = new Role(roleInputDto.getRoleName());
        Role savedRole = roleRepository.save(role);
        return convertToDto(savedRole);
    }

    // Update een bestaande rol
    public RoleDto updateRole(Long id, RoleInputDto roleInputDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Role not found with id: " + id));

        role.setRoleName(roleInputDto.getRoleName());
        Role updatedRole = roleRepository.save(role);
        return convertToDto(updatedRole);
    }

    // Verwijder een rol
    public void deleteRole(Long id) {
        if (roleRepository.existsById(id)) {
            roleRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("Role not found with id: " + id);
        }
    }

    // Helper method: Converteer Role naar RoleDto
    private RoleDto convertToDto(Role role) {
        return new RoleDto(role.getRoleId(), role.getRoleName());
    }
}
