package com.example.tablereservation.reservation.repository;

import com.example.tablereservation.reservation.entity.Reservation;
import com.example.tablereservation.shop.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    int countByShop(Shop shop);
}
