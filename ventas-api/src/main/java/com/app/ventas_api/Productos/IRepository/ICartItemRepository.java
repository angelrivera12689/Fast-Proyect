package com.app.ventas_api.Productos.IRepository;

import com.app.ventas_api.Productos.Entity.CartItem;
import com.app.ventas_api.seguridad.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
    
    List<CartItem> findByUser(User user);
    
    Optional<CartItem> findByUserAndProductId(User user, Long productId);
    
    @Modifying
    void deleteByUser(User user);
    
    @Modifying
    void deleteByUserAndProductId(User user, Long productId);
}
