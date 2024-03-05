package ru.gav19770210.stage2task4.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.gav19770210.stage2task4.model.LogRow;

import java.io.*;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * <b>LogFilesReaderImp</b> это класс-итератор по данным файлов логов, расположенных в заданной папке.
 * <br/>
 * Реализован механизм кеширования прочитанных записей из файлов логов, для чтения данных порциями.
 */
@Component
public class LogFilesReaderImp implements LogFilesReader {
    /**
     * Размер кэша для прочитанных записей из файлов логов.
     */
    private final int cacheSize = 2;
    /**
     * Массив кэша для прочитанных записей из файлов логов.
     */
    private final String[] logCacheData;
    /**
     * Массив индексов файлов соответствующих записям из кэша прочитанных данных.
     */
    private final int[] logCacheFileIndexes;
    /**
     * Папка с файлами логов.
     */
    private String pathName;
    /**
     * Массив с именами файлов логов из папки.
     */
    private String[] logFiles;
    /**
     * Объект для чтения файлов логов.
     */
    private BufferedReader logFileReader;
    /**
     * Текущий индекс файла из массива файлов логов.
     */
    private int fileIndex;
    /**
     * Количество загруженных записей в кэш.
     */
    private int logCacheCount;
    /**
     * Текущий индекс записи в массиве кэша, для выполнения итерации.
     */
    private int logCacheIndex;

    public LogFilesReaderImp() {
        logCacheData = new String[cacheSize];
        logCacheFileIndexes = new int[cacheSize];
        fileIndex = -1;
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
            var logRow = new LogRow(logCacheData[logCacheIndex], logFiles[logCacheFileIndexes[logCacheIndex]]);
            logCacheIndex++;
            return logRow;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Autowired
    @Qualifier("dirLogSource")
    public void setPathName(String pathName) {
        this.pathName = pathName;
        initLogFiles();
    }

    /**
     * Инициализация массива файлов логов из заданной папки.
     */
    private void initLogFiles() {
        File[] files = (new File(this.pathName)).listFiles(File::isFile);
        if (files != null) {
            this.logFiles = Arrays.stream(files).map(File::getName).toArray(String[]::new);
        } else {
            throw new NoSuchElementException("В папке <" + this.pathName + "> нет файлов логов");
        }
    }

    /**
     * Открытие объекта для чтения следующего файла логов.
     *
     * @return Объект для чтения файлов логов
     */
    private BufferedReader getNextFileReader() {
        try {
            if (logFiles == null || logFiles.length == 0 || fileIndex == logFiles.length - 1) {
                return null;
            } else {
                if (logFileReader != null) {
                    try {
                        logFileReader.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                fileIndex++;
                return new BufferedReader(new FileReader(pathName + logFiles[fileIndex]));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Заполнение кэша очередной порцией данных.
     */
    private void fillCacheData() {
        // при первом вызове метода открываем первый файл логов
        if (logFileReader == null) {
            logFileReader = getNextFileReader();
        }
        logCacheCount = 0;
        logCacheIndex = 0;
        if (logFileReader != null) {
            String logLine;
            // заполнение кеша порцией данных из файлов логов
            for (int i = 0; i < cacheSize; i++) {
                try {
                    logLine = logFileReader.readLine();
                    // если очередное чтение из файла достигло конца, то переход к следующему файлу
                    if (logLine == null) {
                        logFileReader = getNextFileReader();
                        if (logFileReader == null) {
                            break;
                        } else {
                            i--;
                        }
                    } else {
                        // если очередное чтение из файла вернуло пустую строку, то переход к следующей строке
                        if (logLine.isBlank()) {
                            i--;
                        } else {
                            logCacheData[i] = logLine;
                            logCacheFileIndexes[i] = fileIndex;
                            logCacheCount++;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
