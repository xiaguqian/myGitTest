package com.zlst.demo.service;

import com.zlst.demo.mapper.AdminRoleMapper;
import com.zlst.demo.polo.AdminRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Evan
 * @date 2019/11
 */
@Service
public class AdminRoleService {
    @Autowired
    AdminRoleMapper adminRoleMapper;
    @Autowired
    UserService userService;
    @Autowired
    AdminUserRoleService adminUserRoleService;
//    @Autowired
//    AdminPermissionService adminPermissionService;
//    @Autowired
//    AdminRolePermissionService adminRolePermissionService;
//    @Autowired
//    AdminMenuService adminMenuService;

//    public List<AdminRole> listWithPermsAndMenus() {
//        List<AdminRole> roles = adminRoleDAO.findAll();
//        List<AdminPermission> perms;
//        List<AdminMenu> menus;
//        for (AdminRole role : roles) {
//            perms = adminPermissionService.listPermsByRoleId(role.getId());
//            menus = adminMenuService.getMenusByRoleId(role.getId());
//            role.setPerms(perms);
//            role.setMenus(menus);
//        }
//        return roles;
//    }
//
//    public List<AdminRole> findAll() {
//        return adminRoleDAO.findAll();
//    }
//
//
//    public void addOrUpdate(AdminRole adminRole) {
//        adminRoleDAO.save(adminRole);
//    }
//
    public List<AdminRole> listRolesByUser(String username) {
        List<Integer> rids = getAllRids(username);
//        根据所有的rid，查询出来所有的角色
        List<AdminRole> result = new LinkedList<>();
        for (Integer  i: rids
             ) {
            result.add(adminRoleMapper.findAllById(i));
        }
        return result;
    }
    public List<Integer> getAllRids(String username ){
        //        根据用户名，获取用户的id
        int uid = userService.findByUsername(username).getId();
//        根据id获取所有的rid
        List<Integer> rids = adminUserRoleService.listAllByUid(uid);
        return rids;
    }

    List<Integer> getAllMid(Integer rid){
//        根据rid获取mid的方法
        List<Integer> mids = adminRoleMapper.findAllPid(rid);
        return mids;
    }
//
//    public AdminRole updateRoleStatus(AdminRole role) {
//        AdminRole roleInDB = adminRoleDAO.findById(role.getId());
//        roleInDB.setEnabled(role.isEnabled());
//        return adminRoleDAO.save(roleInDB);
//    }
//
//    public void editRole(@RequestBody AdminRole role) {
////        adminRoleDAO.save(role);
////        adminRolePermissionService.savePermChanges(role.getId(), role.getPerms());
//    }
}
