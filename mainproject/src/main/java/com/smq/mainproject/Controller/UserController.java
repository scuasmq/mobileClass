package com.smq.mainproject.Controller;

import com.smq.mainproject.Entity.Result;
import com.smq.mainproject.Entity.ReturnUserClassList;
import com.smq.mainproject.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @CrossOrigin
    @RequestMapping(value = "/register")
    @ResponseBody
    public Result GetUserRegisterInfo(@RequestBody User jsondata){
        //现在不支持重复注册检测
        //发送的json数据为{"username":xxx,"password":xxx,"mailbox":xxx,"school":xxx,"school_id":xxx}
        String username = jsondata.getUsername();
        String password = jsondata.getPassword();
        String mailbox = jsondata.getMailbox();
        String school = jsondata.getSchool();
        int school_id = jsondata.getSchool_id();
        String pre_sql="select * from userinfo";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(pre_sql);
        int uid=list.size()+1;
        String sql="insert into userinfo (username,password,mailbox,school,school_id,uid) values(?,?,?,?,?,?)";
        Object args[]={username,password,mailbox,school,school_id,uid};
        int flag = jdbcTemplate.update(sql,args);
        if (flag <=0) return new Result("500","FAILURE");
        return new Result("200","SUCCESS");
    }


    @CrossOrigin
    @RequestMapping(value = "/login/{username}/{password}")
    @ResponseBody
    public Map<String,Object> Login(@PathVariable String username,@PathVariable String password){
        String sql="select * from userinfo where username = ? and password = ?";
        Object args[]={username,password};
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql,args);
        Map<String,Object>map=new HashMap<>();
        if(list.size()==0){
            map.put("uid",-1);
        }
        else{
            map.put("uid",(int)list.get(0).get("uid"));
        }
        return map;
    }

    @CrossOrigin
    @RequestMapping(value = "/get_class_list/{uid}")
    @ResponseBody
    public ReturnUserClassList getClassList(@PathVariable int uid){
        String sql1="select class_name,class_id from create_class where uid = ?";
        String sql2="select distinct class_name,class_id from join_class where uid = ?";
        Object args1[]={uid};
        List<Map<String,Object>>list1=jdbcTemplate.queryForList(sql1,args1);
        List<Map<String,Object>>list2=jdbcTemplate.queryForList(sql2,args1);
        return new ReturnUserClassList(list1,list2);
    }
}
