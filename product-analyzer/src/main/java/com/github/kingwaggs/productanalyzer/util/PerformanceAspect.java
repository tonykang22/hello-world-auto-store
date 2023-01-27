package com.github.kingwaggs.productanalyzer.util;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {
    @Around("@annotation(com.github.kingwaggs.productanalyzer.util.PerformanceLogging)")
    public Object logPerformance(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        int sec = (int) stopWatch.getTotalTimeSeconds();
        int min = sec / 60;
        int hour = min / 60;
        sec = sec % 60;
        min = min % 60;

        log.info("Execution time of {}.{} - {}:{}:{}.", className, methodName, hour, min, sec);
        return result;
    }
}
