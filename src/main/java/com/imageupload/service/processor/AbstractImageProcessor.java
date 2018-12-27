package com.imageupload.service.processor;

import com.imageupload.vo.response.ImageUploadResponseVo;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class AbstractImageProcessor implements ImageProcessor {

    /**
     * Upload image to file server by converting to base64
     * @param url image url
     * @return True (if upload is successful) and false (if upload is unsuccessful)
     */
    @Override
    public ImageUploadResponseVo upload(String url) {
        String base64Str = null;
        try {
            base64Str = convertImageToBase64(url);
            if(base64Str == null){
               return ImageUploadResponseVo.builder().setSuccess(false).build();
            }
            ImageUploadResponseVo imageUploadResponseVo =  uploadToFileServer(base64Str);
            return imageUploadResponseVo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ImageUploadResponseVo.builder().setSuccess(false).build();
    }

    /**
     * Convert image to base64 format
     * @param url image URL
     * @return Base64 encoded String
     * @throws IOException
     */
    public String convertImageToBase64(String url) throws IOException{

        BufferedInputStream in = null;
        ByteArrayOutputStream buffer = null;
        String base64Str = null;
        try{
            in = new BufferedInputStream(new URL(url).openStream());
            buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            base64Str = new String(Base64.encodeBase64(buffer.toByteArray()), "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in != null){
                in.close();
            }
            if(buffer != null){
                buffer.close();
            }
        }
        return base64Str;
    }

    /**
     * It will upload the encoded base64 string to image server
     * @param encodedFile base64 String
     * @return ImageUploadResponseVo
     */
    public abstract ImageUploadResponseVo uploadToFileServer(String encodedFile);
}
