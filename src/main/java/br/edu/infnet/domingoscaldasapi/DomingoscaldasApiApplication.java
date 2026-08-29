package br.edu.infnet.domingoscaldasapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DomingoscaldasApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomingoscaldasApiApplication.class, args);
	}

}
