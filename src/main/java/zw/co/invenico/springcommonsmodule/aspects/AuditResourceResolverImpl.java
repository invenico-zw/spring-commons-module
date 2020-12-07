package zw.co.invenico.springcommonsmodule.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Service
public class AuditResourceResolverImpl implements AuditResourceResolver {

    public String[] resolveFrom(JoinPoint target, Object retval) {
        log.info("### ret value {}", retval);
        if (retval instanceof Collection) {
            final Collection c = (Collection) retval;
            final String[] retvals = new String[c.size()];

            int i = 0;
            for (final Iterator iter = c.iterator(); iter.hasNext() && i < c.size(); i++) {
                final Object o = iter.next();

                if (o != null) {
                    retvals[i] = iter.next().toString();
                }
            }

            return retvals;
        }

        if (retval instanceof Object[]) {
            final Object[] vals = (Object[]) retval;
            final String[] retvals = new String[vals.length];
            for (int i = 0; i < vals.length; i++) {
                retvals[i] = vals[i].toString();
            }

            return retvals;
        }

        return new String[]{retval.toString()};
    }

    public String[] resolveFrom(JoinPoint target, Exception exception) {
        final String message = exception.getMessage();
        if (message != null) {
            return new String[]{message};
        }
        return new String[]{exception.toString()};
    }
}
