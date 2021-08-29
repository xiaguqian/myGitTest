package com.zlst.demo.service;

import com.zlst.demo.mapper.UserMapper;
import com.zlst.demo.polo.User;
import com.zlst.demo.result.Result;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

//    public User get(String username, String password){
////        根据传入的用户名密码，查询出一个user
//        User user = userMapper.selectUserByName(username);
//        if (user == null) {
//            return null;
//        }else{
////            如果不为空，取出盐
//            String salt = user.getSalt();
////            将密码和盐一起哈希
//            int times = 2;
//            // 得到 hash 后的密码
//            String encodedPassword = new SimpleHash("md5", password, salt, times).toString();
//            if (encodedPassword.equals(user.getPassword())) {
////                如果两者相等，返回User
//                return user;
//            }
//            return null;
//        }
//    }

    public boolean isExist(String username) {
//        根据传入的用户名密码，查询出一个user
        boolean result = true;
        User user = userMapper.selectUserByName(username);
        if (user == null) {
            result = false;
        }
        return result;
    }

    public void add(User user) {
        userMapper.insertUser(user);
    }

    public User findByUsername(String userName) {
        User user = null;
        user = userMapper.selectUserByName(userName);
        return user;
    }
}
