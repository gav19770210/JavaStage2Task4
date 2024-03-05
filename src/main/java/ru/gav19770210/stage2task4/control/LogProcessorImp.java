package ru.gav19770210.stage2task4.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
@Component
public class LogProcessorImp implements LogProcessor {
    @Autowired
    private LogFilesReader logFilesReader;
    @Autowired
    private LogCheckRules logCheckRules;
    @Autowired
    private LogErrorWriter logErrorWriter;
    @Autowired
    private LogDbWriter logDbWriter;

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
