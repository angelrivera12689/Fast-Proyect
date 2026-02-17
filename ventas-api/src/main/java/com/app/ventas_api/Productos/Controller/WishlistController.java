package com.app.ventas_api.Productos.Controller;

import com.app.ventas_api.Productos.Entity.Wishlist;
import com.app.ventas_api.Productos.IService.IWishlistService;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.seguridad.IService.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final IWishlistService wishlistService;
    private final IUserService userService;

    public WishlistController(IWishlistService wishlistService, IUserService userService) {
        this.wishlistService = wishlistService;
        this.userService = userService;
    }

    // Get user's wishlist
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<Wishlist>> getMyWishlist(@AuthenticationPrincipal User user) {
        List<Wishlist> wishlist = wishlistService.getUserWishlist(user);
        return ResponseEntity.ok(wishlist);
    }

    // Add product to wishlist
    @PostMapping("/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> addToWishlist(
            @PathVariable Long productId,
            @AuthenticationPrincipal User user) {
        try {
            Wishlist wishlist = wishlistService.addToWishlist(user, productId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product added to wishlist");
            response.put("productId", productId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Remove product from wishlist
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> removeFromWishlist(
            @PathVariable Long productId,
            @AuthenticationPrincipal User user) {
        try {
            wishlistService.removeFromWishlist(user, productId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product removed from wishlist");
            response.put("productId", productId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Check if product is in wishlist
    @GetMapping("/check/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Boolean>> checkWishlist(
            @PathVariable Long productId,
            @AuthenticationPrincipal User user) {
        boolean inWishlist = wishlistService.isInWishlist(user, productId);
        
        Map<String, Boolean> response = new HashMap<>();
        response.put("inWishlist", inWishlist);
        
        return ResponseEntity.ok(response);
    }
}
