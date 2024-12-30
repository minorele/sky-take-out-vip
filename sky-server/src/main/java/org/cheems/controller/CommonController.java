package org.cheems.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cheems.result.Result;
import org.cheems.utils.AliossUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private AliossUtil aliossUtil;

    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        log.info("文件上传：{}",file);

        try {
            // 原始文件名
            String originalFilename = file.getOriginalFilename();
            // 文件扩展名
            String extensionName = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 构造新文件名
            String objectName = UUID.randomUUID() + extensionName;

            String filepath = aliossUtil.upload(file.getBytes(),objectName);
            return Result.success(filepath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
