package com.example.tablereservation.reservation.service;

import com.example.tablereservation.reservation.entity.Reservation;
import com.example.tablereservation.reservation.model.DateInput;
import com.example.tablereservation.reservation.model.ReservationAddInput;
import com.example.tablereservation.reservation.model.ReservationResponse;
import com.example.tablereservation.reservation.model.UserCheckInput;
import com.example.tablereservation.reservation.repository.ReservationRepository;
import com.example.tablereservation.shop.entity.Shop;
import com.example.tablereservation.shop.repository.ShopRepository;
import com.example.tablereservation.user.entity.Partner;
import com.example.tablereservation.user.entity.User;
import com.example.tablereservation.user.model.ServiceResult;
import com.example.tablereservation.user.repository.PartnerRepository;
import com.example.tablereservation.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final ShopRepository shopRepository;

    private final PartnerRepository partnerRepository;

    /**
     * 예약 등록
     * - 예약은 현재 년도의 예약만 받음
     * - 현재 시간 보다 이후의 예약만 받음
     * - 중복 예약 확인
     *
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
        System.out.println("month: " + reservationAddInput.getMonth());
        cal.set(year, reservationAddInput.getMonth() - 1, 1);
        int lastDayNum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        System.out.println("lastDayNum = " + lastDayNum);
        System.out.println("getDay = " + reservationAddInput.getDay());
        System.out.println(reservationAddInput.getDay() < lastDayNum);

        // 입력받은 날짜 유효성 검사
        if (reservationAddInput.getDay() > lastDayNum) {
            System.out.println("here!!!");
            return ServiceResult.fail("해당 월에 없는 날짜 입니다.");
        }

        System.out.println("2222222");

        // 현재보다 이후인지 확인
        LocalDateTime reserveDateTime = LocalDateTime.of(
                year
                , reservationAddInput.getMonth()
                , reservationAddInput.getDay()
                , reservationAddInput.getTime()
                , reservationAddInput.getMinute());
        if (reserveDateTime.isBefore(now)) {
            return ServiceResult.fail("예약이 불가능한 지나간 시간 입니다.");
        }

        LocalDateTime reserveDate = LocalDateTime.of(year, reservationAddInput.getMonth(), reservationAddInput.getDay(), 0, 0, 0);
        LocalTime reserveTime = LocalTime.of(reservationAddInput.getTime(), reservationAddInput.getMinute(), 0);

        // 중복 예약 여부 - 동일한 사용자, 동일한 가게, 동일한 시간
        int count = reservationRepository.countByUserAndShopAndReserveDateAndReserveTime(user, shop, reserveDate, reserveTime);
        if (count > 0) {
            return ServiceResult.fail("이미 동일한 예약이 존재합니다.");
        }

        // 정상 예약

        Reservation reservation = Reservation.builder()
                .user(user)
                .shop(shop)
                .reserveDate(reserveDate)
                .reserveTime(reserveTime)
                .regDate(LocalDateTime.now())
                .build();
        reservationRepository.save(reservation);

        return ServiceResult.success();

    }

    /**
     * 예약 조회
     *
     * @param id
     * @return
     */
    public ServiceResult getReservation(Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (!optionalReservation.isPresent()) {
            return ServiceResult.fail("예약이 존재하지 않습니다.");
        }

        Reservation reservation = optionalReservation.get();

        ReservationResponse reservationResponse = ReservationResponse.builder()
                .userName(reservation.getUser().getUserName())
                .userPhone(reservation.getUser().getPhone())
                .shopName(reservation.getShop().getName())
                .reserveDate(reservation.getReserveDate())
                .reserveTime(reservation.getReserveTime())
                .build();

        return ServiceResult.success(reservationResponse);
    }

    /**
     * 예약 삭제
     *
     * @param id
     * @return
     */
    public ServiceResult deleteReservation(Long id) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);

        if (!optionalReservation.isPresent()) {
            return ServiceResult.fail("예약이 존재하지 않습니다.");
        }

        Reservation reservation = optionalReservation.get();

        reservationRepository.delete(reservation);

        return ServiceResult.success();
    }

    /**
     * 예약 조회
     * - 점주가 매장의 예약을 날짜별로 조회 함.
     * - 점주 존재여부
     * - 매장 존재 여부
     * - 매장과 점주 매칭 여부
     * - 예약 존재 여부
     *
     * @param partnerId
     * @param shopId
     * @return
     */
    public ServiceResult getShopReservationByPartner(Long shopId, Long partnerId, DateInput dateInput) {
        // 점주 존재 여부
        Optional<Partner> optionalPartner = partnerRepository.findById(partnerId);
        if (!optionalPartner.isPresent()) {
            return ServiceResult.fail("해당 점주가 존재하지 않습니다.");
        }
        Partner partner = optionalPartner.get();

        // 매장 존재 여부 확인
        Optional<Shop> optionalShop = shopRepository.findById(shopId);
        if (!optionalShop.isPresent()) {
            return ServiceResult.fail("해당 매장이 존재하지 않습니다.");
        }
        Shop shop = optionalShop.get();

        // 매장과 점주 매칭 여부
        if (partner.getId() != shop.getPartner().getId()) {
            return ServiceResult.fail("해당 점주가 운영하는 매장이 아닙니다.");
        }

        // 입력받은 날짜가 유효한지 - 해당 월에 있는 날짜인지
        // 예약 월의 마지막 날짜
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(dateInput.getYear()), Integer.parseInt(dateInput.getMonth()) - 1, 1);
        int lastDayNum = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // 입력받은 날짜 유효성 검사
        if (Integer.parseInt(dateInput.getDay()) > lastDayNum) {
            return ServiceResult.fail("해당 월에 없는 날짜 입니다.");
        }

        LocalDateTime reserveDate = LocalDateTime.of(Integer.parseInt(dateInput.getYear()), Integer.parseInt(dateInput.getMonth()), Integer.parseInt(dateInput.getDay()), 0, 0, 0);

        // 날짜 순 예약 내역 가져오기
        List<Reservation> reservationList = reservationRepository.findAllByShopAndReserveDateOrderByReserveDate(shop, reserveDate);
        List<ReservationResponse> reservationResponseList = new ArrayList<>();

        reservationList.stream().forEach((r) -> {
            reservationResponseList.add(
                    ReservationResponse.builder()
                            .userName(r.getUser().getUserName())
                            .userPhone(r.getUser().getPhone())
                            .shopName(r.getShop().getName())
                            .reserveDate(r.getReserveDate())
                            .reserveTime(r.getReserveTime())
                            .build()
            );
        });

        return ServiceResult.success(reservationResponseList);

    }

    /**
     * 도착여부 확인
     *
     * @param id
     * @return
     */
    public ServiceResult checkArrive(Long id, UserCheckInput userCheckInput) {
        // 예약 존재 여부
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (!optionalReservation.isPresent()) {
            return ServiceResult.fail("해당 예약이 존재하지 않습니다.");
        }

        Reservation reservation = optionalReservation.get();

        // 유저 존재 여부
        Optional<User> optionalUser = userRepository.findByPhone(userCheckInput.getPhone());
        if (!optionalUser.isPresent()) {
            return ServiceResult.fail("해당 유저가 존재하지 않습니다.");
        }

        User user = optionalUser.get();

        // 유저와 예약 매칭 여부
        if (!reservation.getUser().getPhone().equals(user.getPhone())) {
            return ServiceResult.fail("예약자와 사용자가 다릅니다.");
        }

        // 예약시간 이전 도착 여부
        // 도착 못했을때
        LocalDateTime now = LocalDateTime.now();
        int reserveYear = reservation.getReserveDate().getYear();
        int reserveMonth = reservation.getReserveDate().getMonth().getValue();
        int reserveDay = reservation.getReserveDate().getDayOfMonth();

        int reserveHour = reservation.getReserveTime().getHour();
        int reserveMinute = reservation.getReserveTime().getMinute();

        LocalDateTime reserveTime = LocalDateTime.of(reserveYear, reserveMonth, reserveDay, reserveHour, reserveMinute);


        if (now.isAfter(reserveTime)) {
            reservation.setArrivedYn(false);
            reservationRepository.save(reservation);
            return ServiceResult.fail("예약시간이 지나고 도착했습니다.");

        }
        // 도착 했을때
        // 너무 일찍 도착한 경우
        long minute = ChronoUnit.MINUTES.between(now, reserveTime);

        if (minute > 10) {
            return ServiceResult.fail("10분 전보다 일찍 도착하셨습니다.");
        }

        //10분전에 도착한 경우
        reservation.setArrivedYn(true);
        reservationRepository.save(reservation);
        return ServiceResult.success();
    }
}
