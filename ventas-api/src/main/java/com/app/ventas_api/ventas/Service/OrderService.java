package com.app.ventas_api.ventas.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.seguridad.IRepository.IUserRepository;
import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.IRepository.IOrderRepository;
import com.app.ventas_api.ventas.IService.IOrderService;

/**
 * VENTAS - Service
 * Implementation: OrderService
 */
@Service
public class OrderService implements IOrderService {
    
    @Autowired
    private IOrderRepository repository;
    
    @Autowired
    private ICompanyRepository companyRepository;
    
    @Autowired
    private IUserRepository userRepository;
    
    @Override
    public List<Order> all() throws Exception {
        return repository.findAll();
    }
    
    @Override
    public Optional<Order> findById(Long id) throws Exception {
        Optional<Order> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Order not found");
        }
        return op;
    }
    
    @Override
    public Order save(Order entity) throws Exception {
        try {
            // Fetch and set Company entity
            if (entity.getCompany() != null && entity.getCompany().getId() != null) {
                entity.setCompany(companyRepository.findById(entity.getCompany().getId())
                        .orElseThrow(() -> new Exception("Company not found")));
            }
            // Fetch and set User entity
            if (entity.getUser() != null && entity.getUser().getId() != null) {
                entity.setUser(userRepository.findById(entity.getUser().getId())
                        .orElseThrow(() -> new Exception("User not found")));
            }
            return repository.save(entity);
        } catch (Exception e) {
            throw new Exception("Error saving order: " + e.getMessage());
        }
    }
    
    @Override
    public void update(Long id, Order entity) throws Exception {
        Optional<Order> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Order not found");
        }
        
        Order entityUpdate = op.get();
        entityUpdate.setCompany(entity.getCompany());
        entityUpdate.setUser(entity.getUser());
        entityUpdate.setTotalAmount(entity.getTotalAmount());
        entityUpdate.setStatus(entity.getStatus());
        entityUpdate.setShippingAddress(entity.getShippingAddress());
        entityUpdate.setNotes(entity.getNotes());
        
        repository.save(entityUpdate);
    }
    
    @Override
    public void delete(Long id) throws Exception {
        Optional<Order> op = repository.findById(id);
        if (op.isEmpty()) {
            throw new Exception("Order not found");
        }
        
        repository.delete(op.get());
    }
    
    @Override
    public List<Order> findByCompanyId(Long companyId) {
        return repository.findByCompany_Id(companyId);
    }
    
    @Override
    public List<Order> findByUserId(Long userId) {
        return repository.findByUser_Id(userId);
    }
    
    @Override
    public List<Order> findByStatus(Order.OrderStatus status) {
        return repository.findByStatus(status);
    }
    
    @Override
    public Optional<Order> findByCompanyIdAndId(Long companyId, Long id) throws Exception {
        return repository.findByCompany_IdAndId(companyId, id);
    }
}
