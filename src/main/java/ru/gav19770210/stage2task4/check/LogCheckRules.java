package ru.gav19770210.stage2task4.check;

import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowItem;

/**
 * <b>LogCheckRules</b> это интерфейс для применения правил проверки к значениям элементов разобранной строки лога.
 */
public interface LogCheckRules {
    /**
     * <b>addRule</b> - метод задаёт список правил проверки для элемента строки логов.
     *
     * @param logRowItem элемент строки лога
     * @param rules      список правил проверки элемента строки лога
     */
    LogCheckRules addRule(LogRowItem logRowItem, LogChecker... rules);

    /**
     * <b>processRow</b> - метод применяет правила проверки к значениям элементов разобранной строки лога.
     *
     * @param logRow разобранная строка лога
     * @return разобранная строка лога, обработанная соответствующими правилами проверки
     */
    LogRow processRow(LogRow logRow);
}
