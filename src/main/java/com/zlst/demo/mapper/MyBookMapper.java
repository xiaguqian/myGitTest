package com.zlst.demo.mapper;

import com.zlst.demo.polo.mybook.MyBook;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyBookMapper {

    void updateBook(MyBook book);

    void insertBook(MyBook book);

    MyBook findMyBook(String title);

    MyBook findMyBookById(int bid);

    List<MyBook> findAllBooks();
    List<Integer> findNowBooks();
}
