package ru.gav19770210.stage2task4.control;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.gav19770210.stage2task4.db.LogDbWriterUtils;
import ru.gav19770210.stage2task4.file.LogFilesReaderUtils;

/**
 * Логика тестирования компонента LogProcessor:<br/>
 * - Генерация набора тестовых файлов логов<br/>
 * - Проверка и загрузка данных тестовых файлов логов в БД через компонент<br/>
 * - Сравнение количества сгенерированных и загруженных данных в БД
 */
public class LogProcessorTest {
    AnnotationConfigApplicationContext applicationContext;
    LogProcessor logProcessor;
    SessionFactory sessionFactory;

    @Test
    @DisplayName("Интеграционный тест")
    public void test() {
        System.out.println("Создание контекста Spring");
        Assertions.assertDoesNotThrow(() -> applicationContext = new AnnotationConfigApplicationContext("ru.gav19770210.stage2task4"),
                "Не удалось создать контекст Spring");

        String userName = "Tiger";
        String[] arrUsers = {userName};
        (new LogDbWriterUtils()).clearLogDbData(applicationContext, arrUsers);

        int countUsers = arrUsers.length;
        int countFiles = 1;
        int countRows = 5;
        int countLogins = countUsers * countFiles * countRows;
        Assertions.assertDoesNotThrow(() -> (new LogFilesReaderUtils()).generateLogFiles(applicationContext, arrUsers, countFiles, countRows),
                "Не удалось выполнить генерацию набора тестовых файлов логов");

        System.out.println("Создание компонента LogProcessor");
        Assertions.assertDoesNotThrow(() -> logProcessor = applicationContext.getBean(LogProcessor.class),
                "Не удалось создать компонент LogProcessor");

        System.out.println("Проверка и загрузка данных тестовых файлов логов в БД");
        Assertions.assertDoesNotThrow(() -> logProcessor.uploadLogs(), "Не удалось записать записи логов в БД");

        System.out.println("Получение объекта фабрики сессий БД");
        sessionFactory = applicationContext.getBean(SessionFactory.class);

        System.out.println("Открытие сессии БД");
        Session session = sessionFactory.openSession();

        System.out.println("Получение количества записей в таблице <users>");
        String sQuery = "select count(1) from users where username = '" + userName + "'";
        NativeQuery nativeQuery = session.createNativeQuery(sQuery);
        long recordCount = (Long) nativeQuery.getSingleResult();
        System.out.println("<users>.count = " + recordCount);

        System.out.println("Проверка количества записей в таблице <users>");
        Assertions.assertEquals(countUsers, recordCount, "Созданное в таблице <users> количество записей неверное");

        System.out.println("Получение количества записей в таблице <logins>");
        sQuery = "select count(1) from logins l inner join users u on l.user_id = u.id where u.username = '" + userName + "'";
        nativeQuery = session.createNativeQuery(sQuery);
        recordCount = (Long) nativeQuery.getSingleResult();
        System.out.println("<logins>.count = " + recordCount);

        System.out.println("Проверка количества записей в таблице <logins>");
        Assertions.assertEquals(countLogins, recordCount, "Созданное в таблице <logins> количество записей неверное");

        System.out.println("Закрытие сессии БД");
        session.close();
    }
}
