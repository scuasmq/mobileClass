package zt.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WxLoginController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    String appid = "wx9de82d2d6e3c2c7a";
    String secret = "69bd9d73051cdc085b66b3398b84b064";

    // 返回该微信用户的openid
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

    //用户登陆，把openid与username,uid绑定
    @CrossOrigin
    @RequestMapping(value = "/wx_login/{openid}")
    @ResponseBody
    public String Login(@RequestBody Map<String,Object>map,@PathVariable String openid){
        String username=(String)map.get("username");
        String password=(String)map.get("password");
        //查询用户是否存在
        String pre_sql="select uid from userinfo where username = ? and password = ?";
        Object pre_args[]={username,password};
        List<Map<String,Object>> list=jdbcTemplate.queryForList(pre_sql,pre_args);
        if(list.size()<=0) return "用户名或密码错误";
        int uid = (int)list.get(0).get("uid");
        //更新table wxuserinfo
        // //查询之前的openid,存在则update,不存在则insert
        String openid_sql = "select openid from wxuserinfo where uid = ?";
        Object openid_args[] = {uid};
        List<Map<String,Object>> openid_list = jdbcTemplate.queryForList(openid_sql,openid_args);

        int f = 0;
        if(openid_list.size()>0){ //update
            String sql = "update wxuserinfo set openid = ? where uid = ?";
            Object args[] = {openid,uid};
            f = jdbcTemplate.update(sql,args);
        }
        else{ //insert
            String sql = "insert into wxuserinfo(openid,username,password,uid) values(?,?,?,?)";
            Object args[] = {openid,username,password,uid};
            f = jdbcTemplate.update(sql,args);
        }
        if(f<=0) return "网络错误";
        return "登陆成功";
    }

    //微信用户免登陆
    @CrossOrigin
    @RequestMapping(value = "/wx_no_login/{openid}")
    @ResponseBody
    public int wx_no_login(@PathVariable String openid){
        String sql = "select username from wxuserinfo where openid = ?";
        Object args[] = {openid};
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,args);
        if(list.size()>0) return 1;
        else return 0;
    }
}
