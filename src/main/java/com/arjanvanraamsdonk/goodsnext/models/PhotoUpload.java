package com.arjanvanraamsdonk.goodsnext.models;

import jakarta.persistence.*;

@Entity
@Table(name = "photo_uploads")
public class PhotoUpload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "upload_id")
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;


    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }

    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileType() { return fileType; }

    public void setFileType(String fileType) { this.fileType = fileType; }

    public Long getFileSize() { return fileSize; }

    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public Long getUploadId() { return id; }

    public void setUploadId(Long fileSize) { this.id = id; }
}
