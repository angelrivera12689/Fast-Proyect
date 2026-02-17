package com.app.ventas_api.Productos.Service;

import com.app.ventas_api.Productos.Entity.Wishlist;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.Productos.IRepository.IWishlistRepository;
import com.app.ventas_api.Productos.IService.IWishlistService;
import com.app.ventas_api.seguridad.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WishlistService implements IWishlistService {

    private final IWishlistRepository wishlistRepository;
    private final IProductRepository productRepository;

    public WishlistService(IWishlistRepository wishlistRepository, IProductRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Wishlist addToWishlist(User user, Long productId) {
        // Check if already in wishlist
        if (wishlistRepository.existsByUserIdAndProductId(user.getId(), productId)) {
            throw new RuntimeException("Product already in wishlist");
        }

        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);

        return wishlistRepository.save(wishlist);
    }

    @Override
    @Transactional
    public void removeFromWishlist(User user, Long productId) {
        var wishlist = wishlistRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Product not in wishlist"));
        
        wishlistRepository.delete(wishlist);
    }

    @Override
    public List<Wishlist> getUserWishlist(User user) {
        return wishlistRepository.findByUserId(user.getId());
    }

    @Override
    public boolean isInWishlist(User user, Long productId) {
        return wishlistRepository.existsByUserIdAndProductId(user.getId(), productId);
    }
}
