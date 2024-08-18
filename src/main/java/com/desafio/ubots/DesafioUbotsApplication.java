package com.desafio.ubots;

import com.desafio.ubots.component.RequestProcessorScheduler;
import com.desafio.ubots.config.AppConfig;
import com.desafio.ubots.config.RedisConfig;
import com.desafio.ubots.controller.RequestController;
import com.desafio.ubots.service.ConsumerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {
		RequestController.class,
		AppConfig.class,
		RedisConfig.class,
		ConsumerService.class,
		RequestProcessorScheduler.class
})
@SpringBootApplication
public class DesafioUbotsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioUbotsApplication.class, args);
	}

}
