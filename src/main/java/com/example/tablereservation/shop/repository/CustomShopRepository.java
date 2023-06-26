package com.example.tablereservation.shop.repository;

import com.example.tablereservation.shop.entity.Shop;
import com.example.tablereservation.shop.model.ShopResponseByStar;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomShopRepository {

    private final EntityManager entityManager;

    public List<ShopResponseByStar> findAllShopAvgStar(){
        String sql = "SELECT s.id, s.NAME, s.LOCATION, s.DESCRIPTION, AVG(r.STAR) AS 평균_별점" +
                " FROM Review r" +
                " JOIN Shop s ON r.SHOP_ID = s.ID" +
                " GROUP BY s.id\n" +
                " order by 평균_별점 desc;";


        Query nativeQuery = entityManager.createNativeQuery(sql);
        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        List<ShopResponseByStar> list = jpaResultMapper.list(nativeQuery, ShopResponseByStar.class);

        return list;
    }

}
