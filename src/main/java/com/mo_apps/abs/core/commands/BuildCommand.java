package com.mo_apps.abs.core.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class BuildCommand implements Command {
    private final Logger logger = LoggerFactory.getLogger(BuildCommand.class);

    private enum OS {
        TYPE_UNIX,
        TYPE_WINDOWS,
        TYPE_MAC,
        TYPE_OTHER
    }

    @Override
    public int execute(String workDir, String... command) throws IOException, InterruptedException {
        File dir = new File(workDir);
        if (!dir.exists()) return CODE_NO_WORKDIR;
        if (!dir.canRead()) return CODE_ACCESS_ERROR;

        String formattedWorkDir;
        if (workDir.endsWith(File.separator)) {
            formattedWorkDir = workDir;
        } else {
            formattedWorkDir = workDir + File.separator;
        }

        File gradlew = new File(formattedWorkDir + command[0]);
        if (!gradlew.exists()) return CODE_COMMAND_NOT_FOUND;

        String formattedCommand;
        // В ОС Unix, Linux, Mac для вызова сборки используется исполняемый текстовый файл.
        // Перед сборкой его необходимо обозначить как исполняемый.
        if (checkOsType() == OS.TYPE_UNIX || checkOsType() == OS.TYPE_MAC) {
            if (!gradlew.canExecute()) {

                logger.debug("Setting gradlew as executable in {}", gradlew.getAbsolutePath());
                gradlew.setExecutable(true);
            }

            formattedCommand = "./" + command[0];
            command[0] = formattedCommand;

            // На случай если что-то не будет работать.
            if (!gradlew.canExecute()) return CODE_ACCESS_ERROR;
        }

        logger.debug("Executing command {} in {}", command, workDir);

        ProcessExecutor executor = new ProcessExecutor(workDir, command);
        return executor.invoke();
    }

    /**
     * Проверка типа OS.
     *
     * @return Тип ОС из {@link BuildCommand.OS}
     */
    private OS checkOsType() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) return OS.TYPE_WINDOWS;
        if (osName.contains("nix") ||
                osName.contains("nux") ||
                osName.contains("aix")) return OS.TYPE_UNIX;
        if (osName.contains("mac")) return OS.TYPE_MAC;
        return OS.TYPE_OTHER;
    }

}
