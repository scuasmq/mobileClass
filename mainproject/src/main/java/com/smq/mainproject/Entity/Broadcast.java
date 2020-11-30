package com.smq.mainproject.Entity;

public class Broadcast {
    public Broadcast(int bc_id, int class_id, String message, String publish_time) {
        this.bc_id = bc_id;
        this.class_id = class_id;
        this.message = message;
        this.publish_time = publish_time;
    }

    int bc_id;
    int class_id;
    String message;
    String publish_time;

    public int getBc_id() {
        return bc_id;
    }

    public void setBc_id(int bc_id) {
        this.bc_id = bc_id;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPublish_time() {
        return publish_time;
    }

    public void setPublish_time(String publish_time) {
        this.publish_time = publish_time;
    }
}
