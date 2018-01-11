package com.mo_apps.abs.core;

import io.dropwizard.lifecycle.Managed;
import org.apache.http.client.HttpClient;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Реализация {@link io.dropwizard.lifecycle.Managed} интерфейса для управления {@code httpClient}
 * и {@code executorService}.
 * <p>
 * Во время остановки работы сервера в методе {@code stop} производится закрытие пула потоков и
 * закрытие {@code httpClient}
 */
public class ManagedCore implements Managed {
    private final HttpClient httpClient;
    private final ExecutorService executorService;

    /**
     * Конструктор класса.
     *
     * @param httpClient      сконфигурированный {@link org.apache.http.client.HttpClient}
     * @param executorService пул потоков для выполнения заданий по сборке приложений.
     */
    public ManagedCore(HttpClient httpClient, ExecutorService executorService) {
        this.executorService = executorService;
        this.httpClient = httpClient;
    }

    @Override
    public void start() throws Exception {
        // Nothing to do here
    }

    @Override
    public void stop() throws Exception {
        // В данном месте закрытие пула потоков должно происходить перед закрытием инстанса
        // httpClient, поскольку потоки будут использовать его для отправки данных на сервер
        // по завершении выполнения задач.
//        executorService.awaitTermination(2, TimeUnit.MINUTES);
        executorService.shutdown();
        httpClient.getConnectionManager().shutdown();
    }
}
