package com.zlst.demo.polo;

import java.util.Objects;

public class Chapter {
    private int index;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chapter chapter = (Chapter) o;
        return index == chapter.index &&
                Objects.equals(name, chapter.name);
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "index=" + index +
                ", name='" + name + '\'' +
                '}';
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Chapter(int index, String name) {
        this.index = index;
        this.name = name;
    }
    public Chapter() {

    }
}
