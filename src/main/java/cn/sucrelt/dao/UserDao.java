package cn.sucrelt.dao;

import cn.sucrelt.domain.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface UserDao {
    /**
     * 查询所有记录
     *
     * @return
     */
    // @Select("select * from user")
    List<User> findAll();
}
