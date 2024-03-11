package ru.gav19770210.stage2task4.control;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import ru.gav19770210.stage2task4.check.*;
import ru.gav19770210.stage2task4.db.LogDbWriter;
import ru.gav19770210.stage2task4.db.LogDbWriterImp;
import ru.gav19770210.stage2task4.db.LoginDb;
import ru.gav19770210.stage2task4.db.UserDb;
import ru.gav19770210.stage2task4.file.*;
import ru.gav19770210.stage2task4.model.LogRowItem;
import ru.gav19770210.stage2task4.model.LogRowParser;
import ru.gav19770210.stage2task4.model.LogRowParserImp;

import java.io.IOException;
import java.util.Properties;

/**
 * <b>LogConfig</b> это класс конфигурации Spring компонентов.<p>
 * Объявляются и настраиваются необходимые компоненты.<p>
 * Подгружаются необходимые настройки из ресурсного файла config.ini
 */
@Component
@EnableAspectJAutoProxy
@PropertySource("classpath:hibernate.properties")
public class LogConfig {
    private final static Properties props = new Properties();

    public LogConfig() throws IOException {
        props.load(this.getClass().getResourceAsStream("/config.ini"));
    }

    /**
     * Получение разделителя элементов строки файла лога.
     *
     * @return разделитель элементов строки файла лога
     */
    @Bean(name = "logRowSeparator")
    public String getRowSeparator() {
        return ";";
    }

    /**
     * Получение массива типов элементов строки файла лога.
     *
     * @return массив типов элементов строки файла лога
     */
    @Bean(name = "logRowItems")
    public LogRowItem[] getLogRowItems() {
        return new LogRowItem[]{LogRowItem.LOGIN, LogRowItem.USER_FAMILY, LogRowItem.USER_NAME, LogRowItem.USER_SURNAME,
                LogRowItem.ACCESS_DATE, LogRowItem.APPLICATION};
    }

    @Bean
    public LogRowParser getLogRowParser(@Autowired @Qualifier("logRowSeparator") String logRowSeparator,
                                        @Autowired @Qualifier("logRowItems") LogRowItem[] logRowItems) {
        return new LogRowParserImp(logRowSeparator, logRowItems);
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

    @Bean
    public LogFilesReader getLogFilesReader(@Autowired @Qualifier("dirLogSource") String logDir,
                                            @Autowired LogRowParser logRowParser) {
        return new LogFilesReaderImp(logDir, logRowParser);
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
     * Получение из настроек конфигурации имени файла по умолчанию
     * для журналирования трансформаций элементов строк файлов логов.
     *
     * @return имя файла по умолчанию для журналирования трансформаций элементов строк файлов логов
     */
    @Bean(name = "fileLogTransformationDefault")
    public String getFileLogTransformationDefault() {
        return props.getProperty("FILE_LOG_TRANSFORMATION_DEFAULT");
    }

    @Bean
    public LogTransformationWriter getLogTransformationWriter(@Autowired @Qualifier("dirLogTransformation") String logDir,
                                                              @Autowired @Qualifier("fileLogTransformationDefault") String logFileNameDefault) {
        return new LogTransformationWriterImp(logDir, logFileNameDefault);
    }

    @Bean
    public LogAspect getLogAspect(@Autowired LogTransformationWriter logTransformationWriter) {
        return new LogAspect(logTransformationWriter);
    }

    @Bean("logCheckFIO")
    @DependsOn({"getLogAspect"})
    public LogChecker getLogCheckFIO() {
        return new LogCheckFIO();
    }

    @Bean("logCheckApp")
    @DependsOn({"getLogAspect"})
    public LogChecker getLogCheckApp() {
        return new LogCheckApp();
    }

    @Bean("logCheckDate")
    @DependsOn({"getLogAspect"})
    public LogChecker getLogCheckDate() {
        return new LogCheckDate();
    }

    @Bean
    public LogCheckRules getLogCheckRules(@Autowired @Qualifier("logRowItems") LogRowItem[] logRowItems,
                                          @Autowired @Qualifier("logCheckFIO") LogChecker logCheckFIO,
                                          @Autowired @Qualifier("logCheckApp") LogChecker logCheckApp,
                                          @Autowired @Qualifier("logCheckDate") LogChecker logCheckDate) {
        return (new LogCheckRulesImp(logRowItems))
                .addRule(LogRowItem.USER_FAMILY, logCheckFIO)
                .addRule(LogRowItem.USER_NAME, logCheckFIO)
                .addRule(LogRowItem.USER_SURNAME, logCheckFIO)
                .addRule(LogRowItem.APPLICATION, logCheckApp)
                .addRule(LogRowItem.ACCESS_DATE, logCheckDate);
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

    @Bean
    public LogErrorWriter getLogErrorWriter(@Autowired @Qualifier("dirLogError") String logDir) {
        return new LogErrorWriterImp(logDir);
    }

    @Bean
    public SessionFactory getSessionFactory() {
        org.hibernate.cfg.Configuration config = new org.hibernate.cfg.Configuration();
        config.addAnnotatedClass(UserDb.class);
        config.addAnnotatedClass(LoginDb.class);
        return config.buildSessionFactory();
    }

    @Bean
    public LogDbWriter getLogDbWriter(@Autowired SessionFactory sessionFactory) {
        return new LogDbWriterImp(sessionFactory);
    }

    @Bean
    public LogProcessor getLogProcessor(@Autowired LogFilesReader logFilesReader,
                                        @Autowired LogCheckRules logCheckRules,
                                        @Autowired LogErrorWriter logErrorWriter,
                                        @Autowired LogDbWriter logDbWriter) {
        return new LogProcessorImp(logFilesReader, logCheckRules, logErrorWriter, logDbWriter);
    }
}
