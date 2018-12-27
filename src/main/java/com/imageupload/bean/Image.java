package com.imageupload.bean;

/**
 * Image Entity
 */
public class Image {

    private String id;
    private String url;
    private String status;
    private String uploadedUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUploadedUrl() {
        return uploadedUrl;
    }

    public void setUploadedUrl(String uploadedUrl) {
        this.uploadedUrl = uploadedUrl;
    }
}
