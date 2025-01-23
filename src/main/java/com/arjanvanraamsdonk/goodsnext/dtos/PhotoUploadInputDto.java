package com.arjanvanraamsdonk.goodsnext.dtos;

import jakarta.validation.constraints.NotNull;

public class PhotoUploadInputDto {

    @NotNull(message = "fileName name is required")
    private String fileName;
    @NotNull(message = "fileType name is required")
    private String fileType;
    @NotNull(message = "fileSize name is required")
    private Long fileSize;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
}
