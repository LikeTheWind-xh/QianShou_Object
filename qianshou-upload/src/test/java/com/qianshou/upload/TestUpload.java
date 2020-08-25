package com.qianshou.upload;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qianshou.upload.controller.UploadController;
import org.apache.commons.io.FileUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import utlis.Result;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootTest(classes = UploadApplication.class)
@RunWith(SpringRunner.class)
public class TestUpload {

//    @Autowired
//    public FastFileStorageClient fastFileStorageClient;

    @Autowired
    public UploadController uploadController;
//    @Test
//    public void testUpload(){
//        String path = "C:\\Users\\11415\\Pictures\\IMG_2758(20200106-163912).JPG";
//        File file = new File(path);
//        try {
//            StorePath path1 = fastFileStorageClient.uploadFile(FileUtils.openInputStream(file), file.length(), "jpg", null);
//            System.out.println(path1.getFullPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void testVod(){

    }
}
