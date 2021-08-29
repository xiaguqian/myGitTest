package com.zlst.demo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlst.demo.polo.mybook.EditableChapter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.LinkedList;

@Component
public class FileUtil {
    @Async
    public void createDir(String bookPath) {
//        根据path，创建文件和目录
        File file = new File(bookPath + "/mb");
        File file1 = new File(bookPath + "/text");//这是卷同名文件，将放置chapter的json文本
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!file1.exists()) {
            file1.mkdirs();
        }
    }

    //根据roll路径，读取其文件，从文件中，获取chapter对象列表
    public LinkedList<EditableChapter> getEditChapter(String path) {
//        根据每一卷的对应文件，获取一个章对象列表
        LinkedList<EditableChapter> editableChapters = new LinkedList<EditableChapter>();
//        System.out.println("查询的卷地址" + path);
        File file = new File(path);
        BufferedReader bufferedReader = null;
        String s = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            s = bufferedReader.readLine();
            if (s == null) {
            } else {
                do {
//                    把s变成对象并且，添加到列表中，同时对对象进行处理
                    EditableChapter editableChapter = JSONObject.parseObject(s, EditableChapter.class);
//                    System.out.println(editableChapter.getName());
                    editableChapters.add(editableChapter);
                    s = bufferedReader.readLine();
                } while (s != null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return editableChapters;
    }

    //从某个指定路径获取其文本
    public String getText(String path) {
        File file = new File(path);
        BufferedReader bufferedReader = null;
        String s = null;
        StringBuilder stringBuilder = new StringBuilder();
//        System.out.println(file.getAbsolutePath());
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            s = bufferedReader.readLine();
            if (s == null) {
            } else {
                do {
//                    把s变成对象并且，添加到列表中，同时对对象进行处理
//                    System.out.println(editableChapter.getName());
                    stringBuilder.append(s + "\n");
//                    System.out.println(s);
                    s = bufferedReader.readLine();
                } while (s != null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return stringBuilder.toString();
    }

    //往某个指定路径写入,一个完整的字符串，所以要带着\n
    @Async
    public void writeText(String path, String text, boolean b) {
        File f = new File(path);
        BufferedWriter bufferedWriter = null;
        try {
            if (b) {
                bufferedWriter = new BufferedWriter(new FileWriter(path, b));
            } else {
                bufferedWriter = new BufferedWriter(new FileWriter(path));
            }
            bufferedWriter.write(text);
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
//默认是完全覆盖的
    public void writeText(String path, String text) {
        this.writeText(path,text,false);
    }
//    修改文件内容的方法。传入文件、旧字符串和新字符串

    public boolean editText(File file,String oldString ,String newString){
//        System.out.println("开始执行gettext");
        String s = this.getText(file.getAbsolutePath());
//        System.out.println("开始执行替换" + s);
        if (s != null) {
//            System.out.println("替换前" +s);
//            所以oldString不能是带字符的字符串，而应该是正则表达式
            s = s.replaceAll(oldString,newString);
//            System.out.println("替换后" + s);
        }
        this.writeText(file.getAbsolutePath(),s);
        return true;
    }
}
//    封装有关文件的需要异步执行的方法


