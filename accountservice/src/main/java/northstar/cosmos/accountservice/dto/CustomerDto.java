package northstar.cosmos.accountservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(
        name = "Customer",
        description = "Schema to hold customer and account information"
)
public class CustomerDto {

    @NotBlank(message = "name cannot be null or empty")
    @Size(min = 1, max = 15, message = "length of the name should be between 1 and 15")
    private String name;

    @NotBlank(message = "email cannot be null or empty")
    @Email(message = "email must be valid")
    private String email;

    @Pattern(regexp = "(^$|[0-9]{10})", message = "mobile number must be 10 digits")
    private String mobileNumber;

    private AccountDto accountDto;
}
