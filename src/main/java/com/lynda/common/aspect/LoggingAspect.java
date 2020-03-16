package com.lynda.common.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
@Aspect
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(Loggable)")
    public void executeLogging(){
    }

    @Before("executeLogging()")
    public void logMethodCall (JoinPoint joinPoint){
        StringBuilder message = new StringBuilder(("Method: "));
        message.append(joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        if(null != args && args.length>0){
            message.append("args[");
            Arrays.asList(args).forEach(arg->{
                message.append("arg=").append(arg).append("|");
            });
        }
        LOGGER.info("BEFORE - "  + message.toString());

    }


    @AfterReturning(pointcut = "executeLogging()", returning = "returnValue")
    public void logMethodCallAfter (JoinPoint joinPoint, Object returnValue){
        StringBuilder message = new StringBuilder(("Method: "));
        message.append(joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        if(null != args && args.length>0){
            message.append("args[");
            Arrays.asList(args).forEach(arg->{
                message.append("arg=").append(arg).append("|");
            });
        }
        if(returnValue instanceof Collection){
            message.append("| returning ").append(((Collection) returnValue).size()).append("instance(s)");
        }else{
            message.append("| returning ").append(returnValue.toString());
        }
        LOGGER.info("AFTER - "  + message.toString());

    }

    @Around("executeLogging()")
    public Object logMethodCallAround (ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object returnValue = proceedingJoinPoint.proceed();
        long totalTime = System.currentTimeMillis()-startTime;
        StringBuilder message = new StringBuilder(("Method: "));
        message.append(proceedingJoinPoint.getSignature().getName());
        message.append("Total Time : ").append(totalTime).append("ms ");
        Object[] args = proceedingJoinPoint.getArgs();
        if(null != args && args.length>0){
            message.append("args[");
            Arrays.asList(args).forEach(arg->{
                message.append("arg=").append(arg).append("|");
            });
        }
        if(returnValue instanceof Collection){
            message.append("| returning ").append(((Collection) returnValue).size()).append("instance(s)");
        }else{
            message.append("| returning ").append(returnValue.toString());
        }
        LOGGER.info("AROUND - "  + message.toString());

        return returnValue;
    }
}

