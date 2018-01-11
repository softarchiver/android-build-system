package com.mo_apps.abs.core.commands;

import java.io.File;
import java.io.IOException;

public class KeytoolCommand implements Command {

    @Override
    public int execute(String workDir, String... command) throws IOException, InterruptedException {
        File dir = new File(workDir);
        if (!dir.exists()) return CODE_NO_WORKDIR;
        if (!dir.canRead() || !dir.canWrite()) return CODE_ACCESS_ERROR;

        File bin = new File(command[0]);
        if (!bin.exists()) return CODE_COMMAND_NOT_FOUND;
        if (!bin.canExecute()) return CODE_ACCESS_ERROR;

        ProcessExecutor executor = new ProcessExecutor(workDir, command);
        return executor.invoke();
    }

}
