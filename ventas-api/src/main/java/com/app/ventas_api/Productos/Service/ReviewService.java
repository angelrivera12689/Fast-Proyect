package com.app.ventas_api.Productos.Service;

import com.app.ventas_api.Productos.Entity.Review;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.Productos.IRepository.IReviewRepository;
import com.app.ventas_api.Productos.IService.IReviewService;
import com.app.ventas_api.seguridad.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final IProductRepository productRepository;

    public ReviewService(IReviewRepository reviewRepository, IProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Review createReview(User user, Long productId, Integer rating, String comment) {
        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        // Check if already reviewed
        if (reviewRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new RuntimeException("You have already reviewed this product");
        }

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public Review updateReview(User user, Long reviewId, Integer rating, String comment) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Verify ownership
        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only edit your own reviews");
        }

        // Validate rating
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(User user, Long reviewId) {
        var review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // Verify ownership or admin
        if (!review.getUser().getId().equals(user.getId()) && !user.getRoles().stream()
                .anyMatch(role -> "ADMIN".equals(role.getName()))) {
            throw new RuntimeException("You can only delete your own reviews");
        }

        reviewRepository.delete(review);
    }

    @Override
    public List<Review> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Map<String, Object> getProductRatingSummary(Long productId) {
        Double averageRating = reviewRepository.getAverageRatingByProductId(productId);
        Long totalReviews = reviewRepository.countByProductId(productId);

        Map<String, Object> summary = new HashMap<>();
        summary.put("averageRating", averageRating != null ? averageRating : 0.0);
        summary.put("totalReviews", totalReviews != null ? totalReviews : 0);

        return summary;
    }

    @Override
    public Review getUserReviewForProduct(User user, Long productId) {
        return reviewRepository.findByUserIdAndProductId(user.getId(), productId).orElse(null);
    }
}
