package com.mo_apps.abs;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.mo_apps.abs.api.BuildResource;
import com.mo_apps.abs.api.DownloadResource;
import com.mo_apps.abs.core.ManagedCore;
import com.mo_apps.abs.core.SSLFuckupFactory;
import com.mo_apps.abs.core.freemarker.FreemarkerFactory;
import com.mo_apps.abs.core.providers.*;
import com.mo_apps.abs.health.DirsHealthCheck;
import com.mo_apps.abs.health.KeytoolHealthCheck;
import com.mo_apps.jwt_auth.JwtBundle;
import com.sun.akuma.Daemon;
import io.dropwizard.Application;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Основной класс приложения.
 * <p>
 * Здесь регистрируются ресурсы и health check`и,
 * а также происходит инициализация необходимых ресурсов.
 */
public class AndroidBuildSystem extends Application<AndroidBuildSystemConfiguration> {

    public static final Logger LOGGER = LoggerFactory.getLogger(AndroidBuildSystem.class);

    protected static final String PROPERTY_DAEMON = "daemon";
    protected static final String PROPERTY_PIDFILE = "pidfile";
    protected static final String DEFAULT_PIDFILE = "/var/run/daemon.pid";


    protected AndroidBuildSystem daemonize() throws Exception {
        return daemonize(getPidfileOrDefault(System.getProperties()));
    }


    protected AndroidBuildSystem daemonize(final String pidfilePath) throws Exception {
        final Daemon daemon = new Daemon();
        if (daemon.isDaemonized()) {
            final File pidfile = new File(pidfilePath);
            daemon.init(pidfile.getAbsolutePath());

            if (pidfile.exists()) {
                pidfile.deleteOnExit();
            } else {
                LOGGER.warn("Unable to set automatic deletion of pidfile {}.", pidfile.getAbsolutePath());
            }
        } else if (shouldDaemonize(System.getProperties())) {
            daemon.daemonize();
            System.out.println("Launched daemon into background.");
            System.exit(0);
        }
        return this;
    }


    protected String getPidfileOrDefault(final Properties props) {
        return Optional.fromNullable(Strings.emptyToNull(props.getProperty(PROPERTY_PIDFILE)))
                .or(DEFAULT_PIDFILE);
    }


    protected boolean shouldDaemonize(final Properties props) {
        return Boolean.parseBoolean(props.getProperty(PROPERTY_DAEMON));
    }


    public static void main(String[] args) throws Exception {
        new AndroidBuildSystem().daemonize().run(args);
    }


    @Override
    public void initialize(Bootstrap<AndroidBuildSystemConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<AndroidBuildSystemConfiguration>() {
            @Override
            public SwaggerBundleConfiguration getSwaggerBundleConfiguration(AndroidBuildSystemConfiguration configuration) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
        //bootstrap.addBundle(new JwtBundle());
    }


    @Override
    public void run(AndroidBuildSystemConfiguration configuration, Environment environment)
            throws Exception {
        FreemarkerFactory.initWith(configuration.getPaths().getTemplatesHome());

        // Register exception mappers
        environment.jersey().register(new ConstraintViolationExceptionMapper());
        environment.jersey().register(new EofExceptionMapper());
        environment.jersey().register(new IoExceptionMapper());
        environment.jersey().register(new JsonProcessingExceptionMapper());
        environment.jersey().register(new NotFoundExceptionMapper());
        environment.jersey().register(new RuntimeExceptionMapper());
        environment.jersey().register(new TemplateExceptionMapper());
        environment.jersey().register(new UnsupportedOperationExceptionMapper());

        // HttpClient
        final HttpClient httpClient = new HttpClientBuilder(environment)
                .using(configuration.getHttpClient())
                //.using(SSLFuckupFactory.createSSLSocketRegistry())
                //.using(new LaxRedirectStrategy())
                .build(getName());

        final ExecutorService executorService = Executors.newFixedThreadPool(configuration.getBuildPoolSize());
        final ManagedCore managedCore = new ManagedCore(httpClient, executorService);
        environment.lifecycle().manage(managedCore);

        // Register API resources
        environment.jersey().register(
                new BuildResource(configuration, executorService, httpClient)
        );
        environment.jersey().register(
                new DownloadResource(configuration.getPaths().getDownloadsHome())
        );


        // Регистрация health check`ов
        environment.healthChecks().register(
                "keytool",
                new KeytoolHealthCheck(configuration.getPaths().getKeytoolPath())
        );
        environment.healthChecks().register("dirs", new DirsHealthCheck(configuration));
    }


    @Override
    public String getName() {
        return "android-build-system";
    }

}
