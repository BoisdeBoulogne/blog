package com.example.blog.Service;
import com.aliyun.oss.OSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class OssService {

    @Autowired
    private OSS ossClient;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    public String uploadFile(MultipartFile file) {
        try {
            // 获取上传文件的输入流
            InputStream inputStream = file.getInputStream();

            // 生成文件名，使用UUID确保文件名的唯一性
            String fileName = UUID.randomUUID().toString().replace("-", "") + file.getOriginalFilename().replace(" ","");

            // 上传文件到 OSS
            ossClient.putObject(bucketName, fileName, inputStream);

            return "https://" + bucketName + "." + endpoint + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
