package zt.demo.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import zt.demo.ClassExample.BroadCast;
import zt.demo.ClassExample.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class BroadCastController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    //返回所有的公告
    @CrossOrigin
    @RequestMapping(value = "/return_all_broadcast/{class_id}")
    @ResponseBody
    public BroadCast ReturnAllBroadCast(@PathVariable int class_id) {
        //传入一个课程号，返回与这个课程号所有有关的公告
        String sql = "select bc_id,publish_time,message from broadcast where class_id = ? ";
        Object args[] = {class_id};
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,args);
        int number = list.size();
        return new BroadCast(number,list);
    }
    //发布一个公告
    @CrossOrigin
    @RequestMapping(value = "/publish_one_broadcast/{class_id}")
    @ResponseBody
    public Result PublishOneBroadCast(@RequestBody Map<String,Object> map, @PathVariable int class_id){
        String sql = "insert into broadcast (bc_id,class_id,message,publish_time) values(?,?,?,?)";
        Object args[] = {null,class_id,(String)map.get("message"),(String) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())};
        int flag = jdbcTemplate.update(sql,args);
        if (flag<=0){
            return new Result("500","FAILURE");
        }
        return new Result("200","SUCCESS");
    }
    //删除一个公告
    @CrossOrigin
    @RequestMapping(value = "/delete_one_broadcast")
    @ResponseBody
    public Result DeleteOneBroadCast(@RequestBody Map<String,Object>map){
        int bc_ic=(int)map.get("bc_id");
        String sql="delete from broadcast where bc_id = ?";
        Object[] args={bc_ic};
        int flag=jdbcTemplate.update(sql,args);
        if(flag<=0)return new Result("500","FAILURE");
        return new Result("200","SUCCESS");
    }
}
