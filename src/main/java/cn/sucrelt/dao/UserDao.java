package cn.sucrelt.dao;

import cn.sucrelt.domain.User;

import java.util.List;

public interface UserDao {
    /**
     * 查询所有记录
     *
     * @return
     */
    List<User> findAll();
}
