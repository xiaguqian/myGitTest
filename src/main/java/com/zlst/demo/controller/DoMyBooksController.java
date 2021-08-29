package com.zlst.demo.controller;

import com.zlst.demo.polo.Book;
import com.zlst.demo.polo.mybook.Roll;
import com.zlst.demo.result.Result;
import com.zlst.demo.service.BookService;
import com.zlst.demo.service.MyBookService;
import com.zlst.demo.util.FileUtil;
import com.zlst.demo.util.ImgUtil;
import com.zlst.demo.util.ResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/me/")
public class DoMyBooksController {
    @Autowired
    BookService bookService;
    @Autowired
    MyBookService myBookService;
    @Autowired
    FileUtil fileUtil;
    final static String BOOK = "d:/mybook";
//书整体的方法========================================================================================================================
//    这里将会调用创建book的方法
    @PostMapping("/createBook")
    public Result addOrUpdate(@RequestBody Book book) throws Exception {
    //        接收到book请求后，解析img文件名，将文件复制到img文件夹下
        ImgUtil.setImgDir(book.getCover());
        book = bookService.addAndGetId(book);
//        在book表中更新完数据后，更新myBook表，调用一个异步执行函数，创建相关文件
        String bookPath = myBookService.setAndUpdateMyBook(book);
//        调用执行文件的异步方法
        fileUtil.createDir(bookPath);
        return ResultFactory.buildSuccessResult();
    }
    @GetMapping("books")
    //根据cid 20查询
    public Result listByCategory() throws Exception {
        List<Book> books = null;
//        books = bookService.listByCategory(20);
//        根据ebooks的status，获取全部的bid列表，根据bid列表，获取books
//        在代码循环和在sql中获取列表，利用时间来测试
        books = myBookService.getNowBooks();
        return ResultFactory.buildSuccessResult(books);
    }
    @GetMapping("rac")
    public Result listRollAndChapter(int bid){
//        先根据卷文件，获取所有的卷对象，并生成所有的卷下章文件名，根据卷下章文件名，读取所有的章节对象
        LinkedList<Roll> rollLinkedList = myBookService.getRollsAndChapters(bid);
        return ResultFactory.buildSuccessResult(rollLinkedList);
    }

// roll请求区域========================================================================================================================
//    首先是新建roll的方法，需要查询书籍信息
    @GetMapping("createRoll")
    public Result createRoll(int index,
                             int bid,
                             String name){
//        点击新建roll，会传入,roll的name和index,book的id-bid，
//      查询myBook，获取path和title,该方法是异步方法
        myBookService.createRoll(bid,index,name);
        return ResultFactory.buildSuccessResult();
    }
//    delete删除空白roll   的方法
    @GetMapping("deleteRoll")
    public Result deleteRoll(int bid, int index,String name){
        boolean b = myBookService.deleteRoll(bid,index,name);
        if (b) {
            return ResultFactory.buildSuccessResult();
        }else {
            return ResultFactory.buildFailResult("更改卷名出错，原因或为卷名已存在，或参数错误，或者卷非空白卷，更多其他原因……");
        }    }
//    chapter请求区域========================================================================================================================
//    首先是新建章节的方法
    @GetMapping("createChapter")
    public Result createChapter(int bid,int rollIndex ,String rollName,int index,String name){
//        新建章节，传入的应该是bid ，index和name ，chapter的index和name，并组成chapter对象，将对象转换成json字符串，写入卷文件，并新建相应的名称的章节文件
        myBookService.createChapter( bid, rollIndex , rollName,index,name);
        return ResultFactory.buildSuccessResult();
    }
//    删除章节的方法
    @GetMapping("deleteChapter")
    public Result deleteChapter(int bid,int rollIndex ,String rollName,int index,String name){
        myBookService.deleteChapter( bid, rollIndex , rollName,index,name);
        return ResultFactory.buildSuccessResult();
    }
//    修改章节的方法
    @GetMapping("editChapter")
    public Result editeChapter(int bid,int rollIndex ,String rollName,int index,String name,String newName){
//        需要传入的参数，还要有一个新名字
        boolean b = myBookService.editChapter(bid, rollIndex , rollName,index,name,newName);
        if (b) {
            return ResultFactory.buildSuccessResult();
        }else {
            return ResultFactory.buildFailResult("更改章节名出错，原因或为章节名已存在，或参数错误，更多其他原因……");
        }
    }
//    text请求区域===========================================================================================================================
    @GetMapping("getText")
    public Result getText(int bid,int rollIndex ,String rollName,int index,String name){
//        获取章节文件的内容章节，传入的应该是bid ，index和name ，chapter的index和name，并组成chapter对象，将对象转换成json字符串，写入卷文件，并新建相应的名称的章节文件
        System.out.println("受到的读取文件的参数是" );
        String page = myBookService.getChapterText(bid, rollIndex , rollName,index,name);
        return ResultFactory.buildSuccessResult(page);
    }
    @PostMapping("submitText")
    public Result submitText(@RequestBody Map<String, Object> data){
        Integer bid = Integer.valueOf((String) data.get("bid"));
        Integer rollIndex = Integer.valueOf((String) data.get("rollIndex"));
        String rollName = (String) data.get("rollName");
        Integer index = Integer.valueOf((String) data.get("index"));
        String name = (String) data.get("name");
        String text = (String) data.get("text");
        myBookService.writeText(bid, rollIndex , rollName,index,name,text);
        return ResultFactory.buildSuccessResult();
    }
}
