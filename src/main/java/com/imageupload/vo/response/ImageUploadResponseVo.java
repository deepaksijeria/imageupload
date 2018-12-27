package com.imageupload.vo.response;

public class ImageUploadResponseVo {

    private String link;
    private boolean success;

    private ImageUploadResponseVo(Builder builder){
        this.link = builder.link;
        this.success = builder.success;
    }

    public static Builder builder(){
        return new Builder();
    }

    public String getLink() {
        return link;
    }

    public boolean isSuccess() {
        return success;
    }

    public static class Builder{

       private String link;
       private boolean success;

       public ImageUploadResponseVo build(){
           return new ImageUploadResponseVo(this);
       }

       public Builder setLink(String link){
           this.link = link;
           return this;
       }

       public Builder setSuccess(boolean success){
           this.success = success;
           return this;
       }

    }
}
