package com.mo_apps.abs.core.util;

/**
 * Вспомогательный класс, отвечающий за построение имен пакетов для приложений.
 */
public class PackageNameBuilder {

    public static final String COMPANY_DOMAIN = "com.mo_apps";

    private static final char SEPARATOR_CHAR = '.';

    /**
     * Формирует имя пакета для приложения из заданных частей.
     *
     * @param prefix сайт компании
     * @param userId ИД пользователя
     * @param appId  ИД приложения
     * @return имя пакета приложения (e.g. com.mo_apps._1._1)
     */
    public static String fromParts(String prefix, int userId, int appId) {
        return prefix + SEPARATOR_CHAR + "app" + toPart(userId) + toPart(appId);
    }

    private static String toPart(int i) {
        // Название пакета не может содержать части,
        // которые начинаются с цифры. Для того, чтобы имя было валидным,
        // перед цифрой надо добавить знак "_".
        return String.valueOf(i);
    }
}
