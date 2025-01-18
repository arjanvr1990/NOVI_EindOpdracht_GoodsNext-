package com.arjanvanraamsdonk.goodsnext.repository;

import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {
}
