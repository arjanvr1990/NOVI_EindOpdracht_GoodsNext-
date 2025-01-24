package com.arjanvanraamsdonk.goodsnext.repositories;

import com.arjanvanraamsdonk.goodsnext.models.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, Long> {

    Optional<ContactInfo> findByEmail(String email);

    Optional<ContactInfo> findByAddress(String address);

    Optional<ContactInfo> findByPhoneNumber(String phoneNumber);
}
