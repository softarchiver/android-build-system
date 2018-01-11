package com.mo_apps.abs.core.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Invoker {
    public static final int CODE_IO_EXCEPTION = -1100;
    public static final int CODE_INTERRUPTED_EXCEPTION = -1101;

    private static final Logger LOG = LoggerFactory.getLogger(Invoker.class);

    private final Command buildProjectCommand;
    private final Command generateKeyCommand;

    public Invoker(Command buildProjectCommand, Command generateKeyCommand) {
        this.buildProjectCommand = buildProjectCommand;
        this.generateKeyCommand = generateKeyCommand;
    }

    public int buildProject(String workDir, String command, String theme, boolean assembleRelease) {
        String params;
        if (assembleRelease) {
            params = String.format("assemble%sRelease", theme);
        } else {
            params = String.format("assemble%sDebug", theme);
        }

        int result;
        try {
            result = buildProjectCommand.execute(workDir, command, params);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e.fillInStackTrace());
            result = CODE_IO_EXCEPTION;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e.fillInStackTrace());
            result = CODE_INTERRUPTED_EXCEPTION;
        }
        return result;
    }

    public int generateKey(String workDir, String command, KeytoolParams params) {
        int result;
        try {
            String[] cmdArr = new String[]{
                    command,
                    KeytoolParams.GENKEY,
                    KeytoolParams.KEYSTORE, params.getStoreName(),
                    KeytoolParams.STOREPASS, new String(params.getStorePass()),
                    KeytoolParams.ALIAS, params.getKeyName(),
                    KeytoolParams.KEYPASS, new String(params.getStorePass()),
                    "-dname", "CN=Sergei Munovarov O=MOAPPS C=UA",
                    "-validity", "12000"
            };
            result = generateKeyCommand.execute(workDir, cmdArr);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e.fillInStackTrace());
            result = CODE_IO_EXCEPTION;
        } catch (InterruptedException e) {
            LOG.error(e.getMessage(), e.fillInStackTrace());
            result = CODE_INTERRUPTED_EXCEPTION;
        }
        return result;
    }

}
