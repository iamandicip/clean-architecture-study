package eu.europa.eeas.currencyexchange.adapters.in.job.rabbitmq;

import com.rabbitmq.client.*;
import eu.europa.eeas.currencyexchange.adapters.in.job.EventPublisher;
import eu.europa.eeas.currencyexchange.common.MessageBrokerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

@Slf4j
@MessageBrokerAdapter
public class RabbitMqPublisher implements EventPublisher {

    private Connection connection;
    private Channel channel;

    private final RabbitMqConfiguration config;

    public RabbitMqPublisher(RabbitMqConfiguration config) {
        this.config = config;
    }

    @Override
    public void publishMessage(String payload, String eventType) throws IOException, TimeoutException {
        byte[] messageBodyBytes = payload.getBytes();
        AMQP.BasicProperties messageProperties = new AMQP.BasicProperties().builder()
                .contentType("application/json")
                .timestamp(new Date())
                .type(eventType)
                .build();
        String exchangeName = config.getExchangeName();
        String routingKey = config.getRoutingKey();

        Channel ch = this.getChannel();

        log.debug("Publishing message to RabbitMq exchange {}", exchangeName);
        ch.basicPublish(exchangeName, routingKey, messageProperties, messageBodyBytes);
    }

    public Channel getChannel() throws IOException, TimeoutException {
        /*
         * https://www.rabbitmq.com/api-guide.html#connecting
         *
         * Client connections are meant to be long-lived. The underlying protocol is designed and optimized for
         * long-running connections. That means that opening a new connection per operation, e.g. a message published,
         * is unnecessary and strongly discouraged as it will introduce a lot of network roundtrips and overhead.
         */
        Connection conn = this.getConnection();
        if (this.channel == null || !this.channel.isOpen()) {

            /*
             * Channels are also meant to be long-lived but since many recoverable protocol errors will result in
             * channel closure, channel lifespan could be shorter than that of its connection.
             * Closing and opening new channels per operation is usually unnecessary but can be appropriate.
             * When in doubt, consider reusing channels first.
             */
            log.debug("Creating channel to RabbitMq instance");
            Channel ch = conn.createChannel();
            configureChannel(ch);
            this.channel = ch;
        }

        return this.channel;
    }

    private Connection getConnection() throws IOException, TimeoutException {
        if (this.connection == null || !this.connection.isOpen()) {
            log.debug("Creating connection to RabbitMq instance");
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUsername(config.getUser());
            factory.setPassword(config.getPassword());
            factory.setHost(config.getHost());
            factory.setPort(Integer.parseInt(config.getPort()));
            this.connection = factory.newConnection();
        }

        return this.connection;
    }

    private void configureChannel(Channel channel) throws IOException {
        log.debug("Configuring channel");
        String exchangeName = config.getExchangeName();
        String queueName = config.getQueueName();
        String routingKey = config.getRoutingKey();
        channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true);
        channel.queueDeclare(queueName, true, false, false, null);
        channel.queueBind(queueName, exchangeName, routingKey);
    }
}
