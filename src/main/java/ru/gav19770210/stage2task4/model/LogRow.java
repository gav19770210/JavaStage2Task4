package ru.gav19770210.stage2task4.model;

import lombok.Getter;

import java.util.Map;

/**
 * <b>LogRow</b> это класс определяет структуру строки лога разобранную на элементы.
 */
public class LogRow {
    /**
     * Список значений элементов разобранной исходной строки лога.
     */
    private final Map<LogRowItem, String> logRowParts;
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

    public LogRow(String logRow, String fileName, Map<LogRowItem, String> logRowParts) {
        this.fileName = fileName;
        this.logRow = logRow;
        this.logRowParts = logRowParts;
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
