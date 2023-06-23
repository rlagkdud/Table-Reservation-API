package com.example.tablereservation.reservation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCheckInput {

    @NotBlank(message = "사용자 전화번호는 필수 항목입니다.")
    private String phone;
}
