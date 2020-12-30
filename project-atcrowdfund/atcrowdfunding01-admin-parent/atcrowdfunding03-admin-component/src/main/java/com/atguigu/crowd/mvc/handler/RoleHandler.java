package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.service.api.RoleService;
import com.atguigu.crowd.util.ResultEntity;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shkstart
 * @create 2020-11-11 17:37
 */
@RestController
public class RoleHandler {
    @Autowired
    private RoleService rs;

    ////@ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultEntity<String> removeByRoleIdAarry(@RequestBody List<Integer> roleIdList) {

        rs.removeRole(roleIdList);

        return ResultEntity.successWithoutData();
    }
    //@ResponseBody
    @RequestMapping("/role/update.json")
    public ResultEntity<String> updateRole(Role role) {
        rs.updateRole(role);
        return ResultEntity.successWithoutData();
    }
    //@ResponseBody
    @RequestMapping("/role/save.json")
    public ResultEntity<String> saveRole(Role role){
        rs.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    @PreAuthorize("hasRole('部长')")
    //@ResponseBody
    @RequestMapping("/role/get/page/info.json")
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
            @RequestParam(value="pageSize", defaultValue="5") Integer pageSize,
            @RequestParam(value="keyword", defaultValue="") String keyword) {
// 调用 Service 方法获取分页数据
        PageInfo<Role> pageInfo = rs.getPageInfo(pageNum, pageSize, keyword);
// 封装到 ResultEntity 对象中返回（如果上面的操作抛出异常， 交给异常映射机制处理）
        return ResultEntity.successWithData(pageInfo);
    }
}
