package com.zlst.demo.polo.mybook;

import java.util.LinkedList;

public class MyBook {
//    这是类Ebook的类，存储了可编辑book的本地信息，
    private int id;
    private String title;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String path;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    //    private LinkedList<String> rolls;//这里放置关于roll所有的路径名和index
    private int bid;
}
