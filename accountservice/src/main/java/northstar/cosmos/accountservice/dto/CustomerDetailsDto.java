package northstar.cosmos.accountservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDetailsDto {
    @NotBlank(message = "name cannot be null or empty")
    @Size(min = 1, max = 15, message = "length of the name should be between 1 and 15")
    private String name;

    @NotBlank(message = "email cannot be null or empty")
    @Email(message = "email must be valid")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "mobile number must be 10 digits")
    private String mobileNumber;

    private AccountDto accountDto;

    private LoanDto loanDto;

    private CardDto cardDto;
}
