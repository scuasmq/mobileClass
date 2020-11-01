package com.smq.mainproject.Controller;

import com.smq.mainproject.Entity.ClassTable;
import com.smq.mainproject.Entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ClassController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @CrossOrigin
    @RequestMapping(value = "/insertClass")
    @ResponseBody
    public Result insertClass(@RequestBody ClassTable jsondata) {
        int user_id=jsondata.getUser_id();
        String class_name=jsondata.getClass_name();
        //System.out.println(user_id);
        //System.out.println(class_name);
        String sql="select * from create_class";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        int number=list.size()+1;
        String nxt_sql="insert into create_class (uid,class_name,class_id) values(?,?,?)";
        Object args[]={user_id,class_name,number};
        int flag=jdbcTemplate.update(nxt_sql,args);
        if(flag<=0){
            return new Result("500","FAILURE");
        }
        else{
            return new Result("200","SUCCESS");
        }
    }
    @CrossOrigin
    @RequestMapping(value = "/joinClass/{user_id}/{class_id}")
    @ResponseBody
    public Result JoinClass(@PathVariable int user_id,@PathVariable int class_id){
        //System.out.println("hello");
        String pre_sql="select * from create_class where class_id = ?";
        Object args[]={class_id};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(pre_sql,args);
        String class_name=(String) list.get(0).get("class_name");
        String sql="insert into join_class (uid,class_id,class_name) values(?,?,?)";
        Object args1[]={user_id,class_id,class_name};
        int flag=jdbcTemplate.update(sql,args1);
        if(flag<=0){
            return new Result("500","FAILURE");
        }
        else return new Result("200","SUCCESS");
    }

    @CrossOrigin
    @RequestMapping(value = "/getClassNameList/{class_id}")
    @ResponseBody
    public Map<String,Object> getClassNameList(@PathVariable int class_id){
        //System.out.println(1);
        String sql="select username from userinfo where uid IN (select distinct uid from join_class where class_id = ?)";
        Object args[]={class_id};
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,args);
        List<String>namelist=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            namelist.add((String)list.get(i).get("username"));
        }
        Map<String,Object> map=new HashMap<>();
        map.put("data",namelist);
        return map;
    }

    @CrossOrigin
    @RequestMapping(value = "/getClassGroupList/{class_id}")
    @ResponseBody
    public List<Map<String,Object>> getClassGroupList(@PathVariable int class_id){
        String sql="select distinct group_name,publish_time from problemlist where class_id = ?";
        Object args[]={class_id};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,class_id);
        return list;
    }

}
