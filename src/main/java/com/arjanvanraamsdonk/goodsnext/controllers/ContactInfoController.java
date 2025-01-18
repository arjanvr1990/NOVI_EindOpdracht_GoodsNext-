package com.arjanvanraamsdonk.goodsnext.controllers;



import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
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
        ContactInfoDto contactInfo = contactInfoService.getContactInfoByUsername(username);
        return ResponseEntity.ok(contactInfo);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/me")
    public ResponseEntity<ContactInfoDto> updateMyContactInfo(@Valid @RequestBody ContactInfoDto contactInfoDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ContactInfoDto updatedContactInfo = contactInfoService.updateContactInfoByUsername(username, contactInfoDto);
        return ResponseEntity.ok(updatedContactInfo);
    }

    @PostMapping
    public ResponseEntity<ContactInfoDto> addContactInfo(@Valid @RequestBody ContactInfoDto contactInfoDto) {
        ContactInfoDto createdContactInfo = contactInfoService.addContactInfo(contactInfoDto);
        return ResponseEntity.ok(createdContactInfo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactInfoDto> updateContactInfo(
            @PathVariable Long id, @Valid @RequestBody ContactInfoDto contactInfoDto) {
        return ResponseEntity.ok(contactInfoService.updateContactInfo(id, contactInfoDto));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactInfo(@PathVariable Long id) {
        contactInfoService.deleteContactInfo(id);
        return ResponseEntity.noContent().build();
    }
}
