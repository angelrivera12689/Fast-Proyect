package com.app.ventas_api.Productos.IService;

import com.app.ventas_api.Productos.Entity.Wishlist;
import com.app.ventas_api.seguridad.domain.User;

import java.util.List;

public interface IWishlistService {

    Wishlist addToWishlist(User user, Long productId);

    void removeFromWishlist(User user, Long productId);

    List<Wishlist> getUserWishlist(User user);

    boolean isInWishlist(User user, Long productId);
}
