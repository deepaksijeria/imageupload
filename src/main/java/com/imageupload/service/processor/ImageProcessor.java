package com.imageupload.service.processor;

import com.imageupload.vo.response.ImageUploadResponseVo;

/**
 * processor related to images
 */
public interface ImageProcessor {

    ImageUploadResponseVo upload(String imageUrl);
}
