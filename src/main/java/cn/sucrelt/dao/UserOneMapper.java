package cn.sucrelt.dao;

import cn.sucrelt.domain.QueryVo;
import cn.sucrelt.domain.User;

import java.util.List;

public interface UserOneMapper {
    /**
     * 查询所有记录
     *
     * @return
     */
    List<User> findAll();

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
     * 根据QueryVo中的条件查询
     *
     * @param vo
     * @return List<User>
     */
    List<User> findByVo(QueryVo vo);

    /**
     * 根据传入的参数条件查询
     * @param user 查询条件，可能为空也可能包括user中属性的若干条
     * @return List<User>
     */
    List<User> findUserByCondition(User user);

    /**
     * 根据QueryVo中提供的id集合查询用户信息
     * @param vo
     * @return
     */
    List<User> findUserByIds(QueryVo vo);
}
