package com.mo_apps.abs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mo_apps.abs.configuration.PathsConfiguration;
import io.dropwizard.Configuration;
import io.dropwizard.client.HttpClientConfiguration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Модель данных конфигурации.
 * <p>
 * Поля данного класса содержат параметры,
 * считываемые при запуске приложения из файла конфигурации.
 */
@SuppressWarnings("unused")
public class AndroidBuildSystemConfiguration extends Configuration {

    @URL
    @NotEmpty
    private String cmaUrl;

    @URL
    @NotEmpty
    private String serviceUrl;

    @Min(1)
    @NotNull
    private Integer buildPoolSize;

    @Valid
    @NotNull
    private PathsConfiguration paths;

    @Valid
    @NotNull
    private HttpClientConfiguration httpClient = new HttpClientConfiguration();

    @Valid
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @JsonProperty
    public String getCmaUrl() {
        return cmaUrl;
    }

    @JsonProperty
    public PathsConfiguration getPaths() {
        return paths;
    }

    @JsonProperty
    public HttpClientConfiguration getHttpClient() {
        return httpClient;
    }

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }

    @JsonProperty
    public String getServiceUrl() {
        return serviceUrl;
    }

    @JsonProperty
    public Integer getBuildPoolSize() {
        return buildPoolSize;
    }
}