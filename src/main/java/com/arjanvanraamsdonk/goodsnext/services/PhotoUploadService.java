package com.arjanvanraamsdonk.goodsnext.services;

import com.arjanvanraamsdonk.goodsnext.dtos.PhotoUploadDto;
import com.arjanvanraamsdonk.goodsnext.dtos.PhotoUploadInputDto;
import com.arjanvanraamsdonk.goodsnext.exceptions.RecordNotFoundException;
import com.arjanvanraamsdonk.goodsnext.models.PhotoUpload;
import com.arjanvanraamsdonk.goodsnext.repositories.PhotoUploadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoUploadService {

    private final PhotoUploadRepository photoUploadRepository;

    public PhotoUploadService(PhotoUploadRepository photoUploadRepository) {
        this.photoUploadRepository = photoUploadRepository;
    }

    public PhotoUploadDto createPhotoUpload(PhotoUploadInputDto inputDto) {
        if (inputDto != null) {
            PhotoUpload photoUpload = toEntity(inputDto);
            PhotoUpload savedPhotoUpload = photoUploadRepository.save(photoUpload);
            return toDto(savedPhotoUpload);
        } else {
            throw new IllegalArgumentException("Input data for creating PhotoUpload cannot be null");
        }
    }

    public PhotoUploadDto getPhotoUploadById(Long id) {
        PhotoUpload photoUpload = photoUploadRepository.findById(id).orElse(null);
        if (photoUpload != null) {
            return toDto(photoUpload);
        } else {
            throw new RecordNotFoundException("PhotoUpload not found with ID: " + id);
        }
    }

    public List<PhotoUploadDto> getAllPhotoUploads() {
        List<PhotoUpload> photoUploads = photoUploadRepository.findAll();
        return photoUploads.stream().map(this::toDto).collect(Collectors.toList());
    }

    public PhotoUploadDto updatePhotoUpload(Long id, PhotoUploadInputDto inputDto) {
        PhotoUpload photoUpload = photoUploadRepository.findById(id).orElse(null);
        if (photoUpload != null) {
            updatePhotoUploadFields(photoUpload, inputDto);
            PhotoUpload updatedPhotoUpload = photoUploadRepository.save(photoUpload);
            return toDto(updatedPhotoUpload);
        } else {
            throw new RecordNotFoundException("PhotoUpload not found with ID: " + id);
        }
    }

    public void deletePhotoUpload(Long id) {
        if (photoUploadRepository.existsById(id)) {
            photoUploadRepository.deleteById(id);
        } else {
            throw new RecordNotFoundException("PhotoUpload not found with ID: " + id);
        }
    }

    private PhotoUploadDto toDto(PhotoUpload photoUpload) {
        PhotoUploadDto dto = new PhotoUploadDto();
        dto.setId(photoUpload.getId());
        dto.setFileName(photoUpload.getFileName());
        dto.setFileType(photoUpload.getFileType());
        dto.setFileSize(photoUpload.getFileSize());
        return dto;
    }

    private PhotoUpload toEntity(PhotoUploadInputDto inputDto) {
        PhotoUpload photoUpload = new PhotoUpload();
        photoUpload.setFileName(inputDto.getFileName());
        photoUpload.setFileType(inputDto.getFileType());
        photoUpload.setFileSize(inputDto.getFileSize());
        return photoUpload;
    }

    private void updatePhotoUploadFields(PhotoUpload photoUpload, PhotoUploadInputDto inputDto) {
        photoUpload.setFileName(inputDto.getFileName());
        photoUpload.setFileType(inputDto.getFileType());
        photoUpload.setFileSize(inputDto.getFileSize());
    }
}
