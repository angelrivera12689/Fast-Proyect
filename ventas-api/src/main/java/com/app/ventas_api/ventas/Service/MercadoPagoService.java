package com.app.ventas_api.ventas.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferencePaymentMethodsRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MercadoPagoService {

    private static final Logger logger = LoggerFactory.getLogger(MercadoPagoService.class);

    @Value("${mercadopago.access.token:TEST_ACCESS_TOKEN}")
    private String accessToken;

    @Value("${mercadopago.public.key:TEST_PUBLIC_KEY}")
    private String publicKey;

    @Value("${mercadopago.integration.mode:sandbox}")
    private String mode;

    @Value("${app.base.url:http://localhost:5173}")
    private String baseUrl;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(accessToken);
        logger.info("MercadoPago configurado en modo: {}", mode);
    }

    /**
     * Crea una preferencia de pago en MercadoPago
     */
    public Map<String, Object> createPaymentPreference(Long orderId, BigDecimal amount, String description) {
        try {
            logger.info("Creando preferencia de pago para orden: {}, monto: {}", orderId, amount);

            List<PreferenceItemRequest> items = new ArrayList<>();
            
            PreferenceItemRequest item = PreferenceItemRequest.builder()
                    .id(String.valueOf(orderId))
                    .title("Pedido #" + orderId)
                    .description(description)
                    .quantity(1)
                    .unitPrice(amount)
                    .build();
            
            items.add(item);

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(baseUrl + "/payment/success?orderId=" + orderId)
                    .pending(baseUrl + "/payment/pending?orderId=" + orderId)
                    .failure(baseUrl + "/payment/failure?orderId=" + orderId)
                    .build();

            PreferencePaymentMethodsRequest paymentMethods = PreferencePaymentMethodsRequest.builder()
                    .installments(12)
                    .build();

            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .externalReference(String.valueOf(orderId))
                    .autoReturn("approved")
                    .paymentMethods(paymentMethods)
                    .build();

            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            logger.info("Preferencia creada: {}", preference.getId());

            Map<String, Object> result = new HashMap<>();
            result.put("preferenceId", preference.getId());
            result.put("initPoint", preference.getInitPoint());
            result.put("sandboxInitPoint", preference.getSandboxInitPoint());
            
            boolean isSandbox = "sandbox".equals(mode);
            result.put("paymentUrl", isSandbox ? preference.getSandboxInitPoint() : preference.getInitPoint());
            result.put("isSandbox", isSandbox);

            return result;

        } catch (Exception e) {
            logger.error("Error creando preferencia de pago: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear preferencia de pago: " + e.getMessage());
        }
    }

    /**
     * Consulta el estado de un pago
     */
    public String getPaymentStatus(String paymentId) {
        try {
            logger.info("Consultando estado del pago: {}", paymentId);
            
            PaymentClient client = new PaymentClient();
            Payment payment = client.get(Long.parseLong(paymentId));
            
            logger.info("Estado del pago {}: {}", paymentId, payment.getStatus());
            return payment.getStatus().toString();
            
        } catch (Exception e) {
            logger.error("Error consultando pago: {}", e.getMessage());
            return "UNKNOWN";
        }
    }

    public String getPublicKey() {
        return publicKey;
    }

    public boolean isSandbox() {
        return "sandbox".equals(mode);
    }
}
