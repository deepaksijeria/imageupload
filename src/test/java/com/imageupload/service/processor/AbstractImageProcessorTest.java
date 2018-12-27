package com.imageupload.service.processor;

import com.imageupload.vo.response.ImageUploadResponseVo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AbstractImageProcessorTest {


    List<String> urls = null;


    @Before
    public void setUp(){
        urls = new ArrayList<>(2);
        urls.add("https://farm3.staticflickr.com/2879/11234651086_681b3c2c00_b_d.jpg");
        urls.add("https://farm4.staticflickr.com/3790/11244125445_3c2f32cd83_k_d.jpg");
    }

    @Test
    public void uploadSuccessTest() throws IOException {
        AbstractImageProcessor abstractImageProcessor = Mockito.mock(AbstractImageProcessor.class);
        Mockito.when(abstractImageProcessor.uploadToFileServer("dsadasdasdasdasdasd")).thenReturn(ImageUploadResponseVo.builder().setLink("https://i.imgur.com/KndYwCT.jpg").setSuccess(true).build());
        Mockito.when(abstractImageProcessor.convertImageToBase64(urls.get(0))).thenReturn("dsadasdasdasdasdasd");
        Mockito.doCallRealMethod().when(abstractImageProcessor).upload(urls.get(0));
        Assert.assertEquals(true,abstractImageProcessor.upload(urls.get(0)).isSuccess());
        Assert.assertEquals("https://i.imgur.com/KndYwCT.jpg",abstractImageProcessor.upload(urls.get(0)).getLink());
    }

    @Test
    public void uploadFailedTest() throws IOException {
        AbstractImageProcessor abstractImageProcessor = Mockito.mock(AbstractImageProcessor.class);
        Mockito.when(abstractImageProcessor.uploadToFileServer("dsadasdasdasdasdasd")).thenReturn(ImageUploadResponseVo.builder().setSuccess(false).build());
        Mockito.when(abstractImageProcessor.convertImageToBase64(urls.get(0))).thenReturn("dsadasdasdasdasdasd");
        Mockito.doCallRealMethod().when(abstractImageProcessor).upload(urls.get(0));
        Assert.assertEquals(false,abstractImageProcessor.upload(urls.get(0)).isSuccess());
        Assert.assertEquals(null,abstractImageProcessor.upload(urls.get(0)).getLink());
    }


}
