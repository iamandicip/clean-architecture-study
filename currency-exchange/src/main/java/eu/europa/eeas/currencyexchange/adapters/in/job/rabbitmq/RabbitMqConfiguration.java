package eu.europa.eeas.currencyexchange.adapters.in.job.rabbitmq;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class RabbitMqConfiguration {

    @Value("${rabbitmq.user}")
    private String user;

    @Value("${rabbitmq.password}")
    private String password;

    @Value("${rabbitmq.host}")
    private String host;

    @Value("${rabbitmq.port}")
    private String port;

    @Value("${rabbitmq.exchangeName}")
    private String exchangeName;

    @Value("${rabbitmq.queueName}")
    private String queueName;

    @Value("${rabbitmq.routingKey}")
    private String routingKey;
}
