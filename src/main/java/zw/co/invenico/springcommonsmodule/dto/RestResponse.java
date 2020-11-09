package zw.co.invenico.springcommonsmodule.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Data
@Component
public class RestResponse {

    private int statusCode;
    private String description;
    private String message;
    private Object model;

    public RestResponse() {
        this.statusCode = HttpStatus.OK.value();
        this.description = "Successful";
    }

    public RestResponse(String message, HttpStatus code, String description) {
        this.message = message;
        this.statusCode = code.value();
        this.description = description;
    }
}
