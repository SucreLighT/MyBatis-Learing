package cn.sucrelt.dao;

import cn.sucrelt.domain.Role;

import java.util.List;

public interface RoleMapper {

    /**
     * 查询身份表中所有信息
     *
     * @return
     */
    List<Role> findAll();
}
