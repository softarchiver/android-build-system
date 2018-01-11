package com.mo_apps.abs.core.img;

/**
 * Created by artem on 4/12/16.
 */
public class DownloadImage {

    private int height;
    private int width;
    private String size;

    public DownloadImage(int width, int height, String size) {
        this.height = height;
        this.width = width;
        this.size = size;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
