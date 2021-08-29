package com.zlst.demo.mapper;

import com.zlst.demo.polo.EBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EBookMapper {
    public String selectEBook(String name);
}
