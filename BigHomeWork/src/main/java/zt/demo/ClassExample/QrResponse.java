package zt.demo.ClassExample;

public class QrResponse {
    String state;
    String username;
    int uid;
    public QrResponse(){}
    public QrResponse(String state, String username, int uid) {
        this.state = state;
        this.username = username;
        this.uid = uid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
