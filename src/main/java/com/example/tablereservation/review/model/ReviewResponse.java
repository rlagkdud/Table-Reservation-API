package com.example.tablereservation.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {

    private String userEmail;
    private String description;
    private Integer star;

    private String shopName;
    private String shopLocation;

    private LocalDateTime regDate;
}
