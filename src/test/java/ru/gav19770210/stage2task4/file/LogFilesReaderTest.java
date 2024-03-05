package ru.gav19770210.stage2task4.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Логика тестирования компонента LogFilesReader:<br/>
 * - Генерация набора тестовых файлов логов, и сохранение данных в одную мапированную коллекцию<br/>
 * - Чтение набора тестовых файлов логов через компонент, и сохранение данных в другую мапированную коллекцию<br/>
 * - Сравнение полученных мапированных коллекций
 */
public class LogFilesReaderTest {
    AnnotationConfigApplicationContext applicationContext;
    LogFilesReader logFilesReader;
    LogErrorWriter logErrorWriter;

    Map<String, String> writeData;
    Map<String, String> readData = new HashMap<>();

    @Test
    @DisplayName("Тест компонента LogFilesReader - итератора по строкам файлов лога")
    public void test() throws IOException {
        System.out.println("Создание контекста Spring");
        Assertions.assertDoesNotThrow(() -> applicationContext = new AnnotationConfigApplicationContext("ru.gav19770210.stage2task4"),
                "Не удалось создать контекст Spring");

        String[] arrUsers = {"Dog", "Cat", "Rat"};
        writeData = (new LogFilesReaderUtils()).generateLogFiles(applicationContext, arrUsers, 3, 5);

        System.out.println("Создание компонента LogFilesReader");
        Assertions.assertDoesNotThrow(() -> logFilesReader = applicationContext.getBean(LogFilesReader.class),
                "Не удалось создать компонент LogFilesReader");

        System.out.println("Создание компонента LogErrorWriter");
        Assertions.assertDoesNotThrow(() -> logErrorWriter = applicationContext.getBean(LogErrorWriter.class),
                "Не удалось создать компонент LogErrorWriter");

        System.out.println("Чтение тестовых файлов логов");
        readData.clear();
        Assertions.assertDoesNotThrow(() -> {
            LogRow logRow;
            String logStrKey;
            while (logFilesReader.hasNext()) {
                logRow = logFilesReader.next();
                try {
                    logStrKey = logRow.getLogRowPart(LogRowItem.LOGIN) + logRow.getLogRowPart(LogRowItem.ACCESS_DATE);
                    readData.put(logStrKey, logRow.getLogRow());
                } catch (Exception e) {
                    logErrorWriter.logError(logRow, e);
                }
            }
        }, "Не удалось прочитать данные тестовых файлов логов");

        System.out.println("Сравнение количества сгенерированных и прочитанных записей тестовых файлов логов");
        Assertions.assertEquals(writeData.size(), readData.size(),
                "Количество сгенерированных записей не равно количеству прочитанных записей");

        System.out.println("Сравнение сгенерированных и прочитанных записей тестовых файлов логов");
        writeData.forEach((k, v) -> Assertions.assertEquals(v, readData.get(k),
                "Прочитанная запись не соответствует сгенерированной"));
    }
}
