package com.example.tablereservation.reservation.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DateInput {


    @NotBlank(message = "예약 년도는 필수 항목 입니다.")
    private String year;

    @NotBlank(message = "예약 월은 필수 항목 입니다.")
    @Range(min = 1, max = 12)
    private String month;

    @NotBlank(message = "예약 일자는 필수 항목 입니다.")
    @Range(min = 1, max = 31)
    private String day;

}
