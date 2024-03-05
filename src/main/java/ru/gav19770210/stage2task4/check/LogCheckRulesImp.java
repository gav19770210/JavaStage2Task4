package ru.gav19770210.stage2task4.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowItem;

import java.util.HashMap;

/**
 * <b>LogCheckRulesImp</b> это компонент для применения правил проверки к значениям элементов разобранной строки лога.
 */
@Component
public class LogCheckRulesImp implements LogCheckRules {
    /**
     * Элементы строки лога со списком процедур по их проверке.
     */
    private final HashMap<LogRowItem, LogChecker[]> rules = new HashMap<>();
    /**
     * Список элементов строки лога.
     */
    @Autowired
    @Qualifier("logRowItems")
    private LogRowItem[] logRowItems;

    @Override
    public void addRule(LogRowItem logRowItem, LogChecker... rules) {
        this.rules.put(logRowItem, rules);
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
