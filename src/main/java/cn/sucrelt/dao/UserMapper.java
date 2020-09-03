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

    /**
     * 更新用户
     *
     * @param user
     */
    void updateUser(User user);

    /**
     * 根据id删除用户
     *
     * @param userId
     */
    void deleteUser(Integer userId);

    /**
     * 根据id查询一个用户
     *
     * @param userID
     * @return User
     */
    User findById(Integer userID);

    /**
     * 根据name模糊查询用户
     *
     * @param username
     * @return List<User>
     */
    List<User> findByName(String username);

    /**
     * 查询所有记录数
     *
     * @return
     */
    int findTotalCount();

}
