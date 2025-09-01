package com.manh.ecommerce_java.services;

import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class MinioService {
    @Autowired
    MinioAdapter minioAdapter;

    public String uploadFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String newFileName = String.format("%s_%s", timeStamp, fileName);
        String fileUrl = "";
        try {
            // Check if the bucket exists
            boolean isExist = minioAdapter.bucketExists("public-bucket");
            if (!isExist) {
                minioAdapter.makeBucket("public-bucket");
            }
            // Upload the file
            String contentType = file.getContentType();
            minioAdapter.putObject("public-bucket", file.getInputStream(), newFileName, contentType);

            fileUrl = "http://localhost:9000/" + "public-bucket/" + newFileName;
//            fileUrl = "test1/" + newFileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileUrl;
    }
}