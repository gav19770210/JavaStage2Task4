package ru.gav19770210.stage2task4.check;

import org.springframework.stereotype.Component;

/**
 * <b>LogCheckApp</b> это компонент проверки поля приложения в строке логов.
 */
@LogTransformation("LogCheckApp.log")
@Component
public class LogCheckApp implements LogChecker {
    @Override
    public String apply(String s) {
        if (s.equals("web") || s.equals("mobile")) {
            return s;
        } else {
            return "other:" + s;
        }
    }
}
