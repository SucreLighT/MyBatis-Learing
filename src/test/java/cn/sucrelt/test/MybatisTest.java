package cn.sucrelt.test;

import cn.sucrelt.dao.UserMapper;
import cn.sucrelt.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/08/31
 * @time: 09:22
 */
public class MybatisTest {
    private InputStream in;
    private SqlSession sqlSession;
    private UserMapper userMapper;

    /**
     * @throws IOException
     */
    @Before
    public void init() throws IOException {
        //1.读取配置文件
        in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.创建SqlSessionFactory工厂
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in);
        //3.使用工厂生产SqlSession对象
        sqlSession = factory.openSession();
        //4.使用SqlSession对象创建Dao接口的代理对象
        userMapper = sqlSession.getMapper(UserMapper.class);
    }

    /**
     * @throws IOException
     */
    @After
    public void destroy() throws IOException {
        //提交事务
        sqlSession.commit();

        in.close();
        sqlSession.close();
    }

    /**
     * 测试查询所有方法
     *
     * @throws IOException
     */
    @Test
    public void testFindAll() throws IOException {
        //使用代理对象执行方法
        List<User> users = userMapper.findAll();
        for (User user :
                users) {
            System.out.println(user);
        }
    }

    /**
     * 测试保存方法
     */
    @Test
    public void testSave() throws IOException {
        User user = new User();
        user.setUsername("mybatis saveuser");
        user.setBirthday(new Date());
        user.setSex("男");
        user.setAddress("南京");

        //使用代理对象执行方法
        userMapper.saveUser(user);
    }

    /**
     * 测试更新方法
     */
    @Test
    public void testUpdate() throws IOException {
        User user = new User();
        user.setId(51);
        user.setUsername("mybatis updateUser");
        user.setBirthday(new Date());
        user.setSex("男");
        user.setAddress("南京");

        //使用代理对象执行方法
        userMapper.updateUser(user);
    }

    /**
     * 测试删除方法
     */
    @Test
    public void testDelete() throws IOException {

        //使用代理对象执行方法
        userMapper.deleteUser(48);
    }
}
