package ru.gav19770210.stage2task4.file;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gav19770210.stage2task4.control.LogConfig;
import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Логика тестирования компоненета LogErrorWriter:<br/>
 * - Запись в файл ошибок тестовой строки через компонент<br/>
 * - Чтение тестовой строки из файла ошибок<br/>
 * - Проверка результатов записи/чтения тестовой строки файла ошибок
 */
@SpringBootTest(classes = {LogConfig.class})
public class LogErrorWriterTest {
    @Autowired
    LogErrorWriter logErrorWriter;
    @Autowired
    LogRowParser logRowParser;
    @Autowired
    @Qualifier("logRowSeparator")
    String logRowSeparator;
    @Autowired
    @Qualifier("dirLogError")
    String errorPath;
    String errorFileName = "test_log.txt";
    LogRow logRow;
    String logStr;
    String readLine;

    @Test
    @DisplayName("Тест компонента LogErrorWriter - запись в файл ошибок информации об исключениях")
    public void test() throws IOException {
        System.out.println("Удаление всех файлов каталога dirLogError");
        File dirError = new File(errorPath);
        FileUtils.cleanDirectory(dirError);

        logStr = "LoginX" + logRowSeparator + "iVanov" + logRowSeparator + "ivaN" + logRowSeparator + "ivanOvich"
                + logRowSeparator + "2024-01-02 03:04:05" + logRowSeparator + "far";
        System.out.println("Тестовая строка: " + logStr);

        System.out.println("Создание экземпляра класса LogRow");
        Assertions.assertDoesNotThrow(() -> logRow = new LogRow(logStr, errorFileName, logRowParser.parseLogRow(logStr)),
                "Не удалось создать экземпляр класса LogRow");

        var errorFilePath = errorPath + errorFileName + ".err";
        System.out.println("Создание файла ошибок: " + errorFilePath);

        var errorText = "Тест ошибки!";
        System.out.println("Запись в файл ошибок тестовой строки");
        Assertions.assertDoesNotThrow(() -> logErrorWriter.logError(logRow, new Exception(errorText)),
                "Не удалось записать в файл ошибок тестовую строку");

        System.out.println("Чтение тестовой строки из файла ошибок");
        Assertions.assertDoesNotThrow(() -> readLine = (new BufferedReader(new FileReader(errorFilePath))).readLine(),
                "Не удалось прочитать тестовую строку из файла ошибок");

        System.out.println("Проверка результатов записи/чтения тестовой строки файла ошибок");
        Assertions.assertEquals(logStr + ": " + errorText, readLine,
                "Записанная строка в файл ошибок не соответствует требованиям");
    }
}
