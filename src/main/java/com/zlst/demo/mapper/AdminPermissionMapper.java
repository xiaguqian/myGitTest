package com.zlst.demo.mapper;

import com.zlst.demo.polo.AdminMenu;
import com.zlst.demo.polo.AdminPermission;
import com.zlst.demo.polo.AdminRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface AdminPermissionMapper {
//根据rid，查询所有的pid
    List<Integer> findAllPid(int rid);
//    根据所有的pid,查询所有的permission对象
    AdminPermission getPermissionBypid(Integer pid);
//    根据mid获取所有的menu
    AdminMenu getMenuById(Integer mid);
//  查询菜单的所有子菜单
    List<AdminMenu> getAllByParentId(int parent_id);

    List<AdminPermission> findAll();
}
