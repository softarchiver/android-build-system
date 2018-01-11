package com.mo_apps.abs.core.commands;

import java.io.IOException;

/**
 * Данный интерфейс определяет контракт для кажого класса, реализующего вызов команды в ОС.
 */
public interface Command {
    /**
     * Индикатор отсутствия заданной директории.
     */
    int CODE_NO_WORKDIR = -1000;

    /**
     * Индикатор ошибки доступа (по чтеню/записи/выполнению).
     */
    int CODE_ACCESS_ERROR = -1001;

    /**
     * Индикатор невозможности выполнения заданной коменды.
     */
    int CODE_COMMAND_NOT_FOUND = -1002;

    /**
     * Выолняет команду с заданными параметрами.
     *
     * @param workDir Рабочая директория. Может использоваться при необходимости вызова команды,
     *                для которой необходимо задавать относительный путь.
     * @param command Команда, которую необходимо выполнить. Может содержать полнуый пусть к команде
     *                либо относительный (при корректном определении {@code workDir}).
     * @return Код завершения, возвращаемый командой.
     * @throws IOException
     * @throws InterruptedException
     */
    public int execute(String workDir, String... command)
            throws IOException, InterruptedException;
}
