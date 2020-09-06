package cn.sucrelt.domain;

import java.io.Serializable;

/**
 * @description:
 * @author: sucre
 * @date: 2020/09/06
 * @time: 12:55
 */
public class Account implements Serializable {
    private Integer id;
    private Integer uid;
    private Double money;

    /**
     * 从表实体包含一个主表实体的对象引用
     */
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "AccountMapper{" +
                "id=" + id +
                ", uid=" + uid +
                ", money=" + money +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
