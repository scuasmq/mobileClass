package zt.demo.ClassExample;

import java.util.UUID;

public class QrCode {
    UUID uuid;
    String path;
    public QrCode(){}
    public QrCode(UUID uuid, String path){
        this.uuid = uuid;
        this.path = path;
    }
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


}
