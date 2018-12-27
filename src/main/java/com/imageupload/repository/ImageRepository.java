package com.imageupload.repository;

import com.imageupload.bean.Image;
import com.imageupload.enums.StatusEnum;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In Memory data structure to contain image details
 */
@Component
public class ImageRepository {

    private Map<String,Image> imageMap = new ConcurrentHashMap<>();

    private Map<String,Image> completedImageUploadMap = new ConcurrentHashMap<>();

    /**
     * It create the image object in pending state using the URL
     * @param url image url to be uploaded
     * @return Image Object
     */
    private Image add(String url){
        Image image = new Image();
        String imageId = UUID.randomUUID().toString();
        image.setId(imageId);
        image.setStatus(StatusEnum.PENDING.getValue());
        image.setUrl(url);
        imageMap.put(imageId,image);
        return image;
    }

    /**
     * It create a list of image object in pending state using List of URLS
     * @param urls list of image URLS to be uploaded
     * @return List of Image Object
     */
    public List<Image> add(List<String> urls){
        if(urls == null || urls.isEmpty()){
            return Collections.emptyList();
        }
        List<Image> images = new ArrayList<>(urls.size());
        for(String url : urls){
            images.add(add(url));
        }
        return images;
    }

    /**
     * Update the status of image along with uploaded url if image upload is complete
     * @param imageId String
     * @param status String
     * @param linkUrl uploaded URL String
     */
    public void update(String imageId,String status,String linkUrl){
        Image image = imageMap.get(imageId);
        if(image != null){
            image.setStatus(status);
            if(linkUrl != null){
                image.setUploadedUrl(linkUrl);
            }
            if(status.equals(StatusEnum.COMPLETE.getValue())){
                completedImageUploadMap.put(imageId,image);
            }
        }
    }

    /**
     * Update only the status of image using imageID
     * @param imageId String
     * @param status String
     */
    public void update(String imageId,String status){
        update(imageId,status,null);
    }

    /**
     * Get the list of urls of uploaded images
     * @return List of String URLS
     */
    public  List<String> getCompletedUrls(){
        return completedImageUploadMap.values().stream().map(image ->  image.getUploadedUrl()).collect(Collectors.toList());
    }



}
