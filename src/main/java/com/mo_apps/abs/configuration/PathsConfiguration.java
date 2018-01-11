package com.mo_apps.abs.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.validation.ValidationMethod;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.File;

/**
 * В данной модели сгруппированы настройки путей и зависимостей приложения.
 */
@SuppressWarnings("unused")
public class PathsConfiguration {

    // Fields -------------------------------------------------------------------------------------

    /**
     * Путь к директории Android SDK.
     */
    @NotEmpty
    private String androidSdkHome;

    /**
     * Путь к Java keytool.
     */
    @NotEmpty
    private String keytoolPath;

    /**
     * Путь к директории, в которой будут создаваться проекты.
     */
    @NotEmpty
    private String buildsHome;

    /**
     * Путь к директории с шаблонами проектов.
     */
    @NotEmpty
    private String projectTemplatesHome;

    /**
     * Путь к директории, в которой будут содержаться ftl шаблоны.
     */
    @NotEmpty
    private String templatesHome;

    /**
     * Путь к директории, в которой будут содержаться собранные приложения для загрузки.
     */
    @NotEmpty
    private String downloadsHome;

    // --------------------------------------------------------------------------------------------


    // Constructors -------------------------------------------------------------------------------

    public PathsConfiguration() {
        // Jackson nop
    }

    // --------------------------------------------------------------------------------------------


    // Getters & Setters --------------------------------------------------------------------------

    @JsonProperty
    public String getAndroidSdkHome() {
        return androidSdkHome;
    }

    @JsonProperty
    public String getKeytoolPath() {
        return keytoolPath;
    }

    @JsonProperty
    public String getBuildsHome() {
        return buildsHome;
    }

    @JsonProperty
    public String getProjectTemplatesHome() {
        return projectTemplatesHome;
    }

    @JsonProperty
    public String getTemplatesHome() {
        return templatesHome;
    }

    @JsonProperty
    public String getDownloadsHome() {
        return downloadsHome;
    }

    // --------------------------------------------------------------------------------------------


    // Config validation methods ------------------------------------------------------------------

    @ValidationMethod(message = "specified Android SDK Home is not valid")
    public boolean isAndroidSdkHomeValid() {
        return isDirValid(androidSdkHome);
    }

    @ValidationMethod(message = "specified Builds Home is not valid")
    public boolean isBuildsHomeValid() {
        return isDirValid(buildsHome);
    }

    @ValidationMethod(message = "specified Project Templates Home is not valid")
    public boolean isProjectTemplatesHomeValid() {
        return isDirValid(projectTemplatesHome);
    }

    @ValidationMethod(message = "specified Templates Home is not valid")
    public boolean isTemplatesHomeValid() {
        return isDirValid(templatesHome);
    }

    @ValidationMethod(message = "specified Downloads Home is not valid")
    public boolean isDownloadsHomeValid() {
        return isDirValid(downloadsHome);
    }

    @ValidationMethod(message = "specified Keytool path is not valid")
    public boolean isKeytoolPathValid() {
        File keytool = new File(keytoolPath);
        return !(!keytool.exists() || !keytool.canExecute());
    }

    @JsonIgnore
    private boolean isDirValid(String path) {
        File dir = new File(path);
        return !(!dir.exists() || !dir.isDirectory());
    }

    // --------------------------------------------------------------------------------------------
}
