package com.example.tablereservation.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAddInput {

    private String description;

    @NotNull(message = "별점은 필수 항목 입니다.")
    @Range(min= 1, max = 5, message = "별점은 1~5개 사이 입니다.")
    private Integer star;

    @NotBlank(message = "사용자 이메일은 필수 항목 입니다.")
    private String userEmail;

    @NotBlank(message = "가게 이름은 필수 항목입니다.")
    private String shopName;

    @NotBlank(message = "가게 위치는 필수 항목 입니다.")
    private String shopLocation;
}
