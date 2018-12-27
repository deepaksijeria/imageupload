package com.imageupload.service;

import com.imageupload.bean.Image;
import com.imageupload.bean.ImageJob;
import com.imageupload.job.JobExecutor;
import com.imageupload.repository.ImageJobRepository;
import com.imageupload.vo.response.UploadVO;
import com.imageupload.enums.StatusEnum;
import com.imageupload.repository.ImageRepository;
import com.imageupload.vo.request.UploadRequestVO;
import com.imageupload.vo.response.GetAllImageVo;
import com.imageupload.vo.response.ImageUploadStatusVO;
import com.imageupload.vo.response.JobResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implement services related to Image
 */
@Component
public class ImageServiceImpl implements ImageService{

    private ImageRepository imageRepository;

    private ImageJobRepository imageJobRepository;

    private JobExecutor jobExecutor;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,ImageJobRepository imageJobRepository,JobExecutor jobExecutor){
        this.imageJobRepository = imageJobRepository;
        this.imageRepository = imageRepository;
        this.jobExecutor = jobExecutor;
    }

    public ImageRepository getImageRepository() {
        return imageRepository;
    }

    public ImageJobRepository getImageJobRepository() {
        return imageJobRepository;
    }

    public JobExecutor getJobExecutor() {
        return jobExecutor;
    }

    /**
     * Upload image to file server by adding to queue.
     * It uploads the image asynchronously
     * @param uploadRequestVO UploadRequestVO contains list of URLS
     * @return UploadVo containing jobId
     */
    @Override
    public UploadVO upload(UploadRequestVO uploadRequestVO){
        List<String> urls = uploadRequestVO.getUrls();
        if(urls == null || urls.isEmpty()){
            return null;
        }

        List<Image> images = getImageRepository().add(uploadRequestVO.getUrls());
        String jobId = getImageJobRepository().add(images);
        getJobExecutor().add(jobId);
        UploadVO uploadVO = new UploadVO();
        uploadVO.setJobId(jobId);
        return uploadVO;
    }


    /**
     * Get the job details using the jobId
     * @param jobId String
     * @return JobResponseVO contain job details
     */
    @Override
    public JobResponseVO getJob(String jobId){
        ImageJob imageJob = getImageJobRepository().get(jobId);
        if(imageJob == null){
            return null;
        }
        List pending = new ArrayList();
        List complete = new ArrayList();
        List failed = new ArrayList();

        List<Image> images = imageJob.getImages();
        for(Image image : images){
            if(image.getStatus().equals(StatusEnum.PENDING.getValue())){
                pending.add(image.getUrl());
            }else if(image.getStatus().equals(StatusEnum.COMPLETE.getValue())){
                complete.add(image.getUploadedUrl());
            }else{
               failed.add(image.getUrl());
            }
        }
        ImageUploadStatusVO imageUploadStatusVO = new ImageUploadStatusVO();
        imageUploadStatusVO.setPending(pending);
        imageUploadStatusVO.setComplete(complete);
        imageUploadStatusVO.setFailed(failed);
        JobResponseVO jobResponseVO = new JobResponseVO();
        jobResponseVO.setUploaded(imageUploadStatusVO);
        jobResponseVO.setCreated(convertDateToFormat(imageJob.getCreated()));
        jobResponseVO.setId(imageJob.getJobId());
        jobResponseVO.setStatus(imageJob.getStatus());
        jobResponseVO.setFinished(convertDateToFormat(imageJob.getFinished()));
        return jobResponseVO;
    }

    /**
     * Get all the successfully uploaded images url
     * @return GetAllImageVo
     */
    @Override
    public GetAllImageVo getAllImages(){
        List<String> completedUrls =  getImageRepository().getCompletedUrls();
        GetAllImageVo getAllImageVo = new GetAllImageVo();
        getAllImageVo.setUploaded(completedUrls);
        return getAllImageVo;
    }

    /**
     * convert date to specific format
     * @param date Date
     * @return Formatted Date
     */
    private String convertDateToFormat(Date date){
        if(date == null){
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ").format(date.getTime());
    }

}
