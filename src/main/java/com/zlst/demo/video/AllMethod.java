package com.zlst.demo.video;



import com.zlst.demo.video.download.M3u8DownloadFactory;
import com.zlst.demo.video.listener.DownloadListener;
import com.zlst.demo.video.utils.Constant;
import com.zlst.demo.video.utils.HtmlUtil;
import com.zlst.demo.video.utils.Log;
import com.zlst.demo.video.utils.MediaFormat;
import com.zlst.demo.video.web.GetHTML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class AllMethod {
//        这是一个所有供外界调用的方法的类
//    一 、 第一个方法是最基础的方法，输入网址名，下载到指定目录下mp4文件夹
    @Autowired
    TaskExecutor taskExector;


    public int downloadMp4(String website, String filePath, String fileName){
        M3u8DownloadFactory.M3u8Download m3u8Download = M3u8DownloadFactory.getInstance(website);
        //设置生成目录
        m3u8Download.setDir("E://m3u8JavaTest/" + filePath);
        //设置视频名称
        m3u8Download.setFileName(fileName);
        //设置线程数
        m3u8Download.setThreadCount(100);
        //设置重试次数
        m3u8Download.setRetryCount(100);
        //设置连接超时时间（单位：毫秒）
        m3u8Download.setTimeoutMillisecond(10000L);
        /*
        设置日志级别
        可选值：NONE INFO DEBUG ERROR
        */
//        m3u8Download.setLogLevel(Constant.INFO);
        m3u8Download.setLogLevel(Constant.INFO);
        //设置监听器间隔（单位：毫秒）
        m3u8Download.setInterval(500L);

        m3u8Download.setFixedThreadPool(taskExector);

        //添加额外请求头
      /*  Map<String, Object> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "text/html;charset=utf-8");
        m3u8Download.addRequestHeaderMap(headersMap);*/
        //添加监听器
        m3u8Download.addListener(new DownloadListener() {
            @Override
            public void start() {
                System.out.println("开始下载！");
            }

            @Override
            public void process(String downloadUrl, int finished, int sum, float percent) {
                System.out.println("下载网址：" + downloadUrl + "\t已下载" + finished + "个\t一共" + sum + "个\t已完成" + percent + "%");
            }

            @Override
            public void speed(String speedPerSecond) {
                System.out.println("下载速度：" + speedPerSecond);
            }

            @Override
            public void end() {
                System.out.println("下载完毕");
            }
        });
        //开始下载
        m3u8Download.start();

        return 1;
    }
    @Autowired
    GetHTML getHTML;
//    项目启动时，启动一个监听器，一旦消息队列中有了数据，就启动该方法
    public String getHtml(String url){
        String s = "";
//        System.out.println("获取的url" + url);
        try {
             s = getHTML.getHtml(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
    @Autowired
    HtmlUtil htmlUtil;
    public String getM3u8(String url,String type){
//        对url进行字段校验
        if (!MediaFormat.UrlFormat(url)) {
            Log.i("输入的url的不是http链接");
            return "不是链接";
        }
        String string = htmlUtil.getHtml(url);
//        System.out.println("网页是" + string);
//        根据String获取m3u8

            String m3u8 = htmlUtil.getM3u8(string, type);
            System.out.println("m3u8地址是" + m3u8);
//      获取的m3u8，不要验证，因为后续下载时会验证
            return m3u8;
    }
}
