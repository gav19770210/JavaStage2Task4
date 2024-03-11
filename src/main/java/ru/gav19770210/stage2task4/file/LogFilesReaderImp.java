package ru.gav19770210.stage2task4.file;

import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * <b>LogFilesReaderImp</b> это класс-итератор по данным файлов логов, расположенных в заданной папке.
 * <br/>
 * Реализован механизм кеширования прочитанных записей из файлов логов, для чтения данных порциями.
 */
public class LogFilesReaderImp implements LogFilesReader {
    /**
     * Парсер строки на составляющие элементы
     */
    private final LogRowParser logRowParser;
    /**
     * Папка с файлами логов.
     */
    private String logDir;
    /**
     * Массив файлов логов из папки.
     */
    private File[] logFiles;
    /**
     * Текущий индекс файла из массива файлов логов.
     */
    private int fileIndex;
    /**
     * Массив кэша для прочитанных записей из файлов логов.
     */
    private String[] logCacheData;
    /**
     * Количество загруженных записей в кэш.
     */
    private int logCacheCount;
    /**
     * Текущий индекс записи в массиве кэша, для выполнения итерации.
     */
    private int logCacheIndex;

    public LogFilesReaderImp(String logDir, LogRowParser logRowParser) {
        this.logRowParser = logRowParser;
        setLogDir(logDir);
    }

    @Override
    public boolean hasNext() {
        if (logCacheIndex == logCacheCount) {
            fillCacheData();
        }
        return (logCacheCount != 0);
    }

    @Override
    public LogRow next() {
        if (hasNext()) {
            var logRow = new LogRow(logCacheData[logCacheIndex],
                    logFiles[fileIndex].getName(),
                    logRowParser.parseLogRow(logCacheData[logCacheIndex]));
            logCacheIndex++;
            return logRow;
        } else {
            throw new NoSuchElementException();
        }
    }

    public void setLogDir(String logDir) {
        this.logDir = logDir;
        initLogFiles();
    }

    /**
     * Инициализация массива файлов логов из заданной папки.
     */
    private void initLogFiles() {
        clear();
        logFiles = (new File(logDir)).listFiles(File::isFile);
    }

    private void clear() {
        fileIndex = -1;
        logFiles = null;
        logCacheCount = 0;
        logCacheIndex = 0;
        logCacheData = null;
    }

    /**
     * Заполнение кэша очередной порцией данных.
     */
    private void fillCacheData() {
        logCacheCount = 0;
        logCacheIndex = 0;
        if (logFiles != null && logFiles.length > 0 && fileIndex < logFiles.length - 1) {
            fileIndex++;
            try (var logFileReader = new BufferedReader(new FileReader(logFiles[fileIndex]))) {
                logCacheData = logFileReader.lines().filter(s -> !s.isBlank()).toArray(String[]::new);
                if (logCacheData.length > 0) {
                    logCacheCount = logCacheData.length;
                } else {
                    fillCacheData();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
