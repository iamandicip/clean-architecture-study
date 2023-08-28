package eu.europa.eeas.currencyexchange.adapters.out.persistence.repository;

import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.OutboxJpaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxJpaRepository extends JpaRepository<OutboxJpaEntity, Long> {

    // we are using a pessimistic lock in order to prevent other instances of this service to process the row
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select event from OutboxJpaEntity event order by createdAt")
    List<OutboxJpaEntity> findAllOrderByCreatedAt();
}
