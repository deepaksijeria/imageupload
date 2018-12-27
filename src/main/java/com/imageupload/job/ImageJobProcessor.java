package com.imageupload.job;

import com.imageupload.bean.Image;
import com.imageupload.bean.ImageJob;
import com.imageupload.config.ApplicationContextHolder;
import com.imageupload.enums.StatusEnum;
import com.imageupload.repository.ImageJobRepository;
import com.imageupload.repository.ImageRepository;
import com.imageupload.service.processor.ImageProcessor;
import com.imageupload.vo.response.ImageUploadResponseVo;

import java.util.Date;
import java.util.concurrent.*;

/**
 * Its responsible for uploading images in a job to file server concurrently
 */
public class ImageJobProcessor implements Runnable {

    private String jobId;


    public ImageJobProcessor(String jobId){
        this.jobId = jobId;
    }

    /**
     * It get the job using jobId and concurrently upload the images to image server.
     * Once the upload is completed it will update the status of job as well as all images inside the job.
     */
    @Override
    public void run() {

        ExecutorService executor = Executors.newFixedThreadPool(3);
        ImageJob job = getImageJobRepository().get(jobId);
        getImageJobRepository().updateStatus(job.getJobId(), StatusEnum.INPROGRESS.getValue());
        try{
            CountDownLatch latch = new CountDownLatch(job.getImages().size());
            for(Image image : job.getImages()){
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("upload image ::"+image.getUrl());
                        ImageUploadResponseVo responseVo = getProcessor().upload(image.getUrl());
                        System.out.println("image uploaded ::"+responseVo.getLink());
                        if(responseVo.isSuccess()){
                            getImageRepository().update(image.getId(),StatusEnum.COMPLETE.getValue(),responseVo.getLink());
                        }else{
                            getImageRepository().update(image.getId(),StatusEnum.FAILED.getValue());
                        }
                        latch.countDown();
                    }
                });
            }
            latch.await(60L, TimeUnit.SECONDS);
            getImageJobRepository().updateStatus(job.getJobId(),StatusEnum.COMPLETE.getValue(),new Date());

        } catch (InterruptedException e) {
            getImageJobRepository().updateStatus(job.getJobId(),StatusEnum.FAILED.getValue());
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public ImageProcessor getProcessor(){
        return (ImageProcessor) ApplicationContextHolder.getBean("imgurImageProcessorImpl");
    }

    public ImageRepository getImageRepository(){
        return (ImageRepository) ApplicationContextHolder.getBean("imageRepository");
    }

    public ImageJobRepository getImageJobRepository(){
        return (ImageJobRepository)ApplicationContextHolder.getBean("imageJobRepository");
    }
}
