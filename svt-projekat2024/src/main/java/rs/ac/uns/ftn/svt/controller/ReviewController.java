package rs.ac.uns.ftn.svt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.svt.dto.ReviewDTO;
import rs.ac.uns.ftn.svt.model.Comment;
import rs.ac.uns.ftn.svt.model.Rate;
import rs.ac.uns.ftn.svt.model.Review;
import rs.ac.uns.ftn.svt.repository.*;
import rs.ac.uns.ftn.svt.service.ReviewService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final RateRepository rateRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ReviewController(ReviewService reviewService, UserRepository userRepository, FacilityRepository facilityRepository, RateRepository rateRepository, CommentRepository commentRepository) {
        this.reviewService = reviewService;
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
        this.rateRepository = rateRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/all-by-user")
    public ResponseEntity<List<ReviewDTO>> getReviewsByUserId(@RequestParam Long userId) {
        try {
            List<Review> reviews = reviewService.getReviewsByUserId(userId);

            if (reviews.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<ReviewDTO> reviewDTOs = reviews.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(reviewDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    private ReviewDTO convertToDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getCreatedAt(),
                review.getExerciseCount(),
                review.getHidden(),
                review.getFacility() != null ? review.getFacility().getName() : null,
                review.getComment() != null ? review.getComment().getText() : null,
                review.getRate()
        );
    }

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody Map<String, Object> reviewData) {
        try {
            // Kreiranje komentara
            String commentText = (String) reviewData.get("commentText");
            Comment comment = new Comment();
            comment.setText(commentText);
            comment.setCreatedAt(LocalDateTime.now());
            Comment savedComment = commentRepository.save(comment);

            // Kreiranje ocene (Rate)
            Map<String, Integer> rateData = (Map<String, Integer>) reviewData.get("rate");
            Rate rate = new Rate();
            rate.setEquipment(rateData.get("equipment"));
            rate.setStaff(rateData.get("staff"));
            rate.setHygiene(rateData.get("hygiene"));
            rate.setSpace(rateData.get("space"));
            Rate savedRate = rateRepository.save(rate);

            // Kreiranje recenzije
            Review review = new Review();
            review.setCreatedAt(LocalDateTime.now());
            review.setExerciseCount((Integer) reviewData.get("exerciseCount"));
            review.setHidden((Boolean) reviewData.get("hidden"));
            review.setUser(userRepository.findById(Long.valueOf((Integer) reviewData.get("userId"))).orElseThrow());
            review.setFacility(facilityRepository.findById(Long.valueOf((Integer) reviewData.get("facilityId"))).orElseThrow());
            review.setComment(savedComment);
            review.setRate(savedRate);

            Review createdReview = reviewService.createReview(review);

            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating review: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}