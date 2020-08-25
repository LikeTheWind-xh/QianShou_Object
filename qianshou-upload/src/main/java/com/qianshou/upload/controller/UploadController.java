package com.qianshou.upload.controller;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qianshou.upload.cofig.aliOssConfig;
import com.qianshou.upload.service.FaceService;
import com.qianshou.upload.service.VodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.format.datetime.joda.JodaDateTimeFormatAnnotationFormatterFactory;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utlis.Result;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Api(value = "图片功能",description = "图片处理")
@RestController
@Slf4j
@RequestMapping("/images/")
@EnableConfigurationProperties(aliOssConfig.class)
public class UploadController {

    @Autowired
    private aliOssConfig  aliOssConfig;

    @Autowired
    private FaceService faceService;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private VodService vodService;

    private static final List<String> image_type= Arrays.asList("image/jpeg","image/jpg","image/png");

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file){
        log.info("file:{}",file.getName());
        if(!image_type.contains(file.getContentType())){
            return Result.error().message("参类型不合法");
        }
        try {
            boolean isPortrait = faceService.checkIsPortrait(file.getBytes());
            if(!isPortrait){
                return Result.error().message("请上传人脸图像");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        OSS oss = new OSSClientBuilder().build(aliOssConfig.getEndpoint(),aliOssConfig.getAccessKeyId(),aliOssConfig.getAccessKeySecret());
        String filename = "qianshou/img/user/"+new DateTime().toString("yyyy/MM/dd")+"/"+ UUID.randomUUID().toString().replaceAll("-","") +file.getOriginalFilename();
        try {
            oss.putObject(aliOssConfig.getBucketName(),filename,file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error().message("上传时发生了异常，请重试");
        }
        oss.shutdown();
        return Result.ok().message("上传成功").data("imageUrl","https://"+aliOssConfig.getBucketName()+"."+aliOssConfig.getEndpoint()+"/"+filename);
    }


    @ApiOperation("上传图片")
    @PostMapping("/upload2")
    public Result uploadFile2(@RequestParam("file") MultipartFile file){
        log.info("file:{}",file.getOriginalFilename());
        System.out.println(file.getContentType());
        if(!image_type.contains(file.getContentType())){
            return Result.error().message("参类型不合法");
        }
        OSS oss = new OSSClientBuilder().build(aliOssConfig.getEndpoint(),aliOssConfig.getAccessKeyId(),aliOssConfig.getAccessKeySecret());
        String filename = "qianshou/img/item/"+new DateTime().toString("yyyy/MM/dd")+"/"+ UUID.randomUUID().toString().replaceAll("-","") +file.getOriginalFilename();
        try {
            oss.putObject(aliOssConfig.getBucketName(),filename,file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error().message("上传时发生了异常，请重试");
        }
        oss.shutdown();
        return Result.ok().message("上传成功").data("imageUrl","https://"+aliOssConfig.getBucketName()+"."+aliOssConfig.getEndpoint()+"/"+filename);
    }

    @PostMapping("/delete/image")
    public boolean deleteImage(@RequestParam("fileName") String filename){
        String s = filename.substring(0, filename.indexOf("m"));
        String s1 = filename.substring(s.length() + 1, filename.length());
        System.out.println(s1);
        String s2 = s1.substring(1, s1.length());
        OSS oss = new OSSClientBuilder().build(aliOssConfig.getEndpoint(),aliOssConfig.getAccessKeyId(),aliOssConfig.getAccessKeySecret());
        oss.deleteObject(aliOssConfig.getBucketName(),s2);
        oss.shutdown();
        return true;
    };

    @ApiOperation("上传视频")
    @PostMapping("/uploadVod")
    public Result uploadVod(@RequestParam("file") MultipartFile file){
        System.out.println(file.getOriginalFilename());
        System.out.println(file.getContentType());
        String fileName = file.getOriginalFilename();
        try {
            StorePath path = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileName.substring(fileName.lastIndexOf(".")+1), null);
            String vodUrl = "http://39.101.132.2/"+path.getFullPath();
            InputStream stream = file.getInputStream();
            File logo = vodService.getVideoCover(stream);
            String cover = "qianshou/img/cover/"+new DateTime().toString("yyyy/MM/dd")+"/"+logo.getName();
            OSS oss = new OSSClientBuilder().build(aliOssConfig.getEndpoint(),aliOssConfig.getAccessKeyId(),aliOssConfig.getAccessKeySecret());
            oss.putObject(aliOssConfig.getBucketName(),cover,logo);
            String ImageLogo = "https://"+aliOssConfig.getBucketName()+"."+aliOssConfig.getEndpoint()+"/"+cover;
            return Result.ok().data("vodUrl",vodUrl).data("ImageLogo",ImageLogo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
