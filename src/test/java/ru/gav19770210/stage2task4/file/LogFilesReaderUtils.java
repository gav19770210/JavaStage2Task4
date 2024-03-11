package ru.gav19770210.stage2task4.file;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class LogFilesReaderUtils {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DecimalFormat decimalFormat = new DecimalFormat("00");

    public Map<String, String> generateLogFiles(String logDir, String logRowSeparator,
                                                String[] arrUsers, int countFiles, int countRows) throws IOException {
        System.out.println("Удаление всех файлов каталога dirLogSource");
        File dirSource = new File(logDir);
        FileUtils.cleanDirectory(dirSource);

        System.out.println("dirLogSource: " + logDir);
        System.out.println("Генерация набора тестовых файлов логов");
        System.out.println("logRowSeparator: " + logRowSeparator);

        Map<String, String> writeData = new HashMap<>();
        String logFileName;
        String logStr;
        String logStrKey;
        String timeStr;
        for (int i = 1; i <= countFiles; i++) {
            logFileName = logDir + "test_log" + i + ".txt";
            System.out.println(logFileName);
            for (String userName : arrUsers) {
                for (int k = 0; k < countRows; k++) {
                    timeStr = "2024-01-02 03:" + decimalFormat.format(i) + ":" + decimalFormat.format(k);
                    logStr = userName + logRowSeparator + userName + "Family"
                            + logRowSeparator + userName + "Name"
                            + logRowSeparator + userName + "Surname"
                            + logRowSeparator + timeStr
                            + logRowSeparator + "Application";
                    Files.write(Path.of(logFileName), (logStr + System.lineSeparator()).getBytes(),
                            StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    logStrKey = userName + dateFormat.format(Timestamp.valueOf(timeStr));
                    writeData.put(logStrKey, logStr);
                }
            }
        }
        return writeData;
    }
}
