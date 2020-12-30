package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Student;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import com.atguigu.crowd.entity.Admin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author shkstart
 * @create 2020-11-09 16:31
 */
@Controller
public class TestHandler {
    @Autowired
    private AdminService as;
    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @ResponseBody
    @RequestMapping("/send/compose/object.json")
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody Student student,HttpServletRequest req) {
        boolean b = CrowdUtil.judgeRequestType(req);
        logger.info("res="+b);
        logger.info(student.toString());
        // 将“查询”到的Student对象封装到ResultEntity中返回
        ResultEntity<Student> resultEntity = ResultEntity.successWithData(student);
        return resultEntity;
    }

    @RequestMapping("/send/array/three.html")
    public String testAjax3(@RequestBody List<Integer> arr){
        System.out.println(arr);
        return "target";
    }
    @RequestMapping("/send/array/one.html")
    public String testAjax1(@RequestParam("array[]")List<Integer>arr){
        System.out.println(arr);
        return "target";
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap mm,HttpServletRequest req){
//        boolean b = CrowdUtil.judgeRequestType(req);
//        logger.info("res="+b);
        String s=null;
        System.out.println(s.length());
        List<Admin> list=as.getAll();
        System.out.println("测试"+list);
        mm.addAttribute("adminList",list);
        return "target";
    }
}
