package com.app.ventas_api.ventas.IRepository;

import com.app.ventas_api.ventas.domain.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IQuoteRepository extends JpaRepository<Quote, Long> {

    List<Quote> findByCompanyId(Long companyId);

    List<Quote> findByStatus(Quote.QuoteStatus status);

    List<Quote> findByCompanyIdAndStatus(Long companyId, Quote.QuoteStatus status);

    Optional<Quote> findByQuoteNumber(String quoteNumber);

    long countByStatus(Quote.QuoteStatus status);
}
