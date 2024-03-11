package ru.gav19770210.stage2task4.check;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <b>LogTransformation</b> это аннотация для компонентов, реализующих метод проверки данных лога.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogTransformation {
    String value() default "";
}
