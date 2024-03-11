package ru.gav19770210.stage2task4.model;

import java.util.HashMap;
import java.util.Map;

public class LogRowParserImp implements LogRowParser {
    /**
     * Разделитель элементов строки лога.
     */
    private final String logRowSeparator;
    /**
     * Список составляющих элементов строки лога.
     */
    private final LogRowItem[] logRowItems;

    public LogRowParserImp(String logRowSeparator, LogRowItem[] logRowItems) {
        this.logRowSeparator = logRowSeparator;
        this.logRowItems = logRowItems;
    }

    @Override
    public Map<LogRowItem, String> parseLogRow(String logRow) {
        Map<LogRowItem, String> logRowParts = new HashMap<>();
        int i = 0;
        for (String s : logRow.split(logRowSeparator)) {
            logRowParts.put(logRowItems[i++], s);
        }
        return logRowParts;
    }
}
