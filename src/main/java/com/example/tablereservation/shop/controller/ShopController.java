package com.example.tablereservation.shop.controller;

import com.example.tablereservation.shop.model.ShopAddInput;
import com.example.tablereservation.shop.model.ShopUpdateInput;
import com.example.tablereservation.shop.service.ShopService;
import com.example.tablereservation.user.model.ResponseError;
import com.example.tablereservation.user.model.ResponseMessage;
import com.example.tablereservation.user.model.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;

    /**
     * 매장 등록 - create
     */
    @PostMapping("/api/shop")
    public ResponseEntity<?> addShop(@RequestBody @Valid ShopAddInput shopAddInput, Errors errors){
        // 입력값 유효성 체크
        if(errors.hasErrors()){
            List<ResponseError> responseErrorList = ResponseError.of(errors.getAllErrors());
            return new ResponseEntity(ResponseMessage.fail("입력값이 정확하지 않습니다.", responseErrorList), HttpStatus.BAD_REQUEST);
        }
        // 서비스 호출
        ServiceResult result = shopService.addShop(shopAddInput);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /**
     * 매장 상세 정보 - read
     */
    @GetMapping("/api/shop/{id}")
    public ResponseEntity<?> getShop(@PathVariable Long id){
        ServiceResult result = shopService.getShop(id);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseMessage.success(result.getData()));
    }

    /**
     * 매장 정보 수정 - update
     */
    @PutMapping("/api/shop/{id}")
    public ResponseEntity<?> updateShop(@PathVariable Long id, @RequestBody @Valid ShopUpdateInput shopUpdateInput, Errors errors){
        // 입력값에 대한 유효성 검사
        if(errors.hasErrors()){
            List<ResponseError> responseErrorList = ResponseError.of(errors.getAllErrors());
            return new ResponseEntity<>(ResponseMessage.fail("입력값이 정확하지 않습니다.", responseErrorList), HttpStatus.BAD_REQUEST);
        }

        ServiceResult result = shopService.updateShop(id, shopUpdateInput);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }
        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /**
     * 매장 삭제 - delete
     */
    @DeleteMapping("/api/shop/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable Long id){
        ServiceResult result = shopService.deleteShop(id);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));

        }
        return ResponseEntity.ok().body(ResponseMessage.success());
    }

    /** 매장 검색
     *
     */
    @GetMapping("/api/shop")
    public ResponseEntity<?> searchShop(@RequestParam("keyword")String keyword){
        ServiceResult result = shopService.searchShop(keyword);

        if(!result.isResult()){
            return ResponseEntity.ok().body(ResponseMessage.fail(result.getMessage()));
        }

        return ResponseEntity.ok().body(ResponseMessage.success(result.getData()));
    }
}
