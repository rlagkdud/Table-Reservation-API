package com.example.tablereservation.shop.repository;

import com.example.tablereservation.shop.entity.Shop;
import com.example.tablereservation.user.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {
    int countByPartner(Partner partner);

    int countByNameAndLocation(String name, String location);
}
