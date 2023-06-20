package com.example.tablereservation.user.controller;

import com.example.tablereservation.user.exception.PartnerNotFoundException;
import com.example.tablereservation.user.model.*;
import com.example.tablereservation.user.service.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class PartnerController {
    private final PartnerService partnerService;

    /**
     * 파트너(점주) 추가 - create
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

    /**
     * 파트너(점주) 조회 - read
     */
    @GetMapping("/api/partner/{id}")
    public ResponseEntity<?> getPartner(@PathVariable Long id){
        // 서비스 호출 - id에 해당하는 파트너가 존재하지 않으면 예외 발생
        PartnerResponse partner = partnerService.getPartner(id);

        return ResponseEntity.ok().body(ResponseMessage.success(partner));

    }
    @ExceptionHandler(PartnerNotFoundException.class)
    public ResponseEntity<?> partnerNotFoundExceptionHandler(PartnerNotFoundException e){
        return ResponseEntity.ok().body(ResponseMessage.fail(e.getMessage()));
    }

    /**
     * 파트너(점주) 수정 - update
     */
    @PutMapping("/api/partner/{id}")
    public ResponseEntity<?> updatePartner(@PathVariable Long id, @RequestBody @Valid PartnerUpdateInput partnerUpdateInput, Errors errors){

        // 입력값 유효성 검사
        if(errors.hasErrors()){
            List<ResponseError> responseErrorList = ResponseError.of(errors.getAllErrors());
            return new ResponseEntity(ResponseMessage.fail("입력값이 정확하지 않습니다", responseErrorList), HttpStatus.BAD_REQUEST);
        }

        // 서비스 호출
        ServiceResult result = partnerService.updatePartner(id, partnerUpdateInput);
        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().body(ResponseMessage.success());

    }

    /**
     * 파트너(점주) 삭제 - delete
     */
    @DeleteMapping("/api/partner/{id}")
    public ResponseEntity<?> deletePartner(@PathVariable Long id){
        ServiceResult result = partnerService.deletePartner(id);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().body(ResponseMessage.success());

    }

}
