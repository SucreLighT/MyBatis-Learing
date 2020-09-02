package cn.sucrelt.test;

import cn.sucrelt.dao.UserMapper;
import cn.sucrelt.dao.impl.UserMapperImpl;
import cn.sucrelt.domain.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/08/31
 * @time: 15:02
 */
public class MybatisTest2 {
    /**
     * 入门案例,使用Dao的实现类创建对象并使用MyBatis
     * @param args
     */
    public static void main(String[] args) throws IOException {
        //1.读取配置文件
        InputStream in = Resources.getResourceAsStream("SqlMapConfig.xml");
        //2.创建SqlSessionFactory工厂
        SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
        SqlSessionFactory factory = builder.build(in);
        //3.使用实现类创建Dao对象
        UserMapper userMapper = new UserMapperImpl(factory);
        //4.使用代理对象执行方法
        List<User> users = userMapper.findAll();
        for (User user :
                users) {
            System.out.println(user);
        }
        //5.释放资源
        in.close();
    }
}
