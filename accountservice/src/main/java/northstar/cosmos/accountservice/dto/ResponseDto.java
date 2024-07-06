package northstar.cosmos.accountservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Schema to hold response information"
)
public class ResponseDto {

    private HttpStatus statusCode;
    private String statusMessage;
}
