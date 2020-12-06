package zt.demo.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import zt.demo.ClassExample.QrCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@RequestMapping("/home")
@Controller
public class HomeController {
    //生成带logo的二维码到response
    @RequestMapping("/qrcode")
    public void qrcode(HttpServletRequest request, HttpServletResponse response) {
        String requestUrl = "http://www.baidu.com";
        try {
            OutputStream os = response.getOutputStream();
            QrCodeUtil.encode(requestUrl, ".\\data\\logo.jpg", os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //生成不带logo的二维码到response
    @RequestMapping("/qrnologo")
    public void qrnologo(HttpServletRequest request, HttpServletResponse response) {
        String requestUrl = "http://www.baidu.com";
        try {
            OutputStream os = response.getOutputStream();
            QrCodeUtil.encode(requestUrl, null, os);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //把二维码保存成文件
    @RequestMapping("/qrsave")
    @ResponseBody
    public String qrsave() {
        String requestUrl = "http://www.baidu.com";
        try {
            String logo_path = null; //".\\data\\logo.jpg"
            QrCodeUtil.save(requestUrl,logo_path, ".\\data\\qrcode2.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "文件已保存";
    }

    //解析二维码中的文字
    @RequestMapping("/qrtext")
    @ResponseBody
    public String qrtext() {
        String url = "";
        try {
            url = QrCodeUtil.decode(".\\data\\qrcode2.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "解析到的url:"+url;
    }
    @RequestMapping("/dir")
    @ResponseBody
    public String getdir(){
        System.out.println(System.getProperty("user.dir"));//user.dir指定了当前的路径
        return System.getProperty("user.dir");
    }
}