package cn.sucrelt.test;

import cn.sucrelt.dao.AccountMapper;
import cn.sucrelt.domain.Account;
import cn.sucrelt.domain.AccountUser;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/09/06
 * @time: 13:16
 */
public class MybatisAccountTest {

    private InputStream in;
    private SqlSession sqlSession;
    private AccountMapper accountMapper;

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
        accountMapper = sqlSession.getMapper(AccountMapper.class);
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
     * 测试查询所有账户
     *
     * @throws IOException
     */
    @Test
    public void testFindAll() throws IOException {
        //使用代理对象执行方法
        List<Account> accounts = accountMapper.findAll();
        for (Account account :
                accounts) {
            System.out.println(account);
        }
    }

    /**
     * 查询所有账户，同时包含用户名称
     *
     * @throws IOException
     */
    @Test
    public void testFindAllAccount() throws IOException {
        //使用代理对象执行方法
        List<AccountUser> accountUsers = accountMapper.findAllAccount();
        for (AccountUser accountUser :
                accountUsers) {
            System.out.println(accountUser);
        }
    }
}
