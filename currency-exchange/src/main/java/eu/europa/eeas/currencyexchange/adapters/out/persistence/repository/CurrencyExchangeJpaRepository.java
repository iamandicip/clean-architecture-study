package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.CurrencyExchangeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyExchangeJpaRepository extends JpaRepository<CurrencyExchangeJpaEntity, Long> {

    Optional<CurrencyExchangeJpaEntity> findByCodeFromAndCodeTo(String from, String to);
}
