package com.example.tablereservation.user.repository;

import com.example.tablereservation.user.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    int countByEmail(String email);

    Optional<Partner> findByEmail(String partnerEmail);
}
