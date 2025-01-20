package com.arjanvanraamsdonk.goodsnext.controllers;

import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoInputDto;
import com.arjanvanraamsdonk.goodsnext.services.ContactInfoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contactinfo")
public class ContactInfoController {

    private final ContactInfoService contactInfoService;

    public ContactInfoController(ContactInfoService contactInfoService) {
        this.contactInfoService = contactInfoService;
    }

    @GetMapping
    public ResponseEntity<List<ContactInfoDto>> getAllContactInfo() {
        return ResponseEntity.ok(contactInfoService.getAllContactInfo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactInfoDto> getContactInfoById(@PathVariable Long id) {
        return ResponseEntity.ok(contactInfoService.getContactInfoById(id));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<ContactInfoDto> getMyContactInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(contactInfoService.getContactInfoByUsername(username));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/me")
    public ResponseEntity<ContactInfoDto> updateMyContactInfo(@Valid @RequestBody ContactInfoInputDto inputDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(contactInfoService.updateContactInfoByUsername(username, inputDto));
    }

    @PostMapping
    public ResponseEntity<ContactInfoDto> addContactInfo(@Valid @RequestBody ContactInfoInputDto inputDto) {
        return ResponseEntity.ok(contactInfoService.addContactInfo(inputDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactInfoDto> updateContactInfo(
            @PathVariable Long id, @Valid @RequestBody ContactInfoInputDto inputDto) {
        return ResponseEntity.ok(contactInfoService.updateContactInfo(id, inputDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactInfo(@PathVariable Long id) {
        contactInfoService.deleteContactInfo(id);
        return ResponseEntity.noContent().build();
    }
}
