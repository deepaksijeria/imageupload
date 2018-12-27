package com.imageupload.enums;

/**
 * It contain the status related to job and image
 */
public enum StatusEnum {

    PENDING("pending"),INPROGRESS("in-progress"),COMPLETE("complete"),FAILED("failed");

    private StatusEnum(String status){
        this.value = status;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
