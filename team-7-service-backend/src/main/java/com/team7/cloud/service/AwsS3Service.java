package com.team7.cloud.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Service
@Slf4j
@RequiredArgsConstructor
public class AwsS3Service {

    private final AmazonS3 amazonS3;

    @Value("team7-image-bucket")
    private String bucketName;

    public String serviceStatus(){
        System.out.println(amazonS3.getS3AccountOwner().getDisplayName());
        return amazonS3.getS3AccountOwner().getDisplayName();
    }

    public String uploadFile(String keyName, MultipartFile file) throws IOException{
        PutObjectResult putObjectResult = amazonS3.putObject(bucketName, keyName, file.getInputStream(),null);
        return URLDecoder.decode(amazonS3.getUrl(bucketName, keyName).toString(), StandardCharsets.UTF_8);
    }

    public void delete (String keyName){

        try{
            amazonS3.deleteObject(bucketName, keyName);
        }
        catch(AmazonServiceException e){
            log.error(e.toString());
        }

    }

    public S3Object getFile(String keyName){

        return amazonS3.getObject(bucketName, keyName);
    }

    public String getFilePresignedURL(String keyName){
        URL url = amazonS3.generatePresignedUrl(bucketName, keyName,new Date(System.currentTimeMillis() + 1000*100L));
        return URLDecoder.decode(url.toString(), StandardCharsets.UTF_8);
    }

}
