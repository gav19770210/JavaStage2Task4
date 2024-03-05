package ru.gav19770210.stage2task4.control;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.gav19770210.stage2task4.check.LogCheckRules;
import ru.gav19770210.stage2task4.check.LogChecker;
import ru.gav19770210.stage2task4.check.LogTransformation;
import ru.gav19770210.stage2task4.check.LogTransformationProxy;
import ru.gav19770210.stage2task4.db.LoginDb;
import ru.gav19770210.stage2task4.db.UserDb;
import ru.gav19770210.stage2task4.model.LogRowItem;

import java.io.IOException;
import java.util.Properties;

/**
 * <b>LogConfig</b> это класс конфигурации Spring компонентов.<p>
 * Объявляются и настраиваются необходимые компоненты.<p>
 * Подгружаются необходимые настройки из ресурсного файла config.ini
 */
@Configuration
@PropertySource("classpath:hibernate.properties")
public class LogConfig {
    private final static Properties props = new Properties();
    private LogChecker logCheckFIO;
    private LogChecker logCheckApp;
    private LogChecker logCheckDate;

    public LogConfig() throws IOException {
        props.load(this.getClass().getResourceAsStream("/config.ini"));
    }

    /**
     * Получение из настроек конфигурации папки с файлами логов.
     *
     * @return папка с файлами логов
     */
    @Bean(name = "dirLogSource")
    public String getDirLogSource() {
        String path = props.getProperty("DIR_LOG_SOURCE");
        path = path.endsWith("/") ? path : path + '/';
        return path;
    }

    /**
     * Получение из настроек конфигурации папка для файла с ошибками обработки.
     *
     * @return папка для файла с ошибками обработки
     */
    @Bean(name = "dirLogError")
    public String getDirLogError() {
        String path = props.getProperty("DIR_LOG_ERROR");
        path = path.endsWith("/") ? path : path + '/';
        return path;
    }

    /**
     * Получение из настроек конфигурации папки для файлов с журналом трансформаций элементов строк файлов логов.
     *
     * @return папка для файлов с журналом трансформаций элементов строк файлов логов
     */
    @Bean(name = "dirLogTransformation")
    public String getDirLogTransformation() {
        String path = props.getProperty("DIR_LOG_TRANSFORMATION");
        path = path.endsWith("/") ? path : path + '/';
        return path;
    }

    /**
     * Получение разделителя элементов строки файла лога.
     *
     * @return разделитель элементов строки файла лога
     */
    @Bean(name = "logRowSeparator")
    public String getSeparator() {
        return ";";
    }

    /**
     * Получение массива типов элементов строки файла лога.
     *
     * @return массив типов элементов строки файла лога
     */
    @Bean(name = "logRowItems")
    public LogRowItem[] getLogRowItems() {
        return new LogRowItem[]{LogRowItem.LOGIN, LogRowItem.USER_FAMILY, LogRowItem.USER_NAME, LogRowItem.USER_SURNAME, LogRowItem.ACCESS_DATE, LogRowItem.APPLICATION};
    }

    @Autowired
    @Qualifier("logCheckFIO")
    public void setLogCheckFIO(LogChecker logChecker) {
        logCheckFIO = getWrapperChecker(logChecker);
    }

    @Autowired
    @Qualifier("logCheckApp")
    public void checkApplication(LogChecker logChecker) {
        logCheckApp = getWrapperChecker(logChecker);
    }

    @Autowired
    @Qualifier("logCheckDate")
    public void setDateChecker(LogChecker logChecker) {
        logCheckDate = getWrapperChecker(logChecker);
    }

    /**
     * Создание прокси-объекта для исходного объекта проверки полей в строке логов.
     *
     * @param logChecker исходный объект проверки полей в строке логов
     * @return прокси-объект для исходного объекта проверки полей в строке логов
     */
    private LogChecker getWrapperChecker(LogChecker logChecker) {
        if (logChecker.getClass().isAnnotationPresent(LogTransformation.class)) {
            var logTransformationProxy = new LogTransformationProxy<LogChecker>();
            var logFileTransformation = logChecker.getClass().getAnnotation(LogTransformation.class).value();
            return logTransformationProxy.getProxy(logChecker, logFileTransformation);
        } else {
            return logChecker;
        }
    }

    /**
     * Создание и заполнение методами проверки компонента по их применению
     *
     * @param logCheckRules компонент для применения правил
     */
    @Autowired
    public void setLogCheckRules(LogCheckRules logCheckRules) {
        logCheckRules.addRule(LogRowItem.USER_FAMILY, logCheckFIO);
        logCheckRules.addRule(LogRowItem.USER_NAME, logCheckFIO);
        logCheckRules.addRule(LogRowItem.USER_SURNAME, logCheckFIO);
        logCheckRules.addRule(LogRowItem.APPLICATION, logCheckApp);
        logCheckRules.addRule(LogRowItem.ACCESS_DATE, logCheckDate);
    }

    @Bean
    public SessionFactory getSessionFactory() {
        org.hibernate.cfg.Configuration config = new org.hibernate.cfg.Configuration();
        config.addAnnotatedClass(UserDb.class);
        config.addAnnotatedClass(LoginDb.class);
        return config.buildSessionFactory();
    }
}
