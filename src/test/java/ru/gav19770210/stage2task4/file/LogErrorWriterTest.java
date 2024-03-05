package ru.gav19770210.stage2task4.file;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.gav19770210.stage2task4.model.LogRow;

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
public class LogErrorWriterTest {
    AnnotationConfigApplicationContext applicationContext;
    LogErrorWriter logErrorWriter;
    String logRowSeparator;
    String errorPath;
    String errorFileName = "test_log.txt";
    LogRow logRow;
    String logStr;
    String readLine;

    @Test
    @DisplayName("Тест компонента LogErrorWriter - запись в файл ошибок информации об исключениях")
    public void test() throws IOException {
        System.out.println("Создание контекста Spring");
        Assertions.assertDoesNotThrow(() -> applicationContext = new AnnotationConfigApplicationContext("ru.gav19770210.stage2task4"),
                "Не удалось создать контекст Spring");

        System.out.println("Создание компонента LogErrorWriter");
        Assertions.assertDoesNotThrow(() -> logErrorWriter = applicationContext.getBean(LogErrorWriter.class),
                "Не удалось создать компонент LogErrorWriter");

        System.out.println("Создание компонента logRowSeparator");
        Assertions.assertDoesNotThrow(() -> logRowSeparator = applicationContext.getBean("logRowSeparator", String.class),
                "Не удалось создать компонент logRowSeparator");
        System.out.println("logRowSeparator: " + logRowSeparator);

        logStr = "LoginX" + logRowSeparator + "iVanov" + logRowSeparator + "ivaN" + logRowSeparator + "ivanOvich"
                + logRowSeparator + "2024-01-02 03:04:05" + logRowSeparator + "far";
        System.out.println("Тестовая строка: " + logStr);

        System.out.println("Создание экземпляра класса LogRow");
        Assertions.assertDoesNotThrow(() -> logRow = new LogRow(logStr, errorFileName),
                "Не удалось создать экземпляр класса LogRow");

        System.out.println("Создание компонента dirLogError");
        Assertions.assertDoesNotThrow(() -> errorPath = applicationContext.getBean("dirLogError", String.class),
                "Не удалось создать компонент dirLogError");
        System.out.println("dirLogError: " + errorPath);

        System.out.println("Удаление всех файлов каталога dirLogError");
        File dirError = new File(errorPath);
        FileUtils.cleanDirectory(dirError);

        var errorFilePath = errorPath + errorFileName + ".err";
        System.out.println("Создание файла ошибок: " + errorFilePath);
        File errorFile = new File(errorFilePath);
        errorFile.delete();

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
