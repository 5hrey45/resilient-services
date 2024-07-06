package northstar.cosmos.accountservice.service.client;

import northstar.cosmos.accountservice.dto.LoanDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loanservice", fallback = LoanFallback.class)
public interface LoanFeignClient {
    @GetMapping(value = "/api/fetch", consumes = "application/json")
    public ResponseEntity<LoanDto> getLoanDetails(@RequestParam String mobileNumber);
}
