package com.imageupload.vo.response;

public class JobResponseVO {

    private String id;
    private String created;
    private String finished;
    private String status;
    private ImageUploadStatusVO uploaded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ImageUploadStatusVO getUploaded() {
        return uploaded;
    }

    public void setUploaded(ImageUploadStatusVO uploaded) {
        this.uploaded = uploaded;
    }
}
