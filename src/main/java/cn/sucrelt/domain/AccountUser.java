package cn.sucrelt.domain;

/**
 * @description:扩展Account类，使用该子类实现Account和User表的一对一多表查询
 * @author: sucre
 * @date: 2020/09/06
 * @time: 13:31
 */
public class AccountUser extends Account {
    private String username;
    private String address;

    @Override
    public String toString() {
        return super.toString() + "AccountUser{" +
                "username='" + username + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
