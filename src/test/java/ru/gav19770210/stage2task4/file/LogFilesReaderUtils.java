package ru.gav19770210.stage2task4.file;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    String logRowSeparator;
    String logPath;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    DecimalFormat decimalFormat = new DecimalFormat("00");

    public Map<String, String> generateLogFiles(AnnotationConfigApplicationContext applicationContext,
                                                String[] arrUsers, int countFiles, int countRows) throws IOException {
        System.out.println("Генерация набора тестовых файлов логов");

        logPath = applicationContext.getBean("dirLogSource", String.class);
        System.out.println("dirLogSource: " + logPath);

        System.out.println("Удаление всех файлов каталога dirLogSource");
        File dirSource = new File(logPath);
        FileUtils.cleanDirectory(dirSource);

        logRowSeparator = applicationContext.getBean("logRowSeparator", String.class);
        System.out.println("logRowSeparator: " + logRowSeparator);

        Map<String, String> writeData = new HashMap<>();
        String logFileName;
        String logStr;
        String logStrKey;
        String timeStr;
        for (int i = 1; i <= countFiles; i++) {
            logFileName = logPath + "test_log" + i + ".txt";
            System.out.println(logFileName);
            File logFile = new File(logFileName);
            logFile.delete();
            for (String userName : arrUsers) {
                for (int k = 0; k < countRows; k++) {
                    timeStr = "2024-01-02 03:" + decimalFormat.format(i) + ":" + decimalFormat.format(k);
                    logStr = userName + logRowSeparator + userName + "Family"
                            + logRowSeparator + userName + "Name"
                            + logRowSeparator + userName + "Surname"
                            + logRowSeparator + timeStr
                            + logRowSeparator + "Application";
                    Files.write(Path.of(logFileName), (logStr + "\r\n").getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                    logStrKey = userName + dateFormat.format(Timestamp.valueOf(timeStr));
                    writeData.put(logStrKey, logStr);
                }
            }
        }
        return writeData;
    }
}
