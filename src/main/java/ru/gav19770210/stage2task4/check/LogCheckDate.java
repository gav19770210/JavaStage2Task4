package ru.gav19770210.stage2task4.check;

import org.springframework.stereotype.Component;

/**
 * <b>LogCheckDate</b> это компонент проверки поля даты в строке логов.
 */
@Component
public class LogCheckDate implements LogChecker {
    @Override
    public String apply(String s) {
        if ((s == null) || s.isBlank()) {
            throw new IllegalArgumentException("Дата не может быть пустой!");
        }
        return s;
    }
}
