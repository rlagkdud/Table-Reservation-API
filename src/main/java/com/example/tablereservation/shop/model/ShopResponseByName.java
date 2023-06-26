package com.example.tablereservation.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopResponseByName {
    private Long id;
    private String name;
    private String location;
    private String description;
}
