package com.mo_apps.abs.core.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

import java.io.File;
import java.io.IOException;

/**
 * Конфигурация FreeMarker.
 * Данный класс представляет собой синглтон, содержащий инстанс
 * {@link freemarker.template.Configuration}.
 * <p>
 * На приложение необходим один инстанс {@link freemarker.template.Configuration},
 * поскольку FreeMarker использует кэширование шаблонов для большей производительности,
 * а создание новой конфигурации является дорогой операцией.
 */
public class FreemarkerFactory {
    private static volatile FreemarkerFactory instance;

    private Configuration configuration;

    // приватный конструктор синглтона
    private FreemarkerFactory() {
        configuration = new Configuration(new Version(2, 3, 20));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    private FreemarkerFactory(String templatesHome) throws IOException {
        configuration = new Configuration(new Version(2, 3, 20));
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        configuration.setDirectoryForTemplateLoading(new File(templatesHome));
    }

    /**
     * Возвращает инстанс класса
     *
     * @return инстанс FreemarkerFactory
     */
    public static FreemarkerFactory getInstance() {
        FreemarkerFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (FreemarkerFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new FreemarkerFactory();
                }
            }
        }

        return localInstance;
    }

    public static FreemarkerFactory initWith(String templatesHome) throws IOException {
        FreemarkerFactory localInstance = instance;
        if (localInstance == null) {
            synchronized (FreemarkerFactory.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new FreemarkerFactory(templatesHome);
                }
            }
        }

        return localInstance;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Задает директорию, в которой находятся шаблоны для FreeMarker.
     *
     * @param path Путь к директории шаблонов.
     * @throws IOException Ошибка при установке директории.
     */
    public void setTemplateDirectory(String path) throws IOException {
        configuration.setDirectoryForTemplateLoading(new File(path));
    }
}
