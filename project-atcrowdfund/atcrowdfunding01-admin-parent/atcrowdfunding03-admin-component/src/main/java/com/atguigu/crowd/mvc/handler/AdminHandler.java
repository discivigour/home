package com.atguigu.crowd.mvc.handler;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;

@Controller
public class AdminHandler {
	
	@Autowired
	private AdminService adminService;
	@RequestMapping("/admin/to/editPage.html")
//	去往更新页面
	public String toEditPage(
			@RequestParam("adminId") Integer adminId,
			ModelMap modelMap
	) {

		// 1.根据adminId查询Admin对象
		Admin admin = adminService.getAdminById(adminId);

		// 2.将Admin对象存入模型
		modelMap.addAttribute("admin", admin);

		return "admin-edit";
	}
	//更新管理员信息
	@RequestMapping("/admin/update.html")
	public String updateAdmin(Admin admin,@RequestParam("pageNum") Integer pageNum, @RequestParam("keyword") String keyword){
			adminService.updateAdmin(admin);
			return "redirect:/admin/getPages.html?pageNum="+pageNum+"&keyword="+keyword;
	}

//	添加管理员
	@RequestMapping("/admin/save.html")
	public String addAdmin(Admin admin){
		adminService.saveAdmin(admin);
		return "redirect:/admin/getPages.html?pageNum="+Integer.MAX_VALUE;
	}

	//	删除管理员
	@RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
	public  String removeAdmin(
			@PathVariable("adminId") Integer adminId,
			@PathVariable("pageNum") Integer pageNum,
			@PathVariable("keyword") String keyword
	){
		adminService.removeAdmin(adminId);
		return "redirect:/admin/getPages.html?pageNum="+pageNum+"&keyword="+keyword;
	}

	//查询数据
	@RequestMapping("/admin/getPages.html")
	public String getAdminPage(
				// 使用@RequestParam注解的defaultValue属性，指定默认值，在请求中没有携带对应参数时使用默认值
				// keyword默认值使用空字符串，和SQL语句配合实现两种情况适配
				@RequestParam(value="keyword", defaultValue="") String keyword,

				// pageNum默认值使用1
				@RequestParam(value="pageNum", defaultValue="1") Integer pageNum,

				// pageSize默认值使用5
				@RequestParam(value="pageSize", defaultValue="5") Integer pageSize,

				ModelMap modelMap

			) {

		// 调用Service方法获取PageInfo对象
		PageInfo<Admin> pageInfo = adminService.getAdminPage(keyword, pageNum, pageSize);

		// 将PageInfo对象存入模型
		modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);

		return "admin-page";
	}
	
	@RequestMapping("/admin/do/logout.html")
	public String doLogout(HttpSession session) {
		
		// 强制Session失效
		session.invalidate();
		
		return "redirect:/admin/to/login/page.html";
	}
	
	@RequestMapping("/admin/do/login.html")
	public String doLogin(
				@RequestParam("loginAcct") String loginAcct,
				@RequestParam("userPswd") String userPswd,
				HttpSession session
			) {
		
		// 调用Service方法执行登录检查
		// 这个方法如果能够返回admin对象说明登录成功，如果账号、密码不正确则会抛出异常
		Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
		
		// 将登录成功返回的admin对象存入Session域
		session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
		
		return "redirect:/admin/to/main/page.html";
	}

}
