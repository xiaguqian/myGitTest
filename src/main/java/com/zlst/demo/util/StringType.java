package com.zlst.demo.util;

public enum StringType {
    BOOK_PATH("通过书籍title返回书籍路径");
    private final String name;
    private StringType(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
