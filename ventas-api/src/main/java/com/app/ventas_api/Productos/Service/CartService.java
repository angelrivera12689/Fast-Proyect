package com.app.ventas_api.Productos.Service;

import com.app.ventas_api.Productos.Entity.CartItem;
import com.app.ventas_api.Productos.IRepository.ICartItemRepository;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.Productos.IService.ICartService;
import com.app.ventas_api.seguridad.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService implements ICartService {

    private final ICartItemRepository cartItemRepository;
    private final IProductRepository productRepository;

    public CartService(ICartItemRepository cartItemRepository, IProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public CartItem addToCart(User user, Long productId, Integer quantity) {
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if product already in cart
        var existingItem = cartItemRepository.findByUserAndProductId(user, productId);
        
        if (existingItem.isPresent()) {
            // Update quantity
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartItemRepository.save(cartItem);
        }

        // Create new cart item
        CartItem cartItem = new CartItem();
        cartItem.setUser(user);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public CartItem updateCartItemQuantity(User user, Long productId, Integer quantity) {
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        var cartItem = cartItemRepository.findByUserAndProductId(user, productId)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void removeFromCart(User user, Long productId) {
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        var cartItem = cartItemRepository.findByUserAndProductId(user, productId)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));
        
        cartItemRepository.delete(cartItem);
    }

    @Override
    public List<CartItem> getUserCart(User user) {
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        return cartItemRepository.findByUser(user);
    }

    @Override
    @Transactional
    public void clearCart(User user) {
        if (user == null) {
            throw new RuntimeException("User not authenticated");
        }
        cartItemRepository.deleteByUser(user);
    }
}
