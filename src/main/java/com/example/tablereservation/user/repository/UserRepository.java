package com.example.tablereservation.user.repository;

import com.example.tablereservation.user.entity.Partner;
import com.example.tablereservation.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    int countByEmail(String email);

    Optional<User> findByPhone(String userPhone);
}
