package com.app.ventas_api.Productos.Controller;

import com.app.ventas_api.Productos.DTO.Request.CartRequestDto;
import com.app.ventas_api.Productos.DTO.Response.CartItemResponseDto;
import com.app.ventas_api.Productos.Entity.CartItem;
import com.app.ventas_api.Productos.IService.ICartService;
import com.app.ventas_api.seguridad.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }

    // Get user's cart
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<List<CartItemResponseDto>> getMyCart(@AuthenticationPrincipal User user) {
        List<CartItem> cartItems = cartService.getUserCart(user);
        List<CartItemResponseDto> response = new ArrayList<>();
        
        for (CartItem item : cartItems) {
            CartItemResponseDto dto = new CartItemResponseDto();
            dto.setId(item.getId());
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setProductCategory(item.getProduct().getCategory() != null ? item.getProduct().getCategory().getName() : null);
            dto.setProductLaboratory(item.getProduct().getLaboratory() != null ? item.getProduct().getLaboratory().getName() : null);
            dto.setPrice(item.getProduct().getBasePrice() != null ? item.getProduct().getBasePrice().doubleValue() : 0.0);
            dto.setStock(item.getProduct().getStock());
            dto.setQuantity(item.getQuantity());
            dto.setImageUrl(item.getProduct().getImageUrl());
            response.add(dto);
        }
        
        return ResponseEntity.ok(response);
    }

    // Add product to cart
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestBody CartRequestDto request,
            @AuthenticationPrincipal User user) {
        try {
            int quantity = request.getQuantity() != null ? request.getQuantity() : 1;
            CartItem cartItem = cartService.addToCart(user, request.getProductId(), quantity);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product added to cart");
            response.put("productId", request.getProductId());
            response.put("quantity", cartItem.getQuantity());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Update cart item quantity
    @PutMapping("/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> updateCartItem(
            @PathVariable Long productId,
            @RequestBody CartRequestDto request,
            @AuthenticationPrincipal User user) {
        try {
            CartItem cartItem = cartService.updateCartItemQuantity(user, productId, request.getQuantity());
            
            Map<String, Object> response = new HashMap<>();
            if (cartItem != null) {
                response.put("message", "Cart updated");
                response.put("quantity", cartItem.getQuantity());
            } else {
                response.put("message", "Product removed from cart");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Remove product from cart
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @PathVariable Long productId,
            @AuthenticationPrincipal User user) {
        try {
            cartService.removeFromCart(user, productId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Product removed from cart");
            response.put("productId", productId);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    // Clear cart
    @DeleteMapping
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> clearCart(@AuthenticationPrincipal User user) {
        try {
            cartService.clearCart(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Cart cleared");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
