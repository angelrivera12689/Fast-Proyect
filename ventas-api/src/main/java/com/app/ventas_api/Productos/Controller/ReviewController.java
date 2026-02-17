package com.app.ventas_api.Productos.Controller;

import com.app.ventas_api.Productos.DTO.Request.ReviewRequestDto;
import com.app.ventas_api.Productos.Entity.Review;
import com.app.ventas_api.Productos.IService.IReviewService;
import com.app.ventas_api.seguridad.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final IReviewService reviewService;

    public ReviewController(IReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Get reviews for a product
    @GetMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getProductReviews(productId);
        return ResponseEntity.ok(reviews);
    }

    // Get rating summary for a product
    @GetMapping("/product/{productId}/summary")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getProductRatingSummary(@PathVariable Long productId) {
        Map<String, Object> summary = reviewService.getProductRatingSummary(productId);
        return ResponseEntity.ok(summary);
    }

    // Create a review
    @PostMapping("/product/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Review> createReview(
            @PathVariable Long productId,
            @Valid @RequestBody ReviewRequestDto request,
            @AuthenticationPrincipal User user) {
        try {
            Review review = reviewService.createReview(user, productId, request.getRating(), request.getComment());
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Update a review
    @PutMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Review> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewRequestDto request,
            @AuthenticationPrincipal User user) {
        try {
            Review review = reviewService.updateReview(user, reviewId, request.getRating(), request.getComment());
            return ResponseEntity.ok(review);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete a review
    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, String>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal User user) {
        try {
            reviewService.deleteReview(user, reviewId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Review deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Get user's review for a product
    @GetMapping("/product/{productId}/my-review")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Review> getMyReview(
            @PathVariable Long productId,
            @AuthenticationPrincipal User user) {
        Review review = reviewService.getUserReviewForProduct(user, productId);
        if (review != null) {
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }
}
