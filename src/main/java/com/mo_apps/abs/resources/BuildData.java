package com.mo_apps.abs.resources;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import io.dropwizard.validation.ValidationMethod;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Модель данных запроса на сборку приложения.
 */
@ApiModel
@SuppressWarnings("unused")
public class BuildData {

    // Fields -------------------------------------------------------------------------------------

    /**
     * ID пользователя, для которого собирается приложение.
     * Используется самим приложением для идентификации, а также в названии пакета.
     */
    @ApiModelProperty
    @NotNull
    private Integer userId;

    /**
     * ID собираемого приложения.
     * Используется приложением для идентификации, а также в названии пакета.
     */
    @ApiModelProperty
    @NotNull
    private Integer appId;

    /**
     * Название приложения. Может содержать кириллицу.
     */
    @ApiModelProperty
    @NotBlank
    private String appName;

    /**
     * Код версии приложения.
     */
    @ApiModelProperty
    @Min(1)
    @NotNull
    private Integer appVersionCode;

    /**
     * Тип приложения.
     * Определяет название директории, в которой хранятся шаблоны проектов для разных тем.
     */
    @ApiModelProperty(allowableValues = "estore, taxi, restaurant")
    @NotBlank
    private String type;

    /**
     * Тема приложения.
     * Определяет build flavor, который будет использоваться при сборке.
     */
    @NotBlank
    private String theme;

    /**
     * Ссылка для загрузки иконки приложения.
     */
    @URL
    @NotNull
    private String iconUrl;

    /**
     * Ссылка для загрузки фона приложения.
     */
    @URL
    @NotNull
    private String backgroundUrl;

    /**
     * Ссылка для загрузки хранилища ключей. Для новых приложений не задается.
     */
    @URL
    private String keystoreUrl;

    /**
     * Контрольная сумма хранилища ключей.
     */
    private String keystoreMd5;


    // Constructors -------------------------------------------------------------------------------

    public BuildData() {
        // Jackson
    }

    public BuildData(Integer userId,
                     Integer appId,
                     String appName,
                     Integer appVersionCode,
                     String type,
                     String theme,
                     String iconUrl,
                     String backgroundUrl) {
        this.userId = userId;
        this.appId = appId;
        this.appName = appName;
        this.appVersionCode = appVersionCode;
        this.type = type;
        this.theme = theme;
        this.iconUrl = iconUrl;
        this.backgroundUrl = backgroundUrl;
    }

    public BuildData(Integer userId,
                     Integer appId,
                     String appName,
                     Integer appVersionCode,
                     String type,
                     String theme,
                     String iconUrl,
                     String backgroundUrl,
                     String keystoreUrl,
                     String keystoreMd5) {
        this.userId = userId;
        this.appId = appId;
        this.appName = appName;
        this.appVersionCode = appVersionCode;
        this.type = type;
        this.theme = theme;
        this.iconUrl = iconUrl;
        this.backgroundUrl = backgroundUrl;
        this.keystoreUrl = keystoreUrl;
        this.keystoreMd5 = keystoreMd5;
    }

    public BuildData(Integer userId,
                     Integer appId,
                     String appName,
                     Integer appVersionCode,
                     String type,
                     String theme,
                     String iconUrl,
                     String backgroundUrl,
                     String keystoreUrl) {
        this.userId = userId;
        this.appId = appId;
        this.appName = appName;
        this.appVersionCode = appVersionCode;
        this.type = type;
        this.theme = theme;
        this.iconUrl = iconUrl;
        this.backgroundUrl = backgroundUrl;
        this.keystoreUrl = keystoreUrl;
    }


    // Getters & setters --------------------------------------------------------------------------

    @JsonProperty
    public Integer getUserId() {
        return userId;
    }

    @JsonProperty
    public Integer getAppId() {
        return appId;
    }

    @JsonProperty
    public String getAppName() {
        return appName;
    }

    @JsonProperty
    public Integer getAppVersionCode() {
        return appVersionCode;
    }

    @JsonProperty
    public String getType() {
        return type;
    }

    @JsonProperty
    public String getTheme() {
        return theme;
    }

    @JsonProperty
    public String getIconUrl() {
        return iconUrl;
    }

    @JsonProperty
    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    @JsonProperty
    public String getKeystoreUrl() {
        return keystoreUrl;
    }

    @JsonProperty
    public String getKeystoreMd5() {
        return keystoreMd5;
    }

    @JsonProperty
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @JsonProperty
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    @JsonProperty
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @JsonProperty
    public void setAppVersionCode(Integer appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    @JsonProperty
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty
    public void setTheme(String theme) {
        this.theme = theme;
    }

    @JsonProperty
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @JsonProperty
    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    @JsonProperty
    public void setKeystoreUrl(String keystoreUrl) {
        this.keystoreUrl = keystoreUrl;
    }

    @JsonProperty
    public void setKeystoreMd5(String keystoreMd5) {
        this.keystoreMd5 = keystoreMd5;
    }

    @ValidationMethod(message = "type is not valid")
    boolean isTypeValid() {
        return type != null &&
                ("estore".contentEquals(type) || "taxi".contentEquals(type) || "restaurant".contentEquals(type));
    }
}
