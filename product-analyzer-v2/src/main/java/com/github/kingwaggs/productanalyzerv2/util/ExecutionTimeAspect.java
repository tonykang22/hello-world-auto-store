package com.github.kingwaggs.productanalyzerv2.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class ExecutionTimeAspect {

    @Around("@annotation(com.github.kingwaggs.productanalyzerv2.util.TrackExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        int sec = (int) stopWatch.getTotalTimeSeconds();
        int min = sec / 60;
        int hour = min / 60;
        sec = sec % 60;
        min = min % 60;

        MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = proceedingJoinPoint.getSignature().getName();

        log.info("Execution time of [{}::{}] - [{}:{}:{}]", className, methodName, hour, min, sec);
        return result;
    }

}
