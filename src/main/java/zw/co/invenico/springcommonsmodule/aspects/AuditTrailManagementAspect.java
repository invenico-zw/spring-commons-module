package zw.co.invenico.springcommonsmodule.aspects;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

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

        String clientIPAddress = null;

        String serverIPAddress = null;

        String username = null;

        try {
            Object result = joinPoint.proceed();

            HttpServletRequest request =
                    ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                            .getRequest();

            clientIPAddress = request.getHeader("X-FORWARDED-FOR");

            clientIPAddress = clientIPAddress.contains(",") ? clientIPAddress.split(",")[0] : clientIPAddress;

            if (clientIPAddress == null) {
                clientIPAddress = request.getRemoteAddr();
            }
            serverIPAddress = request.getLocalAddr();

            username = getUsername();

            log.info("The method {}() ends with {}", joinPoint.getSignature().getName(), result);

            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument {} in {}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getName());

            HttpServletRequest request =
                    ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                            .getRequest();

            clientIPAddress = request.getHeader("X-FORWARDED-FOR");

            clientIPAddress = clientIPAddress.contains(",") ? clientIPAddress.split(",")[0] : clientIPAddress;

            if (clientIPAddress == null) {
                clientIPAddress = request.getRemoteAddr();
            }

            serverIPAddress = request.getLocalAddr();

            username = getUsername();
            throw e;
        } finally {
            String finalClientIPAddress = clientIPAddress;
            String finalServerIPAddress = serverIPAddress;
            String finalUsername = username;
            new Thread(() -> {
                val auditActionRequest = AuditActionRequest.builder()
                        .actionPerformed(audit.action())
                        .resource(audit.resource())
                        .clientIpAddress(finalClientIPAddress)
                        .dateTime(LocalDateTime.now())
                        .serverIpAddress(finalServerIPAddress)
                        .username(finalUsername)
                        .build();
                Object response = auditFeignClientService.create(auditActionRequest);

                log.info("### response {}", response);
            }).start();
        }
    }

    private String getUsername() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return username;
    }

}
