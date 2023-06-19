package com.example.tablereservation.user.controller;

import com.example.tablereservation.user.model.PartnerAddInput;
import com.example.tablereservation.user.model.ResponseError;
import com.example.tablereservation.user.model.ResponseMessage;
import com.example.tablereservation.user.model.ServiceResult;
import com.example.tablereservation.user.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;

    /**
     * 파트너(점주) 추가
     */
    @PostMapping("/api/partner")
    public ResponseEntity<?> addPartner(@RequestBody @Valid PartnerAddInput partnerAddInput, Errors errors){

        // 입력값에 대한 유효성 검사
        if(errors.hasErrors()){
            List<ResponseError> responseErrorList = ResponseError.of(errors.getAllErrors());
            return new ResponseEntity(ResponseMessage.fail("입력값이 정확하지 않습니다", responseErrorList), HttpStatus.BAD_REQUEST);
        }

        // 서비스 호출 - 등록여부 확인, 저장
        ServiceResult result = partnerService.addPartner(partnerAddInput);
        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseMessage.success());

    }

}
