package northstar.cosmos.accountservice.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import northstar.cosmos.accountservice.dto.AccountContactInfoDto;
import northstar.cosmos.accountservice.dto.CustomerDto;
import northstar.cosmos.accountservice.dto.ResponseDto;
import northstar.cosmos.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
@Tag(
        name = "REST APIs for account microservice",
        description = "REST APIs to Create, Read, Update and Delete account details"
)
public class AccountController {

    private AccountService accountService;

    @Value("${build.version}")
    private String buildVersion;

    private Environment environment;
    private AccountContactInfoDto accountContactInfoDto;

    @Autowired
    public AccountController(AccountService accountService,
                             Environment environment, AccountContactInfoDto accountContactInfoDto) {
        this.accountService = accountService;
        this.environment = environment;
        this.accountContactInfoDto = accountContactInfoDto;
    }

    @Operation(
            summary = "Fetch account REST API",
            description = "REST API to fetch Customer and Account details from account microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND"
            )
    })
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDto> fetchAccountDetails(@RequestParam
                                                           @NotBlank(message = "email should not be null or empty")
                                                           @Email(message = "email must be valid")
                                                           String email) {
        CustomerDto customerDto = accountService.fetchAccountDetails(email);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(customerDto);
    }

    @Operation(
            summary = "Create account REST API",
            description = "REST API to create new Customer and Account inside account microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "BAD REQUEST"
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@Valid @RequestBody CustomerDto customerDto) {
        accountService.createAccount(customerDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.CREATED, "Resource created"));
    }

    @Operation(
            summary = "Update account REST API",
            description = "REST API to update an existing Customer and Account details inside account microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND"
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateAccount(@Valid @RequestBody CustomerDto customerDto) {
        accountService.updateAccount(customerDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK, "Resource updated"));
    }

    @Operation(
            summary = "Delete account REST API",
            description = "REST API to delete an existing Customer and Account from account microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "NOT FOUND"
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteAccount(@RequestParam
                                                     @NotBlank(message = "email should not be null or empty")
                                                     @Email(message = "email must be valid")
                                                     String email) {
        accountService.deleteAccount(email);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK, "Resource deleted"));
    }

    @Operation(
            summary = "Get build information",
            description = "Get build information of account microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL SERVER ERROR"
            )
    })
    @Retry(name = "getBuildInfo", fallbackMethod = "getBuildInfoFallback")
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        System.out.println("getBuildInfo invoked");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    public ResponseEntity<String> getBuildInfoFallback(Throwable throwable) {
        System.out.println("getBuildInfoFallback invoked");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong, please try again later.");
    }

    @Operation(
            summary = "Get java version information",
            description = "Get java version information of account microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "INTERNAL SERVER ERROR"
            )
    })
    @RateLimiter(name = "getJavaVersion", fallbackMethod = "getJavaVersionFallback")
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    public ResponseEntity<String> getJavaVersionFallback(Throwable throwable) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body("Please do not spam and try again later.");
    }

    @GetMapping("/contact-details")
    public ResponseEntity<AccountContactInfoDto> getContactDetails() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountContactInfoDto);
    }
}
