package br.com.leonardoferreira.jirareport.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class BenchMarkAspect {

    @Around("@annotation(br.com.leonardoferreira.jirareport.aspect.annotation.ExecutionTime)")
    public Object around(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return proceedingJoinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
            String methodName = proceedingJoinPoint.getSignature().getName();

            log.info("A=BenchMarkAspect, operation={}#{}, executionTime={}, unit=ms", className, methodName, end - start);
        }
    }

}
