package com.example.tablereservation.user.service;

import com.example.tablereservation.reservation.entity.Reservation;
import com.example.tablereservation.reservation.repository.ReservationRepository;
import com.example.tablereservation.user.entity.User;
import com.example.tablereservation.user.model.ServiceResult;
import com.example.tablereservation.user.model.UserAddInput;
import com.example.tablereservation.user.model.UserResponse;
import com.example.tablereservation.user.model.UserUpdateInput;
import com.example.tablereservation.user.repository.UserRepository;
import com.example.tablereservation.utils.EncryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
}
