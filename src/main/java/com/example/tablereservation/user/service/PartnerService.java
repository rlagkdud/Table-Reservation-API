package com.example.tablereservation.user.service;

import com.example.tablereservation.user.entity.Partner;
import com.example.tablereservation.user.model.PartnerAddInput;
import com.example.tablereservation.user.model.ServiceResult;
import com.example.tablereservation.user.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;


    /**
     * 점주 등록
     * @param partnerAddInput
     * @return
     */
    public ServiceResult addPartner(PartnerAddInput partnerAddInput){

        // 등록여부 확인 - 이메일이 존재하면 이미 등록된 점주!
        int count = partnerRepository.countByEmail(partnerAddInput.getEmail());
        if(count > 0){
            return ServiceResult.fail("이미 존재하는 이메일 입니다.");
        }

        // 정상 등록
        // 비밀번호 암호화
        String encryptPassword = getEncryptPassword(partnerAddInput.getPassword());
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

    private String getEncryptPassword(String password){
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
    }
}
