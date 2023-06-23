package com.example.tablereservation.review.repository;

import com.example.tablereservation.review.entity.Review;
import com.example.tablereservation.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByUser(User user);
}
