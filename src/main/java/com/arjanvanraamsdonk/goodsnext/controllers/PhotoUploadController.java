package com.arjanvanraamsdonk.goodsnext.controllers;

import com.arjanvanraamsdonk.goodsnext.dtos.PhotoUploadDto;
import com.arjanvanraamsdonk.goodsnext.dtos.PhotoUploadInputDto;
import com.arjanvanraamsdonk.goodsnext.services.PhotoUploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoUploadController {

    private final PhotoUploadService photoUploadService;

    public PhotoUploadController(PhotoUploadService photoUploadService) {
        this.photoUploadService = photoUploadService;
    }

    @PostMapping
    public ResponseEntity<PhotoUploadDto> createPhotoUpload(@RequestBody PhotoUploadInputDto inputDto) {
        PhotoUploadDto photoUpload = photoUploadService.createPhotoUpload(inputDto);
        return ResponseEntity.ok(photoUpload);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhotoUploadDto> getPhotoUpload(@PathVariable Long id) {
        PhotoUploadDto photoUpload = photoUploadService.getPhotoUploadById(id);
        return ResponseEntity.ok(photoUpload);
    }

    @GetMapping
    public ResponseEntity<List<PhotoUploadDto>> getAllPhotoUploads() {
        List<PhotoUploadDto> photoUploads = photoUploadService.getAllPhotoUploads();
        return ResponseEntity.ok(photoUploads);
    }
}
