package com.example.client_account_ms.configurations;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfiguration {
    private final AmqpAdmin rabbitAdmin;
    @Value("${rabbitmq.exchange.name}")
    private String exchangeName;
    private final boolean durable = true;
    private final boolean autoDelete = false;

    @Autowired
    public RabbitMQConfiguration(AmqpAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }
    @PostConstruct
    public void declareExchange() {
        DirectExchange exchange = new DirectExchange(exchangeName, durable, autoDelete);
        rabbitAdmin.declareExchange(exchange);
    }
}