package com.example.tablereservation.shop.model;

import com.example.tablereservation.user.entity.Partner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopAddInput {

    @NotBlank(message = "매장 명은 필수 항목입니다.")
    private String name;
    @NotBlank(message = "매장 위치는 필수 항목입니다.")
    private String location;

    @NotBlank(message = "매장 설명은 필수 항목입니다.")
    private String description;

    @NotBlank(message = "점주는 필수 항목입니다.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String partnerEmail;
}
