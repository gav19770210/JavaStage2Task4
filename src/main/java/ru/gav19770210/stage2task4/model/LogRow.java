package ru.gav19770210.stage2task4.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * <b>LogRow</b> это класс определяет структуру строки лога разобранную на элементы.
 */
@Component
public class LogRow {
    /**
     * Разделитель элементов строки лога.
     */
    private static String logRowSeparator;
    /**
     * Список составляющих элементов строки лога.
     */
    private static LogRowItem[] logRowItems;
    /**
     * Список значений элементов разобранной исходной строки лога.
     */
    private final HashMap<LogRowItem, String> logRowParts = new HashMap<>();
    /**
     * Имя файла, которому принадлежит исходная строка.
     */
    @Getter
    private String fileName;
    /**
     * Исходная строка лога.
     */
    @Getter
    private String logRow;

    private LogRow() {
    }

    public LogRow(String logRow, String fileName) {
        this.fileName = fileName;
        this.logRow = logRow;
        setParts(logRow, logRowItems);
    }

    @Autowired
    @Qualifier("logRowSeparator")
    public void setLogRowSeparator(String logRowSeparator) {
        LogRow.logRowSeparator = logRowSeparator;
    }

    @Autowired
    @Qualifier("logRowItems")
    public void setLogRowItems(LogRowItem[] logRowItems) {
        LogRow.logRowItems = logRowItems;
    }

    private void setParts(String logRow, LogRowItem[] logRowItems) {
        int i = 0;
        for (String s : logRow.split(logRowSeparator)) {
            logRowParts.put(logRowItems[i++], s);
        }
    }

    public String getLogRowPart(LogRowItem logRowItem) {
        return logRowParts.get(logRowItem);
    }

    public void setLogRowPart(LogRowItem logRowItem, String logRowPart) {
        logRowParts.put(logRowItem, logRowPart);
    }

    @Override
    public String toString() {
        return "LogRow{" + "logRowParts=" + logRowParts + ", fileName='" + fileName + '\'' + ", logRow='" + logRow + '\'' + '}';
    }
}
