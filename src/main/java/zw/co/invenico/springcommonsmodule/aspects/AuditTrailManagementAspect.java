package zw.co.invenico.springcommonsmodule.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class AuditTrailManagementAspect {

    private final AuditFeignClientService auditFeignClientService;

    public AuditTrailManagementAspect(AuditFeignClientService auditFeignClientService) {
        this.auditFeignClientService = auditFeignClientService;
    }

    @Around(value = "@annotation(audit)", argNames = "audit")
    public Object handleAuditTrail(final ProceedingJoinPoint joinPoint, final Audit audit) throws Throwable {

        log.info("## action: {} resource: {}", audit.action(), audit.resource());

        try {
            Object result = joinPoint.proceed();

            log.info("The method {}() ends with {}", joinPoint.getSignature().getName(), result);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument {} in {}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getName());

            throw e;
        } finally {
            new Thread(() -> {

                Object response = auditFeignClientService.create(new AuditActionRequest(audit.resource(), audit.action()));

                log.info("### response {}", response);
            }).start();
        }
    }

}
