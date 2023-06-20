package com.example.tablereservation.shop.controller;

import com.example.tablereservation.shop.model.ShopAddInput;
import com.example.tablereservation.shop.service.ShopService;
import com.example.tablereservation.user.model.ResponseError;
import com.example.tablereservation.user.model.ResponseMessage;
import com.example.tablereservation.user.model.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
