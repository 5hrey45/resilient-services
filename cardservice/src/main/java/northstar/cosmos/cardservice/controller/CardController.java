package northstar.cosmos.cardservice.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import northstar.cosmos.cardservice.dto.CardContactInfoDto;
import northstar.cosmos.cardservice.dto.CardDto;
import northstar.cosmos.cardservice.dto.ResponseDto;
import northstar.cosmos.cardservice.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Validated
@Tag(
        name = "REST APIs for card microservice",
        description = "REST APIs to Create, Read, Update and Delete card details"
)
public class CardController {

    private CardService cardService;

    @Value("${build.version}")
    private String buildVersion;

    private CardContactInfoDto cardContactInfoDto;


    @Autowired
    public CardController(CardService cardService,
                          CardContactInfoDto cardContactInfoDto) {
        this.cardService = cardService;
        this.cardContactInfoDto = cardContactInfoDto;
    }

    @GetMapping("/fetch")
    public ResponseEntity<CardDto> getCardDetails(@Valid
                                                  @NotBlank(message = "mobile number must not be empty or blank")
                                                  @Pattern(regexp = "(^$|[0-9]{10})", message = "mobile number must be 10 digits")
                                                  @RequestParam String mobileNumber) {
        CardDto cardDto = cardService.getCardDetails(mobileNumber);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cardDto);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createNewCard(@Valid @RequestBody CardDto cardDto) {
        cardService.createCard(cardDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.OK, "Resource created"));
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateExistingCard(@Valid @RequestBody CardDto cardDto) {
        cardService.updateCard(cardDto);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK, "Resource updated"));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteExistingCard(@Valid
                                                          @NotBlank(message = "mobile number must not be empty or blank")
                                                          @Pattern(regexp = "(^$|[0-9]{10})", message = "mobile number must be 10 digits")
                                                          @RequestParam String mobileNumber) {
        cardService.deleteCard(mobileNumber);

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
    public ResponseEntity<CardContactInfoDto> getContactDetails() {
        System.out.println("card contact details invoked");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cardContactInfoDto);
    }
}
