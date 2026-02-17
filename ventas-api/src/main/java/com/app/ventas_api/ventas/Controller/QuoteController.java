package com.app.ventas_api.ventas.Controller;

import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.ventas.domain.Quote;
import com.app.ventas_api.ventas.IService.IQuoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {

    private final IQuoteService quoteService;

    public QuoteController(IQuoteService quoteService) {
        this.quoteService = quoteService;
    }

    /**
     * Crear solicitud de cotización
     * POST /api/quotes
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN')")
    public ResponseEntity<Quote> createQuote(
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal User user) {
        
        Long companyId = Long.valueOf(request.get("companyId").toString());
        @SuppressWarnings("unchecked")
        Map<Long, Integer> products = (Map<Long, Integer>) request.get("products");
        String notes = (String) request.get("notes");

        Quote quote = quoteService.createQuote(user, companyId, products, notes);
        return ResponseEntity.ok(quote);
    }

    /**
     * Obtener cotización por ID
     * GET /api/quotes/{id}
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN', 'ADMIN')")
    public ResponseEntity<Quote> getQuote(@PathVariable Long id) {
        Quote quote = quoteService.getQuoteById(id);
        return ResponseEntity.ok(quote);
    }

    /**
     * Obtener cotizaciones de mi empresa
     * GET /api/quotes/my
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN')")
    public ResponseEntity<List<Quote>> getMyQuotes(@AuthenticationPrincipal User user) {
        Long companyId = user.getCompany().getId();
        List<Quote> quotes = quoteService.getCompanyQuotes(companyId);
        return ResponseEntity.ok(quotes);
    }

    /**
     * Obtener todas las cotizaciones (ADMIN)
     * GET /api/quotes
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Quote>> getAllQuotes() {
        List<Quote> quotes = quoteService.getAllQuotes();
        return ResponseEntity.ok(quotes);
    }

    /**
     * Obtener cotizaciones pendientes (ADMIN)
     * GET /api/quotes/pending
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Quote>> getPendingQuotes() {
        List<Quote> quotes = quoteService.getPendingQuotes();
        return ResponseEntity.ok(quotes);
    }

    /**
     * Aprobar cotización (ADMIN)
     * POST /api/quotes/{id}/approve
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Quote> approveQuote(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request,
            @AuthenticationPrincipal User admin) {
        
        @SuppressWarnings("unchecked")
        Map<Long, BigDecimal> quotedPrices = (Map<Long, BigDecimal>) request.get("quotedPrices");
        BigDecimal discount = request.get("discount") != null ? 
            new BigDecimal(request.get("discount").toString()) : BigDecimal.ZERO;
        String adminResponse = (String) request.get("adminResponse");

        Quote quote = quoteService.approveQuote(id, quotedPrices, discount, adminResponse, admin);
        return ResponseEntity.ok(quote);
    }

    /**
     * Rechazar cotización (ADMIN)
     * POST /api/quotes/{id}/reject
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Quote> rejectQuote(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal User admin) {
        
        String adminResponse = request.get("adminResponse");
        Quote quote = quoteService.rejectQuote(id, adminResponse, admin);
        return ResponseEntity.ok(quote);
    }

    /**
     * Convertir cotización a pedido
     * POST /api/quotes/{id}/convert
     */
    @PostMapping("/{id}/convert")
    @PreAuthorize("hasAnyRole('USER', 'COMPANY_ADMIN')")
    public ResponseEntity<Quote> convertToOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {
        
        Quote quote = quoteService.convertToOrder(id, user);
        return ResponseEntity.ok(quote);
    }
}
