package com.zlst.demo.mapper;

import com.zlst.demo.polo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public User selectUserByName(String username);
    public void insertUser(User user);
}
