package com.zlst.demo.util;

import com.zlst.demo.polo.Chapter;
import com.zlst.demo.polo.mybook.Roll;

import java.util.Random;

public class StringUtils {
    public static String getRandomString(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
    public static  String imgPath(String imgPath){
        imgPath = imgPath.substring(imgPath.length()-10);
        return imgPath;
    }
    //这是通过章节文件名 的字符串处理，获得chapter对象的方法
    public  static Chapter getChapter(String s) {
        Chapter result = new Chapter();
        String s1 = s.replaceAll("/", "");
        char[] chars = s1.toCharArray();
        StringBuffer index = new StringBuffer();
        StringBuffer chapter = new StringBuffer();
        for (char c :
                chars) {
            if (!intExits(c)) {
                index.append(c);
            } else {
//                把s中的index去除，得到的值，终止循环
                s1 = s1.substring(index.toString().length());
                s = s1.replaceAll(".txt", "");
                chapter.append(s);
                break;
            }
        }
//        String[] strings = {index.toString(), chapter.toString()};
        result.setIndex(Integer.valueOf(index.toString()));
        result.setName(chapter.toString());
        return result;
    }

    private static boolean intExits(char c) {
        char[] chars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
        for (char c1 :
                chars) {
            if (c == c1) {
                return false;
            }
            ;
        }
        return true;
    }

    private static String getBookPath(String title) {
        return "d:/mybook/" + title;
    }

    public static String getRollPath(Roll roll, String title) {
//        该方法，是用来根据roll对象获取对象所在地址的方法
        int index= roll.getIndex();
        String name = roll.getRollName();
        return "d:/mybook/" + title + "/mb/"+index + name + ".txt";
    }
//    创建一个方法，该方法，是根据参数的不同，获取不同格式字符串的方法
    public static String getNewString(Object o,StringType type){
        String s = null;
        switch (type){
            case BOOK_PATH:
                s = getBookPath((String) o);
                break;
        }
        return s;
    }
}
