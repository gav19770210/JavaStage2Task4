package ru.gav19770210.stage2task4.check;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gav19770210.stage2task4.control.LogConfig;
import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowItem;
import ru.gav19770210.stage2task4.model.LogRowParser;

@SpringBootTest(classes = {LogConfig.class})
public class LogCheckRulesTest {
    @Autowired
    LogCheckRules logCheckRules;
    @Autowired
    LogRowParser logRowParser;
    @Autowired
    @Qualifier("logRowSeparator")
    String logRowSeparator;
    LogRow logRow;
    String logStr;

    @Test
    @DisplayName("Тест компонента LogCheckRules - применения правил проверки")
    public void Test() {
        logStr = "LoginIII" + logRowSeparator + "iVanov" + logRowSeparator + "ivaN" + logRowSeparator + "ivanOvich"
                + logRowSeparator + "2024-01-02 03:04:05" + logRowSeparator + "far";
        System.out.println("Тестовая строка: " + logStr);

        System.out.println("Создание экземпляра класса LogRow");
        Assertions.assertDoesNotThrow(() -> logRow = new LogRow(logStr, "test_log.txt", logRowParser.parseLogRow(logStr)),
                "Не удалось создать экземпляр класса LogRow");

        System.out.println("Применение правил проверки к экземпляру класса LogRow");
        logCheckRules.processRow(logRow);

        System.out.println("Проверка результатов применения правил проверки к экземпляру класса LogRow");
        Assertions.assertEquals("Ivanov", logRow.getLogRowPart(LogRowItem.USER_FAMILY),
                "Значение поля <Фамилия пользователя> не соответствует требованиям");
        Assertions.assertEquals("Ivan", logRow.getLogRowPart(LogRowItem.USER_NAME),
                "Значение поля <Имя пользователя> не соответствует требованиям");
        Assertions.assertEquals("Ivanovich", logRow.getLogRowPart(LogRowItem.USER_SURNAME),
                "Значение поля <Отчество пользователя> не соответствует требованиям");
        Assertions.assertEquals("2024-01-02 03:04:05", logRow.getLogRowPart(LogRowItem.ACCESS_DATE),
                "Значение поля <Время> не соответствует требованиям");
        Assertions.assertEquals("other:far", logRow.getLogRowPart(LogRowItem.APPLICATION),
                "Значение поля <Приложение> не соответствует требованиям");
    }
}
