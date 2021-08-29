package com.zlst.demo.service;

import com.zlst.demo.mapper.BookMapper;
import com.zlst.demo.polo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class BookService {

    @Autowired
    BookMapper bookMapper;

    public void addOrUpdate(Book book) {
//        先判断此book是否存在，存在即更新，不存在即删除。假设不存在，则需要add，若此刻有人添加了同一本书，即标题和内容简介一致，此刻我执行add方法会报sql错误，因此需要回滚，重新执行就会是update
//        当执行成功时，跳出循环
        if (!(book.getCategory().getId() >= 1 && book.getCategory().getId()<13)) {
            return;
        }
        if (bookMapper.findBook(book.getId(),book.getTitle()) != null) {
            bookMapper.updateBook(book);
        }else {
            bookMapper.insertBook(book);
        }
    }

    public void deleteById(@RequestBody int id) {
        System.out.println(id);
        bookMapper.deleteBookById(id);
    }

    public List<Book> list(){
        List<Book> books = null;
        books = bookMapper.findAllBooks();
        return books;
    }

    public List<Book> listByCategory(int cid) {
//       首先不等于20，在1-13之外
        if (cid != 20  && !(cid>0 && cid<13)){
            System.out.println(cid);
            return null;
        }
        List<Book> list = null;
        list = bookMapper.findBookByCid(cid);

        return list;
    }
    public List<Book> booksByKey(String key){
//        对输入的数据进行操作排序，智能查询结果
        String key1 = "%" + key +"%";
        String key2 = "%" + key +"%";
        List<Book> books = bookMapper.findBookByKey(key1,key2);
        return books;
    }
    public Book getBookById(int id){
        return bookMapper.findBookById(id);
    }

    public Book addAndGetId(Book book) {
        Book result = bookMapper.findBookByTitle(book.getTitle());
        if (result == null) {
            bookMapper.insertBook(book);
            return bookMapper.findBookByTitle(book.getTitle());
        }else {
            bookMapper.updateBook(book);
            return result;
        }
    }

    public List<Book> listByIds(List<Integer> bookIds) {
        List<Book> books = null;
        books = bookMapper.findBookByIds(bookIds);
        return books;
    }
}
