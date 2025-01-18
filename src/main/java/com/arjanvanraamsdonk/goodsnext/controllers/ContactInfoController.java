package com.arjanvanraamsdonk.goodsnext.controllers;



import com.arjanvanraamsdonk.goodsnext.dtos.ContactInfoDto;
import com.arjanvanraamsdonk.goodsnext.services.ContactInfoService;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<ContactInfoDto> addContactInfo(@RequestBody ContactInfoDto contactInfoDto) {
        ContactInfoDto createdContactInfo = contactInfoService.addContactInfo(contactInfoDto);
        return ResponseEntity.ok(createdContactInfo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactInfoDto> updateContactInfo(
            @PathVariable Long id, @RequestBody ContactInfoDto contactInfoDto) {
        return ResponseEntity.ok(contactInfoService.updateContactInfo(id, contactInfoDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContactInfo(@PathVariable Long id) {
        contactInfoService.deleteContactInfo(id);
        return ResponseEntity.noContent().build();
    }
}
