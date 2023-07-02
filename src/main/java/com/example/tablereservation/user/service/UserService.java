package com.example.tablereservation.user.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.tablereservation.reservation.entity.Reservation;
import com.example.tablereservation.reservation.repository.ReservationRepository;
import com.example.tablereservation.user.entity.User;
import com.example.tablereservation.user.model.*;
import com.example.tablereservation.user.repository.UserRepository;
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
public class UserService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 사용자 추가
     * - 이미 존재하는 이메일이면 가입 불가
     * @param userAddInput
     * @return
     */
    public ServiceResult addUser(UserAddInput userAddInput) {
        // 등록여부확인
        int count = userRepository.countByEmail(userAddInput.getEmail());
        if (count > 0) {
            return ServiceResult.fail("이미 존재하는 이메일 입니다.");
        }

        // 정상등록
        String encryptPassword = EncryptUtils.getEncryptPassword(userAddInput.getPassword());
        User user = User.builder()
                .userName(userAddInput.getUserName())
                .email(userAddInput.getEmail())
                .password(encryptPassword)
                .phone(userAddInput.getPhone())
                .regDate(LocalDateTime.now())
                .build();
        userRepository.save(user);

        return ServiceResult.success();
    }

    /**
     * 사용자 조회
     * @param id
     * @return
     */
    public ServiceResult getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if(!optionalUser.isPresent()){
            return ServiceResult.fail("해당 사용자가 없습니다.");
        }

        User user = optionalUser.get();
        UserResponse userResponse = UserResponse.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .regDate(user.getRegDate())
                .build();
        return ServiceResult.success(userResponse);
    }

    /**
     * 사용자 수정
     * - 이름, 번호만
     * @param userUpdateInput
     * @return
     */
    public ServiceResult updateUser(Long id, UserUpdateInput userUpdateInput) {
        // id에 해당하는 사용자 없을때,
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()){
            return ServiceResult.fail("해당하는 사용자가 없습니다.");
        }

        // 있을때
        User user = optionalUser.get();
        user.setUserName(userUpdateInput.getUserName());
        user.setPhone(userUpdateInput.getPhone());
        user.setUpdateDate(LocalDateTime.now());
        userRepository.save(user);

        return ServiceResult.success();
    }

    /**
     * 사용자 삭제
     * - 사용자가 한 예약까지 모두 삭제
     * @param id
     * @return
     */
    public ServiceResult deleteUser(Long id) {
        // 사용자 존재 확인
        Optional<User> optionalUser = userRepository.findById(id);
        if(!optionalUser.isPresent()){
            return ServiceResult.fail("해당하는 사용자가 없습니다.");
        }

        User user = optionalUser.get();

        // 예약 존재 여부 확인
        List<Reservation> reservationList = reservationRepository.findAllByUser(user);

        // 예약 있으면 예약 삭제
        if(reservationList.size() > 0){
            reservationList.stream().forEach(r->{
                reservationRepository.deleteById(r.getId());
            });
        }

        // 사용자 삭제
        userRepository.delete(user);
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

        // 사용자 정보가 존재하는지 확인
        Optional<User> optionalUser = userRepository.findByEmail(userLogin.getEmail());

        if(!optionalUser.isPresent()){
            return ServiceResult.fail("사용자가 존재하지 않습니다.");
        }
        User user = optionalUser.get();

        // 비밀번호 일치여부 확인
        // password 비교하는 util을 만들어 사용
        if (!EncryptUtils.equalPassword(userLogin.getPassword(), user.getPassword())) {
            return ServiceResult.fail("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 만료 시간 설정
        LocalDateTime expiredDateTime = LocalDateTime.now().plusMonths(1);
        Date expiredDate = Timestamp.valueOf(expiredDateTime);

        // 토큰 발행 시점
        String token = JWT.create()
                .withExpiresAt(expiredDate)
                .withClaim("user_id", user.getId())
                .withSubject(user.getUserName())
                .withIssuer(user.getEmail())
                .sign(Algorithm.HMAC512("fastcampus".getBytes()));

        // 모델에 담아서 반환
        return ServiceResult.success(UserLoginToken.builder()
                .token(token)
                .build());
    }
}
