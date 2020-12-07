package zw.co.invenico.springcommonsmodule.aspects;

import org.aspectj.lang.JoinPoint;

public interface AuditResourceResolver {

    String[] resolveFrom(JoinPoint target, Object returnValue);

    String[] resolveFrom(JoinPoint target, Exception exception);
}
