package com.example.blog.Controller;
import com.example.blog.Pojo.Result.Result;
import com.example.blog.Service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/oss")
public class OssController {

    @Autowired
    private OssService ossService;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        String picUrl =  ossService.uploadFile(file);
        return Result.success(picUrl);
    }
}
