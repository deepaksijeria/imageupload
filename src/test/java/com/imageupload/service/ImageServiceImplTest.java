package com.imageupload.service;

import com.imageupload.bean.Image;
import com.imageupload.bean.ImageJob;
import com.imageupload.enums.StatusEnum;
import com.imageupload.job.JobExecutor;
import com.imageupload.repository.ImageJobRepository;
import com.imageupload.repository.ImageRepository;
import com.imageupload.vo.request.UploadRequestVO;
import com.imageupload.vo.response.GetAllImageVo;
import com.imageupload.vo.response.JobResponseVO;
import com.imageupload.vo.response.UploadVO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageServiceImplTest {


    List<String> urls = null;

    ImageServiceImpl imageService = null;


    @Before
    public void setUp() {
        urls = new ArrayList<>(2);
        urls.add("https://farm3.staticflickr.com/2879/11234651086_681b3c2c00_b_d.jpg");
        urls.add("https://farm4.staticflickr.com/3790/11244125445_3c2f32cd83_k_d.jpg");

        ImageJobRepository imageJobRepository = new ImageJobRepository();
        ImageRepository imageRepository = new ImageRepository();
        imageService = new ImageServiceImpl(imageRepository, imageJobRepository,new JobExecutor());
    }


    @Test
    public void uploadSuccessTest() {

        UploadRequestVO uploadRequestVO = new UploadRequestVO();
        uploadRequestVO.setUrls(urls);
        UploadVO uploadVO = imageService.upload(uploadRequestVO);
        Assert.assertEquals(true, uploadVO != null && uploadVO.getJobId() != null);

    }

    @Test
    public void uploadFailureTest() {
        UploadRequestVO uploadRequestVO = new UploadRequestVO();
        UploadVO uploadVO = imageService.upload(uploadRequestVO);
        Assert.assertEquals(true, uploadVO == null);
    }

    @Test
    public void getJobInPendingTest(){

        String jobId = "wqerwerwerwer";
        Image image = new Image();
        image.setUrl(urls.get(0));
        image.setUploadedUrl("https://i.imgur.com/KndYwCT.jpg");
        image.setStatus(StatusEnum.COMPLETE.getValue());
        image.setId("dfgdfggg");

        Image image2 = new Image();
        image2.setUrl(urls.get(1));
        image2.setStatus(StatusEnum.PENDING.getValue());
        image2.setId("asdasddd");

        List<Image> images = new ArrayList<>(2);
        images.add(image);
        images.add(image2);

        ImageJob imageJob = new ImageJob();
        imageJob.setJobId(jobId);
        imageJob.setImages(images);
        imageJob.setCreated(new Date());
        imageJob.setStatus(StatusEnum.INPROGRESS.getValue());

        ImageJobRepository imageJobRepository = Mockito.mock(ImageJobRepository.class);
        Mockito.when(imageJobRepository.get(jobId)).thenReturn(imageJob);

        ImageServiceImpl imageServiceSpy = Mockito.spy(new ImageServiceImpl(new ImageRepository(),imageJobRepository,new JobExecutor()));
        JobResponseVO jobResponseVO = imageServiceSpy.getJob(jobId);

        Assert.assertEquals(jobResponseVO.getId(),jobId);
        Assert.assertEquals(jobResponseVO.getStatus(),StatusEnum.INPROGRESS.getValue());
        Assert.assertEquals(jobResponseVO.getUploaded().getPending().size() == 1,true);
        Assert.assertEquals(jobResponseVO.getUploaded().getComplete().size() == 1,true);

    }

    @Test
    public void getJobInCompleteTest(){

        String jobId = "wqerwerwerwer";
        Image image = new Image();
        image.setUrl(urls.get(0));
        image.setUploadedUrl("https://i.imgur.com/KndYwCT.jpg");
        image.setStatus(StatusEnum.COMPLETE.getValue());
        image.setId("dfgdfggg");

        Image image2 = new Image();
        image2.setUrl(urls.get(1));
        image2.setStatus(StatusEnum.COMPLETE.getValue());
        image2.setId("asdasddd");

        List<Image> images = new ArrayList<>(2);
        images.add(image);
        images.add(image2);

        ImageJob imageJob = new ImageJob();
        imageJob.setJobId(jobId);
        imageJob.setImages(images);
        imageJob.setCreated(new Date());
        imageJob.setFinished(new Date());
        imageJob.setStatus(StatusEnum.COMPLETE.getValue());

        ImageJobRepository imageJobRepository = Mockito.mock(ImageJobRepository.class);
        Mockito.when(imageJobRepository.get(jobId)).thenReturn(imageJob);

        ImageServiceImpl imageServiceSpy = Mockito.spy(new ImageServiceImpl(new ImageRepository(),imageJobRepository, new JobExecutor()));
        JobResponseVO jobResponseVO = imageServiceSpy.getJob(jobId);

        Assert.assertEquals(jobResponseVO.getId(),jobId);
        Assert.assertEquals(jobResponseVO.getStatus(),StatusEnum.COMPLETE.getValue());
        Assert.assertEquals(jobResponseVO.getUploaded().getPending().size() == 0,true);
        Assert.assertEquals(jobResponseVO.getUploaded().getComplete().size() == 2,true);
        Assert.assertEquals(jobResponseVO.getFinished() != null,true);

    }

    @Test
    public void getAllImagesTest(){

        List<String> completedUrls = new ArrayList<>(2);
        completedUrls.add("https://i.imgur.com/KndYwCT.jpg");
        completedUrls.add("https://i.imgur.com/CnfgwFD.jpg");

        ImageRepository imageRepository = Mockito.mock(ImageRepository.class);
        Mockito.when(imageRepository.getCompletedUrls()).thenReturn(completedUrls);

        ImageServiceImpl imageServiceSpy = Mockito.spy(new ImageServiceImpl(imageRepository,new ImageJobRepository(),new JobExecutor()));
        GetAllImageVo getAllImageVo = imageServiceSpy.getAllImages();

        Assert.assertEquals(getAllImageVo.getUploaded().size() == 2, true);

    }

}
