package com.mo_apps.abs.health;

import com.codahale.metrics.health.HealthCheck;
import com.mo_apps.abs.AndroidBuildSystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class DirsHealthCheck extends HealthCheck {
    private final Logger logger = LoggerFactory.getLogger(DirsHealthCheck.class);
    private List<String> dirs;

    public DirsHealthCheck(AndroidBuildSystemConfiguration configuration) {
        dirs = new LinkedList<>();

        dirs.add(configuration.getPaths().getAndroidSdkHome());
        dirs.add(configuration.getPaths().getDownloadsHome());
        dirs.add(configuration.getPaths().getBuildsHome());
        dirs.add(configuration.getPaths().getTemplatesHome());
    }

    @Override
    protected Result check() throws Exception {
        for (String dir : dirs) {
            File currentDir = new File(dir);

            String msg;
            if (!currentDir.isDirectory()) {
                msg = String.format("specified path %s is not a directory",
                        currentDir.getAbsolutePath());
                logger.error("error during health check: " + msg);
                return Result.unhealthy(msg);
            }
            if (!currentDir.exists()) {
                msg = String.format("specified path %s does not exist",
                        currentDir.getAbsolutePath());
                logger.error("error during health check: " + msg);
                return Result.unhealthy(msg);
            }
            if (!currentDir.canRead() || !currentDir.canWrite()) {
                msg = String.format("don't have read/write permissions for %s",
                        currentDir.getAbsolutePath());
                logger.error("error during health check: " + msg);
                return Result.unhealthy(msg);
            }
        }

        return Result.healthy();
    }
}
