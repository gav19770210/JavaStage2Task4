package ru.gav19770210.stage2task4.file;

/**
 * <b>LogTransformationWriter</b> это интерфейс для записи в файл журнала трансформации данных файлов логов.
 */
public interface LogTransformationWriter {
    /**
     * Запись в файл строки информации о трансформации данных файла лога.
     *
     * @param s           строка для записи в лог трансформации
     * @param logFileName наименование файла логов трансформации
     */
    void logWrite(String s, String logFileName);
}
