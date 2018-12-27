package com.imageupload.service;

import com.imageupload.vo.response.UploadVO;
import com.imageupload.vo.request.UploadRequestVO;
import com.imageupload.vo.response.GetAllImageVo;
import com.imageupload.vo.response.JobResponseVO;

/**
 * It defines the service related to image
 */
public interface ImageService {

    /**
     * Upload image to file server using List of URls
     * @param uploadRequestVO UploadRequestVO contains list of URLS
     * @return UploadVO containing jobId
     */
    UploadVO upload(UploadRequestVO uploadRequestVO);

    /**
     * Get Job details using jobId
     * @param jobId String
     * @return JobResponseVO containing job details
     */
    JobResponseVO getJob(String jobId);

    /**
     * Get all successfully uploaded images
     * @return GetAllImageVO
     */
    GetAllImageVo getAllImages();
}
