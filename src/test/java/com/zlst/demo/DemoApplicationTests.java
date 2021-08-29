package com.zlst.demo;

import com.zlst.demo.polo.*;
import com.zlst.demo.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
@SpringBootTest
class DemoApplicationTests {
//    测试jdbc连接数据库
    @Autowired
    DataSource dataSource;
    @Autowired
    UserService userService;
    @Autowired
    BookService bookService;
    @Autowired
    EBookService eBookService;
    @Autowired
    MyBookService myBookService;
    @Autowired
    AdminPermissionService adminPermissionService;
    @Test
    void contextLoads() throws SQLException {
        ResultSet resultSet = null;
        System.out.println(dataSource.getClass());
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select id, username,password from user ");
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            System.out.println(id + "--" + username + "--" + password);
        }
        System.out.println("jdbc结束");
        connection.close();
    }

    @Test
    void bookMapperTest(){
        List<AdminRole> adminRoles = adminPermissionService.getAllRole("admin");
        for (AdminRole a:adminRoles
             ) {
            System.out.println("遍历所有的role" + a);
        }
//        利用复杂sql得到结果
        Set<String> stringSet =  adminPermissionService.listPermissionURLsByUser("admin");
        for (String s: stringSet
             ) {
            System.out.println("利用复杂sql得到结果" + s);
        }
    }
    @Test
    void utilTest() {
        String str = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS")).format(new Date());
        System.out.println("开始时间：" + str);
//        myBookService.getNowBooks();
//        GetVideo.httpDownload("https://lajiaoapi.com/watch?url=https://vip5.bobolj.com/20210730/aKO9nZgW/index.m3u8","D:/Download/test.mp4");
        System.out.println("结束时间：" + str);
    }

}
