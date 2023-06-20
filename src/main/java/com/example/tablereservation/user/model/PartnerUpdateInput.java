package com.example.tablereservation.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerUpdateInput {

    @NotBlank(message = "점주 이름은 필수 항목입니다.")
    private String userName;

    @NotBlank(message = "점주 전화번호는 필수 항목입니다.")
    private String phone;
}
