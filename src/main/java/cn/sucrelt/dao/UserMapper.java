package cn.sucrelt.dao;

import cn.sucrelt.domain.User;


import java.util.List;

public interface UserMapper {
    /**
     * 查询所有记录
     *
     * @return
     */
    // @Select("select * from user")
    List<User> findAll();

    /**
     * 保存用户
     *
     * @param user
     */
    void saveUser(User user);
}
