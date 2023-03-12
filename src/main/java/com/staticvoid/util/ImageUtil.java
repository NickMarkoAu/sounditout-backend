package com.staticvoid.util;

import com.amazonaws.util.IOUtils;
import com.staticvoid.fileupload.service.S3StorageService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    public static byte[] getByteArrayFromImageS3Bucket(String s3Uri) throws IOException {
        S3StorageService s3Service = new S3StorageService();
        InputStream in = s3Service.getS3Object(s3Uri).getObjectContent();

        BufferedImage imageFromAWS = ImageIO.read(in);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(imageFromAWS, "png", baos );
        byte[] imageBytes = baos.toByteArray();
        IOUtils.drainInputStream(in);
        in.close();
        return imageBytes;
    }
}
