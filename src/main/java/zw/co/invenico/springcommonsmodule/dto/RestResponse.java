package zw.co.invenico.springcommonsmodule.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Data
@Component
public class RestResponse {

    private int statusCode;
    private String message;
    private String timestamp;
    private HttpStatus status;

    public RestResponse() {
        this.statusCode = HttpStatus.OK.value();
    }

    public RestResponse(String message, String timestamp, HttpStatus status) {
        this.statusCode = status.value();
        this.message = message;
        this.timestamp = timestamp;
        this.status = status;
    }

//    public RestResponse(String message, HttpStatus code, String description) {
//        this.message = message;
//        this.statusCode = code.value();
//        this.description = description;
//    }
}
