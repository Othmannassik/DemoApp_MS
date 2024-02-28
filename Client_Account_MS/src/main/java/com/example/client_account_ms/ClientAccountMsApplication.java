package com.example.client_account_ms;

import com.example.client_account_ms.entities.Client;
import com.example.client_account_ms.repositories.ClientRepository;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.stream.Stream;

@SpringBootApplication
@EnableJpaAuditing
@EnableFeignClients
public class ClientAccountMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClientAccountMsApplication.class, args);
	}

}
