package com.example.tablereservation.reservation.model;

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
public class ReservationPartnerInput {

    @NotBlank(message = "이메일은 필수 항목 입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

}
