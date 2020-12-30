package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-11-09 9:13
 */
public interface AdminService {

    List<Admin> getAll();
    Admin getAdminByLoginAcct(String loginAcct, String userPswd);
    PageInfo<Admin> getAdminPage(String keyword, Integer pageNum, Integer pageSize);
    void removeAdmin(Integer keyId);
    void saveAdmin(Admin a);
    void updateAdmin(Admin a);

    Admin getAdminById(Integer adminId);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);
    Admin getAdminByLoginAcct(String username);
}
