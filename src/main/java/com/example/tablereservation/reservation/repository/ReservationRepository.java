package com.example.tablereservation.reservation.repository;

import com.example.tablereservation.reservation.entity.Reservation;
import com.example.tablereservation.shop.entity.Shop;
import com.example.tablereservation.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    int countByShop(Shop shop);

    List<Reservation> findAllByUser(User user);

    int countByUserAndShopAndReserveDateAndReserveTime(User user, Shop shop, LocalDate reserveDate, LocalTime reserveTime);
}
