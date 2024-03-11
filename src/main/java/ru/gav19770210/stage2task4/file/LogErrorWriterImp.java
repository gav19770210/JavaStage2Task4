package ru.gav19770210.stage2task4.file;

import ru.gav19770210.stage2task4.model.LogRow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class LogErrorWriterImp implements LogErrorWriter {
    private final String logDir;

    public LogErrorWriterImp(String logDir) {
        this.logDir = logDir;
    }

    @Override
    public void logError(LogRow logRow, Exception e) {
        try {
            var outFileName = logDir + logRow.getFileName() + ".err";
            var outLine = logRow.getLogRow() + ": " + e.getMessage() + System.lineSeparator();
            Files.write(Path.of(outFileName), outLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
