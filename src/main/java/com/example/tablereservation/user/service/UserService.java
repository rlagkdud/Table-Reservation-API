package com.example.tablereservation.user.service;

import com.example.tablereservation.user.entity.User;
import com.example.tablereservation.user.model.ServiceResult;
import com.example.tablereservation.user.model.UserAddInput;
import com.example.tablereservation.user.repository.UserRepository;
import com.example.tablereservation.utils.EncryptUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


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
}
