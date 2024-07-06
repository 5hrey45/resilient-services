package northstar.cosmos.loanservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoanDto {

    @NotBlank(message = "mobile number must not be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "mobile number must be 10 digits")
    private String mobileNumber;

    @NotBlank(message = "loan number should not be empty or blank")
    private String loanNumber;

    @NotBlank(message = "loan type should not be empty or blank")
    private String loanType;

    @NotNull(message = "total loan should not be empty or blank")
    @Min(value = 1, message = "total loan should be greater than 0")
    @Max(value = 10000000, message = "total loan should not exceed 10000000")
    private Long totalLoan;

    @NotNull(message = "amount paid should not be empty or blank")
    private Long amountPaid;

    @NotNull(message = "outstanding amount should not be empty or blank")
    private Long outstandingAmount;
}
