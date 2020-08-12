package zw.co.invenico.springcommonsmodule.exception;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }
}
