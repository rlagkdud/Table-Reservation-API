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
public class ReviewUpdateInput {

    @NotBlank(message = "리뷰 내용은 필수 항목입니다.")
    private String description;

    @NotNull(message = "별점은 필수 항목입니다.")
    @Range(min = 1, max = 5)
    private Integer star;
}
