package com.mo_apps.abs.core.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;

public class FormatUtils {

    public static String formatPath(String path) {
        String formattedPath;
        if (path.endsWith(File.separator)) {
            formattedPath = path;
        } else {
            formattedPath = path + File.separator;
        }
        return formattedPath;
    }

    /**
     * Assembles a path using list of given dir names.
     * @param parts List of directory name in hierarchial order
     * @return formatted path
     */
    public static String buildPathFromParts(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            sb.append(formatPath(part));
        }
        return sb.toString();
    }

    /**
     * Generates a version string for an app.
     * @return version in yyyyMMdd format (e.g. 20151010)
     */
    public static String generateVersionName() {
        DateTime dateTime = new DateTime();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
        return formatter.print(dateTime.getMillis());
    }
}
