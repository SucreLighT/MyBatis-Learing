package cn.sucrelt.test;

import cn.sucrelt.dao.UserMapper;
import cn.sucrelt.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/08/31
 * @time: 09:22
 */
public class MybatisTest {

    /**
     * 入门案例,使用xml或注解方式使用MyBatis
     * @param args
     */
    public static void main(String[] args) throws IOException {
        //1.读取配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        //3.使用工厂生产SqlSession对象
        SqlSession session = factory.openSession();
        //4.使用SqlSession对象创建Dao接口的代理对象
        UserMapper userMapper = session.getMapper(UserMapper.class);
        //5.使用代理对象执行方法
        List<User> users = userMapper.findAll();
        for (User user :
                users) {
            System.out.println(user);
        }
        //6.释放资源
        session.close();
        in.close();
    }
}
