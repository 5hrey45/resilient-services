package northstar.cosmos.gatewayserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

    @GetMapping("/contactsupport")
    public Mono<String> contactSupport() {
        return Mono.just("An error occurred, please try again after some time or contact support!");
    }
}
