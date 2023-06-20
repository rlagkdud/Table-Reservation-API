package com.example.tablereservation.user.service;

import com.example.tablereservation.shop.repository.ShopRepository;
import com.example.tablereservation.user.entity.Partner;
import com.example.tablereservation.user.exception.PartnerNotFoundException;
import com.example.tablereservation.user.model.PartnerAddInput;
import com.example.tablereservation.user.model.PartnerResponse;
import com.example.tablereservation.user.model.PartnerUpdateInput;
import com.example.tablereservation.user.model.ServiceResult;
import com.example.tablereservation.user.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerService {
    private final PartnerRepository partnerRepository;
    private final ShopRepository shopRepository;


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

    private String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder.encode(password);
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
}
