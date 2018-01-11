package com.mo_apps.abs.core.freemarker;

public class AppStringsTemplateModel {

    private String userId;
    private String appId;
    private String appName;

    public AppStringsTemplateModel(String userId,
                                   String appId,
                                   String appName) {
        this.userId = userId;
        this.appId = appId;
        this.appName = appName;
    }

    public String getUserId() {
        return userId;
    }

    public String getAppId() {
        return appId;
    }

    public String getAppName() {
        return appName;
    }
}
