package com.example.tablereservation.shop.entity;

import com.example.tablereservation.user.entity.Partner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String location;
    private String description;

    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    @ManyToOne
    @JoinColumn
    private Partner partner;

}
