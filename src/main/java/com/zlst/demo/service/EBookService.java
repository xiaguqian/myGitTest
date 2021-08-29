package com.zlst.demo.service;

import com.zlst.demo.mapper.EBookMapper;
import com.zlst.demo.polo.Chapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class EBookService {

    @Autowired
    EBookMapper eBookMapper;
    public List<Chapter> getFilePath(String name){
        String s = eBookMapper.selectEBook(name);
        if (s == null) {
            return null;
        }
        String a = s.replaceAll("textDir","chapterDir");
        File file = new File(a + ".page");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        List chapters = new ArrayList();
        StringBuilder stringBuilder = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
//            chapter主要有三个点，分别是两个等号，一个逗号，一个后大括号
//            indexOf(String str)返回指定子字符串第一次出现的字符串内的索引。
//            int lastIndexOf(String str)返回指定子字符串最右边出现的字符串内的索引
//            substring(int start, int end)返回一个新的 String ，其中包含此序列中当前包含的字符的子序列。
            String s1 = null;
            int index = 0;
            String chapter = "";
            int begin = 0;
            int end = 0;
            while (true){
                s1 = bufferedReader.readLine();
                if (s1 != null){
                    stringBuilder = new StringBuilder(s1);
//                    获取index,开始点是等号第一个的下标，结束是逗号的下标
                    begin = stringBuilder.indexOf("=") + 1;
                    end = stringBuilder.indexOf(",");
                    index = Integer.valueOf(stringBuilder.substring(begin,end));
                    begin = stringBuilder.lastIndexOf("=") + 1;
                    end=stringBuilder.indexOf("}");
                    chapter = stringBuilder.substring(begin,end);
                    Chapter c = new Chapter(index,chapter);
                    chapters.add(index,c);
                }else {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
//            e.printStackTrace();系统找不到指定文件，返回空chapter
            System.out.println("找不到指定文件" + file.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return chapters;
    }
}
