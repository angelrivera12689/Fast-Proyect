package com.app.ventas_api.ventas.Controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.ventas_api.ventas.domain.Order;
import com.app.ventas_api.ventas.IService.IOrderService;

/**
 * VENTAS - Controller
 * Webhook para recibir notificaciones de pago de pasarelas externas
 * (Stripe, MercadoPago, etc.)
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/webhooks")
public class PaymentWebhookController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentWebhookController.class);

    @Autowired
    private IOrderService orderService;


    @PostMapping("/payment")
    public ResponseEntity<Map<String, String>> handlePaymentWebhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-Webhook-Signature", required = false) String signature) {
        
        try {
            logger.info("Webhook de pago recibido: {}", payload);
            
            // Extraer el ID del pedido del payload
            Long orderId = extractOrderId(payload);
            
            if (orderId == null) {
                logger.warn("No se pudo extraer el orderId del webhook");
                return ResponseEntity.badRequest().body(Map.of("error", "Missing orderId"));
            }
            
            // Determinar el tipo de evento
            String eventType = extractEventType(payload);
            
            switch (eventType) {
                case "payment_intent.succeeded":
                case "payment.updated":
                case "payment.succeeded":
                case "PAID":
                    // Confirmar el pago y reducir stock
                    Order order = orderService.confirmPayment(orderId);
                    logger.info("Pago confirmado para orden: {}, nuevo estado: {}", orderId, order.getStatus());
                    return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "orderId", orderId.toString(),
                        "orderStatus", order.getStatus().name()
                    ));
                    
                case "payment.failed":
                case "payment.canceled":
                    // Cancelar el pedido
                    Order cancelledOrder = orderService.cancelOrder(orderId);
                    logger.info("Pago fallido/cancelado para orden: {}, estado: {}", orderId, cancelledOrder.getStatus());
                    return ResponseEntity.ok(Map.of(
                        "status", "cancelled",
                        "orderId", orderId.toString(),
                        "orderStatus", cancelledOrder.getStatus().name()
                    ));
                    
                default:
                    logger.info("Evento de pago no procesado: {}", eventType);
                    return ResponseEntity.ok(Map.of("status", "ignored", "eventType", eventType));
            }
            
        } catch (Exception e) {
            logger.error("Error procesando webhook de pago: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Webhook específico para Stripe
     * POST /api/webhooks/stripe
     */
    @PostMapping("/stripe")
    public ResponseEntity<Map<String, String>> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        
        try {
            logger.info("Webhook de Stripe recibido");
            
            // Aquí se verificaría la firma de Stripe
            // En producción: verificarSignature(payload, sigHeader)
            
            // Por ahora, asumimos que el payload viene en JSON
            return ResponseEntity.ok(Map.of("status", "received"));
            
        } catch (Exception e) {
            logger.error("Error procesando webhook de Stripe: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Webhook específico para MercadoPago
     * POST /api/webhooks/mercadopago
     */
    @PostMapping("/mercadopago")
    public ResponseEntity<Map<String, String>> handleMercadoPagoWebhook(
            @RequestBody Map<String, Object> payload) {
        
        try {
            logger.info("Webhook de MercadoPago recibido: {}", payload);
            
            // Extraer ID del pago de MercadoPago
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            if (data == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Missing data"));
            }
            
            // El orderId generalmente viene en la notificación
            // Marketplace: topic=payment, id=payment_id
            String topic = (String) payload.get("topic");
            
            if ("payment".equals(topic)) {
                String paymentId = String.valueOf(data.get("id"));
                logger.info("Procesando pago de MercadoPago: {}", paymentId);
                
                // En una implementación real, consultarías el estado del pago
                // usando el SDK de MercadoPago
                
                return ResponseEntity.ok(Map.of(
                    "status", "received",
                    "paymentId", paymentId
                ));
            }
            
            return ResponseEntity.ok(Map.of("status", "ignored"));
            
        } catch (Exception e) {
            logger.error("Error procesando webhook de MercadoPago: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Endpoint de salud para verificar el webhook
     * GET /api/webhooks/health
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "payment-webhook"
        ));
    }

    // Métodos auxiliar para extraer datos del payload

    private Long extractOrderId(Map<String, Object> payload) {
        // Intentar múltiples ubicaciones del orderId
        
        // 1. En metadata
        Map<String, Object> data = (Map<String, Object>) payload.get("data");
        if (data != null) {
            Map<String, Object> object = (Map<String, Object>) data.get("object");
            if (object != null) {
                Map<String, Object> metadata = (Map<String, Object>) object.get("metadata");
                if (metadata != null && metadata.get("orderId") != null) {
                    return Long.parseLong(String.valueOf(metadata.get("orderId")));
                }
            }
        }
        
        // 2.直接 en el payload raíz
        if (payload.get("orderId") != null) {
            return Long.parseLong(String.valueOf(payload.get("orderId")));
        }
        
        // 3. En data.id (para algunos formatos)
        if (data != null && data.get("id") != null) {
            try {
                return Long.parseLong(String.valueOf(data.get("id")));
            } catch (NumberFormatException e) {
                // No es un número, ignorar
            }
        }
        
        return null;
    }

    private String extractEventType(Map<String, Object> payload) {
        // Intentar múltiples ubicaciones del tipo de evento
        
        if (payload.get("type") != null) {
            return (String) payload.get("type");
        }
        
        if (payload.get("action") != null) {
            return (String) payload.get("action");
        }
        
        if (payload.get("eventType") != null) {
            return (String) payload.get("eventType");
        }
        
        return "unknown";
    }
}
