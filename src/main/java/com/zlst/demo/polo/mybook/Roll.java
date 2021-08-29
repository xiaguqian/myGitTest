package com.zlst.demo.polo.mybook;

import java.util.LinkedList;

public class Roll{
//   这是卷类，里面放了章节列表，本身是一个列表
    private String rollName;
    private int index;

    public boolean isChange() {
        return change;
    }

    public void setChange(boolean change) {
        this.change = change;
    }

    private boolean change;
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public LinkedList<EditableChapter> getChapters() {
        return chapters;
    }

    public void setChapters(LinkedList<EditableChapter> chapters) {
        this.chapters = chapters;
    }

    private LinkedList<EditableChapter> chapters;

    public String getRollName() {
        return rollName;
    }

    public void setRollName(String rollName) {
        this.rollName = rollName;
    }

}
