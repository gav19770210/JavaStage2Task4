package ru.gav19770210.stage2task4.file;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gav19770210.stage2task4.control.LogConfig;
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
@SpringBootTest(classes = {LogConfig.class})
public class LogFilesReaderTest {
    @Autowired
    LogFilesReader logFilesReader;
    @Autowired
    LogErrorWriter logErrorWriter;
    @Autowired
    @Qualifier("logRowSeparator")
    String logRowSeparator;
    @Autowired
    @Qualifier("dirLogSource")
    String logDir;

    @Test
    @DisplayName("Тест компонента LogFilesReader - итератора по строкам файлов лога")
    public void test() throws IOException {
        String[] arrUsers = {"Dog", "Cat", "Rat"};
        Map<String, String> writeData = (new LogFilesReaderUtils()).generateLogFiles(logDir, logRowSeparator,
                arrUsers, 3, 5);

        System.out.println("Чтение тестовых файлов логов");
        Map<String, String> readData = new HashMap<>();
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
        System.out.println("writeData.size(): " + writeData.size());
        System.out.println("readData.size(): " + readData.size());
        Assertions.assertEquals(writeData.size(), readData.size(),
                "Количество сгенерированных записей не равно количеству прочитанных записей");

        System.out.println("Сравнение сгенерированных и прочитанных записей тестовых файлов логов");
        writeData.forEach((k, v) -> Assertions.assertEquals(v, readData.get(k),
                "Прочитанная запись не соответствует сгенерированной"));

        writeData.clear();
        readData.clear();
    }
}
