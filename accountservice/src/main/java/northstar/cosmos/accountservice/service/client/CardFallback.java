package northstar.cosmos.accountservice.service.client;

import northstar.cosmos.accountservice.dto.CardDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardFallback implements CardFeignClient {
    @Override
    public ResponseEntity<CardDto> getCardDetails(String mobileNumber) {
        return null;
    }
}
