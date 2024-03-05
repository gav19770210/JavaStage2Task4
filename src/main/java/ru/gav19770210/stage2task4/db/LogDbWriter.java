package ru.gav19770210.stage2task4.db;

import ru.gav19770210.stage2task4.model.LogRow;

/**
 * <b>LogDbWriter</b> это интерфейс сохранения данных логов в БД.
 */
public interface LogDbWriter {
    /**
     * Открытие сессии БД.
     */
    void openSession();

    /**
     * Закрытие сессии БД.
     */
    void closeSession();

    /**
     * Запись данных в БД.
     *
     * @param logRow разобранная строка файла лога
     */
    void writeLogRow(LogRow logRow);
}
