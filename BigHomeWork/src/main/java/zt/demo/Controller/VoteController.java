package zt.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import zt.demo.ClassExample.Result;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class VoteController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //发布投票
    @CrossOrigin
    @RequestMapping(value = "/publish_one_vote/{class_id}")
    @ResponseBody
    public Result PublishVote(@RequestBody Map<String,Object>map,@PathVariable int class_id){
        String vote_theme=(String) map.get("vote_theme");
        int option_num=(int)map.get("option_num");
        List<String>data=(List<String>)map.get("data");
        String option="";
        String option_result="";
        for(int i=0;i<data.size();i++){
            option=option+data.get(i);
            option_result=option_result+"0";
            if(i!=data.size()-1){
                option=option+"-";
                option_result=option_result+"-";
            }
        }

        String sql="insert into voteinfo (class_id,vote_id,theme,option_list,option_num,publish_time,option_result) values(?,?,?,?,?,?,?)";
        Object args[]={class_id, null, vote_theme, option , option_num, (String) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),option_result};
        int flag=jdbcTemplate.update(sql,args);
        if(flag<=0){
            return new Result("500","FAILURE");
        }
        else return new Result("200","SUCCESS");
    }

    //查看某个class下已经发布过的投票
    @CrossOrigin
    @RequestMapping(value = "/check_vote/{class_id}")
    @ResponseBody
    public List<Map<String,Object>> CheckVote(@PathVariable int class_id){
        String pre_sql = "select theme,vote_id,publish_time from voteinfo where class_id = ?";
        Object args[] = {class_id};
        List<Map<String,Object>> list = jdbcTemplate.queryForList(pre_sql,args);
        return list;
    }

    //得到某个投票的信息
    @CrossOrigin
    @RequestMapping(value = "/get_one_vote_info/{class_id}")
    @ResponseBody
    public Map<String,Object> getOneVote(@PathVariable int class_id,@RequestBody Map<String,Object> map){

        int vote_id=(int)map.get("vote_id");
        int uid=(int)map.get("user_id");
        String sql1="select * from voteinfo where class_id = ? and vote_id = ?";
        Object args1[]={class_id,vote_id};
        List<Map<String,Object>>list1=jdbcTemplate.queryForList(sql1,args1);

        String sql2="select * from user_join_vote where uid = ? and class_id = ? and vote_id = ?";
        Object args2[]={uid,class_id,vote_id};
        List<Map<String,Object>>list2=jdbcTemplate.queryForList(sql2,args2);

        String t1=(String)list1.get(0).get("option_result");
        List<String>option_result=new ArrayList<>();
        String[] t2=t1.split("-");
        for(int i=0;i<t2.length;i++){
            option_result.add(t2[i]);
        }
        List<String>data=new ArrayList<>();
        String[] t4=((String)list1.get(0).get("option_list")).split("-");
        for(int i=0;i<t4.length;i++){
            data.add(t4[i]);
        }

        //返回结果
        Map<String,Object>ans=new HashMap<>();
        ans.put("vote_theme",(String)list1.get(0).get("theme"));
        ans.put("option_num",Integer.parseInt((String) list1.get(0).get("option_num")));
        ans.put("option_result",option_result);
        ans.put("data",data);
        if(list2.size()==0){
            ans.put("option_selected",null);
            return ans;
        }
        String temp1=(String) list2.get(0).get("option_selected");
        String[] t3=temp1.split("-");
        List<String>option_selected=new ArrayList<>();
        for(int i=0;i<t3.length;i++){
            option_selected.add(t3[i]);
        }
        ans.put("option_selected",option_selected);
        return ans;
    }

    //提交投票结果
    @CrossOrigin
    @RequestMapping(value = "/submit_one_vote/{class_id}")
    @ResponseBody
    public Result SubmitVoteResult(@RequestBody Map<String,Object>map,@PathVariable int class_id){
        String sql = "insert into user_join_vote (vote_id,class_id,uid,option_selected) values(?,?,?,?)";
        Object args[]={(int)map.get("vote_id"),class_id,(int)map.get("user_id"),(String)map.get("vote_result")};
        int flag=jdbcTemplate.update(sql,args);
        if(flag<=0){
            return new Result("500","FAILURE");
        }
        String vote_result = (String)map.get("vote_result");
        //选了哪些选项
        String[] option = vote_result.split("-");

        sql = "select option_result from voteinfo where class_id = ? and vote_id = ?";
        Object args_1[] = {class_id,(int)map.get("vote_id")};
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql,args_1);
        if(list.size()==0){
            return new Result("500","FAILURE");
        }
        //获得选票结果
        String old_option_result = (String)list.get(0).get("option_result");
        String[] cur_option_result = old_option_result.split("-");
        for (int i=0;i<option.length;++i) {
            int idx = Integer.parseInt(option[i]) - 1;
            int add = Integer.parseInt(cur_option_result[idx]) + 1;
            cur_option_result[idx] = String.valueOf(add);
        }
        String ret_option = String.join("-",cur_option_result);
        sql = "update voteinfo set option_result = ? where class_id = ? and vote_id = ?";
        Object args_2[] = {ret_option,class_id,(int)map.get("vote_id")};

        flag = jdbcTemplate.update(sql,args_2);
        if(flag<=0){
            return new Result("500","FAILURE");
        }
        return new Result("200","SUCCESS");
    }
}