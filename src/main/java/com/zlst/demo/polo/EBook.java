package com.zlst.demo.polo;

public class EBook {
//ebook中除了book所具有的属性之外，还应该具有本地的文件路径，原资源路径，完结状态，外键id，最后更新，
    private int id;
    private String title;

    public String getTitle() {
        return title;
    }


    @Override
    public String toString() {
        return "EBook{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", book_link='" + book_link + '\'' +
                ", file_path='" + file_path + '\'' +
                ", finished=" + finished +
                ", bid=" + bid +
                '}';
    }

    public void setTitle(String titile) {
        this.title = titile;
    }

    private String book_link;
    private String file_path;
    private int finished;
    private int bid;

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBook_link() {
        return book_link;
    }

    public void setBook_link(String book_link) {
        this.book_link = book_link;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }
}
