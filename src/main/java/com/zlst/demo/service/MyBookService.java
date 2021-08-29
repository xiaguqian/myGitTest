package com.zlst.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zlst.demo.mapper.MyBookMapper;
import com.zlst.demo.polo.Book;
import com.zlst.demo.polo.mybook.EditableChapter;
import com.zlst.demo.polo.mybook.MyBook;
import com.zlst.demo.polo.mybook.Roll;
import com.zlst.demo.util.FileUtil;
import com.zlst.demo.util.StringType;
import com.zlst.demo.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

@Service
public class MyBookService {
    @Autowired
    MyBookMapper myBookMapper;
    @Autowired
    BookService bookService;
    @Autowired
    FileUtil fileUtil;
    public String setAndUpdateMyBook(Book book) {
        MyBook myBook = new MyBook();
//        先写入book表
        int bid = book.getId();
        String bookPath = StringUtils.getNewString(book.getTitle(), StringType.BOOK_PATH);
        String allRolls = bookPath + "/allRolls.txt";
        File file2 = new File(allRolls);
//        if (file2.exists()){
//            file2.delete();
//        }//除非是前端，对此接口的访问控制改好，不然不加此段代码
        try {
            file2.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        myBook.setBid(bid);
        myBook.setPath(bookPath);
        myBook.setTitle(book.getTitle());
        if (myBookMapper.findMyBook(book.getTitle()) != null) {
            myBookMapper.updateBook(myBook);
        }else {
            myBookMapper.insertBook(myBook);
        }
        return bookPath;
    }
    @Async
    public void createRoll(int bid, int index, String name) {
//        根据书的id，index，以及name，
        MyBook myBook = myBookMapper.findMyBookById(bid);
        String rollPath= myBook.getPath() + "/mb/" + index + name;//卷名对应的章节目录文件夹
        String rollFilePath = rollPath + ".txt";//卷名对应的章节目录文件
        String allRolls = myBook.getPath() + "/allRolls.txt";//放置卷名对象的json的文件
        File file = new File(rollPath);
        File file1 = new File(rollFilePath);
        File file2 = new File(allRolls);
        Roll roll = new Roll();
        roll.setIndex(index);
        roll.setRollName(name);
        roll.setChange(true);
        String json= JSON.toJSONString(roll);//关键
        if (!file.exists()){
            file.mkdirs();
        }
        try {
            file1.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileUtil.writeText(file2.getAbsolutePath(),json + "\n",true);
    }
    @Async
    public void createChapter(int bid, int rollIndex, String rollName, int index, String name) {
        MyBook myBook = myBookMapper.findMyBookById(bid);
        String rollPath= myBook.getPath() + "/mb/" + rollIndex + rollName + "/" + index + name + ".txt";
        String rollFilePath = myBook.getPath() + "/mb/" + rollIndex + rollName + ".txt";
        File file = new File(rollPath);
//        File file1 = new File(rollFilePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EditableChapter editableChapter = new EditableChapter();
        editableChapter.setIndex(index);
        editableChapter.setName(name);
        editableChapter.setChange(true);
        String json= JSON.toJSONString(editableChapter);//关键
        fileUtil.writeText(rollFilePath,json + "\n",true);
    }
//这是获取所有的章节的方法
//    这个后续用缓存，调用的本地太多了
    public LinkedList<Roll> getRollsAndChapters(int bid) {
        LinkedList<Roll> rollLinkedList = new LinkedList<Roll>();
        MyBook myBook = myBookMapper.findMyBookById(bid);
        String path = myBook.getPath() +  "/allRolls.txt";
        BufferedReader bufferedReader =null;
        File file = new File(path);
        String s = null;
        Roll roll = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(path));
            s = bufferedReader.readLine();
//            System.out.println("s的值" + s + "\n" +  "s是否为空" + (s ==null));
            if (s == null || s.length() <= 5) {
            }else {
                do {
//                    把s变成对象并且，添加到列表中，同时对对象进行处理
                    roll = JSONObject.parseObject(s,Roll.class);
                    path = StringUtils.getRollPath(roll,myBook.getTitle());
                    LinkedList<EditableChapter> editableChapters = fileUtil.getEditChapter(path);
                    roll.setChapters(editableChapters);
                    rollLinkedList.add(roll);
                    s = bufferedReader.readLine();
                }while (s != null);
            }
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
        return rollLinkedList;
    }

    public String getChapterText(int bid, int rollIndex, String rollName, int index, String name) {
//        在这里，组件对应章节的地址，并且，查询其内容
        String path = "d:/mybook/" + myBookMapper.findMyBookById(bid).getTitle() + "/mb/" + rollIndex + rollName +"/" +index + name + ".txt";
        System.out.println("获取的文件路径是"+path);
        String result = fileUtil.getText(path);
        return result;
    }
//向指定的章节文件，写入章节内容
    public void writeText(Integer bid, Integer rollIndex, String rollName, Integer index, String name, String text) {
        System.out.println("得到了参数" + bid + rollIndex + rollName + text);
        String path = "d:/mybook/" + myBookMapper.findMyBookById(bid).getTitle() + "/mb/" + rollIndex + rollName +"/" +index + name + ".txt";
        fileUtil.writeText(path,text);
    }
//获取正在编辑的，状态为在更的books
    public List<Book> getNowBooks() {
        List<Book> books = null;
//        第一种方法，从数据库获取列表，按列表查询,一点都不循环
        List<Integer> bookIds = myBookMapper.findNowBooks();
        books = bookService.listByIds(bookIds);
        return books;
    }
//删除本地章节的方法
    @Async
    public void deleteChapter(int bid, int rollIndex, String rollName, int index, String name) {
            MyBook myBook = myBookMapper.findMyBookById(bid);
            String rollPath= myBook.getPath() + "/mb/" + rollIndex + rollName + "/" + index + name + ".txt";
            String rollFilePath = myBook.getPath() + "/mb/" + rollIndex + rollName + ".txt";
            File file = new File(rollPath);
            File file1 = new File(rollFilePath);
            if (file.exists()) {
                file.delete();
            }
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;
            String s = null;
            StringBuilder stringBuilder = new StringBuilder();
//            EditableChapter editableChapter = new EditableChapter();
//            editableChapter.setIndex(index);
//            editableChapter.setName(name);
//            editableChapter.setChange(true);
//            String json= JSON.toJSONString(editableChapter);//关键
            String zz = "[\\{]{1}" + "[^\\{\\}]+" + name +"\"" +  "[\\}]{1}";
            fileUtil.editText(file1,zz,"");
//            try {
//                bufferedReader = new BufferedReader(new FileReader(file1));
//                do {
//                    s = bufferedReader.readLine();
//                    if (s != null) {
//                        if (json.equals(s) || s.endsWith(json.substring(json.indexOf("index")))) {
//                            s = "";
//                            stringBuilder.append(s);
//                        }else {
//                            stringBuilder.append(s + "\n");
//                        }
//                    }
//                }while (s != null);
//                bufferedWriter= new BufferedWriter(new FileWriter(file1));
//                bufferedWriter.write(stringBuilder.toString());
//                bufferedWriter.flush();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }finally {
//                if (!(bufferedWriter == null)) {
//                    try {
//                        bufferedWriter.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                if (!(bufferedReader == null)) {
//                    try {
//                        bufferedReader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
        }
    @Async
//    更新章节的方法
    public boolean editChapter(int bid, int rollIndex, String rollName, int index, String name, String newName) {
        MyBook myBook = myBookMapper.findMyBookById(bid);
        String rollPath= myBook.getPath() + "/mb/" + rollIndex + rollName + "/" + index + name + ".txt";
        String rollFilePath = myBook.getPath() + "/mb/" + rollIndex + rollName + ".txt";
        File file = new File(rollPath);
        File file1 = new File(myBook.getPath() + "/mb/" + rollIndex + rollName + "/" + index + newName + ".txt");
        boolean b = file.renameTo(file1);//更改名字
        EditableChapter editableChapter = new EditableChapter();
        editableChapter.setIndex(index);
//        editableChapter.setName(name);
        editableChapter.setChange(true);
//        String json= JSON.toJSONString(editableChapter);//关键
        editableChapter.setName(newName);
        String newJson = JSON.toJSONString(editableChapter);
//        正则应该表示的字符串是一个，以{“  ”}作为开头结尾，中间出现一次index，一次name，不止一次逗号，并且
        String zz = "[\\{]{1}" + "[^\\{\\}]+" + name +"\"" +  "[\\}]{1}";
//        System.out.println("正则表达式为" +zz);
        fileUtil.editText(new File(rollFilePath),zz,newJson);
        return b;
    }
//这里是删除空白卷文件夹的方法
    public boolean deleteRoll(int bid, int index, String name) {
//        根据书的id，index，以及name，
        MyBook myBook = myBookMapper.findMyBookById(bid);
        String rollPath= myBook.getPath() + "/mb/" + index + name;//卷名对应的章节目录文件夹
        String rollFilePath = rollPath + ".txt";//卷名对应的章节目录文件
        String allRolls = myBook.getPath() + "/allRolls.txt";//放置卷名对象的json的文件
        File file = new File(rollPath);
        File file1 = new File(rollFilePath);
        File file2 = new File(allRolls);
//        System.out.println("接受了参数" + bid  + index);
//        Roll roll = new Roll();
//        roll.setIndex(index);
//        roll.setRollName(name);
//        roll.setChange(true);
//        String json= JSON.toJSONString(roll);//关键
        boolean b  = false;
        if (file.delete() && file1.delete()) {
            b = true;
        }
//        System.out.println("删除了文件");
//        文件删除了，修改记录
        String zz = "[\\{]{1}" + "[^\\{\\}]+"  + name +"\"" +  "[\\}]{1}" + "\n";
        fileUtil.editText(file2,zz,"");
//        System.out.println("改变了内容");
        return b;
    }


}
