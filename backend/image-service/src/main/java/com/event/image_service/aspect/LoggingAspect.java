package com.event.image_service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.event.image_service.controller..*(..)) || execution(* com.event.image_service.service..*(..))")
    public void controllerAndServiceMethods() {}

    @Before("controllerAndServiceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Entering method: {}.{}",
                signature.getDeclaringType().getSimpleName(),
                signature.getName());
    }

    @AfterReturning(pointcut = "controllerAndServiceMethods()")
    public void logAfterReturning(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Exiting method: {}.{}",
                signature.getDeclaringType().getSimpleName(),
                signature.getName());
    }

    @AfterThrowing(pointcut = "controllerAndServiceMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.error("Exception in method: {}.{} with cause: {}",
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                exception.getMessage(), exception);
    }

    @After("controllerAndServiceMethods()")
    public void logAfter(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Method {}.{} execution completed",
                signature.getDeclaringType().getSimpleName(),
                signature.getName());
    }

    @Around("controllerAndServiceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Method {}.{} executed in {} ms",
                signature.getDeclaringType().getSimpleName(),
                signature.getName(),
                duration);
        return result;
    }
}
