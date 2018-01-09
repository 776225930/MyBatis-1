package com.example.mybatis.test;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.example.mybatis.Employee;
import com.example.mybatis.dao.EmployeeMapper;
/**
 * 1、接口式编程
 * 	原生：		Dao		====>  DaoImpl
 * 	mybatis：	Mapper	====>  xxMapper.xml
 * 
 * 2、SqlSession代表和数据库的一次会话；用完必须关闭；
 * 3、SqlSession和connection一样都是非线程安全。每次使用都应该去获取新的对象。
 * 4、mapper接口没有实现类，但是mybatis会为这个接口生成一个代理对象。
 * 		（将接口和xml进行绑定）
 * 		EmployeeMapper empMapper =	sqlSession.getMapper(EmployeeMapper.class);
 * 5、两个重要的配置文件：
 * 		mybatis的全局配置文件：包含数据库连接池信息，事务管理器信息等...系统运行环境信息
 * 		sql映射文件：保存了每一个sql语句的映射信息：
 * 					将sql抽取出来。
 * @author JHao
 *
 */
public class MyBatisTest {
	private SqlSessionFactory sessionFactory=null;
	//非线程安全。这样写不对,仅在此这么用
	private SqlSession session=null;
	@Before
	public void init() throws IOException{
		String resource="mybatis-config.xml";
		InputStream in=Resources.getResourceAsStream(resource);
	    sessionFactory=new SqlSessionFactoryBuilder().build(in);
	    session=sessionFactory.openSession();
	}
	@After
	public void destroy(){
		session.close();
	}
	/**
	 * 1.根据xml文件(全局配置文件)创建一个SqlSessionFactory 
	 * 2、sql映射文件；配置了每一个sql，以及sql的封装规则等。 
	 * 3、将sql映射文件注册在全局配置文件中
	 * 4、写代码：
	 * 		1）、根据全局配置文件得到SqlSessionFactory；
	 * 		2）、使用sqlSession工厂，获取到sqlSession对象使用他来执行增删改查
	 * 			一个sqlSession就是代表和数据库的一次会话，用完关闭
	 * 		3）、使用sql的唯一标志来告诉MyBatis执行哪个sql。sql都是保存在sql映射文件中的。
	 * @throws IOException
	 */
	@Test
	public void test() throws IOException {
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		// 获取SqlSession实例,能直接执行已经映射的sql语句
		SqlSession session = sqlSessionFactory.openSession();
		try {
			Employee emp = session.selectOne("selectEmp", 1);
			System.out.println(emp);
		} finally {
			session.close();
		}
	}
	@Test
	public void test1() throws IOException{
		// 3、获取接口的实现类对象
		//会为接口自动的创建一个代理对象，代理对象去执行增删改查方法
		EmployeeMapper mapper=session.getMapper(EmployeeMapper.class);
		
		Employee employee=mapper.getEmpById(1);
		System.out.println(mapper.getClass());
        System.out.println(employee);
	}

}
