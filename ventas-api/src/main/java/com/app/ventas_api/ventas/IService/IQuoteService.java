package com.app.ventas_api.ventas.IService;

import com.app.ventas_api.ventas.domain.Quote;
import com.app.ventas_api.seguridad.domain.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IQuoteService {

    Quote createQuote(User user, Long companyId, Map<Long, Integer> products, String notes);

    Quote getQuoteById(Long quoteId);

    List<Quote> getCompanyQuotes(Long companyId);

    List<Quote> getAllQuotes();

    List<Quote> getPendingQuotes();

    Quote approveQuote(Long quoteId, Map<Long, BigDecimal> quotedPrices, BigDecimal discount, String adminResponse, User admin);

    Quote rejectQuote(Long quoteId, String adminResponse, User admin);

    Quote convertToOrder(Long quoteId, User user);
}
