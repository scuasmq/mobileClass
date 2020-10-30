package com.smq.testdbconn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DemoApplication {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/getdatabase")
    public Integer getDatabase(){
        String name = "2";
        String sql = "select count(*) from userinfo where school = ?";
        return this.jdbcTemplate.queryForObject(sql, Integer.class, name);
    }
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
