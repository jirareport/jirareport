package br.com.jiratorio.aspect

import br.com.jiratorio.util.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.util.StopWatch

@Aspect
@Component
class BenchMarkAspect {

    private val log = logger()

    @Around("@annotation(br.com.jiratorio.aspect.annotation.ExecutionTime)")
    fun around(proceedingJoinPoint: ProceedingJoinPoint): Any? {
        val stopWatch = StopWatch(proceedingJoinPoint.signature.declaringType.simpleName)

        try {
            stopWatch.start(proceedingJoinPoint.signature.name)
            return proceedingJoinPoint.proceed()
        } finally {
            stopWatch.stop()

            log.info(
                "Aspect=BenchMarkAspect, operation={}#{}, executionTime={}",
                stopWatch.id, stopWatch.lastTaskName, stopWatch.lastTaskTimeMillis
            )
        }
    }

}
