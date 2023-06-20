package com.example.tablereservation.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PartnerAddInput {

    @NotBlank(message = "점주명은 필수 항목입니다.")
    private String userName;

    @NotBlank(message = "점주 이메일은 필수 항목 입니다.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

    @NotBlank(message = "점주 전화번호는 필수 항목입니다.")
    private String phone;

    @NotBlank(message = "점주 비밀번호는 필수 항목입니다.")
    private String password;
}
