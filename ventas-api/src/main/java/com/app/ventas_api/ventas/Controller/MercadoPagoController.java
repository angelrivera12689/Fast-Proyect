package com.app.ventas_api.ventas.Controller;

import com.app.ventas_api.ventas.Service.MercadoPagoService;
import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.IRepository.IOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments/mercadopago")
public class MercadoPagoController {

    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoController.class);

    private final MercadoPagoService mercadoPagoService;
    private final IOrderRepository orderRepository;

    public MercadoPagoController(MercadoPagoService mercadoPagoService, IOrderRepository orderRepository) {
        this.mercadoPagoService = mercadoPagoService;
        this.orderRepository = orderRepository;
    }

    /**
     * Crea una preferencia de pago para un pedido
     * POST /api/payments/mercadopago/create/{orderId}
     */
    @PostMapping("/create/{orderId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> createPaymentPreference(@PathVariable Long orderId) {
        try {
            logger.info("Solicitando preferencia de pago para orden: {}", orderId);

            // Buscar el pedido
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            // Verificar que el pedido esté en estado válido para pagar
            if (order.getStatus() != Order.OrderStatus.CART && 
                order.getStatus() != Order.OrderStatus.PENDING_PAYMENT) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "El pedido no está en estado válido para pagar",
                    "currentStatus", order.getStatus().name()
                ));
            }

            // Crear descripción del pedido
            String description = "Pedido #" + orderId + " - " + 
                    (order.getCompany() != null ? order.getCompany().getBusinessName() : "Cliente");

            // Crear preferencia en MercadoPago
            Map<String, Object> preference = mercadoPagoService.createPaymentPreference(
                    orderId,
                    order.getTotalAmount(),
                    description
            );

            // Actualizar estado del pedido
            order.setStatus(Order.OrderStatus.PENDING_PAYMENT);
            orderRepository.save(order);

            // Devolver la URL de pago
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orderId", orderId);
            response.put("amount", order.getTotalAmount());
            response.put("paymentUrl", preference.get("paymentUrl"));
            response.put("preferenceId", preference.get("preferenceId"));
            response.put("isSandbox", preference.get("isSandbox"));

            logger.info("Pago iniciado para orden: {}, URL: {}", orderId, preference.get("paymentUrl"));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error creando preferencia de pago: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Error al iniciar pago: " + e.getMessage()
            ));
        }
    }

    /**
     * Obtiene la configuración de MercadoPago (para el frontend)
     * GET /api/payments/mercadopago/config
     */
    @GetMapping("/config")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put("publicKey", mercadoPagoService.getPublicKey());
        config.put("isSandbox", mercadoPagoService.isSandbox());
        return ResponseEntity.ok(config);
    }

    /**
     * Consulta el estado de un pago
     * GET /api/payments/mercadopago/status/{paymentId}
     */
    @GetMapping("/status/{paymentId}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getPaymentStatus(@PathVariable String paymentId) {
        try {
            String status = mercadoPagoService.getPaymentStatus(paymentId);
            return ResponseEntity.ok(Map.of(
                "paymentId", paymentId,
                "status", status
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Error consultando estado: " + e.getMessage()
            ));
        }
    }
}
