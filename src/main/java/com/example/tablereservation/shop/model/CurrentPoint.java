package com.example.tablereservation.shop.model;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CurrentPoint {

    @NotNull(message = "현재 위치(경도)는 필수 항목입니다.")
    @Range(min = 124, max = 133, message = "우리나라 경도는 124~133사이 입니다.")
    private Double currentLongitude;

    @NotNull(message = "현재 위치(위도)는 필수 항목입니다.")
    @Range(min = 33, max = 44, message = "우리나라 위도는 33~44사이 입니다.")
    private Double currentLatitude;
}
