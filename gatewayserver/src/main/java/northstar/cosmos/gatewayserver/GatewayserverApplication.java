package northstar.cosmos.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
    }

    @Bean
    public RouteLocator bankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder
                .routes()
                .route(p ->
                        p.path("/bank/accountservice/**")
                                .filters(f -> f.rewritePath("/bank/accountservice/(?<remaining>.*)", "/${remaining}")
                                        .addResponseHeader("Response-Timestamp", LocalDateTime.now().toString())
                                        .circuitBreaker(config -> config.setName("accountCircuitBreaker")
                                                .setFallbackUri("forward:/contactsupport")))
                                .uri("lb://ACCOUNTSERVICE"))
                .route(p ->
                        p.path("/bank/loanservice/**")
                                .filters(f -> f.rewritePath("/bank/loanservice/(?<remaining>.*)", "/${remaining}")
                                        .addResponseHeader("Response-Timestamp", LocalDateTime.now().toString()))
                                .uri("lb://LOANSERVICE"))
                .route(p ->
                        p.path("/bank/cardservice/**")
                                .filters(f -> f.rewritePath("/bank/cardservice/(?<remaining>.*)", "/${remaining}")
                                        .addResponseHeader("Response-Timestamp", LocalDateTime.now().toString())
                                        .retry(retryConfig -> retryConfig.setRetries(3)
                                                .setMethods(HttpMethod.GET)
                                                .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                                        .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter()).setKeyResolver(userKeyResolver())))
                                .uri("lb://CARDSERVICE"))
                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 20, 5);
    }

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
                .defaultIfEmpty("anonymous");
    }

}
