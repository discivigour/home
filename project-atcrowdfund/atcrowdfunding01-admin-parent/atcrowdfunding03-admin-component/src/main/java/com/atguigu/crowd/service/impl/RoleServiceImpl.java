package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.RoleExample;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-11-11 17:33
 */
@Service
public class RoleServiceImpl implements RoleService{

    @Autowired(required = false)
    private RoleMapper rm;

    @Override
    public PageInfo<Role> getPageInfo(Integer pageNum, Integer pageSize, String keyword) {
// 1.开启分页功能
        PageHelper.startPage(pageNum, pageSize);
// 2.执行查询
        List<Role> roleList = rm.selectRoleByKeyword(keyword);
// 3.封装为 PageInfo 对象返回
        return new PageInfo<>(roleList);
    }

    @Override
    public void saveRole(Role role) {
        rm.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        rm.updateByPrimaryKey(role);
    }

    @Override
    public void removeRole(List<Integer> r) {
        RoleExample example = new RoleExample();

        RoleExample.Criteria criteria = example.createCriteria();

        //delete from t_role where id in (5,8,12)
        criteria.andIdIn(r);

        rm.deleteByExample(example);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {
        return rm.selectAssignedRole(adminId);
    }
    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        return rm.selectUnAssignedRole(adminId);
    }
}
