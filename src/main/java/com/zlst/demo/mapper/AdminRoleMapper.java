package com.zlst.demo.mapper;

import com.zlst.demo.polo.AdminRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface AdminRoleMapper {
    //    根据全部的rids，查询全部的role
    AdminRole findAllById(Integer rid);
//根据rid获取全部的mid
    List<Integer> findAllPid(Integer rid);
}