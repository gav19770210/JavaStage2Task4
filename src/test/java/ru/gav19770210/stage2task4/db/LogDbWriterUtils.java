package ru.gav19770210.stage2task4.db;

import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LogDbWriterUtils {
    SessionFactory sessionFactory;

    public void clearLogDbData(AnnotationConfigApplicationContext applicationContext, String[] arrUsers) {
        System.out.println("Удаление данных в БД по списку логинов пользователей");

        System.out.println("Получение объекта фабрики сессий БД");
        sessionFactory = applicationContext.getBean(SessionFactory.class);

        System.out.println("Открытие сессии БД");
        Session session = sessionFactory.openSession();

        System.out.println("Открытие транзакции сессии БД");
        session.getTransaction().begin();

        UserDb userDb;
        String queryDb;
        for (String userName : arrUsers) {
            System.out.println("username: " + userName);
            try {
                queryDb = "from UserDb where userName = '" + userName + "'";
                userDb = session.createQuery(queryDb, UserDb.class).getSingleResult();

                session.createQuery("delete from LoginDb where userId = :userDb")
                        .setParameter("userDb", userDb)
                        .executeUpdate();

                session.remove(userDb);
            } catch (NoResultException ignored) {
            }
        }

        System.out.println("Завершение транзакции сессии БД");
        session.getTransaction().commit();

        System.out.println("Закрытие сессии БД");
        session.close();
    }
}
