package com.mo_apps.abs.core.util;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Содержит методы для работы с md5 суммами файлов.
 */
public class Md5Utils {

    /**
     * Проверяет контрольныую сумму файла на соответствие заданной.
     *
     * @param filePath    Путь к проверяемому файлу.
     * @param expectedMd5 Ожидаемая контрольная сумма
     * @return true, если контрольные суммы совпали, иначе - false.
     * @throws IOException при возникновении ошибки во время считывания файла.
     */
    public static boolean isFileValid(String filePath, String expectedMd5)
            throws IOException {
        String actualMd5 = calculateMd5(filePath);
        return actualMd5.contentEquals(expectedMd5);
    }

    /**
     * Рассчитывает контрольную сумму заданного файла.
     *
     * @param filePath Путь к файлу.
     * @return md5 сумму
     * @throws IOException при возникновении ошибки во время считывания файла.
     */
    public static String calculateMd5(String filePath) throws IOException {
        File file = new File(filePath);
        HashCode md5sum = Files.hash(file, Hashing.md5());
        return md5sum.toString();
    }
}
