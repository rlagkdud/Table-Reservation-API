package com.example.tablereservation.review.controller;

import com.example.tablereservation.review.model.ReviewAddInput;
import com.example.tablereservation.review.model.ReviewUpdateInput;
import com.example.tablereservation.review.service.ReviewService;
import com.example.tablereservation.user.model.ResponseError;
import com.example.tablereservation.user.model.ResponseMessage;
import com.example.tablereservation.user.model.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 예약 이용 후 리뷰 등록 - create
     */
    @PostMapping("/api/review/reservation/{reservationId}")
    public ResponseEntity<?> addReview(@PathVariable Long reservationId, @RequestBody @Valid ReviewAddInput reviewAddInput, Errors errors){

        // 입력값 유효성 검사
        if(errors.hasErrors()){
            List<ResponseError> responseErrorList = ResponseError.of(errors.getAllErrors());
            return new ResponseEntity(ResponseMessage.fail("입력값이 정확하지 않습니다", responseErrorList), HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = reviewService.addReview(reservationId, reviewAddInput);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /**
     * 리뷰 조회 - read
     */
    @GetMapping("/api/review/{id}")
    public ResponseEntity<?> getReview(@PathVariable Long id){
        ServiceResult result = reviewService.getReview(id);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().body(ResponseMessage.success(result.getData()));
    }

    /**
     * 사용자가 작성한 모든 리뷰 조회 - read
     */
    @GetMapping("/api/review/user/{userId}")
    public ResponseEntity<?> getReviewListByUser(@PathVariable Long userId){
        ServiceResult result = reviewService.getReviewListByUser(userId);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().body(ResponseMessage.success(result.getData()));
    }

    /**
     * 리뷰 수정 - update(별점, 내용)
     */
    @PutMapping("/api/review/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody @Valid  ReviewUpdateInput reviewUpdateInput, Errors errors){

        // 입력값 유효성 검사
        if(errors.hasErrors()){
            List<ResponseError> responseErrorList = ResponseError.of(errors.getAllErrors());
            return new ResponseEntity(ResponseMessage.fail("입력값이 정확하지 않습니다", responseErrorList), HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = reviewService.updateReview(id, reviewUpdateInput);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().body(ResponseMessage.success());

    }
}
