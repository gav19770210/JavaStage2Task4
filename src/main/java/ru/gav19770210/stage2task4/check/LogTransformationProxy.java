package ru.gav19770210.stage2task4.check;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

/**
 * <b>LogTransformationProxy</b> это класс-прокси для записи в лог входящих и исходящих параметров выполняемых методов,
 * заданых интерфейсом T оборачиваемого объекта с аннотацией <b>@LogTransformation</b>.
 */
@Component
public class LogTransformationProxy<T> implements InvocationHandler {
    /**
     * Разделитель данных выводимых в лог.
     */
    private static final String separator = "\t";
    /**
     * Файл лога по умолчанию.
     */
    private static final String logFileNameDefault = "transformation.log";
    /**
     * Папка для логов.
     */
    private static String logPath;
    /**
     * Оборачиваемый объект с аннотацией <b>@LogTransformation</b>.
     */
    private T wrappedObject;
    /**
     * Файл лога из аннотации <b>@LogTransformation</b>.
     */
    private String logFileName;

    @Autowired
    @Qualifier("dirLogTransformation")
    public void setLogPath(String outPath) {
        LogTransformationProxy.logPath = outPath;
    }

    /**
     * Создание прокси-объекта для исходного объекта.
     *
     * @param object      исходный объект
     * @param logFileName файл лога из аннотации <b>@LogTransformation</b>
     * @return прокси-объект
     */
    public T getProxy(T object, String logFileName) {
        this.wrappedObject = object;
        this.logFileName = ((logFileName == null || logFileName.isEmpty()) ? logFileNameDefault : logFileName);
        return (T) Proxy.newProxyInstance(object.getClass().getClassLoader(), object.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var startTime = LocalDateTime.now();
        Object result = method.invoke(wrappedObject, args);
        if (logPath != null && logFileName != null) {
            var outFileName = logPath + logFileName;
            String outLine = startTime + separator + wrappedObject.getClass().getName() + separator + args[0] + separator + result + "\r\n";
            Files.write(Path.of(outFileName), outLine.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        return result;
    }
}
