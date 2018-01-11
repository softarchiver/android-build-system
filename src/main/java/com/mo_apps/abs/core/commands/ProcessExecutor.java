package com.mo_apps.abs.core.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * В данном классе реализован вызов команд ОС.
 */
public class ProcessExecutor {
    private static final Logger LOG = LoggerFactory.getLogger(ProcessExecutor.class);

    private final String workDir;
    private final String[] command;
    private Process process;

    /**
     * Конструктор класса.
     *
     * @param workDir директория, из которой будет производиться вызов команды
     * @param command команда
     */
    public ProcessExecutor(String workDir, String... command) {
        this.workDir = workDir;
        this.command = command;
    }

    /**
     * Вызывает определенную в конструкторе команду.
     *
     * @return Код завершения процесса.
     * @throws IOException          Не удалось вызвать команду по какой-либо причине.
     * @throws InterruptedException
     */
    public int invoke() throws IOException, InterruptedException {
        logCommand();

        process = Runtime.getRuntime().exec(
                command,
                null,
                new File(workDir).getAbsoluteFile()
        );
        int returnValue = process.waitFor();

        logOutput();
        return returnValue;
    }

    private void logCommand() {
        StringBuilder commandString = new StringBuilder();
        for (String str : command) {
            commandString.append(str).append(' ');
        }

        LOG.debug("Invoking command {}", commandString.toString());
    }

    private void logOutput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        LOG.debug("Command output: {}", stringBuilder.toString());
    }
}
