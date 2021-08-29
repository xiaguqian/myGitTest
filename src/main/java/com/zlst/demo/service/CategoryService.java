package com.zlst.demo.service;

import com.zlst.demo.mapper.CategoryMapper;
import com.zlst.demo.polo.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    public Category getCategoryByCid(Integer cid) {
        return categoryMapper.getCategoryByCid(cid);
    }
}
