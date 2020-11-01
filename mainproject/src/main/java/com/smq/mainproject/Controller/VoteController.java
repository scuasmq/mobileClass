package com.smq.mainproject.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class VoteController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
//
//    @CrossOrigin
//    @RequestMapping(value = "/insert_vote/{class_id}")
//    @ResponseBody
//    public List<Map<String,Object>> returnProblemsListByGroup(@RequestBody Map<String,Object>map, @PathVariable int class_id){
//
//    }
//    @CrossOrigin
//    @RequestMapping(value = "/submit_one_vote/{class_id}")
//    @ResponseBody
//    public List<Map<String,Object>> returnProblemsListByGroup(@RequestBody Map<String,Object>map,@PathVariable int class_id){
//
//    }

}
