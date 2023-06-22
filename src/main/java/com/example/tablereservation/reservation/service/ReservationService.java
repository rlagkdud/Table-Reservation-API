package com.example.tablereservation.reservation.service;

import com.example.tablereservation.reservation.entity.Reservation;
import com.example.tablereservation.reservation.model.ReservationAddInput;
import com.example.tablereservation.reservation.model.ReservationResponse;
import com.example.tablereservation.reservation.repository.ReservationRepository;
import com.example.tablereservation.shop.entity.Shop;
import com.example.tablereservation.shop.repository.ShopRepository;
import com.example.tablereservation.user.entity.User;
import com.example.tablereservation.user.model.ServiceResult;
import com.example.tablereservation.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final ShopRepository shopRepository;

    /**
     * 예약 등록
     * - 예약은 현재 년도의 예약만 받음
     * - 현재 시간 보다 이후의 예약만 받음
     * - 중복 예약 확인
     * @param reservationAddInput
     * @return
     */
    public ServiceResult addReservation(ReservationAddInput reservationAddInput) {
        // 사용자 존재 여부
        Optional<User> optionalUser = userRepository.findByPhone(reservationAddInput.getUserPhone());
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("해당 사용자가 존재하지 않습니다.");
        }

        User user = optionalUser.get();

        // 매장 존재 여부
        Optional<Shop> optionalShop = shopRepository.findByNameAndLocation(reservationAddInput.getShopName(), reservationAddInput.getShopLocation());
        if (!optionalShop.isPresent()) {
            return ServiceResult.fail("해당 가게가 존재하지 않습니다.");
        }

        Shop shop = optionalShop.get();

        // 예약날짜 유효성 여부 - 해당 월에 있는 날짜인지, 현재보다 이후의 예약이 맞는지
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();

        // 예약 월의 마지막 날짜
        Calendar cal = Calendar.getInstance();
        int lastDayNum = cal.getActualMaximum(reservationAddInput.getMonth());

        // 입력받은 날짜 유효성 검사
        if (reservationAddInput.getDay() > lastDayNum) {
            return ServiceResult.fail("해당 월에 없는 날짜 입니다.");
        }
        // 현재보다 이후인지 확인
        LocalDateTime reserveDate = LocalDateTime.of(
                year
                , reservationAddInput.getMonth()
                , reservationAddInput.getDay()
                , reservationAddInput.getTime()
                , reservationAddInput.getMinute());
        if (reserveDate.isBefore(now)) {
            return ServiceResult.fail("예약이 불가능한 지나간 시간 입니다.");
        }

        // 중복 예약 여부 - 동일한 사용자, 동일한 가게, 동일한 시간
        int count = reservationRepository.countByUserAndShopAndReserveDate(user, shop, reserveDate);
        if (count > 0) {
            return ServiceResult.fail("이미 동일한 예약이 존재합니다.");
        }

        // 정상 예약
        Reservation reservation = Reservation.builder()
                .user(user)
                .shop(shop)
                .reserveDate(reserveDate)
                .regDate(LocalDateTime.now())
                .build();
        reservationRepository.save(reservation);

        return ServiceResult.success();

    }

    /**
     * 예약 조회
     * @param id
     * @return
     */
    public ServiceResult getReservation(Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if(!optionalReservation.isPresent()){
            return ServiceResult.fail("예약이 존재하지 않습니다.");
        }

        Reservation reservation = optionalReservation.get();

        ReservationResponse reservationResponse = ReservationResponse.builder()
                .userName(reservation.getUser().getUserName())
                .userPhone(reservation.getUser().getPhone())
                .shopName(reservation.getShop().getName())
                .reserveDate(reservation.getReserveDate())
                .build();

        return ServiceResult.success(reservationResponse);
    }
}

