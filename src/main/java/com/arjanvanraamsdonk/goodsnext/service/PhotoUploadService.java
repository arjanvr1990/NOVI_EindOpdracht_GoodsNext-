package com.arjanvanraamsdonk.goodsnext.service;

import com.arjanvanraamsdonk.goodsnext.dto.PhotoUploadDto;
import com.arjanvanraamsdonk.goodsnext.dto.PhotoUploadInputDto;
import com.arjanvanraamsdonk.goodsnext.models.PhotoUpload;
import com.arjanvanraamsdonk.goodsnext.repository.PhotoUploadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhotoUploadService {

    private final PhotoUploadRepository photoUploadRepository;

    public PhotoUploadService(PhotoUploadRepository photoUploadRepository) {
        this.photoUploadRepository = photoUploadRepository;
    }

    public PhotoUploadDto createPhotoUpload(PhotoUploadInputDto inputDto) {
        PhotoUpload photoUpload = new PhotoUpload();
        photoUpload.setFileName(inputDto.getFileName());
        photoUpload.setFileType(inputDto.getFileType());
        photoUpload.setFileSize(inputDto.getFileSize());

        PhotoUpload savedPhotoUpload = photoUploadRepository.save(photoUpload);
        return transferToDto(savedPhotoUpload);
    }

    public PhotoUploadDto getPhotoUploadById(Long id) {
        PhotoUpload photoUpload = photoUploadRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PhotoUpload not found with ID: " + id));
        return transferToDto(photoUpload);
    }

    public List<PhotoUploadDto> getAllPhotoUploads() {
        List<PhotoUpload> photoUploads = photoUploadRepository.findAll();
        return photoUploads.stream().map(this::transferToDto).collect(Collectors.toList());
    }

    private PhotoUploadDto transferToDto(PhotoUpload photoUpload) {
        PhotoUploadDto dto = new PhotoUploadDto();
        dto.setId(photoUpload.getId());
        dto.setFileName(photoUpload.getFileName());
        dto.setFileType(photoUpload.getFileType());
        dto.setFileSize(photoUpload.getFileSize());
        return dto;
    }
}
