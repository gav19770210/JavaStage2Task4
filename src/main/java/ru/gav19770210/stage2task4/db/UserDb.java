package ru.gav19770210.stage2task4.db;

import lombok.Getter;
import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowItem;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>Users</b> это класс-сущность определяет данные о пользователе, которые будут храниться в БД.
 */
@Entity
@Table(name = "users", schema = "public")
@Getter
public class UserDb {
    /**
     * Внутренний кэш с пользователями, чтобы уменьшить количество обращений к БД.
     */
    private final static List<UserDb> userCache = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = -1)
    private String userName;

    @Column(name = "fio", nullable = false, length = -1)
    private String fio;

    public UserDb() {
    }

    private UserDb(String userName, String fio) {
        this.userName = userName;
        this.fio = fio;
    }

    /**
     * Возвращает объект пользователя по структуре разобранной строки лога.
     * Выполняется преобразование структры разобранной строки лога к полям БД и перевызов перекрытого метода.
     *
     * @param logRow структура разобранной строки лога
     * @return объект пользователя
     */
    public static UserDb getUserDb(LogRow logRow) {
        return getUserDb(logRow.getLogRowPart(LogRowItem.LOGIN),
                String.join(" ",
                        logRow.getLogRowPart(LogRowItem.USER_FAMILY),
                        logRow.getLogRowPart(LogRowItem.USER_NAME),
                        logRow.getLogRowPart(LogRowItem.USER_SURNAME)
                )
        );
    }

    /**
     * Возвращает объект пользователя по логину и ФИО.
     *
     * @param userName логин пользователя
     * @param fio      ФИО пользователя
     * @return объект пользователя
     */
    public static UserDb getUserDb(String userName, String fio) {
        int idx = getUserCacheIdx(userName);
        if (idx == -1) {
            UserDb userDb = new UserDb(userName, fio);
            userCache.add(userDb);
            return userDb;
        } else {
            return userCache.get(idx);
        }
    }

    /**
     * Поиск пользователя по логину во внутреннем кэше.
     */
    private static int getUserCacheIdx(String userName) {
        for (int i = 0; i < userCache.size(); i++) {
            if (userCache.get(i).userName.equals(userName)) {
                return i;
            }
        }
        return -1;
    }
}
