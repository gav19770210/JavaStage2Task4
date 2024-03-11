package ru.gav19770210.stage2task4.check;

/**
 * <b>LogCheckApp</b> это компонент проверки поля приложения в строке логов.
 */
public class LogCheckApp implements LogChecker {
    @Override
    @LogTransformation("LogCheckApp.log")
    public String apply(String s) {
        if (s.equals("web") || s.equals("mobile")) {
            return s;
        } else {
            return "other:" + s;
        }
    }
}
