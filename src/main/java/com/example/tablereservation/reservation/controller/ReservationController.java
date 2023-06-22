package com.example.tablereservation.reservation.controller;

import com.example.tablereservation.reservation.model.DateInput;
import com.example.tablereservation.reservation.model.ReservationAddInput;
import com.example.tablereservation.reservation.service.ReservationService;
import com.example.tablereservation.user.model.ResponseError;
import com.example.tablereservation.user.model.ResponseMessage;
import com.example.tablereservation.user.model.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.DateTimeException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    /**
     * 예약 등록 - create
     */
    @PostMapping("/api/reservation")
    public ResponseEntity<?> addReservation(@RequestBody @Valid ReservationAddInput reservationAddInput, Errors errors){

        // 입력값 유효성 검사
        if(errors.hasErrors()){
            List<ResponseError> responseErrorList = ResponseError.of(errors.getAllErrors());
            return new ResponseEntity(ResponseMessage.fail("입력값이 정확하지 않습니다", responseErrorList), HttpStatus.BAD_REQUEST);
        }

        // 서비스 호출
        ServiceResult result = reservationService.addReservation(reservationAddInput);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseMessage.success());

    }

    /**
     * 예약 조회 - read
     */
    @GetMapping("/api/reservation/{id}")
    public ResponseEntity<?> getReservation(@PathVariable Long id){

        ServiceResult result = reservationService.getReservation(id);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseMessage.success(result.getData()));

    }

    /**
     * 예약 삭제 - delete
     */
    @DeleteMapping("/api/reservation/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id){

        ServiceResult result = reservationService.deleteReservation(id);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseMessage.success());

    }

    /**
     * 파트너(점주) 날짜별 예약 확인 - GET
     */
    @GetMapping("/api/reservation/shop/{shopId}/partner/{partnerId}")
    public ResponseEntity<?> getShopReservationByPartner(
            @PathVariable Long shopId,
            @PathVariable Long partnerId,
            @RequestBody @Valid DateInput dateInput,
            Errors errors

    ) {
        // 입력값 유효성 검사
        if(errors.hasErrors()){
            List<ResponseError> responseErrorList = ResponseError.of(errors.getAllErrors());
            return new ResponseEntity(ResponseMessage.fail("입력값이 정확하지 않습니다", responseErrorList), HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = reservationService.getShopReservationByPartner(shopId, partnerId, dateInput);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseMessage.success(result.getData()));
    }

//    @ExceptionHandler(DateTimeException.class)
//    public ResponseEntity<?> DateTimeExceptionHandler(DateTimeException e){
//        return ResponseEntity.ok().body(ResponseMessage.fail(e.getMessage()));
//    }

}
