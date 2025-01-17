package com.arjanvanraamsdonk.goodsnext.repositories;

import com.arjanvanraamsdonk.goodsnext.models.PhotoUpload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoUploadRepository extends JpaRepository<PhotoUpload, Long> {
}
