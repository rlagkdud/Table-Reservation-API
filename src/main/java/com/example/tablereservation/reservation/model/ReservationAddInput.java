package com.example.tablereservation.reservation.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationAddInput {

    @NotBlank(message = "예약자 이름은 필수 항목입니다.")
    private String userName;

    @NotBlank(message = "예약자 전화번호는 필수 항목입니다.")
    private String userPhone;

    @NotBlank(message = "예약자 이메일은 필수 항목 입니다.")
    private String userEmail;


    @NotBlank(message = "가게 이름은 필수 항목입니다.")
    private String shopName;
    @NotBlank(message = "가게 위치는 필수 항목입니다.")
    private String shopLocation;


    @Range(min = 1, max = 12)
    private int month;


    @Range(min = 1, max = 31)
    private int day;


    @Range(min = 0, max = 24)
    private int time;


    @Range(min = 0, max = 59)
    private int minute;
}
