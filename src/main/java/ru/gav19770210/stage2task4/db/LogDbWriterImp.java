package ru.gav19770210.stage2task4.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowItem;

import javax.persistence.NoResultException;

public class LogDbWriterImp implements LogDbWriter {
    private final SessionFactory sessionFactory;
    private Session session;

    public LogDbWriterImp(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void openSession() {
        session = sessionFactory.openSession();
        session.getTransaction().begin();
    }

    @Override
    public void closeSession() {
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void writeLogRow(LogRow logRow) {
        UserDb userDb;
        try {
            // Поиск пользователя из БД
            String queryDb = "from UserDb where userName = '" + logRow.getLogRowPart(LogRowItem.LOGIN) + "'";
            userDb = session.createQuery(queryDb, UserDb.class).getSingleResult();
        } catch (NoResultException e) {
            // Добавление пользователя в БД
            userDb = UserDb.getUserDb(logRow);
            session.persist(userDb);
        }
        LoginDb loginDb = new LoginDb(logRow, userDb);
        session.persist(loginDb);
    }
}
