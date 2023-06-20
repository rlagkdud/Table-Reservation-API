package com.example.tablereservation.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopUpdateInput {

    @NotBlank(message = "매장 명은 필수 항목입니다.")
    private String name;

    @NotBlank(message = "매장 위치는 필수 항목입니다.")
    private String location;

    @NotBlank(message = "매장 설명은 필수 항목입니다.")
    private String description;
}
