package com.example.tablereservation.shop.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShopResponseByDistance {

    private Long id;
    private String name;
    private String location;
    private String description;
    private Double distance;

    public ShopResponseByDistance(BigInteger id, String name, String location, String description, Double distance){
        this.id = id.longValue();
        this.name = name;
        this.location = location;
        this.description = description;
        this.distance = distance;
    }
}
