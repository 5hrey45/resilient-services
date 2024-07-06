package northstar.cosmos.accountservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(
        name = "Account",
        description = "Schema to hold account information"
)
public class AccountDto {

    @NotBlank(message = "account number should not be null or empty")
    private Long accountNumber;

    @NotBlank(message = "account type should not be null or empty")
    private String accountType;

    @NotBlank(message = "branch address should not be null or empty")
    private String branchAddress;
}
