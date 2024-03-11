package ru.gav19770210.stage2task4.db;

import lombok.Getter;
import ru.gav19770210.stage2task4.model.LogRow;
import ru.gav19770210.stage2task4.model.LogRowItem;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * <b>Logins</b> это класс-сущность определяет данные о действиях пользователя, которые будут храниться в БД.
 */
@Entity
@Table(name = "logins", schema = "public")
@Getter
public class LoginDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = UserDb.class)
    @JoinColumn(name = "user_id")
    private UserDb userId;

    @Column(name = "access_date")
    private Timestamp accessDate;

    @Column(name = "application", length = -1)
    private String application;

    public LoginDb() {
    }

    public LoginDb(LogRow logRow, UserDb userDb) {
        this.userId = userDb;
        this.accessDate = Timestamp.valueOf(logRow.getLogRowPart(LogRowItem.ACCESS_DATE));
        this.application = logRow.getLogRowPart(LogRowItem.APPLICATION);
    }
}
