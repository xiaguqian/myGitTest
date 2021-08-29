package com.zlst.demo.video.web;

import com.zlst.demo.video.Exception.M3u8Exception;
import com.zlst.demo.video.utils.Log;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class GetHTML {


    public String getHtml(String urls) throws IOException {
        //链接连接超时时间（单位：毫秒）
        long timeoutMillisecond = 1000*60L;
        int count = 1;
        //自定义请求头
        Map<String, Object> requestHeaderMap = new HashMap<>();
        requestHeaderMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
        HttpURLConnection httpURLConnection = null;
        StringBuilder content = new StringBuilder();
        while (true) {
            try {
                URL url = new URL(urls);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout((int) timeoutMillisecond);
                httpURLConnection.setReadTimeout((int) timeoutMillisecond);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setDoInput(true);
                for (Map.Entry<String, Object> entry : requestHeaderMap.entrySet())
                    httpURLConnection.addRequestProperty(entry.getKey(), entry.getValue().toString());
                String line;
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = bufferedReader.readLine()) != null)
                    content.append(line).append("\n");
                bufferedReader.close();
                inputStream.close();
//                    Log.i(content);
//                    用于打印获取的ts文件地址
            } catch (Exception e) {
                Log.d("第" + count + "获取链接重试！\t" + urls);
                count++;
                    e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
            if (count > 100)
                throw new M3u8Exception("连接超时！");

//            Log.i(content);
            return content.toString();
        }
    }
}
