package com.mo_apps.abs.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mo_apps.abs.AndroidBuildSystemConfiguration;
import com.mo_apps.abs.core.commands.BuildCommand;
import com.mo_apps.abs.core.commands.Invoker;
import com.mo_apps.abs.core.commands.KeytoolCommand;
import com.mo_apps.abs.core.freemarker.AppStringsTemplateModel;
import com.mo_apps.abs.core.freemarker.BuildTemplateModel;
import com.mo_apps.abs.core.freemarker.FreemarkerFactory;
import com.mo_apps.abs.core.img.BackgroundGenerator;
import com.mo_apps.abs.core.img.FoneGenerator;
import com.mo_apps.abs.core.img.IconGenerator;
import com.mo_apps.abs.core.commands.KeytoolParams;
import com.mo_apps.abs.core.util.FormatUtils;
import com.mo_apps.abs.core.util.Md5Utils;
import com.mo_apps.abs.core.util.PackageNameBuilder;
import com.mo_apps.abs.resources.BuildData;
import com.mo_apps.abs.resources.BuildReport;
import com.mo_apps.abs.resources.StatusResult;
import com.mo_apps.jwt_auth.core.JwtAuthDataHolder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Реализует процесс генерации и сборки Android-приложения.
 */
public class AppCreator implements Runnable {

    // Fields and constants -----------------------------------------------------------------------
    private static final Logger LOG = LoggerFactory.getLogger(AppCreator.class);
    private final AndroidBuildSystemConfiguration configuration;
    private BuildData buildData;

    private final HttpClient httpClient;
    private final Invoker invoker;

    private String projectDir;
    private String keystoreDir;

    // For build report --------------------------------------------------
    private boolean projectCreated = false;
    private boolean resourcesAdded = false;
    private boolean keystoreAcquired = false;
    private boolean apkBuilt = false;
    private String apkMd5;
    private String keystoreMd5;
    private int userId;
    private int appId;
    // -------------------------------------------------------------------

    private String packageName;

    private File generatedKeystore;

    // --------------------------------------------------------------------------------------------


    // Constructor --------------------------------------------------------------------------------

    public AppCreator(AndroidBuildSystemConfiguration configuration,
                      HttpClient httpClient,
                      BuildData buildData) {
        this.configuration = configuration;
        this.httpClient = httpClient;
        this.buildData = buildData;

        invoker = new Invoker(new BuildCommand(), new KeytoolCommand());

        packageName = PackageNameBuilder.fromParts(PackageNameBuilder.COMPANY_DOMAIN,
                buildData.getUserId(), buildData.getAppId());

        String buildsHome = FormatUtils.formatPath(configuration.getPaths().getBuildsHome());
        projectDir = FormatUtils.buildPathFromParts(
                buildsHome,
                String.format("app%d%d", buildData.getUserId(), buildData.getAppId())
        );

        userId = buildData.getUserId();
        appId = buildData.getAppId();
    }

    // --------------------------------------------------------------------------------------------

    /**
     * Запускает выполнение генерации и сборки проекта.
     */
    @Override
    public void run() {
        LOG.debug(
                "Build started for user {}, app ID: {}",
                buildData.getUserId(),
                buildData.getAppId()
        );

        try {
            cleanDlDir();
            cleanBuildDir();
        } catch (IOException e) {
            LOG.error("Could not clean directories left from previous builds", e);
            sendBuildReport();
            return;
        }

        try {
            copyToBuilds();
            projectCreated = true;
        } catch (IOException e) {
            LOG.error(
                    "could not copy project. user ID: {} app ID: {}",
                    buildData.getUserId(),
                    buildData.getAppId(),
                    e.fillInStackTrace()
            );
            projectCreated = false;
            cleanUp();
            sendBuildReport();
            return;
        }


        String keystoreUrl = buildData.getKeystoreUrl();
        if (keystoreUrl == null) {
            generatedKeystore = generateNewKeystore();
            if (!generatedKeystore.exists()) {
                LOG.error(
                        "could not generate keystore. user ID: {} app ID: {}",
                        buildData.getUserId(),
                        buildData.getAppId()
                );
                sendBuildReport();
                cleanUp();
                return;
            }
        } else {
            try {
                URL url = new URL(configuration.getCmaUrl() + String.format("keystores/app%d%d/keystore.keystore", userId, appId));
                FileUtils.copyURLToFile(
                        url,
                        new File(
                                FormatUtils.buildPathFromParts(projectDir, "app") +
                                        //FormatUtils.buildPathFromParts(projectDir) +
                                        KeytoolParams.DEFAULT_KEYSTORE_NAME
                        )
                );
            } catch (IOException e) {
                LOG.error(e.getMessage(), e.fillInStackTrace());
                sendBuildReport();
                cleanUp();
                return;
            }
        }
        keystoreAcquired = true;

        try {
            addProjectData();
            resourcesAdded = true;
        } catch (IOException | TemplateException | IllegalAccessException e) {
            LOG.error(
                    "could not add project data. user ID: {} app ID: {}. " + e.getMessage(),
                    buildData.getUserId(),
                    buildData.getAppId(),
                    e.fillInStackTrace()
            );
            resourcesAdded = false;
            sendBuildReport();
            cleanUp();
            return;
        }

        int buildResult = buildProject();
        if (buildResult != 0) {
            LOG.error(
                    "Error during application building. UserId: {}, appId: {}",
                    buildData.getUserId(),
                    buildData.getAppId()
            );
            apkBuilt = false;
            sendBuildReport();
            return;
        } else {
            apkBuilt = true;
            try {
                copyToDownloads();
            } catch (IOException e) {
                LOG.error(
                        "could not copy files to download folder. user ID: {} app ID: {}. Reason: {}",
                        buildData.getUserId(),
                        buildData.getAppId(),
                        e.getMessage(),
                        e.fillInStackTrace()
                );
                return;
            }
            sendBuildReport();
        }

        cleanUp();
    } // end run

    private void cleanBuildDir() throws IOException {
        String buildsHomePath = FormatUtils.formatPath(configuration.getPaths().getBuildsHome());
        String buildDirPath = buildsHomePath + String.format("app%d%d", buildData.getUserId(), buildData.getAppId());

        File buildDir = new File(buildDirPath);
        if (buildDir.exists()) {
            FileUtils.deleteDirectory(buildDir);
        }
    }

    private void cleanDlDir() throws IOException {
        String downloadsHome = FormatUtils.formatPath(configuration.getPaths().getDownloadsHome());
        String destDirPath = downloadsHome + String.format("app%d%d", buildData.getUserId(), buildData.getAppId());

        File destDir = new File(destDirPath);
        if (destDir.exists()) {
            FileUtils.deleteDirectory(destDir);
        }
    }


    private void copyToBuilds() throws IOException {
        String buildsHomePath = FormatUtils.formatPath(configuration.getPaths().getBuildsHome());
        String projectTemplatePath = FormatUtils.formatPath(configuration.getPaths().getProjectTemplatesHome())
                + buildData.getType();

        LOG.debug(
                "Copying {} to {}",
                projectTemplatePath,
                buildsHomePath
        );

        String buildDir = buildsHomePath + String.format("app%d%d", buildData.getUserId(), buildData.getAppId());

        FileUtils.copyDirectory(new File(projectTemplatePath), new File(buildDir));

        if (!new File(buildDir).exists()) {
            throw new IOException(String.format("build directory %s does not exist", buildDir));
        }
    }

    protected File generateNewKeystore() {
        String keystoreDir = FormatUtils.buildPathFromParts(projectDir, "app");

        KeytoolParams params = new KeytoolParams();
        String keytoolPath = configuration.getPaths().getKeytoolPath();

        LOG.debug(
                "Generating keystore for userId: {}, appId: {} in {}",
                buildData.getUserId(),
                buildData.getAppId(),
                keystoreDir
        );

        invoker.generateKey(keystoreDir, keytoolPath, params);
        return new File(FormatUtils.buildPathFromParts(keystoreDir, params.getStoreName()));
    }

    protected void addProjectData() throws IOException, IllegalAccessException, TemplateException {
        generateBuildScript();
        generateAppIcon();
        generateAppStrings();
    }

    protected int buildProject() {
        LOG.debug("Building in {}", projectDir);
        return invoker.buildProject(projectDir, "gradlew", buildData.getTheme(), true);
    }

    protected void copyToDownloads() throws IOException {
        String downloadsHome = FormatUtils.formatPath(configuration.getPaths().getDownloadsHome());
        String keystoresHome = FormatUtils.buildPathFromParts(downloadsHome,"keystores");
        String destDirPath = downloadsHome + String.format("app%d%d", buildData.getUserId(), buildData.getAppId());
        String destKeyDirPath = keystoresHome + String.format("app%d%d", buildData.getUserId(), buildData.getAppId());

        File destDir = new File(destDirPath);
        File destKeyDir = new File(destKeyDirPath);
        File destApkFile = new File(FormatUtils.formatPath(destDirPath) + "app-release.apk");

        File keystore = generatedKeystore;
        FileUtils.copyFileToDirectory(keystore, destDir);
        String newKeyPath = FormatUtils.buildPathFromParts(destDirPath, KeytoolParams.DEFAULT_KEYSTORE_NAME);
        File newKeystore = new File(newKeyPath);
        FileUtils.copyFileToDirectory(newKeystore, destKeyDir);

        apkMd5 = Md5Utils.calculateMd5(destApkFile.getAbsolutePath());
        keystoreMd5 = Md5Utils.calculateMd5(generatedKeystore.getAbsolutePath());

        LOG.debug("Apk md5 for {} {} is {}", userId, appId, apkMd5);
    }

    protected void cleanUp() {
        try {
            LOG.info("Deleting {}", projectDir);
            FileUtils.deleteDirectory(new File(projectDir));
        } catch (IOException e) {
            LOG.error("Deletion of dir {} was unsuccessful", projectDir, e.fillInStackTrace());
        }
    }

    protected void sendBuildReport() {
        BuildReport buildReport = new BuildReport(
                projectCreated,
                resourcesAdded,
                keystoreAcquired,
                apkBuilt,
                userId,
                appId,
                apkMd5,
                keystoreMd5,
                String.format(configuration.getServiceUrl() + "app%d%d/app-release.apk", userId, appId),
                String.format(configuration.getServiceUrl() + "keystores/app%d%d/keystore.keystore", userId, appId)
        );

        StatusResult api = new StatusResult(100,String.format(configuration.getServiceUrl() + "app%d%d/app-release.apk",userId, appId),
                String.format(configuration.getServiceUrl() + "keystores/app%d%d/keystore.keystore", userId, appId), appId);

        ObjectMapper mapper = new ObjectMapper();
        HttpPost request = new HttpPost(configuration.getCmaUrl());

        try {
            String json = mapper.writeValueAsString(api);
            StringEntity entity = new StringEntity(json, Charset.forName("UTF-8"));
            entity.setContentType("application/json");
            request.setEntity(entity);

            /*String token = JwtAuthDataHolder.getInstance().getToken();
            if (token != null) {
                request.setHeader("Authorization", String.format("Bearer %s", token));
            } else {
                LOG.warn("No JWT set. Not adding authorization header.");
            }*/

            httpClient.execute(request);

        } catch (JsonProcessingException e) {
            LOG.error("error during json processing: " + e.getMessage());
        } catch (ClientProtocolException e) {
            LOG.error("protocol exception occurred: " + e.getMessage());
        } catch (HttpHostConnectException e) {
            LOG.error("could not connect to remote address: " + e.getMessage());
        } catch (IOException e) {
            LOG.error("io exception occurred: " + e.getMessage());
        }
    }

    private void generateBuildScript() throws IOException, TemplateException {
        LOG.debug(
                "generating build.gradle in {}",
                FormatUtils.buildPathFromParts(projectDir, "app")
        );

        Configuration cfg = FreemarkerFactory.getInstance().getConfiguration();

        BuildTemplateModel buildTemplateModel = new BuildTemplateModel.Builder()
                .setVersionCode(buildData.getAppVersionCode())
                .setVersionName(FormatUtils.generateVersionName())
                .setKeystore(generatedKeystore.getName())
                .setStorepass(new String(KeytoolParams.DEFAULT_KEY_PASS))
                .setKey(KeytoolParams.DEFAULT_ALIAS_NAME)
                .setPackageName(packageName)
                .setDlPath(
                        FormatUtils.buildPathFromParts(
                                configuration.getPaths().getDownloadsHome(),
                                String.format("app%d%d", buildData.getUserId(), buildData.getAppId())
                        )
                )
                .build();

        String buildScriptPath = FormatUtils.buildPathFromParts(projectDir, "app");
        FileWriter writer = new FileWriter(new File(buildScriptPath + "build.gradle"));
        Template buildScriptTemplate = cfg.getTemplate(String.format("%s_build.gradle.ftl", buildData.getType()));
        buildScriptTemplate.process(buildTemplateModel, writer);
    }

    private void generateAppIcon() throws IOException {
        String drawablesPath = FormatUtils.buildPathFromParts(
                projectDir,
                "app",
                "src",
                "main",
                "res"
        );

        LOG.info("generating  app icons in {}", drawablesPath);

        IconGenerator iconGenerator = new IconGenerator(drawablesPath, buildData.getIconUrl());
        iconGenerator.generateDrawables();

        if (buildData.getBackgroundUrl().length()>0) {

            drawablesPath = FormatUtils.buildPathFromParts(
                    projectDir,
                    "app",
                    "src",
                    buildData.getTheme(),
                    "res"
            );

            String path = drawablesPath + "drawable-hdpi/ic_main_bg.png";
            File f = new File(path);
            f.delete();
            LOG.warn(path);
            path = drawablesPath + "drawable-mdpi/ic_main_bg.png";
            f = new File(path);
            f.delete();
            path = drawablesPath + "drawable-xhdpi/ic_main_bg.png";
            f = new File(path);
            f.delete();
            path = drawablesPath + "drawable-xxhdpi/ic_main_bg.png";
            f = new File(path);
            f.delete();

            path = drawablesPath + "drawable-hdpi/ic_activity_bg.png";
            f = new File(path);
            f.delete();
            LOG.warn(path);
            path = drawablesPath + "drawable-mdpi/ic_activity_bg.png";
            f = new File(path);
            f.delete();
            path = drawablesPath + "drawable-xhdpi/ic_activity_bg.png";
            f = new File(path);
            f.delete();
            path = drawablesPath + "drawable-xxhdpi/ic_activity_bg.png";
            f = new File(path);
            f.delete();

            drawablesPath = FormatUtils.buildPathFromParts(
                    projectDir,
                    "app",
                    "src",
                    "main",
                    "res"
            );
            BackgroundGenerator backgroundGenerator = new BackgroundGenerator(drawablesPath, buildData.getBackgroundUrl());
            backgroundGenerator.generateDrawables();
            FoneGenerator foneGenerator = new FoneGenerator(drawablesPath, buildData.getBackgroundUrl());
            foneGenerator.generateDrawables();
        }
    }

    private void generateAppStrings() throws IOException, TemplateException {
        Configuration cfg = FreemarkerFactory.getInstance().getConfiguration();

        AppStringsTemplateModel appStringsTemplateModel = new AppStringsTemplateModel(
                String.valueOf(buildData.getUserId()),
                String.valueOf(buildData.getAppId()),
                buildData.getAppName()
        );

        String stringsTemplatePath = FormatUtils.buildPathFromParts(
                projectDir,
                "app",
                "src",
                "main",
                "res",
                "values"
        );

        LOG.info("generating app strings in {}", stringsTemplatePath);

        FileWriter writer = new FileWriter(new File(stringsTemplatePath + "app_strings.xml"));
        Template appStringsTemplate = cfg.getTemplate("app_strings.xml.ftl");
        appStringsTemplate.process(appStringsTemplateModel, writer);
    }

}