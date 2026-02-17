package com.app.ventas_api.Productos.IService;

import com.app.ventas_api.Productos.Entity.Review;
import com.app.ventas_api.seguridad.domain.User;

import java.util.List;
import java.util.Map;

public interface IReviewService {

    Review createReview(User user, Long productId, Integer rating, String comment);

    Review updateReview(User user, Long reviewId, Integer rating, String comment);

    void deleteReview(User user, Long reviewId);

    List<Review> getProductReviews(Long productId);

    Map<String, Object> getProductRatingSummary(Long productId);

    Review getUserReviewForProduct(User user, Long productId);
}
