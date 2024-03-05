package ru.gav19770210.stage2task4;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.gav19770210.stage2task4.control.LogProcessor;
import ru.gav19770210.stage2task4.control.LogProcessorImp;

public class MainApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("ru.gav19770210.stage2task4");

        LogProcessor logProcessor = context.getBean(LogProcessorImp.class);
        logProcessor.uploadLogs();

        context.close();
    }
}
