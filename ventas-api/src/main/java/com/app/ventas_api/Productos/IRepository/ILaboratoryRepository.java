package com.app.ventas_api.Productos.IRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.ventas_api.Productos.Entity.Laboratory;

@Repository
public interface ILaboratoryRepository extends JpaRepository<Laboratory, Long> {
    
    List<Laboratory> findByActiveTrue();
    
    Optional<Laboratory> findByNameIgnoreCase(String name);
    
    boolean existsByNameIgnoreCase(String name);
}
