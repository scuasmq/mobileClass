package zt.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import zt.demo.ClassExample.QrCode;
import zt.demo.ClassExample.QrCodeUtil;
import zt.demo.ClassExample.QrResponse;
import zt.demo.ClassExample.WxUser;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequestMapping("qr_code")
@RestController
public class QrCodeController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @CrossOrigin
    @RequestMapping(value = "/uuid_path")
    @ResponseBody
    public QrCode RetQrCode() {
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);

        //TODO: 需要修改路径
        String qr_code_path = "\\qr_code\\"+uuid.toString()+".jpg";
        qr_nologo(uuid,qr_code_path);
        return new QrCode(uuid, qr_code_path);
    }

//    @RequestMapping("/qr_nologo_save")
    public void qr_nologo(UUID uuid,String dest_path){
        String requestUrl = uuid.toString();
        try{
            //TODO: 需要修改路径
            String logo_path = null; //".\\data\\logo.jpg"
            QrCodeUtil.save(requestUrl,logo_path,dest_path);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/qr_state/{uuid}")
    @ResponseBody
    public QrResponse qr_state(@PathVariable String uuid){
        //TODO: (1) 查询数据库是否有该uuid，有:success, 无:waiting
        //TODO: (2) 改进一下，用缓存的map而不是数据库查询
        String sql = "select username,user_id from qr_info where uuid = ?";
        Object args[]  = {uuid};
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, args);
        String del_sql = "delete from qr_info where uuid = ?";
        Object del_args[] = {uuid};
        int f = jdbcTemplate.update(del_sql,del_args);
        if(f<=0) System.out.println("qr_code 删除失败");

        if (list.size()>0){
            return new QrResponse("success",(String)list.get(0).get("username"),(int)list.get(0).get("user_id"));
        }
        else{
            return new QrResponse("waiting",null,0);
        }
    }

    // wx 扫码 提交接口
    @RequestMapping(value = "/qr_scaned/{uuid}/{openid}")
    @ResponseBody
    public String qr_scaned(@PathVariable String uuid,@PathVariable String openid ){
        String pre_sql = "select username,password,uid from wxuserinfo where openid = ?";
        Object pre_args[] = {openid};
        List<Map<String,Object>> list = jdbcTemplate.queryForList(pre_sql, pre_args);
        if(list.size()<=0) return "请先在微信登陆";
        String username = (String)list.get(0).get("username");
        String password = (String)list.get(1).get("password");
        int uid = (int)list.get(2).get("uid");
        String sql = "insert into qr_info(uuid,username,uid) values(?,?,?)";
        Object args[] = {uuid,username,uid};
        int f = jdbcTemplate.update(sql,args);
        if(f<=0) return "扫描提交失败";
        return "扫码成功";
    }
}
