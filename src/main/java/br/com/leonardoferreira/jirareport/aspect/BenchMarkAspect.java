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
        Exception e = null;
        Object ret = null;
        long end;

        long start = System.currentTimeMillis();
        try {
            ret = proceedingJoinPoint.proceed();
            end = System.currentTimeMillis();
        } catch (Exception ex) {
            e = ex;
            end = System.currentTimeMillis();
        }

        String className = proceedingJoinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        String fullName = className + "#" + methodName;

        log.info("A=BenchMarkAspect, operation={}, executionTime={}", fullName, (end - start) + "ms");

        if (e != null) {
            throw e;
        }
        return ret;
    }

}
