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
 * Created by artem on 3/29/16.
 */
public class FoneGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(BackgroundGenerator.class);

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
    public static final HashMap<String, DownloadImage> TARGET_SIZES;

    static {
        TARGET_SIZES = new HashMap<>();
        TARGET_SIZES.put(DRAWABLE_SUBDIRS[0], new DownloadImage(120,220,"mdpi"));
        TARGET_SIZES.put(DRAWABLE_SUBDIRS[1], new DownloadImage(180,300,"hdpi"));
        TARGET_SIZES.put(DRAWABLE_SUBDIRS[2], new DownloadImage(240,440,"xhdpi"));
        TARGET_SIZES.put(DRAWABLE_SUBDIRS[3], new DownloadImage(360,660,"xxhdpi"));
        TARGET_SIZES.put(DRAWABLE_SUBDIRS[4], new DownloadImage(960,1410,"xxxhdpi"));
    }

    /**
     * Путь к директории проекта, в которой содержатся ресурсы приложения.
     */
    private String drawablesPath;

    /**
     * URL для скачивания приложения.
     */
    private String backgroundUrl;


    // Constructors -------------------------------------------------------------------------------

    /**
     * Конструктор класса.
     *
     * @param drawablesPath {@code drawablesPath}
     * @param backgroundUrl {@code backgroundUrl}
     */
    public FoneGenerator(String drawablesPath, String backgroundUrl) {
        this.drawablesPath = FormatUtils.formatPath(drawablesPath);
        this.backgroundUrl = backgroundUrl;
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

        for (String subdir : DRAWABLE_SUBDIRS) {
            String imageUrl = backgroundUrl + "android" + File.separator + TARGET_SIZES.get(subdir).getSize() + ".png";
            BufferedImage image = ImageIO.read(new URL(imageUrl));
            // Это тут на случай серьезного факапа с кодом
            if ((TARGET_SIZES.get(subdir) == null || TARGET_SIZES.get(subdir).getHeight() == 0) &&
                    (TARGET_SIZES.get(subdir) == null || TARGET_SIZES.get(subdir).getWidth() == 0)) {
                LOG.error("somebody fucked up HashMap");
                throw new IllegalStateException();
            }

            BufferedImage temp = Scalr.resize(
                    image,
                    Scalr.Method.ULTRA_QUALITY,
                    Scalr.Mode.FIT_EXACT,
                    TARGET_SIZES.get(subdir).getHeight(),
                    TARGET_SIZES.get(subdir).getWidth(),
                    Scalr.OP_ANTIALIAS);

            File drawable = new File(drawablesPath + subdir + File.separator +
                    "ic_activity_bg.png");
            ImageIO.write(temp, "png", drawable);
        }
        /*String url = backgroundUrl + "android/mdpi.png";
        BufferedImage image = ImageIO.read(new URL(url));
        Integer targetSize = TARGET_SIZES.get(DRAWABLE_SUBDIRS[0]);
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
        File drawable = new File(drawablesPath + DRAWABLE_SUBDIRS[0] + File.separator +
                "ic_activity_bg.png");
        ImageIO.write(temp, "png", drawable);


        url = backgroundUrl + "android/hdpi.png";
        image = ImageIO.read(new URL(url));
        targetSize = TARGET_SIZES.get(DRAWABLE_SUBDIRS[1]);
        if (targetSize == null || targetSize == 0) {
            LOG.error("somebody fucked up HashMap");
            throw new IllegalStateException();
        }
        temp = Scalr.resize(
                image,
                Scalr.Method.ULTRA_QUALITY,
                Scalr.Mode.FIT_EXACT,
                targetSize,
                targetSize,
                Scalr.OP_ANTIALIAS);
        drawable = new File(drawablesPath + DRAWABLE_SUBDIRS[1] + File.separator +
                "ic_activity_bg.png");
        ImageIO.write(temp, "png", drawable);


        url = backgroundUrl + "android/xhdpi.png";
        image = ImageIO.read(new URL(url));
        targetSize = TARGET_SIZES.get(DRAWABLE_SUBDIRS[2]);
        if (targetSize == null || targetSize == 0) {
            LOG.error("somebody fucked up HashMap");
            throw new IllegalStateException();
        }
        temp = Scalr.resize(
                image,
                Scalr.Method.ULTRA_QUALITY,
                Scalr.Mode.FIT_EXACT,
                targetSize,
                targetSize,
                Scalr.OP_ANTIALIAS);
        drawable = new File(drawablesPath + DRAWABLE_SUBDIRS[2] + File.separator +
                "ic_activity_bg.png");
        ImageIO.write(temp, "png", drawable);


        url = backgroundUrl + "android/xxhdpi.png";
        image = ImageIO.read(new URL(url));
        targetSize = TARGET_SIZES.get(DRAWABLE_SUBDIRS[3]);
        if (targetSize == null || targetSize == 0) {
            LOG.error("somebody fucked up HashMap");
            throw new IllegalStateException();
        }
        temp = Scalr.resize(
                image,
                Scalr.Method.ULTRA_QUALITY,
                Scalr.Mode.FIT_EXACT,
                targetSize,
                targetSize,
                Scalr.OP_ANTIALIAS);
        drawable = new File(drawablesPath + DRAWABLE_SUBDIRS[3] + File.separator +
                "ic_activity_bg.png");
        ImageIO.write(temp, "png", drawable);


        url = backgroundUrl + "android/xxxhdpi.png";
        image = ImageIO.read(new URL(url));
        targetSize = TARGET_SIZES.get(DRAWABLE_SUBDIRS[4]);
        if (targetSize == null || targetSize == 0) {
            LOG.error("somebody fucked up HashMap");
            throw new IllegalStateException();
        }
        temp = Scalr.resize(
                image,
                Scalr.Method.ULTRA_QUALITY,
                Scalr.Mode.FIT_EXACT,
                targetSize,
                targetSize,
                Scalr.OP_ANTIALIAS);
        drawable = new File(drawablesPath + DRAWABLE_SUBDIRS[4] + File.separator +
                "ic_activity_bg.png");
        ImageIO.write(temp, "png", drawable);*/
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
