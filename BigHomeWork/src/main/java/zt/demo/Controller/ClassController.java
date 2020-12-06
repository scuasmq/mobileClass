package zt.demo.Controller;
import zt.demo.ClassExample.DeleteFileManager;
import zt.demo.ClassExample.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ClassController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //查询用户创建和加入的课程
    @CrossOrigin
    @RequestMapping(value = "/get_class_list/{uid}")
    @ResponseBody
    public Map<String,Object> getClassList(@PathVariable int uid){
        String sql1="select distinct class_name,class_id from create_class where uid = ?";
        String sql2="select distinct class_name,class_id from join_class where uid = ?";
        Object args[]={uid};
        List<Map<String,Object>>list1=jdbcTemplate.queryForList(sql1,args);
        List<Map<String,Object>>list2=jdbcTemplate.queryForList(sql2,args);
        Map<String,Object>ans=new HashMap<>();
        ans.put("list1",list1);
        ans.put("list2",list2);
        return ans;
    }

    //创建课程
    @CrossOrigin
    @RequestMapping(value = "/insertClass")
    @ResponseBody
    public Result insertClass(@RequestBody Map<String,Object>map) {
        int uid=(int)map.get("uid");
        String class_name=(String) map.get("class_name");
        //获取用户名
        String sql="select username from userinfo where uid = ?";
        Object[] args={uid};
        List<Map<String,Object>>pre_list=jdbcTemplate.queryForList(sql,args);
        if(pre_list.size()==0){ return new Result("500","USER NOT EXISTS"); }
        String username=(String) pre_list.get(0).get("username");
        //判断是否创建了相同的课程
        sql="select * from create_class where uid = ? and class_name = ?";
        args=new Object[]{uid,class_name};
        pre_list=jdbcTemplate.queryForList(sql,args);
        if(pre_list.size()>0){
            return new Result("500","CLASS EXISTS");
        }
        //插入数据库
        sql="insert into create_class (uid,username,class_name,class_id) values(?,?,?,?)";
        args=new Object[]{uid,username,class_name,null};
        int flag=jdbcTemplate.update(sql,args);
        if(flag<=0){ return new Result("500","FAILURE"); }
        else{ return new Result("200","SUCCESS"); }
    }

    //加入课程
    @CrossOrigin
    @RequestMapping(value = "/joinClass/{uid}/{class_id}")
    @ResponseBody
    public Result JoinClass(@PathVariable int uid,@PathVariable int class_id){
        //查询有无此课程
        String sql="select class_name from create_class where class_id = ?";
        Object[] args={class_id};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);
        if(list.size()==0){ return new Result("500","CLASS NOT EXISTS"); }
        String class_name=(String) list.get(0).get("class_name");
        //查询该用户名字
        sql="select username from userinfo where uid = ?";
        args= new Object[]{uid};
        list=jdbcTemplate.queryForList(sql,args);
        if(list.size()==0){ return new Result("500","USER NOT EXISTS");}
        String username=(String)list.get(0).get("username");
        //查询是否加入了该课程
        sql="select * from join_class where uid = ? and class_id = ?";
        args=new Object[]{uid,class_id};
        list=jdbcTemplate.queryForList(sql,args);
        if(list.size()>0)return new Result("500","ALREADY JOIN CLASS");
        //插入数据库信息
        sql="insert into join_class (uid,username,class_id,class_name) values(?,?,?,?)";
        args=new Object[]{uid,username,class_id,class_name};
        int flag=jdbcTemplate.update(sql,args);
        if(flag<=0){ return new Result("500","FAILURE"); }
        return new Result("200","SUCCESS");
    }

    //得到参加该课程的学生的所有信息
    @CrossOrigin
    @RequestMapping(value = "/getClassStudentList/{class_id}")
    @ResponseBody
    public List<Map<String,Object>> getClassNameList(@PathVariable int class_id){
        String sql="select username,mailbox,school,school_id,uid from userinfo where uid IN (select distinct uid from join_class where class_id = ?)";
        Object[] args={class_id};
        return jdbcTemplate.queryForList(sql,args);
    }

    //得到该课程下所有套题的信息
    @CrossOrigin
    @RequestMapping(value = "/getClassGroupList/{class_id}")
    @ResponseBody
    public List<Map<String,Object>> getClassGroupList(@PathVariable int class_id){
        String sql="select distinct group_name,publish_time from problemlist where class_id = ?";
        Object[] args={class_id};
        return jdbcTemplate.queryForList(sql,args);
    }

    //删除一个课程
    @CrossOrigin
    @RequestMapping(value = "/delete_one_class")
    @ResponseBody
    public Result DeleteOneClass(@RequestBody Map<String,Object>map){
        int class_id=(int)map.get("class_id");
        Object[] args={class_id};
        String table_list="broadcast,create_class,fileinfo,join_class,problemlist,user_join_test,user_join_vote," +
                "voteinfo,homework,user_join_homework";
        String[] arr_list=table_list.split(",");
        String base_sql="delete from ";
        for(int i=0;i<arr_list.length;i++){
            String sql=base_sql+arr_list[i]+" where class_id = ?";
            int flag=jdbcTemplate.update(sql,args);
        }
        //删除课程存放文件的文件夹
        String class_path="/www/wwwroot/mobile_class.cn/files/"+class_id+"/";
        File class_dir=new File(class_path);
        boolean res=new DeleteFileManager().DeleteDir(class_dir);
        if(!res)return new Result("500","FAILURE");
        //删除课程作业系统的文件夹
        class_path="/www/wwwroot/mobile_class.cn/homework/"+class_id+"/";
        class_dir=new File(class_path);
        res=new DeleteFileManager().DeleteDir(class_dir);
        if(!res)return new Result("500","FAILURE");
        return new Result("200","SUCCESS");
    }
    //抓取学生
    @CrossOrigin
    @RequestMapping(value = "/grab_students")
    @ResponseBody
    public Result GrabStudents(@RequestBody Map<String,Object>map){
        int class_id=(int)map.get("class_id");
        String class_name=(String)map.get("class_name");
        String namelist=(String)map.get("data");
        String[] list=namelist.split(",");
        for(int i=0;i<list.length;i++){
            String sql="select * from userinfo where username = ?";
            Object[] args={list[i]};
            List<Map<String,Object>>list1=jdbcTemplate.queryForList(sql,args);
            if(list1.size()==0){
                return new Result("500","USER NOT REGISTER");
            }
            sql="insert into join_class (uid,username,class_id,class_name) values(?,?,?,?)";
            args=new Object[]{(int)list1.get(0).get("uid"),list[i],class_id,class_name};
            int flag=jdbcTemplate.update(sql,args);
            if(flag<=0)return new Result("500","FAILURE");
        }
        return new Result("200","SUCCESS");
    }
}