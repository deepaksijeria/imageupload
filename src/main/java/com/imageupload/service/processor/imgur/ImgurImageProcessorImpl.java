package com.imageupload.service.processor.imgur;

import com.imageupload.config.ApplicationContextHolder;
import com.imageupload.service.processor.AbstractImageProcessor;
import com.imageupload.vo.response.ImageUploadResponseVo;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**
 * This class responsible for calling imgur related APIs
 */
@Component
public class ImgurImageProcessorImpl extends AbstractImageProcessor {


    private String imgurUrl = "https://api.imgur.com/3/image";


    private String clientId = "cbc20bda2251c65";

    /**
     * It uploads the encoded image into the imgur library
     * @param encodedFile base64 encoded string
     * @return ImageUploadResponseVo
     */
    @Override
    public ImageUploadResponseVo uploadToFileServer(String encodedFile) {

        HttpEntity<MultiValueMap<String, Object>> requestEntity = getRequestEntity(encodedFile);

        RestTemplate restTemplate = getRestTemplate();
        ResponseEntity<ImgurResponse> response = restTemplate.postForEntity(imgurUrl, requestEntity, ImgurResponse.class);

        ImgurResponse imgurResponse = response.getBody();
        if(imgurResponse.isSuccess()){
            return ImageUploadResponseVo.builder().setLink(imgurResponse.getData().getLink()).setSuccess(imgurResponse.isSuccess()).build();
        }
        return ImageUploadResponseVo.builder().setSuccess(imgurResponse.isSuccess()).build();
    }

    /**
     * Gets the Rest template instance
     * @return RestTemplate
     */
    public RestTemplate getRestTemplate(){
        return (RestTemplate) ApplicationContextHolder.getBean("restTemplate");
    }

    /**
     * Gets Request Entity Object for Rest Template
     * @param encodedFile base64 encoded String
     * @return HttpEntity
     */
    public HttpEntity<MultiValueMap<String, Object>> getRequestEntity(String encodedFile){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization","Client-ID "+clientId);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", encodedFile);
        body.add("type","base64");

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);
        return requestEntity;
    }

}
