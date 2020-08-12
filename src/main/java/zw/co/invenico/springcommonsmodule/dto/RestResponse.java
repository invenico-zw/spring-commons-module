package zw.co.invenico.springcommonsmodule.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Data
@Component
public class RestResponse {

    private int statusCode;
    private String description;
    private Number numberResponse;
    private String message;
    private Object model;
    private Iterable models;
    private Page pageable;

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
