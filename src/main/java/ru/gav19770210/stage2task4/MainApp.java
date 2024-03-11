package ru.gav19770210.stage2task4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.gav19770210.stage2task4.control.LogProcessor;
import ru.gav19770210.stage2task4.control.LogProcessorImp;

@SpringBootApplication()
@Configuration
@ComponentScan("ru.gav19770210.stage2task4")
public class MainApp {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MainApp.class, args);

        LogProcessor logProcessor = context.getBean(LogProcessorImp.class);
        logProcessor.uploadLogs();
    }
}
