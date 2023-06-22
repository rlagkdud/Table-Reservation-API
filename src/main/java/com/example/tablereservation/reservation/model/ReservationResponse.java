package com.example.tablereservation.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private String userName;
    private  String userPhone;

    private String shopName;
    private LocalDateTime reserveDate;
}
