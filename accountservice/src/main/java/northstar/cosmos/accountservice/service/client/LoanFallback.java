package northstar.cosmos.accountservice.service.client;

import northstar.cosmos.accountservice.dto.LoanDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoanFallback implements LoanFeignClient {
    @Override
    public ResponseEntity<LoanDto> getLoanDetails(String mobileNumber) {
        return null;
    }
}
