package com.qianshou.upload.service;


import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utlis.Result;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class VodService {
    public File getVideoCover(InputStream fileInputStream){
        FFmpegFrameGrabber fm = new FFmpegFrameGrabber(fileInputStream);
        try {
            fm.start();
            int length = fm.getLengthInAudioFrames();
            int i = 0;
            Frame f = null;
            while (i<length){
                f = fm.grabFrame();
                if((i>5)&&(f.image!=null)){
                    break;
                }
                i++;
            }
            int imageWidth = f.imageWidth;
            int imageHeight = f.imageHeight;

            int width = 800;
            int height = (int) (((double) width / imageWidth) * imageHeight);
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage fecthedImage = converter.getBufferedImage(f);
            BufferedImage bi = new BufferedImage(width,height,BufferedImage.TYPE_3BYTE_BGR);
            bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width,height, Image.SCALE_SMOOTH),0,0,null);
            File file = new File("E:\\image\\logo\\"+ UUID.randomUUID().toString().replaceAll("-","")+".jpg");
            try {
                ImageIO.write(bi, "jpg",file);
                fm.stop();
                return file;
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FrameGrabber.Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
