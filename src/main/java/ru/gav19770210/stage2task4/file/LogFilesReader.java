package ru.gav19770210.stage2task4.file;

import ru.gav19770210.stage2task4.model.LogRow;

import java.util.Iterator;

/**
 * <b>LogFilesReader</b> это интерфейс-итератор по данным файлов логов.
 */
public interface LogFilesReader extends Iterator<LogRow> {
}
