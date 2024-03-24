package cl.proyecto.gatewayserver.scapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class ScApigatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScApigatewayApplication.class, args);
	}

}
