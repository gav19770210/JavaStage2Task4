package ru.gav19770210.stage2task4.file;

import ru.gav19770210.stage2task4.model.LogRow;

/**
 * <b>LogErrorWriter</b> это интерфейс для записи в файл ошибок информации об исключениях,
 * возникших при обработке данных файлов логов.
 */
public interface LogErrorWriter {
    /**
     * Запись в файл ошибок информации об исключении, возникшем при обработке данных файла лога.
     *
     * @param logRow разобранная строка файла лога
     * @param e      исключение, возникшее при обработке строки файла лога
     */
    void logError(LogRow logRow, Exception e);
}
