package com.mo_apps.abs.core.commands;

/**
 * Данный класс содержит параметры для работы с утилитой keytool.
 * <p>
 * Большая часть полей данного класса является статическими. Поля, которые можно задать во время инстанцирования класса,
 * содержат данные, необходимы для получения доступа к конкретному хранилищу ключей и конкретной записи в нем.
 */
public class KeytoolParams {
    public static final String DEFAULT_KEYSTORE_NAME = "keystore.keystore";
    public static final String DEFAULT_ALIAS_NAME = "app";
    public static final char[] DEFAULT_KEY_PASS = {'a', 'b', 'c', '1', '2', '3'};

    /**
     * Ключ, который "говорит" keytool`у о том, что необходимо сгенерировть ключ.
     * Должен идти первым в списке параметров.
     */
    public static final String GENKEY = "-genkey";

    /**
     * Ключ, определяющий путь к хранилищу ключей (обычно это файл с расширением .keystore).
     */
    public static final String KEYSTORE = "-keystore";

    /**
     * Ключ, опредлеяющий пароль от хранилища ключей.
     */
    public static final String STOREPASS = "-storepass";

    /**
     * Ключ, определяющий имя записи (alias) в хранилище ключей.
     */
    public static final String ALIAS = "-alias";

    /**
     * Ключ, определяющий пароль от записи в хранилище ключей.
     * В нашем случае он будет совпадать с паролем от самого хранилища ключей.
     */
    public static final String KEYPASS = "-keypass";

    /**
     * Имя хранилища ключей.
     */
    private String storeName;

    /**
     * Пароль от хранилища ключей.
     * <p>
     * В данном случае он определен как char[] из-за того,
     * что поля типа String могут остаться в памяти JVM после завершения работы,
     * и к ним имеется возможность получить доступ.
     */
    private char[] storePass;

    /**
     * Имя записи в файле хранилища ключей.
     */
    private String keyName;


    // Constructors -------------------------------------------------------------------------------

    /**
     * Инстанцирует объект со значениями приватных полей по умолчанию.
     */
    public KeytoolParams() {
        this.storeName = DEFAULT_KEYSTORE_NAME;
        this.storePass = DEFAULT_KEY_PASS;
        this.keyName = DEFAULT_ALIAS_NAME;
    }

    /**
     * Инстанцирует объект с заданными параметрами.
     *
     * @param storeName Название хранилища ключей
     * @param storePass Пароль хранилища ключей
     * @param keyName   Название записи в хранилище ключей.
     */
    public KeytoolParams(String storeName, char[] storePass, String keyName) {
        if (storeName.endsWith(".keystore")) {
            this.storeName = storeName;
        } else {
            this.storeName = storeName + ".keystore";
        }
        this.storePass = storePass;
        this.keyName = keyName;
    }

    // --------------------------------------------------------------------------------------------


    // Getters and Setters ------------------------------------------------------------------------

    public String getStoreName() {
        return storeName;
    }

    public char[] getStorePass() {
        return storePass;
    }

    public String getKeyName() {
        return keyName;
    }

    // --------------------------------------------------------------------------------------------
}
