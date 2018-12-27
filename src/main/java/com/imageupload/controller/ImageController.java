package com.imageupload.controller;

import com.imageupload.service.ImageService;
import com.imageupload.vo.request.UploadRequestVO;
import com.imageupload.vo.response.GetAllImageVo;
import com.imageupload.vo.response.JobResponseVO;
import com.imageupload.vo.response.UploadVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Image controller is resposible for validation and interacting with imageservice
 */
@RestController()
public class ImageController {

    private ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService){
        this.imageService = imageService;
    }

    /**
     * It is responsible for uploading the image and return jobId
     * @param uploadRequestVO Contains list of image URls
     * @return UploadVO containing jobId
     */
    @RequestMapping(value = "v1/images/upload", method = RequestMethod.POST, consumes = "application/json",produces = "application/json")
    public UploadVO upload(@RequestBody UploadRequestVO uploadRequestVO){
        return imageService.upload(uploadRequestVO);
    }

    /**
     * It get the job details using jobId
     * @param jobId String
     * @return JobResponseVO contains details about all images
     */
    @RequestMapping(value = "v1/images/upload/{jobId}", method = RequestMethod.GET)
    public JobResponseVO getJob(@PathVariable String jobId){
        if(jobId != null && !jobId.isEmpty()){
            return imageService.getJob(jobId.trim());
        }
        return null;
    }

    /**
     * It gets all the successfully uploaded images
     * @return GetAllImageVO contains successfully uploaded URL
     */
    @RequestMapping(value = "v1/images", method = RequestMethod.GET)
    public GetAllImageVo getAllImages(){
        return imageService.getAllImages();
    }

}
