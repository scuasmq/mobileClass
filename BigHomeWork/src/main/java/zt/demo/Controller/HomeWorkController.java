package zt.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zt.demo.ClassExample.Result;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class HomeWorkController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String dir_path="/www/wwwroot/mobile_class.cn/homework";
    //发布一个作业
    @CrossOrigin
    @RequestMapping(value = "/homework_uploader")
    @ResponseBody
    public Result UploadHomeWork(@RequestBody Map<String,Object>map){
        int class_id=(int)map.get("class_id");
        String title=(String)map.get("title");
        String content=(String)map.get("content");
        String ddl=(String)map.get("ddl");
        String sql="insert into homework (hw_id,class_id,title,content,ddl,publish_time) values(?,?,?,?,?,?)";
        Object[] args={null,class_id,title,content,ddl,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())};
        int flag=jdbcTemplate.update(sql,args);
        if(flag<=0)return new Result("500","FAILURE");
        return new Result("200","SUCCESS");
    }
    //返回作业列表
    @CrossOrigin
    @RequestMapping(value = "/return_homework_list")
    @ResponseBody
    public List<Map<String,Object>> ReturnHomeWorkList(@RequestBody Map<String,Object>map){
        int class_id=(int)map.get("class_id");
        String sql="select * from homework where class_id = ?";
        Object[] args={class_id};
        return jdbcTemplate.queryForList(sql,args);
    }
    //学生提交作业
    @CrossOrigin
    @RequestMapping(value = "/submit_one_homework")
    @ResponseBody
    public Result SubmitOneHomeWork(@RequestParam(value = "homework_file") MultipartFile file,
                                    @RequestParam(value = "homework_uid") int uid,
                                    @RequestParam(value = "homework_cid") int cid,
                                    @RequestParam(value = "homework_hid") int hw_id){
        //课程文件夹
        String class_path=dir_path+"/"+cid+"/";
        File class_file=new File(class_path);
        if(!class_file.exists()){
            boolean res=class_file.mkdir();
            if(!res)return new Result("500","FAILURE");
        }
        //用户文件夹
        String user_path=class_path+uid;
        File user_file=new File(user_path);
        if(!user_file.exists()){
            boolean res=user_file.mkdir();
            if(!res)return new Result("500","FAILURE");
        }
        String filename=file.getOriginalFilename();
        String file_path=user_path+"/"+filename;
        File file_file=new File(file_path);
        if(!file_file.exists()){
            try {
                file.transferTo(file_file);
                String rel_path="homework/"+cid+"/"+uid+"/"+filename;
                String sql="select username from userinfo where uid = ?";
                Object[] args={uid};
                List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);
                if(list.size()==0){
                    return new Result("500","FAILURE");
                }
                String username=(String) list.get(0).get("username");
                sql="insert into user_join_homework (uid,username,class_id,hw_id,submit_time,file_path) values(?,?,?,?,?,?)";
                args=new Object[]{uid,username,cid,hw_id,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),rel_path};
                int flag=jdbcTemplate.update(sql,args);
                if(flag<=0)return new Result("500","FAILURE");
            }
            catch (Exception e){
                e.printStackTrace();
                return new Result("500","FAILURE");
            }
        }
        return new Result("200","SUCCESS");
    }
    //老师查看已提交的名单
    @CrossOrigin
    @RequestMapping(value = "/check_one_homework_by_teacher")
    @ResponseBody
    public List<Map<String,Object>> CheckOneHomeWorkByTeacher(@RequestBody Map<String,Object>map){
        int class_id=(int)map.get("class_id");
        int hw_id=(int)map.get("hid");
        String sql="select uid,username,submit_time,file_path from user_join_homework where class_id = ? and hw_id = ?";
        Object[] args={class_id,hw_id};
        return jdbcTemplate.queryForList(sql,args);
    }
    //返回学生提交的作业
    @CrossOrigin
    @RequestMapping(value = "/check_one_homework_by_student")
    @ResponseBody
    public List<Map<String,Object>> CheckOneHomeWorkByStudent(@RequestBody Map<String,Object>map){
        int class_id=(int)map.get("class_id");
        int uid=(int)map.get("uid");
        String sql="select hw_id,submit_time,file_path from user_join_homework where class_id = ? and uid = ?";
        Object[] args={class_id,uid};
        return jdbcTemplate.queryForList(sql,args);
    }

}
