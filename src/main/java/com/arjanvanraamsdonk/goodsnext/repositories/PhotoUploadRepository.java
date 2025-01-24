package com.arjanvanraamsdonk.goodsnext.repositories;

import com.arjanvanraamsdonk.goodsnext.models.PhotoUpload;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoUploadRepository extends JpaRepository<PhotoUpload, Long> {
    Optional<PhotoUpload> findByFileName(String fileName);

}
