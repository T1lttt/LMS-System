package com.lms.lmssystem.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class FileServiceImpl {

    private final MinioClient minioClient;

    @Value("${spring.minio.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile multipartFile) {

        try {

            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .bucket(bucket)
                            .object(multipartFile.getOriginalFilename())
                            .stream(multipartFile.getInputStream(), multipartFile.getSize(), -1)
                            .contentType(multipartFile.getContentType())
                            .build()
            );

            return "File  uploaded successfully";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Some errors on file uploads";

    }

    public ByteArrayResource downloadFile(String fileName){
        try {

            GetObjectArgs getObjectArgs = GetObjectArgs
                    .builder()
                    .bucket(bucket)
                    .object(fileName)
                    .build();

            InputStream stream = minioClient.getObject(getObjectArgs);
            byte [] byteArray = IOUtils.toByteArray(stream);
            stream.close();

            return new ByteArrayResource(byteArray);

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

}
