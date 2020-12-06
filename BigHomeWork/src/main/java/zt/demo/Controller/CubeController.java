//该Controller是用在另一个项目里的,所以可以忽略。
//为了偷懒部署，所以将两个项目的RestFul Api合并在了一个project里

package zt.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import zt.demo.ClassExample.Result;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/cube")
public class CubeController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @CrossOrigin
    @RequestMapping(value = "/submit_score")
    @ResponseBody
    public Result SubmitScore(@RequestBody Map<String,Object>map){
        int score=(int)map.get("score");
        String username=(String)map.get("username");
        System.out.println(score);
        String sql="insert into cubeinfo (username,score,finish_time) value(?,?,?)";
        Object[] args={username,score,(String) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())};
        int flag=jdbcTemplate.update(sql,args);
        if(flag<=0){
            return new Result("500","FAILURE");
        }
        return new Result("200","SUCCESS");
    }
    @CrossOrigin
    @RequestMapping(value = "return_list")
    @ResponseBody
    public List<Map<String,Object>> ReturnList(){
        String sql="select * from cubeinfo";
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql);
        return list;
    }
}