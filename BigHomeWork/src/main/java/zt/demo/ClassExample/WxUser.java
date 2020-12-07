package zt.demo.ClassExample;

public class WxUser {
    String username;
    String password;
    String openid;
    int uid;
    public WxUser(){}
    public WxUser(String username, String password, String openid,int uid) {
        this.username = username;
        this.password = password;
        this.openid = openid;
        this.uid = uid;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
