package com.app.ventas_api.Productos.IService;

import com.app.ventas_api.Productos.Entity.CartItem;
import com.app.ventas_api.seguridad.domain.User;

import java.util.List;

public interface ICartService {

    CartItem addToCart(User user, Long productId, Integer quantity);

    CartItem updateCartItemQuantity(User user, Long productId, Integer quantity);

    void removeFromCart(User user, Long productId);

    List<CartItem> getUserCart(User user);

    void clearCart(User user);
}
