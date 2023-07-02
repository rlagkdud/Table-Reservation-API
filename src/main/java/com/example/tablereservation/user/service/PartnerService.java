package com.example.tablereservation.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.tablereservation.reservation.entity.Reservation;
import com.example.tablereservation.reservation.repository.ReservationRepository;
import com.example.tablereservation.shop.entity.Shop;
import com.example.tablereservation.shop.repository.ShopRepository;
import com.example.tablereservation.user.entity.Partner;
import com.example.tablereservation.user.entity.User;
import com.example.tablereservation.user.exception.PartnerNotFoundException;
import com.example.tablereservation.user.model.*;
import com.example.tablereservation.user.repository.PartnerRepository;
import com.example.tablereservation.utils.EncryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final ShopRepository shopRepository;

    private final ReservationRepository reservationRepository;


    /**
     * 점주 등록
     *
     * @param partnerAddInput
     * @return
     */
    public ServiceResult addPartner(PartnerAddInput partnerAddInput) {

        // 등록여부 확인 - 이메일이 존재하면 이미 등록된 점주!
        int count = partnerRepository.countByEmail(partnerAddInput.getEmail());
        if (count > 0) {
            return ServiceResult.fail("이미 존재하는 이메일 입니다.");
        }

        // 정상 등록
        // 비밀번호 암호화
        String encryptPassword = EncryptUtils.getEncryptPassword(partnerAddInput.getPassword());
        // 저장
        Partner partner = Partner.builder()
                .userName(partnerAddInput.getUserName())
                .email(partnerAddInput.getEmail())
                .phone(partnerAddInput.getPhone())
                .password(encryptPassword)
                .regDate(LocalDateTime.now())
                .build();
        partnerRepository.save(partner);
        return ServiceResult.success();
    }

    /**
     * 점주 조회
     *
     * @param id
     * @return
     */
    public PartnerResponse getPartner(Long id) {

        Partner partner = partnerRepository.findById(id)
                .orElseThrow(() -> new PartnerNotFoundException("등록된 점주가 없습니다."));
        PartnerResponse partnerResponse = PartnerResponse.builder()
                .userName(partner.getUserName())
                .email(partner.getEmail())
                .phone(partner.getPhone())
                .regDate(partner.getRegDate())
                .build();
        return partnerResponse;

    }

    /**
     * 점주 수정 - 이름, 번호만
     *
     * @param id
     * @param partnerUpdateInput
     * @return
     */
    public ServiceResult updatePartner(Long id, PartnerUpdateInput partnerUpdateInput) {
        // id에 해당하는 파트너 없을때,
        Optional<Partner> optionalPartner = partnerRepository.findById(id);
        if(!optionalPartner.isPresent()){
            return ServiceResult.fail("등록된 점주가 없습니다.");
        }

        // 있을때
        Partner partner = optionalPartner.get();

        partner.setUserName(partnerUpdateInput.getUserName());
        partner.setPhone(partnerUpdateInput.getPhone());
        partner.setUpdateDate(LocalDateTime.now());
        partnerRepository.save(partner);
        return ServiceResult.success();
    }


    /**
     * 점주 삭제 - 가게가 있으면 삭제 불가
     * @param id
     * @return
     */
    public ServiceResult deletePartner(Long id) {
        // 점주 존재 여부
        Optional<Partner> optionalPartner = partnerRepository.findById(id);
        if(!optionalPartner.isPresent()){
            return ServiceResult.fail("등록된 점주가 없습니다.");
        }

        Partner partner = optionalPartner.get();

        // 가게 존재 여부
        int count = shopRepository.countByPartner(partner);
        if(count > 0){
            return ServiceResult.fail("해당 점주로 등록된 매장이 존재합니다.");
        }

        partnerRepository.delete(partner);
        return ServiceResult.success();


    }

    /**
     * @param userLogin
     * 사용자 이메일과 비밀번호를 통해 JWT를 발행하는 API
     * - JWT 토큰 발생시 사용자 정보가 유효하지 않을때 예외 발생
     * - 사용자 정보가 존재 여부
     * - 비밀번호 일치 여부
     * - 토큰 발행!!!!
     * - 토큰 유효기간 설정
     * @return
     */
    public ServiceResult login(UserLogin userLogin) {

        // 점주 정보가 존재하는지 확인
        Optional<Partner> optionalPartner = partnerRepository.findByEmail(userLogin.getEmail());

        if(!optionalPartner.isPresent()){
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        Partner partner = optionalPartner.get();

        // 비밀번호 일치여부 확인
        // password 비교하는 util을 만들어 사용
        if (!EncryptUtils.equalPassword(userLogin.getPassword(), partner.getPassword())) {
            return ServiceResult.fail("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 만료 시간 설정
        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = Timestamp.valueOf(expiredDateTime);

        // 토큰 발행 시점
        String token = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("partner_id", partner.getId())
                .withSubject(partner.getUserName())
                .withIssuer(partner.getEmail())
                .sign(Algorithm.HMAC512("fastcampus".getBytes()));

        // 모델에 담아서 반환
        return ServiceResult.success(UserLoginToken.builder()
                .token(token)
                .build());
    }


}
