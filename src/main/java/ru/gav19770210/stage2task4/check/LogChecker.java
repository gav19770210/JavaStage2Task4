package ru.gav19770210.stage2task4.check;

import java.util.function.Function;

/**
 * <b>LogChecker</b> это интерфейс проверки полей в строке логов.
 */
public interface LogChecker extends Function<String, String> {
}
