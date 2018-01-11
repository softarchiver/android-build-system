package com.mo_apps.abs.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * Created by artem on 4/13/16.
 */

@ApiModel
public class StatusResult {

    private String status;

    private boolean err;

    private String errorMessage;

    private int progress;

    private String ipaUrl;

    private String apkUrl;

    private int appId;

    private int userId;

    private String keystoreUrl;

    private String apkMd5;

    private String keystoreMd5;

    public StatusResult(String status, boolean err, String errorMessage, int progress, String ipaUrl, String apkUrl, int appId) {
        this.status = status;
        this.err = err;
        this.errorMessage = errorMessage;
        this.progress = progress;
        this.ipaUrl = ipaUrl;
        this.apkUrl = apkUrl;
        this.appId = appId;
    }

    public StatusResult(String status, int progress) {
        this.status = status;
        this.progress = progress;
        this.err = false;
        this.errorMessage = "no error";
        this.ipaUrl = "";
        this.apkUrl = "";
    }
    
    public StatusResult(int progress, String apkUrl, String keystoreUrl, int appId) {
        this.status = "END";
        this.progress = progress;
        this.err = false;
        this.errorMessage = "no error";
        this.ipaUrl = "";
        this.apkUrl = apkUrl;
        this.keystoreUrl = keystoreUrl;
        this.appId = appId;
    }

    public StatusResult(int progress, String apkUrl, String keystoreUrl, String keystoreMd5, int appId) {
        this.status = "END";
        this.progress = progress;
        this.err = false;
        this.errorMessage = "no error";
        this.ipaUrl = "";
        this.apkUrl = apkUrl;
        this.keystoreUrl = keystoreUrl;
        this.keystoreMd5 = keystoreMd5;
        this.appId = appId;
    }

    @JsonProperty
    public String getStatus() {
        return status;
    }

    @JsonProperty
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty
    public boolean isErr() {
        return err;
    }

    @JsonProperty
    public void setErr(boolean err) {
        this.err = err;
    }

    @JsonProperty
    public String getErrorMessage() {
        return errorMessage;
    }

    @JsonProperty
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonProperty
    public int getProgress() {
        return progress;
    }

    @JsonProperty
    public void setProgress(int progress) {
        this.progress = progress;
    }

    @JsonProperty
    public String getIpaUrl() {
        return ipaUrl;
    }

    @JsonProperty
    public void setIpaUrl(String ipaUrl) {
        this.ipaUrl = ipaUrl;
    }

    @JsonProperty
    public String getApkUrl() {
        return apkUrl;
    }

    @JsonProperty
    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    @JsonProperty
    public int getAppId() {
        return appId;
    }

    @JsonProperty
    public void setAppId(int appId) {
        this.appId = appId;
    }

    @JsonProperty
    public int getUserId() {
        return userId;
    }

    @JsonProperty
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JsonProperty
    public String getKeystoreUrl() {
        return keystoreUrl;
    }

    @JsonProperty
    public void setKeystoreUrl(String keystoreUrl) {
        this.keystoreUrl = keystoreUrl;
    }

    @JsonProperty
    public String getApkMd5() {
        return apkMd5;
    }

    @JsonProperty
    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

    @JsonProperty
    public String getKeystoreMd5() {
        return keystoreMd5;
    }

    @JsonProperty
    public void setKeystoreMd5(String keystoreMd5) {
        this.keystoreMd5 = keystoreMd5;
    }
}
