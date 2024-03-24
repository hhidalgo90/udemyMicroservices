package cl.proyecto.microservicioitems.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4JConfig {

    /**
     * Default configuration for Resilience4J para implementar Circuit breaker.
     * @return
     */
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()) //timeout, si llamada demora mas de 4 seg da timeout
                //.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults()) configuracion por defecto
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                        .slidingWindowSize(10) //cuantos request se deben enviar para evaluarlos
                        .failureRateThreshold(50) //tasa de fallos de los request
                        .waitDurationInOpenState(Duration.ofSeconds(10L)) //tiempo que espera en modo abierto
                        .permittedNumberOfCallsInHalfOpenState(5)// nro de intentos permitidos en estado semi abierto
                        .slowCallRateThreshold(50)//porcentaje de umbral de llamadas lentas, si se cumple umbral se activa circuit breaker
                        .slowCallDurationThreshold(Duration.ofSeconds(2L))// el tiempo maximo que debiese durar una llamada, lo primero que ocurre es el timeout antes que las llamadas lentas
                        .build())
                .build());
    }
}
