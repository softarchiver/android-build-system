package com.mo_apps.abs.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

/**
 * Модель сообщения с отчетом о сборке приложения.
 */
public class BuildReport {

    // Fields -------------------------------------------------------------------------------------

    /**
     * Индикатор успешного создания нового проекта.
     */
    @NotNull
    private boolean projectCreated;

    /**
     * Индикатор успешного добавления ресурсов в проект.
     */
    @NotNull
    private boolean resourcesAdded;

    /**
     * Индикатор успешного получения (загрузки или создания)
     * хранилища ключей.
     */
    @NotNull
    private boolean keystoreAcquired;

    /**
     * Индикатор успешной сборки приложения.
     */
    @NotNull
    private boolean apkBuilt;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer appId;

    /**
     * Контрольная сумма файла приложения.
     * Если приложение не было собрано, она будет равна null.
     */
    private String apkMd5;

    /**
     * Контрольная сумма хранилища ключей.
     * Если ключ не сгенерировался либо генерация не была необходима,
     * она будет равна null.
     */
    private String keystoreMd5;

    private String apkUrl;

    private String keystoreUrl;


    // Constructors -------------------------------------------------------------------------------

    public BuildReport() {
        // Конструктор по умолчанию. Необходим для корректной обработки Jackson.
    }

    public BuildReport(boolean projectCreated,
                       boolean resourcesAdded,
                       boolean keystoreAcquired,
                       boolean apkBuilt,
                       Integer userId,
                       Integer appId,
                       String keystoreMd5,
                       String apkMd5) {
        this.projectCreated = projectCreated;
        this.resourcesAdded = resourcesAdded;
        this.keystoreAcquired = keystoreAcquired;
        this.apkBuilt = apkBuilt;
        this.userId = userId;
        this.appId = appId;
        this.keystoreMd5 = keystoreMd5;
        this.apkMd5 = apkMd5;
    }

    public BuildReport(boolean projectCreated,
                       boolean resourcesAdded,
                       boolean keystoreAcquired,
                       boolean apkBuilt,
                       Integer userId,
                       Integer appId,
                       String apkMd5,
                       String keystoreMd5,
                       String apkUrl,
                       String keystoreUrl) {
        this.projectCreated = projectCreated;
        this.resourcesAdded = resourcesAdded;
        this.keystoreAcquired = keystoreAcquired;
        this.apkBuilt = apkBuilt;
        this.userId = userId;
        this.appId = appId;
        this.apkMd5 = apkMd5;
        this.keystoreMd5 = keystoreMd5;
        this.apkUrl = apkUrl;
        this.keystoreUrl = keystoreUrl;
    }

    // Getters & setters --------------------------------------------------------------------------

    @JsonProperty
    public boolean isProjectCreated() {
        return projectCreated;
    }

    @JsonProperty
    public boolean isResourcesAdded() {
        return resourcesAdded;
    }

    @JsonProperty
    public boolean isKeystoreAcquired() {
        return keystoreAcquired;
    }

    @JsonProperty
    public boolean isApkBuilt() {
        return apkBuilt;
    }

    @JsonProperty
    public String getKeystoreMd5() {
        return keystoreMd5;
    }

    @JsonProperty
    public String getApkMd5() {
        return apkMd5;
    }

    @JsonProperty
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty
    public Integer getAppId() {
        return appId;
    }

    @JsonProperty
    public String getApkUrl() {
        return apkUrl;
    }

    @JsonProperty
    public String getKeystoreUrl() {
        return keystoreUrl;
    }
}
