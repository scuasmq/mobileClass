package zt.demo.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import zt.demo.ClassExample.Result;
import zt.demo.ClassExample.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.util.*;

@RestController
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    //用户注册
    @CrossOrigin
    @RequestMapping(value = "/register")
    @ResponseBody
    public Result GetUserRegisterInfo(@RequestBody User jsondata){
        //获取新用户的数据
        String username = jsondata.getUsername();
        String password = jsondata.getPassword();
        String mailbox = jsondata.getMailbox();
        String school = jsondata.getSchool();
        String school_id = jsondata.getSchool_id();
        //判断名字是否已经被使用过
        String sql="select * from userinfo where username = ?";
        Object[] args={username};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);
        if(list.size()!=0){return new Result("500","USER EXISTS");}
        //插入用户数据
        sql = "insert into userinfo (username,password,mailbox,school,school_id,uid,photo_path) values(?,?,?,?,?,?,?)";
        args = new Object[]{username,password,mailbox,school,school_id,null,null};
        int flag = jdbcTemplate.update(sql,args);
        if (flag <= 0) return new Result("500","FAILURE");
        return new Result("200","SUCCESS");
    }
    //用户登录
    @CrossOrigin
    @RequestMapping(value = "/login")
    @ResponseBody
    public Map<String,Object> Login(@RequestBody Map<String,Object>map){
        String username=(String)map.get("username");
        String password=(String)map.get("password");
        //查询用户是否存在
        String sql="select uid from userinfo where username = ? and password = ?";
        Object args[]={username,password};
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql,args);
        Map<String,Object>ans=new HashMap<>();
        //不存在就返回-1，存在就返回uid
        if(list.size()==0){ ans.put("uid",-1);}
        else{ ans.put("uid",(int)list.get(0).get("uid"));}
        return ans;
    }
    //找回密码
    @CrossOrigin
    @RequestMapping(value = "/get_password")
    @ResponseBody
    public Result ReturnUserPassword(@RequestBody Map<String,Object>map){
        String username=(String) map.get("username");
        String mailbox=(String) map.get("mailbox");
        String sql="select password from userinfo where username = ? and mailbox = ?";
        Object args[]={username,mailbox};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);
        if(list.size()==0){
            return new Result("500","FAILURE");
        }
        else{
            return new Result("200", (String) list.get(0).get("password"));
        }
    }
    //返回特定用户的信息
    @CrossOrigin
    @RequestMapping(value = "/get_one_user_info/{uid}")
    @ResponseBody
    public Map<String,Object> ReturnOneStudentInfo(@PathVariable int uid){
        String sql="select username,mailbox,school,school_id,uid from userinfo where uid = ?";
        Object[] args={uid};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);
        Map<String,Object>ans=new HashMap<>();
        if(list.size()==0){
            ans.put("uid",-1);
            return ans;
        }
        return list.get(0);
    }
    //修改用户信息
    @CrossOrigin
    @RequestMapping(value = "/change_one_user_info")
    @ResponseBody
    public Result ChangeOneStudentInfo(@RequestBody Map<String,Object>map){
        int uid=(int)map.get("uid");
        String username=(String)map.get("username");
        String mailbox=(String)map.get("mailbox");
        String school=(String)map.get("school");
        String school_id=(String)map.get("school_id");

        String sql="select username from userinfo where uid = ?";
        Object[] args={uid};
        List<Map<String,Object>>pre_list=jdbcTemplate.queryForList(sql,args);
        if(pre_list.size()==0){
            return new Result("500","NO THIS USER");
        }
        String pre_username=(String) pre_list.get(0).get("username");
        boolean res=pre_username.equals(username);
        //修改了名字
        if(!res){
            //判断名字是否使用过
            sql="select * from userinfo where username = ?";
            args=new Object[]{username};
            List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);
            if(list.size()>0){
                return new Result("500","USERNAME EXISTS");
            }
        }
        //修改数据库
        sql="update userinfo set username = ?,mailbox = ?,school = ?,school_id = ? where uid = ?";
        args=new Object[]{username,mailbox,school,school_id,uid};
        int flag=jdbcTemplate.update(sql,args);
        if(flag<=0){
            return new Result("500","FAILURE");
        }
        return new Result("200","SUCCESS");
    }
    //修改用户密码
    @CrossOrigin
    @RequestMapping(value = "/change_pwd")
    @ResponseBody
    public Result ChangePassword(@RequestBody Map<String,Object>map){
        int uid=(int)map.get("uid");
        String oldpwd=(String)map.get("oldpwd");
        String newpwd=(String)map.get("newpwd");
        String sql="select * from userinfo where uid = ?";
        Object[] args={uid};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);
        if(!oldpwd.equals(list.get(0).get("password"))){
            return new Result("500","OLD PWD ERROR");
        }
        sql="update userinfo set password = ? where uid = ?";
        args=new Object[]{newpwd,uid};
        int flag=jdbcTemplate.update(sql,args);
        if(flag<=0)return new Result("500","FAILURE");
        else return new Result("200","SUCCESS");
    }
    //修改头像照片
    @CrossOrigin
    @RequestMapping(value = "/change_photo")
    @ResponseBody
    public Result ChangePhoto(@RequestParam(value = "avatar_avatar") MultipartFile file,
                              @RequestParam(value = "avatar_uid") int uid){
        String path="/www/wwwroot/mobile_class.cn/avatar/"+uid;
        File temp_dir = new File(path);
        if(!temp_dir.exists()){
            temp_dir.mkdir();
        }
        String filename=file.getOriginalFilename();
        String save_path=path+"/"+filename;
        File save_file=new File(save_path);
        try {
            file.transferTo(save_file);
            String relative_path="avatar/"+uid+"/"+filename;
            String sql="update userinfo set photo_path = ? where uid = ?";
            Object[] args={relative_path,uid};
            int flag=jdbcTemplate.update(sql,args);
            if(flag<=0)return new Result("500","FAILURE");
        }
        catch (Exception e){
            e.printStackTrace();
            return new Result("500","FAILURE");
        }
        return new Result("200","SUCCESS");
    }
    //返回头像的相对路径
    @CrossOrigin
    @RequestMapping(value = "/get_photo_path")
    @ResponseBody
    public Map<String,Object> GetPhotoPath(@RequestBody Map<String,Object>map){
        Map<String,Object>ans=new HashMap<>();
        int uid=(int)map.get("uid");
        String sql="select photo_path from userinfo where uid = ?";
        Object[] args={uid};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);
        if(list.size()==0) {
            ans.put("message","FAILURE");
            return ans;
        }
        String path=(String) list.get(0).get("photo_path");
        if(path==null)ans.put("path","default");
        else ans.put("path",path);
        return ans;
    }
}