package zt.demo.ClassExample;

import java.util.List;
import java.util.Map;

public class BroadCast {
    int bc_number;
    List<Map<String,Object>> data;
    public BroadCast(){}
    public BroadCast(int bc_number,List<Map<String,Object>>data){
        this.bc_number=bc_number;
        this.data=data;
    }
    public int getBc_number() {
        return bc_number;
    }

    public void setBc_number(int bc_number) {
        this.bc_number = bc_number;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
