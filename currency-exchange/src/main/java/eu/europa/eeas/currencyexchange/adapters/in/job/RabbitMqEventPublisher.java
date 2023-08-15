package eu.europa.eeas.currencyexchange.adapters.in.job;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.entity.OutboxJpaEntity;
import eu.europa.eeas.currencyexchange.adapters.out.persistence.repository.OutboxJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
public class RabbitMqEventPublisher {

    private final OutboxJpaRepository repository;
    private final RabbitMqConfiguration config;

    private final Connection connection;

    public RabbitMqEventPublisher(OutboxJpaRepository repository, RabbitMqConfiguration config)
            throws IOException, TimeoutException {
        this.repository = repository;
        this.config = config;
        this.connection = createConnection();
    }

    private Connection createConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(config.getUser());
        factory.setPassword(config.getPassword());
        factory.setHost(config.getHost());
        factory.setPort(Integer.parseInt(config.getPort()));

        return factory.newConnection();
    }

    @Scheduled(fixedDelay = 1000 * 60)
    public void pollOutbox() throws IOException, TimeoutException {
        log.debug("Starting publishing scheduled job");
        try (Channel channel = connection.createChannel()) {
            configureChannel(channel);
            List<OutboxJpaEntity> events = repository.findAllOrderByCreatedAt();
            for (OutboxJpaEntity event : events) {
                publishEvent(event, channel);
                repository.delete(event);
            }
        } catch (Exception e) {
            log.error("Unexpected error publishing events", e);
        }
    }

    private void configureChannel(Channel channel) throws IOException {
        String exchangeName = config.getExchangeName();
        String queueName = config.getQueueName();
        String routingKey = config.getRoutingKey();
        channel.exchangeDeclare(exchangeName, "direct", true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
    }

    private void publishEvent(OutboxJpaEntity event, Channel channel) throws IOException {
        log.debug("Publishing event {}", event.getEventData());
        String exchangeName = config.getExchangeName();
        String routingKey = config.getRoutingKey();
        byte[] messageBodyBytes = event.getEventData().getBytes();
        AMQP.BasicProperties messageProperties = new AMQP.BasicProperties();
        channel.basicPublish(exchangeName, routingKey, messageProperties, messageBodyBytes);
    }
}
