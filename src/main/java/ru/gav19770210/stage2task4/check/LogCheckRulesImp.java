package ru.gav19770210.stage2task4.check;

import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowItem;

import java.util.HashMap;

public class LogCheckRulesImp implements LogCheckRules {
    /**
     * Элементы строки лога со списком процедур по их проверке.
     */
    private final HashMap<LogRowItem, LogChecker[]> rules = new HashMap<>();
    /**
     * Список элементов строки лога.
     */
    private final LogRowItem[] logRowItems;

    public LogCheckRulesImp(LogRowItem[] logRowItems) {
        this.logRowItems = logRowItems;
    }

    @Override
    public LogCheckRules addRule(LogRowItem logRowItem, LogChecker... rules) {
        this.rules.put(logRowItem, rules);
        return this;
    }

    @Override
    public LogRow processRow(LogRow logRow) {
        for (var logRowItem : logRowItems) {
            String logRowPart = logRow.getLogRowPart(logRowItem);
            if (rules.containsKey(logRowItem)) {
                for (var logChecker : rules.get(logRowItem)) {
                    logRowPart = logChecker.apply(logRowPart);
                }
                logRow.setLogRowPart(logRowItem, logRowPart);
            }
        }
        return logRow;
    }
}
