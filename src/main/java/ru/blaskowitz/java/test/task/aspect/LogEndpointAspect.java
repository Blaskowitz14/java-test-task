package ru.blaskowitz.java.test.task.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogEndpointAspect {
    @Before("execution(* ru.blaskowitz.java.test.task.controller..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering method: {} with arguments: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* ru.blaskowitz.java.test.task.controller..*(..))", returning = "returnValue")
    public void logAfterReturning(JoinPoint joinPoint, Object returnValue) {
        log.info("Exiting method: {} with result: {}", joinPoint.getSignature().toShortString(), returnValue);
    }

    @AfterThrowing(pointcut = "execution(* ru.blaskowitz.java.test.task.controller..*(..))", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("Exception in method: {} with cause: {}", joinPoint.getSignature().toShortString(), error.getMessage());
    }
}
