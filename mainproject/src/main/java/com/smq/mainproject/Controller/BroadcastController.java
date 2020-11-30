package com.smq.mainproject.Controller;

import com.smq.mainproject.Entity.Broadcast;
import com.smq.mainproject.Entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class BroadcastController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @CrossOrigin
    @RequestMapping(value = "/publish_one_broadcast/{class_id}")
    @ResponseBody
    public Result Publish_one_broadcast(@RequestBody Broadcast jsondata, @PathVariable String class_id){
        int classId = Integer.parseInt(class_id);
        String message = jsondata.getMessage();
        String publish_time = jsondata.getPublish_time();
        String sql = "insert into broadcast values(?,?,?,?)";
        String pre_sql = "select count(*) from broadcast";
        int num = jdbcTemplate.queryForObject(pre_sql,Integer.class);
        Object args[] = {num+1,class_id,message,publish_time};
        int flag = jdbcTemplate.update(sql,args);
        if (flag<=0){
            return new Result("500","FAILURE");
        }
        return new Result("200","SUCCESS");
    }
}