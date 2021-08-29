package com.zlst.demo.controller;


import com.zlst.demo.polo.Book;
import com.zlst.demo.polo.Category;
import com.zlst.demo.polo.Chapter;
import com.zlst.demo.polo.ChapterResult;
import com.zlst.demo.result.Result;
import com.zlst.demo.service.BookService;
import com.zlst.demo.service.CategoryService;
import com.zlst.demo.service.EBookService;
import com.zlst.demo.util.ImgUtil;
import com.zlst.demo.util.ResultFactory;
import com.zlst.demo.util.StringUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class LibraryController {
    @Autowired
    BookService bookService;
    @Autowired
    EBookService eBookService;
    @Autowired
    CategoryService categoryService;
//这个请求获得一个book列表
    @GetMapping("/api/books")
    public Result listBooks() {
        return ResultFactory.buildSuccessResult(bookService.list());
    }
//这个可以更新一本书的信息
    @PostMapping("/api/books")
    public Result addOrUpdate(@RequestBody Book book) throws Exception {
//        接收到book请求后，解析img文件名，将文件复制到img文件夹下
        ImgUtil.setImgDir(book.getCover());
        bookService.addOrUpdate(book);
        return ResultFactory.buildSuccessResult();
    }
//根绝传入书籍的id信息删除书籍信息
    @DeleteMapping("/api/delete/{id}")
    public Result delete(@PathVariable int id) throws Exception {
//        同时删除文件
        bookService.deleteById(id);
        return ResultFactory.buildSuccessResult();
    }

//根据cid查询某本书
    @GetMapping("/api/categories/{cid}/books")
    public Result listByCategory(@PathVariable("cid") int cid) throws Exception {
        List<Book> books = null;
        if (0 != cid) {
            books = bookService.listByCategory(cid);
        } else {
           books = bookService.list();
        }
        return ResultFactory.buildSuccessResult(books);
    }
    //根据id查询某本书
    @GetMapping("/api/book/{id}/one")
    public Result getBookById(@PathVariable("id") int id) throws Exception {
        Book book = bookService.getBookById(id);
        return ResultFactory.buildSuccessResult(book);
    }
//    根据关键字，搜索作者或书名，将来可能还有简介
    @GetMapping("/api/search")
    public Result Search(String keywords) {
        List<Book> books = null;
        if (true) {
            books = (List<Book>) bookService.booksByKey(keywords);
        }
        return ResultFactory.buildSuccessResult(books);
    }
    //    处理cover请求
    @PostMapping("/api/covers")
    @ResponseBody
    public String doCover(MultipartFile file) throws Exception{
//        放置处理图片的逻辑代码，并得到一个url对象
        String imgURL = "";
//        文件默认放置在每日清除的一个文件夹中，每日定时删除，只有确认表单，才会到img文件夹
        String folder = "D:/workspace/sav/img0/";
        File imageFolder = new File(folder);
        File f = new File(imageFolder, StringUtils.getRandomString(6) + file.getOriginalFilename()
                .substring(file.getOriginalFilename().length() - 4));
        if (!f.getParentFile().exists()){
            f.getParentFile().mkdirs();
        }
        try {
            file.transferTo(f);
            imgURL = "http://47.242.198.126:8081/api/img/" + f.getName();
//            imgURL = "http://myhouduan.cn.utools.club/api/img/" + f.getName();
            return imgURL;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgURL;
    }
//    根据书名，构建file对象，读取目录文档，生成目录集合====待改进
    @PostMapping("/api/chapter/all")
    public Result getChapterList(@RequestBody String name){
        String s = null;
        if (name != null) {
            s = name.substring("{\"name\":\"".length(),name.length()-2);
        }
        List<Chapter> chapters = eBookService.getFilePath(s);
        if (chapters == null) {
            return ResultFactory.buildErrorResult();
        }
        return ResultFactory.buildSuccessResult(chapters);
    }
//    这是一个根据index返回章节内容的方法
    @RequestMapping("/api/chapter/index")
    public Result getText(@RequestBody Map<String, Object> data){
        Integer cid = Integer.valueOf((String) data.get("cid"));
        Category category = categoryService.getCategoryByCid(cid);
        Integer bid = Integer.valueOf((String) data.get("bid"));
        String bookName = bookService.getBookById(bid).getTitle();
        JSONObject jsonObject= JSONObject.fromObject(data.get("chapter"));
        Chapter chapter = (Chapter) JSONObject.toBean(jsonObject, Chapter.class);
        String f = "";
        if (!category.getName().endsWith("小说")) {
             f = category.getName() + "小说";
        }
        StringBuilder str = new StringBuilder("D://books/textDir/");
        String fileName = chapter.getIndex() + chapter.getName().replace("'","") + ".txt";
//        System.out.println("fileName" +fileName);
        File file = new File((str.append(f + "/").append(bookName + "/").append(fileName)).toString());
        String page= null;
        StringBuilder stringBuilder  = new StringBuilder();
        if (file.exists()) {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new FileReader(file));
                String s = bufferedReader.readLine();
                while (s != null){
                    stringBuilder.append(s);
                    s = bufferedReader.readLine();
                }
                page =stringBuilder.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return ResultFactory.buildSuccessResult(page);
    }
    //    根据传入参数获取上下章信息
    @RequestMapping("/api/chapter/pan")
    public Result getPan(@RequestBody Map<String, Object> data){
        ChapterResult chapterResult = new ChapterResult();
        Integer cid = Integer.valueOf((String) data.get("cid"));
        Category category = categoryService.getCategoryByCid(cid);
        Integer bid = Integer.valueOf((String) data.get("bid"));
        String bookName = bookService.getBookById(bid).getTitle();
        JSONObject jsonObject= JSONObject.fromObject(data.get("chapter"));
        Chapter chapter = (Chapter) JSONObject.toBean(jsonObject, Chapter.class);
//        根据书的类别，找到书名
        String f = "";
        if (!category.getName().endsWith("小说")) {
            f = category.getName() + "小说";
        }
        StringBuilder str = new StringBuilder("D://books/textDir/");
        String fileName = chapter.getIndex() + chapter.getName().replace("'","") + ".txt";
        File file = new File((str.append(f + "/").append(bookName + "/").append(fileName)).toString());
        File file1 = file.getParentFile();
        String[] strings = file1.list();
        Integer i = chapter.getIndex();
        if (i == 0) {
            chapterResult.setPreChapter(null);
//            只需要寻找i+1对应的下标
//                    把文件名，解析为chapter
            for (String s:strings
                 ) {
                chapter = StringUtils.getChapter(s);
                if (chapter.getIndex() == i+1) {
                    chapterResult.setNextChapter(chapter);
                    break;
                }
            }
        } else if (i == strings.length) {
            chapterResult.setNextChapter(null);
            for (String s:strings
            ) {
                chapter = StringUtils.getChapter(s);
                if (chapter.getIndex() == i-1) {
                    chapterResult.setPreChapter(chapter);
                    break;
                }
            }
        }else {
            int x = 0;
            for (String s:strings
            ) {
                chapter = StringUtils.getChapter(s);
                if (chapter.getIndex() == i-1) {
                    x++;
                    chapterResult.setPreChapter(chapter);
                }else if (chapter.getIndex() == i+1) {
                    chapterResult.setNextChapter(chapter);
                    x++;
                }
                if (x == 2) {
                    break;
                }
            }
        }

        return ResultFactory.buildSuccessResult(chapterResult);
    }
}

