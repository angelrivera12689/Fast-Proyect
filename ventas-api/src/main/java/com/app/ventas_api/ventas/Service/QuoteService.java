package com.app.ventas_api.ventas.Service;

import com.app.ventas_api.Productos.Entity.Product;
import com.app.ventas_api.Productos.IRepository.IProductRepository;
import com.app.ventas_api.Organizacion.Entity.Company;
import com.app.ventas_api.Organizacion.IRepository.ICompanyRepository;
import com.app.ventas_api.seguridad.domain.User;
import com.app.ventas_api.ventas.IRepository.IQuoteRepository;
import com.app.ventas_api.ventas.IService.IQuoteService;
import com.app.ventas_api.ventas.domain.Quote;
import com.app.ventas_api.ventas.domain.QuoteItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map;

@Service
public class QuoteService implements IQuoteService {

    private static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

    private final IQuoteRepository quoteRepository;
    private final ICompanyRepository companyRepository;
    private final IProductRepository productRepository;

    public QuoteService(IQuoteRepository quoteRepository, ICompanyRepository companyRepository, 
                        IProductRepository productRepository) {
        this.quoteRepository = quoteRepository;
        this.companyRepository = companyRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Quote createQuote(User user, Long companyId, Map<Long, Integer> products, String notes) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        Quote quote = Quote.builder()
                .company(company)
                .requestedBy(user)
                .status(Quote.QuoteStatus.PENDING)
                .notes(notes)
                .validUntil(LocalDateTime.now().plusDays(7))  // 7 días de validez
                .build();

        quote = quoteRepository.save(quote);

        // Crear items de la cotización
        BigDecimal subtotal = BigDecimal.ZERO;
        List<QuoteItem> items = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : products.entrySet()) {
            Product product = productRepository.findById(entry.getKey())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + entry.getKey()));

            QuoteItem item = QuoteItem.builder()
                    .quote(quote)
                    .product(product)
                    .productName(product.getName())
                    .productSku(product.getSku())
                    .quantity(entry.getValue())
                    .unitPrice(product.getBasePrice())
                    .quotedPrice(product.getBasePrice())  // Precio inicial = precio base
                    .subtotal(product.getBasePrice().multiply(BigDecimal.valueOf(entry.getValue())))
                    .build();

            items.add(item);
            subtotal = subtotal.add(item.getSubtotal());
        }

        quote.setItems(items);
        quote.setSubtotal(subtotal);
        quote.setTotal(subtotal);

        return quoteRepository.save(quote);
    }

    @Override
    public Quote getQuoteById(Long quoteId) {
        return quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
    }

    @Override
    public List<Quote> getCompanyQuotes(Long companyId) {
        return quoteRepository.findByCompanyId(companyId);
    }

    @Override
    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    @Override
    public List<Quote> getPendingQuotes() {
        return quoteRepository.findByStatus(Quote.QuoteStatus.PENDING);
    }

    @Override
    @Transactional
    public Quote approveQuote(Long quoteId, Map<Long, BigDecimal> quotedPrices, BigDecimal discount, 
                             String adminResponse, User admin) {
        Quote quote = getQuoteById(quoteId);

        if (quote.getStatus() != Quote.QuoteStatus.PENDING) {
            throw new RuntimeException("La cotización ya no está pendiente");
        }

        // Actualizar precios cotizados
        BigDecimal newSubtotal = BigDecimal.ZERO;
        for (QuoteItem item : quote.getItems()) {
            Long productId = item.getProduct().getId();
            if (quotedPrices.containsKey(productId)) {
                item.setQuotedPrice(quotedPrices.get(productId));
                item.setSubtotal(quotedPrices.get(productId).multiply(BigDecimal.valueOf(item.getQuantity())));
            }
            newSubtotal = newSubtotal.add(item.getSubtotal());
        }

        // Aplicar descuento
        quote.setSubtotal(newSubtotal);
        quote.setDiscount(discount != null ? discount : BigDecimal.ZERO);
        quote.setTotal(newSubtotal.subtract(quote.getDiscount()));

        quote.setStatus(Quote.QuoteStatus.APPROVED);
        quote.setAdminResponse(adminResponse);
        quote.setReviewedBy(admin);
        quote.setReviewedAt(LocalDateTime.now());

        logger.info("Cotización {} aprobada por admin {}", quote.getQuoteNumber(), admin.getUsername());

        return quoteRepository.save(quote);
    }

    @Override
    @Transactional
    public Quote rejectQuote(Long quoteId, String adminResponse, User admin) {
        Quote quote = getQuoteById(quoteId);

        if (quote.getStatus() != Quote.QuoteStatus.PENDING) {
            throw new RuntimeException("La cotización ya no está pendiente");
        }

        quote.setStatus(Quote.QuoteStatus.REJECTED);
        quote.setAdminResponse(adminResponse);
        quote.setReviewedBy(admin);
        quote.setReviewedAt(LocalDateTime.now());

        logger.info("Cotización {} rechazada por admin {}", quote.getQuoteNumber(), admin.getUsername());

        return quoteRepository.save(quote);
    }

    @Override
    @Transactional
    public Quote convertToOrder(Long quoteId, User user) {
        Quote quote = getQuoteById(quoteId);

        if (quote.getStatus() != Quote.QuoteStatus.APPROVED) {
            throw new RuntimeException("Solo se pueden convertir cotizaciones aprobadas");
        }

        // Aquí crearías un Order a partir de la cotización
        // Por ahora, marcamos como convertida
        quote.setStatus(Quote.QuoteStatus.CONVERTED);

        logger.info("Cotización {} convertida a pedido", quote.getQuoteNumber());

        return quoteRepository.save(quote);
    }
}
