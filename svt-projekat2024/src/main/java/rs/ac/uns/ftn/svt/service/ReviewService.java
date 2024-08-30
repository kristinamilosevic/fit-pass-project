package rs.ac.uns.ftn.svt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.svt.model.Rate;
import rs.ac.uns.ftn.svt.model.Review;
import rs.ac.uns.ftn.svt.repository.RateRepository;
import rs.ac.uns.ftn.svt.repository.ReviewRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RateRepository rateRepository;

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

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}
