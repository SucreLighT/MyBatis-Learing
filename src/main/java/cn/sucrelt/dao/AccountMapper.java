package cn.sucrelt.dao;

import cn.sucrelt.domain.Account;
import cn.sucrelt.domain.AccountUser;

import java.util.List;

public interface AccountMapper {

    /**
     * 查询所有账户信息
     *
     * @return
     */
    List<Account> findAll();


    /**
     * 查询所有账户，并带有用户名和地址信息
     *
     * @return
     */
    List<AccountUser> findAllAccount();
}
