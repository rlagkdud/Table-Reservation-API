package com.example.tablereservation.user.model;


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
public class UserAddInput {

    @NotBlank(message = "사용자명 필수 항목입니다.")
    private String userName;

    @NotBlank(message = "사용자 이메일은 필수 항목 입니다.")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;

    @NotBlank(message = "사용자 전화번호는 필수 항목입니다.")
    private String phone;

    @NotBlank(message = "사용자 비밀번호는 필수 항목입니다.")
    private String password;
}
