package ru.gav19770210.stage2task4.file;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import ru.gav19770210.stage2task4.check.LogTransformation;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.StringJoiner;

@Aspect
public class LogAspect {
    private final LogTransformationWriter logTransformationWriter;

    public LogAspect(LogTransformationWriter logTransformationWriter) {
        this.logTransformationWriter = logTransformationWriter;
    }

    @Around("@annotation(ru.gav19770210.stage2task4.check.LogTransformation)")
    public Object logTransformationAround(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime startTime = LocalDateTime.now();

        Object object = joinPoint.proceed();

        LocalDateTime finishTime = LocalDateTime.now();

        var stringJoiner = new StringJoiner(",")
                .add(startTime.toString())
                .add(finishTime.toString())
                .add(joinPoint.getTarget().getClass().getSimpleName())
                .add(joinPoint.getSignature().toShortString())
                .add(Arrays.toString(joinPoint.getArgs()))
                .add(String.valueOf(object));

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Method originalMethod = joinPoint.getTarget().getClass().getMethod(method.getName(), method.getParameterTypes());
        var logFileName = originalMethod.getAnnotation(LogTransformation.class).value();

        logTransformationWriter.logWrite(stringJoiner.toString(), logFileName);

        return object;
    }
}
