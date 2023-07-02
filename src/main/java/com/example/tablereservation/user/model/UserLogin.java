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
public class UserLogin {

    @NotBlank(message = "이메일은 항목은 필수 입니다.")
    private String email;

    @NotBlank(message = "비밀번호 항목은 필수 입니다.")
    private String password;
}