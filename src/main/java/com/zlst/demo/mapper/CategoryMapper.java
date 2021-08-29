package com.zlst.demo.mapper;

import com.zlst.demo.polo.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    Category getCategoryByCid(Integer cid);
}
