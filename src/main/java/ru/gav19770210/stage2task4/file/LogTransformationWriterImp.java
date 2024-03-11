package ru.gav19770210.stage2task4.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class LogTransformationWriterImp implements LogTransformationWriter {
    /**
     * Папка для логов.
     */
    private final String logDir;
    /**
     * Файл лога по умолчанию.
     */
    private final String logFileNameDefault;

    public LogTransformationWriterImp(String logDir, String logFileNameDefault) {
        this.logDir = logDir;
        this.logFileNameDefault = logFileNameDefault;
    }

    @Override
    public void logWrite(String s, String logFileName) {
        if (logDir != null) {
            var outFileName = ((logFileName == null || logFileName.isEmpty()) ? logFileNameDefault : logFileName);
            outFileName = logDir + outFileName;
            var outLine = s + System.lineSeparator();
            try {
                Files.write(Path.of(outFileName), outLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
