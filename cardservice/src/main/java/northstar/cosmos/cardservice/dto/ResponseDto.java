package northstar.cosmos.cardservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ResponseDto {

    private HttpStatus statusCode;
    private String statusMessage;
}
