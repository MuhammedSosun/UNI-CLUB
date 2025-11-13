package com.uniClub.logging;

import com.uniClub.enums.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    @Around("@annotation(loggableOperation)")//üzerinde @LoggableOperation olan tüm methodların çağrılarını sarar
    //bu çağrılmadan method çalışmaz ilk önce bu çağrılır
    public Object logExecution(ProceedingJoinPoint joinPoint,
                               LoggableOperation loggableOperation) throws Throwable {
        // süre ölçümünde kullanılır
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        // enumdan alınan alanımızı MDC ile satırlara otomotik ekleriz
        OperationType operation = loggableOperation.value();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        MDC.put("operation", operation.name());

        log.info("START [{}] -> Method: {}, Args: {}", operation, methodName, args);

        try {
            Object result = joinPoint.proceed();

            stopWatch.stop();
            log.info("END [{}] -> Duration: {} ms", operation, stopWatch.getTotalTimeMillis());
            return result;

        }catch (Exception e){
            stopWatch.stop();
            log.error("ERROR [{}] -> Duration: {} ms, Exception: {}", operation, stopWatch.getTotalTimeMillis(), e.getMessage());
            throw e;
        }finally {
            MDC.remove("operation");
        }
    }
}
