package ru.gav19770210.stage2task4.check;

import org.springframework.stereotype.Component;

/**
 * <b>LogCheckFIO</b> это компонент проверки полей ФИО в строке логов так,
 * чтобы каждый его элемент начинался с большой буквы.
 */
@LogTransformation
@Component
public class LogCheckFIO implements LogChecker {
    @Override
    public String apply(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
