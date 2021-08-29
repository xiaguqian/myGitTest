package com.zlst.demo.util;

import java.io.*;

public class ImgUtil {

    public static void setImgDir(String cover) throws IOException {
        cover = StringUtils.imgPath(cover);
        String folder = "D:/workspace/sav/img0/" + cover;
        String img = "D:/workspace/sav/subar/src/main/resources/api/img/" + cover;
        File imageFolder = new File(folder);
        File imgDir = new File(img);
        if (!imgDir.exists()) {
            imgDir.createNewFile();
        }
        if (!imageFolder.exists()){
            imageFolder.createNewFile();
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        fis = new FileInputStream(imageFolder);
        fos = new FileOutputStream(imgDir);
        byte[] bytes = new byte[1024];
        int len = bytes.length;
        while ((fis.read(bytes,0,len)) != -1){
            fos.write(bytes);
            fos.flush();
        };
        fis.close();
        fos.close();
    }
}
