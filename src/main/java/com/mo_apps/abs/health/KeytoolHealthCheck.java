package com.mo_apps.abs.health;

import com.codahale.metrics.health.HealthCheck;

import java.io.File;

/**
 * Реализация проверки инструмента keytool.
 */
public class KeytoolHealthCheck extends HealthCheck {
    private final String keytoolPath;

    public KeytoolHealthCheck(String keytoolPath) {
        this.keytoolPath = keytoolPath;
    }

    @Override
    protected Result check() throws Exception {
        File f = new File(keytoolPath);
        if (!f.exists()) {
            return Result.unhealthy("no such keytool file" + keytoolPath);
        } else if (!f.canExecute()) {
            return Result.unhealthy("don't have permission to execute keytool");
        } else {
            return Result.healthy();
        }
    }
}
