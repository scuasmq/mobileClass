package com.smq.mainproject.Controller;

import com.smq.mainproject.Entity.GroupInfo;
import com.smq.mainproject.Entity.Problem;
import com.smq.mainproject.Entity.ProblemList;
import com.smq.mainproject.Entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class QuestionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @CrossOrigin
    @RequestMapping(value = "/upload_problems/{class_id}")
    @ResponseBody
    public Result GetProblems(@RequestBody ProblemList jsondata, @PathVariable int class_id){
        String group_name=jsondata.getGroup_name();
        int cnt=jsondata.getQuestion_count();
        List<Problem> datas=jsondata.getData();
        String pre_sql="select * from problemlist";
        List<Map<String,Object>>list=jdbcTemplate.queryForList(pre_sql);
        int number=list.size()+1;
        //更新数据库
        for(int i=0;i<datas.size();i++){
            Problem temp=datas.get(i);
            String sql="insert into problemlist (test_id,content,a,b,c,d,ans,publish_time,class_id,group_name) values(?,?,?,?,?,?,?,?,?,?,?)";
            Object args[]={number,temp.getQuestion(),temp.getAoption(),temp.getBoption(),temp.getCoption(),temp.getDoption()
                    ,temp.getAnswer(),(String) new SimpleDateFormat("yyyy-MM-dd").format(new Date()),class_id,group_name};
            number=number+1;
            int flag=jdbcTemplate.update(sql,args);
            if(flag<=0){
                return new Result("500","FAILURE");
            }
        }
        return new Result("200","SUCCESS");
    }

    @CrossOrigin
    @RequestMapping(value = "/get_group/{class_id}")
    @ResponseBody
    public List<Map<String,Object>> returnGroup(@PathVariable int class_id){
        String sql="select distinct group_name,publish_time from problemlist where class_id = ?";
        Object args[]={class_id};
        List<Map<String,Object>> list =jdbcTemplate.queryForList(sql,args);
        return list;
    }

    @CrossOrigin
    @RequestMapping(value = "/get_problemslist_by_group/{class_id}")
    @ResponseBody
    public List<Map<String,Object>> returnProblemsListByGroup(@RequestBody Map<String,Object>map,@PathVariable int class_id){
        String sql="select * from problemlist where group_name = ? and class_id = ?";
        Object args[]={(String)map.get("group_name"),class_id};
        List<Map<String,Object>> list =jdbcTemplate.queryForList(sql,args);
        return list;
    }

    @CrossOrigin
    @RequestMapping(value = "/get_one_group_info/{class_id}")
    @ResponseBody
    public GroupInfo returnOneGroup(@RequestBody Map<String,Object>map, @PathVariable int class_id){
        String group_name=(String)map.get("group_name");
        int uid=(int)map.get("uid");
        String sql="select distinct * from problemlist where group_name = ? and class_id = ?";
        Object args[]={group_name,class_id};
        List<Map<String,Object>> list =jdbcTemplate.queryForList(sql,args);
        String sql1="select test_ans from user_join_test where class_id = ? and uid = ? and test_name = ?";
        Object args1[]={class_id,uid,group_name};
        List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql1,args1);
        if(list2.size()==0){
            return new GroupInfo(list.size(),list,null);
        }
        List<String> list3 =new ArrayList<>();
        String t=(String) list2.get(0).get("test_ans");
        String array_t[]=t.split("-");
        for(int i=0;i<array_t.length;i++){
            list3.add(array_t[i]);
        }
        return new GroupInfo(list.size(),list,list3);
    }
    @CrossOrigin
    @RequestMapping(value = "/submit_one_group/{class_id}")
    @ResponseBody
    public Map<String,Object> submitOneGroup(@RequestBody Map<String,Object>map,@PathVariable int class_id){
        List<String>question_result=(List<String>) map.get("question_result");
        String temp="";
        for(int i=0;i<question_result.size();i++){
            temp=temp+question_result.get(i);
            if(i!=question_result.size()-1)temp=temp+"-";
        }
        String sql="insert into user_join_test (uid,class_id,test_name,test_ans) values(?,?,?,?)";
        Object args[]={(int)map.get("uid"),class_id,(String)map.get("group_name"),temp};
        int flag=jdbcTemplate.update(sql,args);

        Map<String,Object>ans_map=new HashMap<>();

        String sql1="select ans from problemlist where class_id = ? and group_name = ?";
        Object args1[]={class_id,(String)map.get("group_name")};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql1,args1);
        int true_cnt=0;
        for(int i=0;i<list.size();i++){
            if(question_result.get(i)==(String) list.get(i).get("ans")){
                true_cnt+=1;
            }
        }
        ans_map.put("question_count",question_result.size());
        ans_map.put("true_count",true_cnt);
        return ans_map;
    }
}
