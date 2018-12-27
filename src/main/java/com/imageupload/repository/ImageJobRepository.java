package com.imageupload.repository;

import com.imageupload.bean.Image;
import com.imageupload.bean.ImageJob;
import com.imageupload.enums.StatusEnum;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In Memory data structure to keep all the job and its related images
 */
@Component
public class ImageJobRepository {

    private Map<String,ImageJob> imageJobMap = new ConcurrentHashMap<>();

    /**
     * It create a imageJob and add the list of image to imageJob.
     * It also create a unique jobId
     * @param imageList List of Images
     * @return JobId String
     */
    public  String add(List<Image> imageList){
        ImageJob imageJob = new ImageJob();
        String jobId = UUID.randomUUID().toString();
        imageJob.setJobId(jobId);
        imageJob.setImages(imageList);
        imageJob.setStatus(StatusEnum.PENDING.getValue());
        imageJob.setCreated(new Date());
        imageJobMap.put(jobId,imageJob);
        return jobId;
    }

    /**
     * It update the status of job and also its finished time if status is complete
     * @param jobId String
     * @param status String
     * @param finishedDate Date
     */
    public void updateStatus(String jobId,String status,Date finishedDate){
        ImageJob imageJob = imageJobMap.get(jobId);
        if(imageJob != null){
            imageJob.setStatus(status);
            if(finishedDate != null){
                imageJob.setFinished(finishedDate);
            }
        }
    }

    /**
     * It update only the status of job
     * @param jobId String
     * @param status String
     */
    public void updateStatus(String jobId,String status){
        updateStatus(jobId,status,null);
    }

    /**
     * Get the job using the jobId
     * @param jobId String
     * @return ImageJob Object
     */
    public ImageJob get(String jobId){
        return imageJobMap.get(jobId);
    }

}
