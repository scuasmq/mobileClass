package com.smq.mainproject.Entity;

import java.util.List;
import java.util.Map;

public class ReturnUserClassList {
    List<Map<String,Object>> list1;
    List<Map<String,Object>> list2;

    public ReturnUserClassList(){ }
    public ReturnUserClassList(List<Map<String,Object>>list1,List<Map<String,Object>>list2){
        this.list1=list1;
        this.list2=list2;
    }

    public List<Map<String, Object>> getList1() {
        return list1;
    }
    public void setList1(List<Map<String, Object>> list1) {
        this.list1 = list1;
    }

    public List<Map<String, Object>> getList2() {
        return list2;
    }
    public void setList2(List<Map<String, Object>> list2) {
        this.list2 = list2;
    }
}
