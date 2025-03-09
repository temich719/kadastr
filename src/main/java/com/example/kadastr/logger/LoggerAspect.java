package com.example.kadastr.logger;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    @Around("execution(* com.example.kadastr.service..*.*(..))")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Service layer method call {} with params {}", methodName, Arrays.toString(args));
        return joinPoint.proceed();
    }

    @Around("execution(* com.example.kadastr.controller..*.*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("Controller layer method call {} with params {}", methodName, Arrays.toString(args));
        return joinPoint.proceed();
    }

    @Around("execution(* com.example.kadastr.dao..*.*(..))")
    public Object logDaoMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        log.info("DAO layer method call {} with params {}", methodName, Arrays.toString(args));
        return joinPoint.proceed();
    }

}
