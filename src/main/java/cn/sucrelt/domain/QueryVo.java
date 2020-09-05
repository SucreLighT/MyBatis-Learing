package cn.sucrelt.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: sucre
 * @date: 2020/09/03
 * @time: 15:46
 */
public class QueryVo implements Serializable {
    private User user;
    private List<Integer> ids;

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
