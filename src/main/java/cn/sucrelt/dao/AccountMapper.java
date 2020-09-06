package cn.sucrelt.dao;

import cn.sucrelt.domain.Account;

import java.util.List;

public interface AccountMapper {

    /**
     * 查询所有账户信息
     *
     * @return
     */
    List<Account> findAll();

}
