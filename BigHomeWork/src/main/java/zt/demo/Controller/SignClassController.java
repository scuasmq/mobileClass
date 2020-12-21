package zt.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
public class SignClassController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @CrossOrigin
    @RequestMapping(value = "/signclass/{class_id}/{uid}")
    @ResponseBody
    public int SignClass(@PathVariable int class_id,@PathVariable int uid){
        return 0;
    }

    @CrossOrigin
    @RequestMapping(value = "/publish_signclass/{class_id}/")
    @ResponseBody
    public int PublishSignClass(@PathVariable int class_id){
        return 0;
    }
}
