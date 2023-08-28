package eu.europa.eeas.currencyexchange.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "outbox")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutboxJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "outbox_seq")
    @SequenceGenerator(name = "outbox_seq", sequenceName = "outbox_seq", allocationSize = 1)
    private Long id;

    private Long aggregateId;

    private String aggregateType;

    private Date createdAt;

    @Lob
    private String payload;

    @Enumerated(EnumType.ORDINAL)
    private EventType eventType;
}
