package zt.demo.ClassExample;

public class QrResponse {
    String state;
    String username;
    int user_id;
    public QrResponse(){}
    public QrResponse(String state, String username, int user_id) {
        this.state = state;
        this.username = username;
        this.user_id = user_id;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
