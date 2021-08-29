package com.zlst.demo.controller;

import com.alibaba.fastjson.JSON;
import com.zlst.demo.result.Result;
import com.zlst.demo.util.FileRecord;
import com.zlst.demo.util.ResultFactory;
import com.zlst.demo.video.AllMethod;
import com.zlst.demo.video.utils.Constant;
import com.zlst.demo.video.utils.FileFormat;
import com.zlst.demo.video.utils.MediaFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin
public class VideoController {
    @Autowired
    AllMethod allMethod;


    //    输入m3u8路径，文件路径，以及文件名，
//    @GetMapping("/api/doM3u8")
//    @ResponseBody
//    public String doUrl(String url,String filePath,String fileName){
//        if (filePath == null) {
//            filePath = "我的视频";
//            System.out.println("filePath为空");
//        }
//        if(fileName == null){
//            System.out.println("fileName为空");
//            fileName = url.substring((url.length())-18,(url.length())-10);
//        }
//        allMethod.downloadMp4(url,filePath,fileName);
//        return "true";
//    }
    @Autowired
    FileRecord fileRecord;
    @PostMapping("/api/getM3u8")
    @ResponseBody
    public Result getM3u8(@RequestBody Map<String, String> data){
        Map<String,String> maps= new HashMap<String, String>();
        int i = 0;
        i++;
        String url = data.get("url").trim();
        String filePath = data.get("filePath").trim();
        String x = Constant.QITA;
        if (filePath.equals("动漫卡通")) {
            x = Constant.DONGMAN;
        }else if (filePath.equals("字幕三级")){
            x= Constant.DONGMAN;
        }
        String fileName =data.get("fileName").trim();
        if (filePath == null) {
            filePath = "我的视频";
            System.out.println("filePath为空");
        }
        if(fileName == null){
            System.out.println("fileName为空");
            fileName = url.substring((url.length())-18,(url.length())-10);
        };
        filePath = FileFormat.fileTrue(filePath);
        fileName = FileFormat.fileTrue(fileName);
        filePath = filePath + "/" + fileName;
        System.out.println("接受了参数" + url +"filePath" + filePath + fileName);
        if (url.endsWith("index.m3u8")) {
            allMethod.downloadMp4(url,filePath,fileName);
            return ResultFactory.buildSuccessResult();
        }
        String result = "";
//        result = allMethod.getHtml(url);
//        System.out.println("result是"+result);
        result = allMethod.getM3u8(url,x);
        if (result != null){
            result = result.substring(result.indexOf("url")+4);
            if (!MediaFormat.UrlFormat(result)) {
                return ResultFactory.buildFailResult("得到的url地址不是m3u8" + url);
            }
        }else {
            System.out.println("没有得到m3u8地址" + url);
            return ResultFactory.buildFailResult("没有得到m3u8地址" +url);
        }
        maps.put("url",result);
        maps.put("path",filePath);
        maps.put("name",fileName);
        String json= JSON.toJSONString(maps);
//        把map写入文件
        fileRecord.writeOneLine(json + "\n");
//        allMethod.downloadMp4(result,filePath,fileName);//此处代码改为将信息存入一个队列
        return ResultFactory.buildSuccessResult();
    }
    @PostMapping("/api/getM3u8Now")
    @ResponseBody
    public Result getM3u8Now(@RequestBody Map<String, String> data){
        String url = data.get("url").trim();
        String filePath = data.get("filePath").trim();
        String x = Constant.QITA;
        if (filePath.equals("动漫卡通")) {
            x = Constant.DONGMAN;
        }else if (filePath.equals("字幕三级")){
            x= Constant.DONGMAN;
        }
        String fileName =data.get("fileName").trim();
        if (filePath == null) {
            filePath = "我的视频";
            System.out.println("filePath为空");
        }
        if(fileName == null){
            System.out.println("fileName为空");
            fileName = url.substring((url.length())-18,(url.length())-10);
        }
//        if (fileName.length() > 18) {
//            fileName = fileName.substring(0,17);
//        }
        filePath = FileFormat.fileTrue(filePath);
        fileName = FileFormat.fileTrue(fileName);
        filePath = filePath + "/" + fileName;
        System.out.println("接受了参数" + url +"filePath" + filePath + fileName);
        if (url.endsWith("index.m3u8")) {
            allMethod.downloadMp4(url,filePath,fileName);
            return ResultFactory.buildSuccessResult();
        }
        String result = "";
//        result = allMethod.getHtml(url);
//        System.out.println("result是"+result);
        result = allMethod.getM3u8(url,x);
        if (result != null){
                result = result.substring(result.indexOf("url")+4);
            if (!MediaFormat.UrlFormat(result)) {
                return ResultFactory.buildFailResult("得到的url地址不是m3u8" + url);
            }
        }else {
            System.out.println("没有得到m3u8地址" + url);
            return ResultFactory.buildFailResult("没有得到m3u8地址" + url);
        }

        allMethod.downloadMp4(result,filePath,fileName);//此处代码改为将信息存入一个队列
        return ResultFactory.buildSuccessResult();
    }
}
