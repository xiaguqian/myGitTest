package com.zlst.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdminUserRoleMapper {
    public List<Integer> selectAllRole(int uid);
}
