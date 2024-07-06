package northstar.cosmos.cardservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CardDto {

    @NotBlank(message = "mobile number must not be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "card number should not be empty or blank")
    private String cardNumber;

    @NotBlank(message = "card type should not be empty or blank")
    private String cardType;

    @NotNull(message = "total limit should not be empty or blank")
    @Min(value = 10000, message = "total limit should be greater than 10000")
    @Max(value = 1000000, message = "total limit should not exceed 1000000")
    private Long totalLimit;

    @NotNull(message = "amount used should not be empty or blank")
    private Long amountUsed;

    @NotNull(message = "available amount should not be empty or blank")
    @Max(value = 1000000, message = "available amount should not exceed 1000000")
    private Long availableAmount;
}
