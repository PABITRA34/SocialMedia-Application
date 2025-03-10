package com.example.SocialMedia.aop;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.SocialMedia.services.*.*(..))")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        LOGGER.info("Executing method: {} with arguments: {}", joinPoint.getSignature(), joinPoint.getArgs());
    }

    @AfterReturning(value = "execution(* com.example.SocialMedia.services.*.*(..))", returning = "result")
    public void logAfterMethodExecution(JoinPoint joinPoint, Object result) {
        LOGGER.info("Method executed: {} with result: {}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "execution(* com.example.SocialMedia.services.*.*(..))", throwing = "exception")
    public void logExceptions(JoinPoint joinPoint, Exception exception) {
        LOGGER.error("Exception in method: {} with cause: {}", joinPoint.getSignature(), exception.getMessage());
    }
}
