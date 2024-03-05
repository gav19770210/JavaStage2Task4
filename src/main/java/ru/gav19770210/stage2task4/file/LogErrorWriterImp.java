package ru.gav19770210.stage2task4.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.gav19770210.stage2task4.model.LogRow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * <b>LogErrorWriterImp</b> это класс для записи в файл ошибок информации об исключениях,
 * возникших при обработке данных файлов логов.
 */
@Component
public class LogErrorWriterImp implements LogErrorWriter {
    @Autowired
    @Qualifier("dirLogError")
    private String logPath;

    @Override
    public void logError(LogRow logRow, Exception e) {
        try {
            var outFileName = logPath + logRow.getFileName() + ".err";
            var outLine = logRow.getLogRow() + ": " + e.getMessage() + "\r\n";
            Files.write(Path.of(outFileName), outLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
