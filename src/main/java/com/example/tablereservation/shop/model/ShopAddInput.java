package com.example.tablereservation.shop.model;

import com.example.tablereservation.user.entity.Partner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @NotNull(message = "매장 상세 위치(경도)는 필수 항목 입니다.")
    @Range(min = 124, max = 133, message = "우리나라의 경도는 124~132사이 입니다.")
    private double longitude;

    @NotNull(message = "매장 상세 위치(위도)는 필수 항목 입니다.")
    @Range(min = 33, max = 44, message = "우리나라의 위도는 33~43사이 입니다")
    private double latitude;
}
