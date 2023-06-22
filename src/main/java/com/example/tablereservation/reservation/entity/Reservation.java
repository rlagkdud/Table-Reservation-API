package com.example.tablereservation.reservation.entity;


import com.example.tablereservation.shop.entity.Shop;
import com.example.tablereservation.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private Shop shop;

    private LocalDate reserveDate;
    private LocalTime reserveTime;

    private LocalDateTime regDate;
}
