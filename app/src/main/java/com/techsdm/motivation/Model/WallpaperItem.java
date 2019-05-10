package com.techsdm.motivation.Model;

/**
 * Created by Monika Bhasin on 18-07-2018.
 */

public class WallpaperItem {

    public String imageLink;
    public int width;
    public int height;
    public String fullHDURL;
    public int download;
    public String username;
    public String userphoto;

    public WallpaperItem(String imageUrl, int width, int height, String fullHDURL, int download, String username, String userphoto) {
        this.imageLink=imageUrl;
        this.width=width;
        this.height=height;
        this.fullHDURL=fullHDURL;
        this.download=download;
        this.username=username;
        this.userphoto=userphoto;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public int getDownloads() {
        return download;
    }

    public void setDownloads(int download) {
        this.download = download;
    }

    public String getFullHDURL() {
        return fullHDURL;
    }

    public void setFullHDURL(String fullHDURL) {
        this.fullHDURL = fullHDURL;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public WallpaperItem() {
    }

    public WallpaperItem(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

}
