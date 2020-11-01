package com.smq.mainproject.Entity;

public class User {
    String username;
    String password;
    String mailbox;
    String school;
    int school_id;

    public String getUsername() { return username; }
    public void setUsername(String username) {this.username = username; }

    public void setPassword(String password) { this.password = password; }
    public String getPassword(){ return password; }

    public void setMailbox(String mailbox) { this.mailbox = mailbox; }
    public String getMailbox(){ return mailbox;}

    public void setSchool(String school){ this.school = school; }
    public String getSchool(){ return school; }

    public int getSchool_id() {return school_id; }
    public void setSchool_id(int school_id) { this.school_id = school_id; }
}
