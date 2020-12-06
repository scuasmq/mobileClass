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
public class FileController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    //文件的绝对路径前缀
    private String dir_path="/www/wwwroot/mobile_class.cn/files/";
    //上传一个文件
    @CrossOrigin
    @RequestMapping(value = "/file_uploader")
    @ResponseBody
    public Result FileUpLoader(@RequestParam(value = "file_file") MultipartFile file,
                               @RequestParam(value = "file_classid") int class_id,
                               @RequestParam(value = "file_uploader") int uid){
        //文件的课程文件夹路径
        String path1=dir_path+class_id;
        File temp_dir=new File(path1);
        if(!temp_dir.exists()){
            temp_dir.mkdir();
        }
        //文件的名字
        String filename=file.getOriginalFilename();
        long file_size=file.getSize();
        String path2=path1+"/"+filename;
        File temp_file=new File(path2);
        if(!temp_file.exists()){
            try {
                //存取文件内容和获取相对路径
                file.transferTo(temp_file);
                String relative_path="files/"+class_id+"/"+filename;

                //获取用户名字
                String pre_sql1="select * from userinfo where uid = ?";
                Object[] pre_args1={uid};
                List<Map<String,Object>>pre_list=jdbcTemplate.queryForList(pre_sql1,pre_args1);
                String owner_name=(String) pre_list.get(0).get("username");
                //插入数据库信息
                String sql="insert into fileinfo(file_id,file_name,file_size,class_id,upload_time,owner_name,file_path,download_cnt) values(?,?,?,?,?,?,?,?)";
                Object[] args={null,filename,file_size,class_id,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),owner_name,relative_path,0};
                int flag=jdbcTemplate.update(sql,args);
                if(flag<=0)return new Result("500","FAILURE");
                return new Result("200","SUCCESS");
            }
            catch (Exception e){
                e.printStackTrace();
                return new Result("500","FAILURE");
            }
        }
        else{
            return new Result("300","EXISTS");
        }
    }
    //返回所有文件的信息
    @CrossOrigin
    @RequestMapping(value = "/return_files/{class_id}")
    @ResponseBody
    public List<Map<String,Object>> ReturnFiles(@PathVariable int class_id){
          String sql="select * from fileinfo where class_id = ?";
          Object args[]={class_id};
          List<Map<String,Object>>list = jdbcTemplate.queryForList(sql,args);
          return list;
    }
    //删除一个文件
    @CrossOrigin
    @RequestMapping(value = "/delete_one_file")
    @ResponseBody
    public Result DeleteOneFile(@RequestBody Map<String,Object>map){
        int file_id=(int)map.get("file_id");
        String sql="select file_path from fileinfo where file_id = ?";
        Object[] args={file_id};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);
        if(list.size()==0){
            return new Result("500","NO THIS FILE ID");
        }
        String abs_path="/www/wwwroot/mobile_class.cn/"+(String) list.get(0).get("file_path");
        File file=new File(abs_path);
        try {
            boolean res=file.delete();
            if(!res)return new Result("500","FAILURE");
            sql="delete from fileinfo where file_id = ?";
            int flag=jdbcTemplate.update(sql,args);
            if(flag<=0)return new Result("500","FAILURE");
        }
        catch (Exception e){
            e.printStackTrace();
            return new Result("500","FAILURE");
        }
        return new Result("200","SUCCESS");
    }
    //下载一个文件
    @CrossOrigin
    @RequestMapping(value = "/download_one_file")
    @ResponseBody
    public Result DownLoadOneFile(@RequestBody Map<String,Object>map){
        int file_id=(int)map.get("file_id");
        String sql="select download_cnt from fileinfo where file_id = ?";
        Object[] args={file_id};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);
        if(list.size()==0)return new Result("500","FAILURE");
        int tmp_cnt=(int)list.get(0).get("download_cnt")+1;
        sql="update fileinfo set download_cnt = ? where file_id = ?";
        args=new Object[]{tmp_cnt,file_id};
        int flag=jdbcTemplate.update(sql,args);
        return new Result("200","SUCCESS");
    }

}