package com.zlst.demo.mapper;

import com.zlst.demo.polo.Book;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
//    查询所有的书，加载到页面
    public List<Book> findAllBooks();
    public List<Book> findBookByCid(int cid);
//    按照关键词，查询特定几列的结果
    public List<Book> findBookByKey(String key1,String key2);

//    update and add 方法需要按book查询，更新book，插入
    public Book findBook(Integer id,String title);
    public int insertBook(Book book);
    public int updateBook(Book book);
//    deleteById(int id)
    public int deleteBookById(int id);
//    根据id查询书籍信息
    public Book findBookById(int id);
    public Book findBookByTitle(String title);

    List<Book> findBookByIds(List<Integer> bookIds);
}
