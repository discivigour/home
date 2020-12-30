package com.atguigu.crowd;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



// 在类上标记必要的注解，Spring整合Junit
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {
	
	@Autowired
	private DataSource dataSource;
	@Autowired
	private RoleMapper rm;
	@Autowired(required = false)//不加required就会报错，但是运行没有任何问题
	private AdminMapper adminMapper;
	@Autowired(required = false)
	private AdminService as;

	@Test
	public void testRoleSave() {
		for(int i = 0; i < 235; i++) {
			rm.insert(new Role(null, "role"+i));
		}
	}

	@Test
	public void testSaveAdminMulti() {
		for(int i = 0; i < 352; i++) {
			adminMapper.insert(new Admin(null, "loginAcct" + i, "userPswd" + i, "userName" + i,
					"email" + i + "@qq.com", null));
		}
	}
	@Test
	public void testTx(){
		Admin admin=new Admin(null,"megumi","icy tail","惠","megumi@qq.com",null);
		as.saveAdmin(admin);
	}
	@Test
	public void testInsertAdmin() {
		Admin admin = new Admin(null, "megumi", "icy tail", "汤姆", "tom@qq.com", null);
		int count = adminMapper.insert(admin);

		// 如果在实际开发中，所有想查看数值的地方都使用sysout方式打印，会给项目上线运行带来问题！
		// sysout本质上是一个IO操作，通常IO的操作是比较消耗性能的。如果项目中sysout很多，那么对性能的影响就比较大了。
		// 即使上线前专门花时间删除代码中的sysout，也很可能有遗漏，而且非常麻烦。
		// 而如果使用日志系统，那么通过日志级别就可以批量的控制信息的打印。
		System.out.println("受影响的行数="+count);
	}
	
	@Test
	public void testConnection() throws SQLException {
		Connection connection = dataSource.getConnection();
		System.out.println(connection);
	}

}
