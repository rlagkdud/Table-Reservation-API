package com.example.tablereservation.review.service;

import com.example.tablereservation.reservation.entity.Reservation;
import com.example.tablereservation.reservation.repository.ReservationRepository;
import com.example.tablereservation.review.entity.Review;
import com.example.tablereservation.review.model.ReviewAddInput;
import com.example.tablereservation.review.model.ReviewResponse;
import com.example.tablereservation.review.model.ReviewUpdateInput;
import com.example.tablereservation.review.repository.ReviewRepository;
import com.example.tablereservation.shop.entity.Shop;
import com.example.tablereservation.shop.repository.ShopRepository;
import com.example.tablereservation.user.entity.User;
import com.example.tablereservation.user.model.ServiceResult;
import com.example.tablereservation.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private  final ShopRepository shopRepository;

    private final ReviewRepository reviewRepository;

    /**
     * 예약 이용 후 리뷰 등록
     * - 예약 유무확인
     * - 사용자 유무 확인
     * - 가게 유무확인
     * - 예약한 사용자가 맞는지 확인
     * - 예약한 가게를 이용한게 맞는지 확인
     * - 예약 도착 확인
     * @param reservationId
     * @param reviewAddInput
     * @return
     */
    public ServiceResult addReview(Long reservationId, ReviewAddInput reviewAddInput) {

        // 예약 유무 확인
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if(!optionalReservation.isPresent()){
            return ServiceResult.fail("해당 예약이 존재하지 않습니다.");
        }

        Reservation reservation = optionalReservation.get();

        // 사용자 유무확인
        Optional<User> optionalUser = userRepository.findByEmail(reviewAddInput.getUserEmail());
        if(!optionalUser.isPresent()){
            return ServiceResult.fail("해당 사용자가 존재하지 않습니다.");
        }

        User user = optionalUser.get();

        // 가게 유무 확인
        Optional<Shop> optionalShop = shopRepository.findByNameAndLocation(reviewAddInput.getShopName(), reviewAddInput.getShopLocation());
        if(!optionalShop.isPresent()){
            return ServiceResult.fail("해당 매장이 존재하지 않습니다.");
        }

        Shop shop = optionalShop.get();

        // 예약자와 리뷰남길 사용자가 동일한지 확인
        if(!reservation.getUser().getEmail().equals(user.getEmail())){
            return ServiceResult.fail("사용자와 예약자가 다릅니다.");
        }

        // 예약가게와 리뷰 가계가 동일한지 확인
        if(reservation.getShop().getId() != shop.getId()){
            return ServiceResult.fail("리뷰를 남길 가게와 예약한 가게가 다릅니다.");
        }

        // 예약을 이용했는지 확인
        if(!reservation.getArrivedYn().booleanValue() || reservation.getArrivedYn()== null){
            return ServiceResult.fail("예약을 이용하지 않았습니다.");
        }

        // 리뷰 등록
        Review review = Review.builder()
                .description(reviewAddInput.getDescription())
                .star(reviewAddInput.getStar())
                .regDate(LocalDateTime.now())
                .user(user)
                .shop(shop)
                .build();

        reviewRepository.save(review);

        return ServiceResult.success();
    }

    /**
     * 리뷰 조회
     * @param id
     * @return
     */
    public ServiceResult getReview(Long id) {
        Optional<Review> optionalReview = reviewRepository.findById(id);

        if(!optionalReview.isPresent()){
            return ServiceResult.fail("해당 리뷰가 존재하지 않습니다.");
        }

        Review review = optionalReview.get();

        ReviewResponse reviewResponse = ReviewResponse.builder()
                .userEmail(review.getUser().getEmail())
                .description(review.getDescription())
                .star(review.getStar())
                .shopName(review.getShop().getName())
                .shopLocation(review.getShop().getLocation())
                .regDate(review.getRegDate())
                .build();

        return ServiceResult.success(reviewResponse);

    }

    /**
     * 사용자가 작성한 모든 리뷰 조회
     * - 사용자 존재여부 확인
     * @param userId
     * @return
     */
    public ServiceResult getReviewListByUser(Long userId) {
        // 사용자 존재 여부
        Optional<User> optionalUser = userRepository.findById(userId);
        if(!optionalUser.isPresent()){
            return ServiceResult.fail("해당 사용자가 존재하지 않습니다.");
        }

        User user = optionalUser.get();

        // 사용자가 작성한 리뷰 목록 가져오기
        List<Review> reviewList = reviewRepository.findAllByUser(user);
        List<ReviewResponse> reviewResponseList = new ArrayList<>();

        reviewList.stream().forEach(r->{
            reviewResponseList.add(
                    ReviewResponse.builder()
                            .userEmail(r.getUser().getEmail())
                            .description(r.getDescription())
                            .star(r.getStar())
                            .shopName(r.getShop().getName())
                            .shopLocation(r.getShop().getLocation())
                            .regDate(r.getRegDate())
                            .build()
            );
        });
        return ServiceResult.success(reviewResponseList);

    }

    /**
     * 리뷰 수정
     * - 내용과 별점만 수정 가능
     * @param id
     * @return
     */
    public ServiceResult updateReview(Long id, ReviewUpdateInput reviewUpdateInput) {
        // 리뷰 유무
        Optional<Review> optionalReview = reviewRepository.findById(id);
        if(!optionalReview.isPresent()){
            return ServiceResult.fail("해당 리뷰가 존재하지 않습니다.");
        }

        Review review = optionalReview.get();

        review.setDescription(reviewUpdateInput.getDescription());
        review.setStar(reviewUpdateInput.getStar());
        review.setUpdateDate(LocalDateTime.now());

        reviewRepository.save(review);

        return ServiceResult.success();

    }
}
