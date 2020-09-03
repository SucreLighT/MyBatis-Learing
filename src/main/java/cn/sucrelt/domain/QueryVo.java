package cn.sucrelt.domain;

import java.io.Serializable;

/**
 * @description:
 * @author: sucre
 * @date: 2020/09/03
 * @time: 15:46
 */
public class QueryVo implements Serializable {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
