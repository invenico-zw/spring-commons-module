package zw.co.invenico.springcommonsmodule.exception;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String s) {
        super(s);
    }
}
