package ru.gav19770210.stage2task4.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import ru.gav19770210.stage2task4.control.LogConfig;
import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowParser;

import java.sql.Timestamp;
import java.util.List;

/**
 * Логика тестирования компонента LogDbWriter:<br/>
 * - Запись данных экземпляра класса LogRow в БД через компонент<br/>
 * - Чтение данных экземпляра класса LogRow, ранее записанных в БД<br/>
 * - Проверка результатов записи/чтения данных БД
 */
@SpringBootTest(classes = {LogConfig.class})
public class LogDbWriterTest {
    @Autowired
    LogDbWriter logDbWriter;
    @Autowired
    LogRowParser logRowParser;
    @Autowired
    @Qualifier("logRowSeparator")
    String logRowSeparator;
    LogRow logRow;
    String logStr;
    @Autowired
    SessionFactory sessionFactory;
    UserDb userDb;
    List<LoginDb> logins;

    @Test
    @DisplayName("Тест компонента LogDbWriter - запись данных в БД")
    public void test() {
        String userName = "LoginX";
        String[] arrUsers = {userName};
        (new LogDbWriterUtils()).clearLogDbData(sessionFactory, arrUsers);

        logStr = userName + logRowSeparator + "iVanov" + logRowSeparator + "ivaN" + logRowSeparator + "ivanOvich"
                + logRowSeparator + "2024-01-02 03:04:05" + logRowSeparator + "far";
        System.out.println("Тестовая строка: " + logStr);

        System.out.println("Создание экземпляра класса LogRow");
        Assertions.assertDoesNotThrow(() -> logRow = new LogRow(logStr, "test_log.txt", logRowParser.parseLogRow(logStr)),
                "Не удалось создать экземпляр класса LogRow");

        System.out.println("Открытие сессии БД");
        Assertions.assertDoesNotThrow(() -> logDbWriter.openSession(),
                "Не удалось открыть сессию БД");

        System.out.println("Запись данных экземпляра класса LogRow в БД");
        Assertions.assertDoesNotThrow(() -> logDbWriter.writeLogRow(logRow),
                "Не удалось записать данные экземпляра класса LogRow в БД");

        System.out.println("Закрытие сессии БД");
        Assertions.assertDoesNotThrow(() -> logDbWriter.closeSession(),
                "Не удалось закрыть сессию БД");

        System.out.println("Открытие сессии БД");
        Session session = sessionFactory.openSession();

        System.out.println("Открытие транзакции сессии БД");
        session.getTransaction().begin();

        System.out.println("Поиск пользователя <LoginX> в БД");
        Assertions.assertDoesNotThrow(() -> userDb = session.createQuery("from UserDb where userName = 'LoginX'", UserDb.class)
                .getSingleResult(), "В БД не найден пользователь <LoginX>");

        System.out.println("Сравнение значения поля <fio>");
        Assertions.assertEquals("iVanov ivaN ivanOvich", userDb.getFio(),
                "В БД сохранилось не корректное значение поля <fio>");

        System.out.println("Поиск в БД у пользователя <LoginX> записей в таблице <Logins>");
        Query<LoginDb> query = session.createQuery("from LoginDb where userId = :userDb", LoginDb.class)
                .setParameter("userDb", userDb);
        Assertions.assertDoesNotThrow(() -> logins = query.getResultList(),
                "В БД у пользователя <LoginX> не найдено записей в таблице <Logins>");

        System.out.println("Сравнение значения поля <accessDate>");
        Assertions.assertEquals(Timestamp.valueOf("2024-01-02 03:04:05"), logins.get(0).getAccessDate(),
                "В БД сохранилось не корректное значение поля <accessDate>");

        System.out.println("Сравнение значения поля <application>");
        Assertions.assertEquals("far", logins.get(0).getApplication(),
                "В БД сохранилось не корректное значение поля <application>");

        System.out.println("Завершение транзакции сессии БД");
        session.getTransaction().commit();

        System.out.println("Закрытие сессии БД");
        session.close();
    }
}
