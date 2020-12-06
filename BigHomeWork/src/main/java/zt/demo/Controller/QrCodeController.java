package zt.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import zt.demo.ClassExample.QrCode;
import zt.demo.ClassExample.QrCodeUtil;
import zt.demo.ClassExample.QrResponse;

import java.io.OutputStream;
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
        String sql = "";
        Object args[]  = {"aaa"};
        int num =this.jdbcTemplate.queryForObject(sql,Integer.class,args);
        if(num>0){

        }
        else{

        }
    }
}
