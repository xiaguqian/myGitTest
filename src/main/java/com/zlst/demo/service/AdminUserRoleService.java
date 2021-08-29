package com.zlst.demo.service;

import com.zlst.demo.mapper.AdminUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class AdminUserRoleService {
    @Autowired
    AdminUserRoleMapper adminUserRoleMapper;
    public List<Integer> listAllByUid(int uid) {
//        根据uid，查询所有rid
        List<Integer> rids = null;
        rids = adminUserRoleMapper.selectAllRole(uid);
        return rids;
    }
}
