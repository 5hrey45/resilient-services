package northstar.cosmos.loanservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import northstar.cosmos.loanservice.dto.LoanContactInfoDto;
import northstar.cosmos.loanservice.dto.LoanDto;
import northstar.cosmos.loanservice.dto.ResponseDto;
import northstar.cosmos.loanservice.entity.Loan;
import northstar.cosmos.loanservice.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
@Tag(
        name = "REST APIs for loan microservice",
        description = "REST APIs to Create, Read, Update and Delete loan details"
)
public class LoanController {

    private LoanService loanService;

    @Value("${build.version}")
    private String buildVersion;

    private LoanContactInfoDto loanContactInfoDto;

    @Autowired
    public LoanController(LoanService loanService,
                          LoanContactInfoDto loanContactInfoDto) {
        this.loanService = loanService;
        this.loanContactInfoDto = loanContactInfoDto;
    }

    @GetMapping("/fetch")
    public ResponseEntity<LoanDto> getLoanDetails(@Valid
                                                  @NotBlank(message = "mobile number must not be empty or blank")
                                                  @Pattern(regexp = "(^$|[0-9]{10})", message = "mobile number must be 10 digits")
                                                  @RequestParam String mobileNumber) {
        LoanDto loanDto = loanService.getLoanDetails(mobileNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loanDto);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createNewLoan(@Valid @RequestBody LoanDto loanDto) {
        loanService.createLoan(loanDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.OK, "Resource created"));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateExistingLoan(@Valid @RequestBody LoanDto loanDto) {
        loanService.updateLoan(loanDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK, "Resource updated"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteExistingLoan(@Valid
                                                          @NotBlank(message = "mobile number must not be empty or blank")
                                                          @Pattern(regexp = "(^$|[0-9]{10})", message = "mobile number must be 10 digits")
                                                          @RequestParam String mobileNumber) {
        loanService.deleteLoan(mobileNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK, "Resource deleted"));
    }

    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @GetMapping("/contact-details")
    public ResponseEntity<LoanContactInfoDto> getContactDetails() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(loanContactInfoDto);
    }
}
