package com.atguigu.crowd.handler;



import javax.servlet.http.HttpSession;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;




import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;


import com.atguigu.crowd.util.ResultEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberHandler {


//	@Autowired(required = false)
//	private RedisRemoteService redisRemoteService;

	@Autowired(required = false)
	private MySQLRemoteService mySQLRemoteService;

	/**
	 * 用户登出
	 * @param session
	 * @return
	 */
	@RequestMapping("/auth/member/logout")
	public String logout(HttpSession session) {

		session.invalidate();

		return "redirect:/";
	}

	/**
	 * 用户登录
	 * @param loginacct
	 * @param userpswd
	 * @param modelMap
	 * @param session
	 * @return
	 */
	@RequestMapping("/auth/member/do/login")
	public String login(
			@RequestParam("loginacct") String loginacct,
			@RequestParam("userpswd") String userpswd,
			ModelMap modelMap,
			HttpSession session) {

		// 1.调用远程接口根据登录账号查询MemberPO对象
		ResultEntity<MemberPO> resultEntity =
				mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);

		if(ResultEntity.FAILED.equals(resultEntity.getResult())) {

			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());

			return "member-login";

		}

		MemberPO memberPO = resultEntity.getData();

		if(memberPO == null) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);

			return "member-login";
		}

		// 2.比较密码
		String userpswdDataBase = memberPO.getUserpswd();

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		boolean matcheResult = passwordEncoder.matches(userpswd, userpswdDataBase);

		if(!matcheResult) {
			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);

			return "member-login";
		}

		// 3.创建MemberLoginVO对象存入Session域
		MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
		session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);

		return "redirect:http://www.crowd.com/auth/member/to/center/page";
	}

    /**
     * 用户注册到数据库
     * @param memberVO
     * @param modelMap
     * @return
     */
	@RequestMapping("/auth/do/member/register")
	public String register(MemberVO memberVO, ModelMap modelMap) {


		// 1.执行密码加密
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String userpswdBeforeEncode = memberVO.getUserpswd();

		String userpswdAfterEncode = passwordEncoder.encode(userpswdBeforeEncode);

		memberVO.setUserpswd(userpswdAfterEncode);

		// 2.执行保存
		// ①创建空的MemberPO对象
		MemberPO memberPO = new MemberPO();

		// ②复制属性
		BeanUtils.copyProperties(memberVO, memberPO);
        System.out.println(memberPO);
        // ③调用远程方法
		ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);

		if(ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {

			modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());

			return "member-reg";
		}

		// 使用重定向避免刷新浏览器导致重新执行注册流程
		return "redirect:/auth/member/to/login/page";
	}

}
