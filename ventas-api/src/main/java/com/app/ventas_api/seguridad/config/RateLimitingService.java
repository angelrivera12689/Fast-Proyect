package com.app.ventas_api.seguridad.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate Limiting Service - Previene ataques de fuerza bruta
 * Limita el número de requests por IP/usuario
 */
@Component
public class RateLimitingService {

    // Mapa de buckets por IP de cliente
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    // Límites de configuración
    private static final int REQUESTS_PER_MINUTE = 10; // 10 requests por minuto para login
    private static final int REQUESTS_PER_HOUR = 100; // 100 requests por hora general

    /**
     * Obtiene o crea un bucket para una IP específica
     */
    public Bucket resolveBucket(String key) {
        return buckets.computeIfAbsent(key, this::createBucket);
    }

    /**
     * Crea un bucket con límites configurados
     */
    private Bucket createBucket(String key) {
        // Límite: 10 requests por minuto, con refill gradual
        Bandwidth limit = Bandwidth.classic(REQUESTS_PER_MINUTE, 
            Refill.greedy(REQUESTS_PER_MINUTE, Duration.ofMinutes(1)));
        
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    /**
     * Intenta consumir un request del bucket
     * @param key Identificador (IP o userId)
     * @return true si puede proceder, false si excedió el límite
     */
    public boolean tryConsume(String key) {
        Bucket bucket = resolveBucket(key);
        return bucket.tryConsume(1);
    }

    /**
     * Obtiene el número de requests restantes
     */
    public long getAvailableTokens(String key) {
        Bucket bucket = resolveBucket(key);
        return bucket.getAvailableTokens();
    }

    /**
     * Limpia buckets vencidos (para memoria)
     */
    public void cleanOldBuckets() {
        // En una implementación real, you'd cleanup old entries periodically
        if (buckets.size() > 10000) {
            buckets.clear();
        }
    }
}
