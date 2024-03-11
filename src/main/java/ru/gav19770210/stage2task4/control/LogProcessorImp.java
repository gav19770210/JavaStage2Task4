package ru.gav19770210.stage2task4.control;

import ru.gav19770210.stage2task4.check.LogCheckRules;
import ru.gav19770210.stage2task4.db.LogDbWriter;
import ru.gav19770210.stage2task4.file.LogErrorWriter;
import ru.gav19770210.stage2task4.file.LogFilesReader;
import ru.gav19770210.stage2task4.model.LogRow;

/**
 * <b>LogProcessorImp</b> это компонент реализации основного алгоритма задачи.<p>
 * - чтения файлов логов из заданной папки<p>
 * - обработка разобранных данных по списку проверок<p>
 * - запись проверенных данных в БД<p>
 * - запись исключний обработки данных в файл ошибок
 */
public class LogProcessorImp implements LogProcessor {
    private final LogFilesReader logFilesReader;
    private final LogCheckRules logCheckRules;
    private final LogErrorWriter logErrorWriter;
    private final LogDbWriter logDbWriter;

    public LogProcessorImp(LogFilesReader logFilesReader,
                           LogCheckRules logCheckRules,
                           LogErrorWriter logErrorWriter,
                           LogDbWriter logDbWriter) {
        this.logFilesReader = logFilesReader;
        this.logCheckRules = logCheckRules;
        this.logErrorWriter = logErrorWriter;
        this.logDbWriter = logDbWriter;
    }

    @Override
    public void uploadLogs() {
        logDbWriter.openSession();

        LogRow logRow;
        // построчное чтение файлов логов
        while (logFilesReader.hasNext()) {
            logRow = logFilesReader.next();
            try {
                // применение к разобранной строке файла логов методов проверки
                logRow = logCheckRules.processRow(logRow);
                // сохранение разобранных данных в БД
                logDbWriter.writeLogRow(logRow);
            } catch (Exception e) {
                // при возникновении исключения выполняется запись ошибочной строки в файл ошибок
                logErrorWriter.logError(logRow, e);
            }
        }
        logDbWriter.closeSession();
    }
}
