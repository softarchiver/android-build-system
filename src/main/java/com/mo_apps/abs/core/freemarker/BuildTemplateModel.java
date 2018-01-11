package com.mo_apps.abs.core.freemarker;

public class BuildTemplateModel {

    private Integer versionCode;

    private String versionName;

    private String keystore;

    private String storepass;

    private String key;

    private String packageName;

    private String dlPath;

    private BuildTemplateModel(Integer versionCode,
                               String versionName,
                               String keystore,
                               String storepass,
                               String key,
                               String packageName,
                               String dlPath) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.keystore = keystore;
        this.storepass = storepass;
        this.key = key;
        this.packageName = packageName;
        this.dlPath = dlPath;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getKeystore() {
        return keystore;
    }

    public String getStorepass() {
        return storepass;
    }

    public String getKey() {
        return key;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getDlPath() {
        return dlPath;
    }

    public static class Builder {

        private Integer versionCode;
        private String versionName;
        private String keystore;
        private String storepass;
        private String key;
        private String packageName;
        private String dlPath;

        public Builder setVersionCode(Integer versionCode) {
            this.versionCode = versionCode;
            return this;
        }

        public Builder setVersionName(String versionName) {
            this.versionName = versionName;
            return this;
        }

        public Builder setKeystore(String keystore) {
            this.keystore = keystore;
            return this;
        }

        public Builder setStorepass(String storepass) {
            this.storepass = storepass;
            return this;
        }

        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        public Builder setPackageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder setDlPath(String dlPath) {
            this.dlPath = dlPath;
            return this;
        }

        public BuildTemplateModel build() {
            return new BuildTemplateModel(
                    versionCode,
                    versionName,
                    keystore,
                    storepass,
                    key,
                    packageName,
                    dlPath
            );
        }
    }

}
