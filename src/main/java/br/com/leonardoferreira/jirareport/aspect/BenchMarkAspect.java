package br.com.leonardoferreira.jirareport.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author lferreira on 19/05/18
 */
@Slf4j
@Aspect
@Component
public class BenchMarkAspect {

    @Around("@annotation(br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime)")
    public Object around(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = 0;
        try {
            start = System.currentTimeMillis();
            return proceedingJoinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = proceedingJoinPoint.getSignature().getName();
            String fullName = className + "#" + methodName;

            log.info("A=BenchMarkAspect, operation={}, executionTime={}", fullName, (end - start) + "ms");
        }
    }

}
