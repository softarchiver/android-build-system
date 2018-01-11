package com.mo_apps.abs.core.img;

import com.mo_apps.abs.core.util.FormatUtils;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

/**
 * Данный класс отвечает за генерацию иконок для приложения.
 */
public class IconGenerator {

    // Fields & constants -------------------------------------------------------------------------

    private static final Logger LOG = LoggerFactory.getLogger(IconGenerator.class);

    /**
     * Директории для графики, соответствующие разным плотностям экранов.
     */
    public static final String[] DRAWABLE_SUBDIRS = {
            "drawable-mdpi",
            "drawable-hdpi",
            "drawable-xhdpi",
            "drawable-xxhdpi",
            "drawable-xxxhdpi"
    };

    /**
     * Содержит размеры иконок, привязанные к плотности экрана (px).
     */
    public static final HashMap<String, Integer> TARGET_SIZES;

    static {
        TARGET_SIZES = new HashMap<>();
        TARGET_SIZES.put(DRAWABLE_SUBDIRS[0], 48);
        TARGET_SIZES.put(DRAWABLE_SUBDIRS[1], 72);
        TARGET_SIZES.put(DRAWABLE_SUBDIRS[2], 96);
        TARGET_SIZES.put(DRAWABLE_SUBDIRS[3], 144);
        TARGET_SIZES.put(DRAWABLE_SUBDIRS[4], 192);
    }

    /**
     * Путь к директории проекта, в которой содержатся ресурсы приложения.
     */
    private String drawablesPath;

    /**
     * URL для скачивания приложения.
     */
    private String imageUrl;


    // Constructors -------------------------------------------------------------------------------

    /**
     * Конструктор класса.
     *
     * @param drawablesPath {@code drawablesPath}
     * @param imageUrl      {@code imageUrl}
     */
    public IconGenerator(String drawablesPath, String imageUrl) {
        this.drawablesPath = FormatUtils.formatPath(drawablesPath);
        this.imageUrl = imageUrl;
    }


    // Methods ------------------------------------------------------------------------------------

    /**
     * Генерирует иконки приложения.
     *
     * @throws IOException           Возникает в случае ошибки при загрузке изображения
     *                               либо при записи на диск.
     * @throws IllegalStateException Возникает, если кто-то расхренячил код.
     */
    public void generateDrawables() throws IOException, IllegalStateException {
        initDirs();

        LOG.info("Downloading image from {}", imageUrl);
        BufferedImage image = ImageIO.read(new URL(imageUrl));

        for (String subdir : DRAWABLE_SUBDIRS) {
            Integer targetSize = TARGET_SIZES.get(subdir);
            // Это тут на случай серьезного факапа с кодом
            if (targetSize == null || targetSize == 0) {
                LOG.error("somebody fucked up HashMap");
                throw new IllegalStateException();
            }

            BufferedImage temp = Scalr.resize(
                    image,
                    Scalr.Method.ULTRA_QUALITY,
                    Scalr.Mode.FIT_EXACT,
                    targetSize,
                    targetSize,
                    Scalr.OP_ANTIALIAS);

            File drawable = new File(drawablesPath + subdir + File.separator +
                    "ic_launcher.png");
            ImageIO.write(temp, "png", drawable);
        }
    }

    private void initDirs() {
        for (String subdir : DRAWABLE_SUBDIRS) {
            File dir =
                    new File(FormatUtils.formatPath(drawablesPath) + subdir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

        }
    }
}
