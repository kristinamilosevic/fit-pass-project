package rs.ac.uns.ftn.svt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.model.Rate;
import rs.ac.uns.ftn.svt.model.Review;
import rs.ac.uns.ftn.svt.repository.RateRepository;
import rs.ac.uns.ftn.svt.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RateRepository rateRepository;
    @Autowired
    private FacilityService facilityService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, RateRepository rateRepository) {
        this.reviewRepository = reviewRepository;
        this.rateRepository = rateRepository;
    }

    public Review createReview(Review review) {
        review.setCreatedAt(LocalDateTime.now());
        Rate savedRate = rateRepository.save(review.getRate());
        review.setRate(savedRate);
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByFacilityId(Long facilityId) {
        return reviewRepository.findByFacilityId(facilityId)
                .stream()
                .filter(review -> Boolean.TRUE.equals(review.getIsActive()) && Boolean.TRUE.equals(review.getHidden()))
                .collect(Collectors.toList());
    }
    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    public void updateReview(Review review) {
        reviewRepository.save(review);
    }

    public Optional<Review> findById(Long reviewId) {
        return reviewRepository.findById(reviewId);
    }

    public void setHiddenFalse(Long reviewId) {
        Optional<Review> review = reviewRepository.findById(reviewId);
        if (review.isPresent()) {
            Review existingReview = review.get();
            existingReview.setHidden(false);
            reviewRepository.save(existingReview);
        } else {
            throw new IllegalArgumentException("Review not found");
        }
    }

    public void setHiddenTrue(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id " + reviewId));
        review.setHidden(true);
        reviewRepository.save(review);
    }

    public void deleteReview(Long reviewId) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            review.setIsActive(false);
            updateReview(review);

            Long facilityId = review.getFacility().getId();
            facilityService.updateTotalRating(facilityId);

        }
    }
}
