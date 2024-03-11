package ru.gav19770210.stage2task4.model;

import java.util.Map;

/**
 * Парсер строки на составляющие элементы
 */
public interface LogRowParser {
    /**
     * Метод разбора строки на составляющие элементы
     */
    Map<LogRowItem, String> parseLogRow(String logRow);
}
