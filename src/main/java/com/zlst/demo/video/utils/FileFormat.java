package com.zlst.demo.video.utils;

public class FileFormat {
//    把文件名规范化
    private static boolean charExits(char c) {
        char[] chars = {'/', '\\', '\'', ':', '*', '\"', '<', '>', '|', '?'};
        for (char c1 :
            chars) {
            if (c == c1) {
                return false;
            }
        }
        return true;
    }
    //    将string字符串转化为可以作为路径的字符串
    public static String fileTrue(String s) {
        char[] chars = s.toCharArray();
        StringBuffer buffer = new StringBuffer();
        for (char c :
                chars) {
            if (!charExits(c)) {
                continue;
            }
            buffer.append(c);
        }
        return buffer.toString();
    }

}
