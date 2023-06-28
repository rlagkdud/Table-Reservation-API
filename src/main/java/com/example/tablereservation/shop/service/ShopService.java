package com.example.tablereservation.shop.service;

import com.example.tablereservation.reservation.repository.ReservationRepository;
import com.example.tablereservation.shop.entity.Shop;
import com.example.tablereservation.shop.model.*;
import com.example.tablereservation.shop.repository.CustomShopRepository;
import com.example.tablereservation.shop.repository.ShopRepository;
import com.example.tablereservation.user.entity.Partner;
import com.example.tablereservation.user.model.ServiceResult;
import com.example.tablereservation.user.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;
    private final PartnerRepository partnerRepository;

    private final ReservationRepository reservationRepository;

    private final CustomShopRepository customShopRepository;


    /**
     * 매장 등록
     * - 점주가 존재하지 않으면 등록 불가
     * - 중복 매장 등록 불가
     *
     * @param shopAddInput
     * @return
     */
    public ServiceResult addShop(ShopAddInput shopAddInput) {
        // 점주 등록 여부 확인
        Optional<Partner> optionalPartner = partnerRepository.findByEmail(shopAddInput.getPartnerEmail());
        if (!optionalPartner.isPresent()) {
            return ServiceResult.fail("해당 점주가 존재하지 않습니다.");
        }

        Partner partner = optionalPartner.get();

        // 매장 중복 여부 확인
        int count = shopRepository.countByNameAndLocation(shopAddInput.getName(), shopAddInput.getLocation());
        if (count > 0) {
            return ServiceResult.fail("이미 존재하는 매장입니다.");
        }

        Shop shop = Shop.builder()
                .name(shopAddInput.getName())
                .location(shopAddInput.getLocation())
                .description(shopAddInput.getDescription())
                .regDate(LocalDateTime.now())
                .partner(partner)
                .longitude(shopAddInput.getLongitude())
                .latitude(shopAddInput.getLatitude())
                .build();

        shopRepository.save(shop);
        return ServiceResult.success();

    }

    /**
     * 매장 조회
     *
     * @param id
     * @return
     */
    public ServiceResult getShop(Long id) {
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (!optionalShop.isPresent()) {
            return ServiceResult.fail("등록된 매장이 없습니다.");
        }
        Shop shop = optionalShop.get();
        ShopResponse shopResponse = ShopResponse.builder()
                .name(shop.getName())
                .location(shop.getLocation())
                .description(shop.getDescription())
                .partnerName(shop.getPartner().getUserName())
                .partnerPhone(shop.getPartner().getPhone())
                .build();
        return ServiceResult.success(shopResponse);
    }

    /**
     * 매장 정보 수정
     *
     * @param id
     * @param shopUpdateInput
     * @return
     */
    public ServiceResult updateShop(Long id, ShopUpdateInput shopUpdateInput) {
        // 매장 존재여부
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (!optionalShop.isPresent()) {
            return ServiceResult.fail("해당 매장이 존재하지 않습니다.");
        }

        Shop shop = optionalShop.get();
        shop.setName(shopUpdateInput.getName());
        shop.setLocation(shopUpdateInput.getLocation());
        shop.setDescription(shopUpdateInput.getDescription());
        shop.setUpdateDate(LocalDateTime.now());
        shopRepository.save(shop);

        return ServiceResult.success();

    }

    /**
     * 매장 삭제
     * - 예약 존재하면 삭제 불가
     *
     * @param id
     * @return
     */
    public ServiceResult deleteShop(Long id) {
        // 메장 존재 여부
        Optional<Shop> optionalShop = shopRepository.findById(id);
        if (!optionalShop.isPresent()) {
            return ServiceResult.fail("해당 매장이 존재하지 않습니다.");
        }

        Shop shop = optionalShop.get();

        // 예약 존재 여부
        int count = reservationRepository.countByShop(shop);
        if (count > 0) {
            return ServiceResult.fail("삭제하려는 매장에 예약이 존재합니다.");
        }

        // 삭제
        shopRepository.delete(shop);
        return ServiceResult.success();
    }

    /**
     * 매장 검색
     * - 검색어가 포함된 매장 찾기
     *
     * @param keyword
     * @return
     */
    public ServiceResult searchShop(String keyword) {
        List<Shop> shopList = shopRepository.findByNameContaining(keyword);

        if (shopList.size() == 0) {
            return ServiceResult.fail("매장을 찾지 못했습니다.");
        }

        List<ShopResponse> shopResponseList = new ArrayList<>();
        shopList.stream().forEach(s -> {
            shopResponseList.add(
                    ShopResponse.builder()
                            .name(s.getName())
                            .location(s.getLocation())
                            .description(s.getDescription())
                            .partnerName(s.getPartner().getUserName())
                            .partnerPhone(s.getPartner().getPhone())
                            .build()
            );
        });
        return ServiceResult.success(shopResponseList);
    }

    /**
     * 별점순 매장 조회
     *
     * @return
     */
    public ServiceResult getShopListOrderByStar() {
        List<ShopResponseByStar> list = customShopRepository.findAllShopAvgStar();
        return ServiceResult.success(list);

    }


    /**
     * 가나다순 매장 조회
     *
     * @return
     */
    public ServiceResult getShopListOrderByName() {
        List<Shop> shopList = shopRepository.findAllByOrderByNameAsc();
        List<ShopResponseByName> shopResponseByNameList = new ArrayList<>();

        shopList.stream().forEach(s -> {
            shopResponseByNameList.add(
                    ShopResponseByName.builder()
                            .id(s.getId())
                            .name(s.getName())
                            .location(s.getLocation())
                            .description(s.getDescription())
                            .build()
                    );
        });

        return ServiceResult.success(shopResponseByNameList);

    }

    /**
     * 거리순 매장 조회
     *
     * @param currentPoint
     * @return
     */
    public ServiceResult getShopListOrderByDistance(CurrentPoint currentPoint) {
        List<ShopResponseByDistance> list = customShopRepository.findAllShopDistance(currentPoint);

        return ServiceResult.success(list);
    }
}
