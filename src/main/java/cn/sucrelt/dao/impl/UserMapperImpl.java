package cn.sucrelt.dao.impl;

import cn.sucrelt.dao.UserMapper;
import cn.sucrelt.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * @description: 实现UserDao类，通过实现Dao类可以不通过SqlSession实例获取到实体类，但是没有必要，这样会增加需要编写的代码量
 * @author: sucre
 * @date: 2020/08/31
 * @time: 15:00
 */
public class UserMapperImpl implements UserMapper {

    private SqlSessionFactory factory;

    /**
     * 覆盖默认构造函数，保证该factory对象有值
     *
     * @param factory
     */
    public UserMapperImpl(SqlSessionFactory factory) {
        this.factory = factory;
    }

    public List<User> findAll() {
        //使用factory创建Session对象并执行查询方法
        SqlSession session = factory.openSession();
        List<User> users = session.selectList("cn.sucrelt.dao.UserDao.findAll");

        session.close();
        return users;
    }

    public void saveUser(User user) {

    }

    public void updateUser(User user) {

    }

    public void deleteUser(Integer userId) {

    }

    public User findById(Integer userID) {
        return null;
    }

    public List<User> findByName(String username) {
        return null;
    }

    public int findTotalCount() {
        return 0;
    }
}
