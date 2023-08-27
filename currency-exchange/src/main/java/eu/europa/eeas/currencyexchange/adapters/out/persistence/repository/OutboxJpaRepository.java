package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.OutboxJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxJpaRepository extends JpaRepository<OutboxJpaEntity, Long> {
    @Query("select event from OutboxJpaEntity event order by createdAt")
    List<OutboxJpaEntity> findAllOrderByCreatedAt();
}
