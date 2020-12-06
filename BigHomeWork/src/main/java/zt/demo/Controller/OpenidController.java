package zt.demo.Controller;

import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

@RestController
public class OpenidController {
    String appid = "wx9de82d2d6e3c2c7a";
    String secret = "69bd9d73051cdc085b66b3398b84b064";
    @CrossOrigin
    @RequestMapping(value = "/openid/{code}")
    @ResponseBody
    public String RetOpenid(@PathVariable String code){
        BufferedReader in = null;
        String url="https://api.weixin.qq.com/sns/jscode2session?appid="
                +appid+"&secret="+secret+"&js_code="+code+"&grant_type=authorization_code";
        try{
            URL weChatUrl = new URL(url);
            URLConnection connection = weChatUrl.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();

            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while((line = in.readLine())!=null) sb.append(line);
            return sb.toString();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        finally {
            try{
                if(in!=null) in.close();
            }
            catch (Exception e2){
                e2.printStackTrace();
            }
        }
    }
}
