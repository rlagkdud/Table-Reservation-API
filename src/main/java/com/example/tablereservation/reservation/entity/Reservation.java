package com.example.tablereservation.reservation.entity;


import com.example.tablereservation.shop.entity.Shop;
import com.example.tablereservation.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private LocalDateTime reserveDate;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;
}
