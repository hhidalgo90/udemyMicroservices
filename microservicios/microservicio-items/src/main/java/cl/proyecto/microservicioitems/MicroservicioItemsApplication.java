package cl.proyecto.microservicioitems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EntityScan({"cl.proyecto.commons.models.entity"})//Para que encuentre clase alojada en otro proyecto diferente al package base
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})//excluye la configuracion de conexion a la bd ya que en este proyecto no se necesita.
public class MicroservicioItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioItemsApplication.class, args);
	}

}
