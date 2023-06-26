package com.example.tablereservation.reservation.model;

import com.example.tablereservation.reservation.type.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private String userName;
    private  String userPhone;

    private String shopName;
    private LocalDateTime reserveDate;
    private LocalTime reserveTime;

    private ReservationStatus status;
}
