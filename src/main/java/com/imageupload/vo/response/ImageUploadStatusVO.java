package com.imageupload.vo.response;

import java.util.List;

public class ImageUploadStatusVO {

    private List<String> pending;
    private List<String> complete;
    private List<String> failed;

    public List<String> getPending() {
        return pending;
    }

    public void setPending(List<String> pending) {
        this.pending = pending;
    }

    public List<String> getComplete() {
        return complete;
    }

    public void setComplete(List<String> complete) {
        this.complete = complete;
    }

    public List<String> getFailed() {
        return failed;
    }

    public void setFailed(List<String> failed) {
        this.failed = failed;
    }
}
