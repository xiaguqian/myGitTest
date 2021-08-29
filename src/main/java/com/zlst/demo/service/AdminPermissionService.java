package com.zlst.demo.service;

import com.zlst.demo.mapper.AdminPermissionMapper;
import com.zlst.demo.polo.AdminMenu;
import com.zlst.demo.polo.AdminPermission;
import com.zlst.demo.polo.AdminRole;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminPermissionService {
    @Autowired
    AdminRoleService adminRoleService;
    @Autowired
    AdminPermissionMapper adminPermissionMapper;
    @Autowired
    AdminRolePermissionService adminRolePermissionService;
//    根据用户名查询得到该用户全部的role
    public Set<String> listPermissionURLsByUser(String username) {
//        首先按名称，查询所有的rolelist
        Set<String> result = new HashSet<>();
        List<AdminPermission> adminPermissions = getAllPermission(username);
        for (AdminPermission a: adminPermissions
             ) {
            result.add(a.getUrl());
        }
        return result;
    }
    //    根据用户名查询得到该用户全部的role的name
    public Set<String> listRoelNameByUser(String username) {
//        首先按名称，查询所有的rolelist
        Set<String> result = new HashSet<>();
        for (AdminRole a: getAllRole(username)
        ) {
            result.add(a.getName());
        }
        return result;
    }
//    根据角色列表，查询所有的权限对象,里面利用简单的sql
    public List<AdminPermission> getAllPermission(String username){
//        首先按名称，查询所有的rid
        List<Integer> rids = adminRoleService.getAllRids(username);
        List<AdminPermission> result = new LinkedList<>();
//        遍历rids，查询每个rids对应的pids，并查询pid对应的p对象
        for (Integer  i: rids
        ) {
//            查询每一个i对应的所有权限，利用是role和permission
            List<AdminPermission> adminPermissions= this.getAllPermissionById(i);
            for (AdminPermission a:adminPermissions
                 ) {
                result.add(a);
            }
        }
        return result;
    }
    //    根据输入的rid,查询出来所有的pid，然后根据pid，查询所有的permission
    public List<AdminPermission> getAllPermissionById(int rid) {
        List<Integer> pids  = getAllPid(rid);
        List<AdminPermission> adminPermissions = new LinkedList<>();
        AdminPermission adminPermission = null;
        for (Integer i:pids
             ) {
            adminPermission = adminPermissionMapper.getPermissionBypid(i);
            adminPermissions.add(adminPermission);
        }
        return adminPermissions;
    }
    public List<Integer> getAllPid(int rid){
        List<Integer> pids = adminPermissionMapper.findAllPid(rid);
        return pids;
    }
    public List<AdminRole> getAllRole(String username){
        //        首先查询该用户全部的role，得到rolelist
        List<AdminRole> adminRoleList = adminRoleService.listRolesByUser(username);
        return adminRoleList;
    }
    public boolean needFilter(String requestAPI) {
        List<AdminPermission> ps = adminPermissionMapper.findAll();
        for (AdminPermission p: ps) {
            if (p.getUrl().equals(requestAPI)) {
                return true;
            }
        }
        return false;
    }

    public List<AdminMenu> getAllMenu(String username) {
        //        首先按名称，查询所有的rid
        List<Integer> rids = adminRoleService.getAllRids(username);
        List<AdminMenu> result = new LinkedList<>();
//        遍历rids，查询每个rids对应的pids，并查询pid对应的p对象
        for (Integer  i: rids
        ) {
//            查询每一个根据rids查询所有的mid
            List<Integer> mids = adminRoleService.getAllMid(i);
            for (Integer mid: mids
                 ) {
                result.add(adminPermissionMapper.getMenuById(mid));
            }
        }
        handleMenus(result);
        return result;
    }

    public void handleMenus(List<AdminMenu> menus) {
        for (AdminMenu menu : menus) {
            List<AdminMenu> children = adminPermissionMapper.getAllByParentId(menu.getId());
            menu.setChildren(children);
        }

        Iterator<AdminMenu> iterator = menus.iterator();
        while (iterator.hasNext()) {
            AdminMenu menu = iterator.next();
            if (menu.getParent_id() != 0) {
                iterator.remove();
            }
        }
    }

}
