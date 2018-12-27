package com.imageupload.service.processor.imgur;

import com.imageupload.vo.response.ImageUploadResponseVo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


//@RunWith(MockitoJUnitRunner.class)
public class ImgurImageProcessorTest {

    List<String> urls = null;

    private ImgurImageProcessorImpl imgurImageProcessor = new ImgurImageProcessorImpl();


    @Before
    public void setUp(){
        urls = new ArrayList<>(2);
        urls.add("https://farm3.staticflickr.com/2879/11234651086_681b3c2c00_b_d.jpg");
        urls.add("https://farm4.staticflickr.com/3790/11244125445_3c2f32cd83_k_d.jpg");

        imgurImageProcessor = new ImgurImageProcessorImpl();
    }

    @Test
    public void testImageUploadSuccess() throws Exception {

        ImgurData data = new ImgurData();
        data.setId("tyyasdasd");
        data.setLink("https://i.imgur.com/KndYwCT.jpg");

        ImgurResponse imgurResponse = new ImgurResponse();
        imgurResponse.setData(data);
        imgurResponse.setStatus(200);
        imgurResponse.setSuccess(true);

        String encodedFile = "asdasdasdasddsasdasdasdasd";

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = imgurImageProcessor.getRequestEntity(encodedFile);

        ResponseEntity<ImgurResponse> responseEntity = new ResponseEntity<ImgurResponse>(imgurResponse,HttpStatus.OK);

        RestTemplate restTemplateSpy = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplateSpy.postForEntity("https://api.imgur.com/3/image",requestEntity,ImgurResponse.class))
                .thenReturn(new ResponseEntity<ImgurResponse>(imgurResponse,HttpStatus.OK)).thenReturn(responseEntity);
        ImgurImageProcessorImpl imgurSpy = Mockito.spy(new ImgurImageProcessorImpl());
        Mockito.doReturn(requestEntity).when(imgurSpy).getRequestEntity(encodedFile);
        Mockito.doReturn(restTemplateSpy).when(imgurSpy).getRestTemplate();
        ImageUploadResponseVo responseVo = imgurSpy.uploadToFileServer(encodedFile);
        Assert.assertEquals(true,responseVo.isSuccess());


    }

    @Test
    public void testImageUploadFailed() throws Exception {

        ImgurData data = new ImgurData();

        ImgurResponse imgurResponse = new ImgurResponse();
        imgurResponse.setData(data);
        imgurResponse.setStatus(400);
        imgurResponse.setSuccess(false);

        String encodedFile = "asdasdasdasddsasdasdasdasd";

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = imgurImageProcessor.getRequestEntity(encodedFile);

        ResponseEntity<ImgurResponse> responseEntity = new ResponseEntity<ImgurResponse>(imgurResponse,HttpStatus.OK);

        RestTemplate restTemplateSpy = Mockito.mock(RestTemplate.class);
        Mockito.when(restTemplateSpy.postForEntity("https://api.imgur.com/3/image",requestEntity,ImgurResponse.class))
                .thenReturn(new ResponseEntity<ImgurResponse>(imgurResponse,HttpStatus.OK)).thenReturn(responseEntity);
        ImgurImageProcessorImpl imgurSpy = Mockito.spy(new ImgurImageProcessorImpl());
        Mockito.doReturn(requestEntity).when(imgurSpy).getRequestEntity(encodedFile);
        Mockito.doReturn(restTemplateSpy).when(imgurSpy).getRestTemplate();
        ImageUploadResponseVo responseVo = imgurSpy.uploadToFileServer(encodedFile);
        Assert.assertEquals(false,responseVo.isSuccess());


    }
}
