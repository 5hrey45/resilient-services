package northstar.cosmos.accountservice.service.client;

import northstar.cosmos.accountservice.dto.CardDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cardservice", fallback = CardFallback.class)
public interface CardFeignClient {
    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<CardDto> getCardDetails(@RequestParam String mobileNumber);
}
