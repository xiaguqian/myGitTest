package com.zlst.demo.util;

import com.alibaba.fastjson.JSONObject;
import com.zlst.demo.polo.mybook.EditableChapter;
import com.zlst.demo.video.AllMethod;
import com.zlst.demo.video.utils.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class FileRecord {
    @Autowired
    FileUtil fileUtil;
    @Autowired
    AllMethod allMethod;
    final static String DIR = "E://m3u8JavaTest/record.txt";
    final static File RECORD = new File(DIR);
    public FileRecord() {
        if (!RECORD.exists()) {
            try {
                RECORD.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        new Thread(() -> {
//            线程休眠一次，就从本地文件中获取一次，并删除原本的
            while (true){
                int count =0;
                count++;
//                每次循环，读取文件，获得一个map列表
                List<Map<String,String>> mapList = this.getMaps();
                int x = 10;
                if (mapList != null) {
                    System.out.println("监测线程开始执行");
//                    先把里面的数据取出来去执行
//                    for (int j = 0; j < mapList.size(); j++) {
//                        allMethod.downloadMp4(mapList[j].get(""), paths.get(j), names.get(j));
//                        System.out.println("第" +count +"次监测线程开始循环====:"+"执行第" + (j+1) + "次下载");
//                    }
                    int i = 0;
                    for (Map<String,String> map:mapList
                         ) {
                        i++;
                        try {
                            allMethod.downloadMp4(map.get("url"), map.get("path"), map.get("name"));
                            System.out.println("第" +count +"次监测线程开始循环====:"+"执行第" + (i+1) + "次下载，本次共有" + mapList.size() + "次i循环");
                        }catch (Exception e){
                            Log.e("线程执行出现了异常");
                            e.printStackTrace();
                        }
                    }
                    if (i<=4) {
                        x= i*15;
                    }
                    if (i>4 && i<10){
                        x =50;
                    }else if (i>=10){
                        x=i*7;
                    }else if (i>20){
                        x=160;
                    }
                }
                try {
//                    System.out.println("监测线程休眠中，即将休眠"+ (x*6000/1000)/60 +"分钟");
                    Thread.sleep(x*6000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private List<Map<String, String>> getMaps() {
//        从本地文件获取map列表的方法
        List<Map<String,String>> mapList = new LinkedList<Map<String, String>>();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(RECORD));
            String s = bufferedReader.readLine();
            boolean b = !(s ==null || s.length() <=2);
            if (!b) {
                return null;
            }
            while (b){
                Map<String, String> map = JSONObject.parseObject(s,Map.class);
                for (String c:map.keySet()
                     ) {
                    System.out.print("得到的值" + c +":" + map.get(c) + "\t");
                };
                System.out.println("\n");
                mapList.add(map);
                s = bufferedReader.readLine();
                b = !(s ==null || s.length() <=2);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        最后把文件清空
        fileUtil.writeText(DIR,"",false);
        return mapList;
    }

    public void writeOneLine(String json) {
//        向记录文件中写入json
        fileUtil.writeText(DIR,json,true);
    }
}
