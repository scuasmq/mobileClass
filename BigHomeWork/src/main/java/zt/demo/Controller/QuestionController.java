package zt.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

import zt.demo.ClassExample.GroupInfo;
import zt.demo.ClassExample.Problem;
import zt.demo.ClassExample.ProblemList;
import zt.demo.ClassExample.Result;

@RestController
public class QuestionController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //上传一套试题
    @CrossOrigin
    @RequestMapping(value = "/upload_problems/{class_id}")
    @ResponseBody
    public Result UploadProblems(@RequestBody ProblemList jsondata,@PathVariable int class_id){
        String group_name=jsondata.getGroup_name();
        List<Problem> datas=jsondata.getData();
        //更新数据库
        for(int i=0;i<datas.size();i++){
            Problem temp=datas.get(i);
            String sql="insert into problemlist (test_id,content,a,b,c,d,ans,publish_time,class_id,group_name) values(?,?,?,?,?,?,?,?,?,?)";
            Object args[]={null,temp.getQuestion(),temp.getAoption(),temp.getBoption(),temp.getCoption(),temp.getDoption()
            ,temp.getAnswer(),(String) new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),class_id,group_name};
            int flag=jdbcTemplate.update(sql,args);
            if(flag<=0){ return new Result("500","FAILURE");}
        }
        return new Result("200","SUCCESS");
    }
    //学生得到某套试题的信息
    @CrossOrigin
    @RequestMapping(value = "/get_one_group_info_by_student/{class_id}")
    @ResponseBody
    public GroupInfo returnOneGroupByStudent(@RequestBody Map<String,Object>map, @PathVariable int class_id){
        String group_name=(String)map.get("group_name");
        int uid=(int)map.get("uid");
        //得到问题列表
        String sql="select distinct * from problemlist where group_name = ? and class_id = ?";
        Object[] args={group_name,class_id};
        List<Map<String,Object>> list =jdbcTemplate.queryForList(sql,args);
        //得到用户用户提交记录
        sql="select test_ans from user_join_test where class_id = ? and uid = ? and test_name = ?";
        args=new Object[]{class_id,uid,group_name};
        List<Map<String,Object>> list2=jdbcTemplate.queryForList(sql,args);
        if(list2.size()==0){
            return new GroupInfo(list.size(),list,null);
        }
        //分解返回用户的提交答案
        List<String> list3 =new ArrayList<>();
        for(int i=0;i<list2.size();i++){
            list3.add((String) list2.get(i).get("test_ans"));
        }
        return new GroupInfo(list.size(),list,list3);
    }
    //老师得到某套题的统计信息
    @CrossOrigin
    @RequestMapping(value = "get_one_group_info_by_teacher/{class_id}")
    @ResponseBody
    public Map<String,Object> returnOneGroupByTeacher(@RequestBody Map<String,Object>map,@PathVariable int class_id){
        //返回值
        Map<String,Object>ans=new HashMap<>();
        //得到所有的题目id和答案
        String sql="select * from problemlist where group_name = ? and class_id = ?";
        Object[] args={(String)map.get("group_name"),class_id};
        List<Map<String,Object>>list=jdbcTemplate.queryForList(sql,args);

        List<Integer>TestId=new ArrayList<>();
        List<String>Ans=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            TestId.add((int)list.get(i).get("test_id"));
            Ans.add((String)list.get(i).get("ans"));
        }

        sql="select * from user_join_test where test_id = ?";
        List<Double>error_rate=new ArrayList<>();
        for(int i=0;i<TestId.size();i++){
            int cnt=0;
            args=new Object[]{TestId.get(i)};
            list=jdbcTemplate.queryForList(sql,args);
            for(int j=0;j<list.size();j++){
                String tmp=(String) list.get(j).get("test_ans");
                if(tmp.equals(Ans.get(j)))continue;
                else{ cnt++; }
            }
            double temp=cnt*1.00/list.size();
            error_rate.add(temp);
        }

        List<Integer>Uid=new ArrayList<>();
        List<String>UserName=new ArrayList<>();

        for(int i=0;i<list.size();i++){
            Uid.add((int)list.get(i).get("uid"));
            UserName.add((String)list.get(i).get("username"));
        }

        ans.put("uid",Uid);
        ans.put("username",UserName);
        ans.put("error_rate",error_rate);
        return ans;
    }

    //提交试题答案
    @CrossOrigin
    @RequestMapping(value = "/submit_one_group/{class_id}")
    @ResponseBody
    public Map<String,Object> submitOneGroup(@RequestBody Map<String,Object>map,@PathVariable int class_id){
        Map<String,Object>ans_map=new HashMap<>();
        List<String>question_result=(List<String>) map.get("question_result");
        //得到用户名
        String sql="select username from userinfo where uid = ?";
        Object[] args={(int)map.get("uid")};
        List<Map<String,Object>>pre_list=jdbcTemplate.queryForList(sql,args);
        if(pre_list.size()==0){
            ans_map.put("message","USER NOT EXISTS");
            return ans_map;
        }
        String username=(String)pre_list.get(0).get("username");
        //获取题目的test_id
        sql="select test_id,ans from problemlist where group_name = ? and class_id = ?";
        args=new Object[]{(String)map.get("group_name"),class_id};
        pre_list=jdbcTemplate.queryForList(sql,args);
        if(pre_list.size()==0){
            ans_map.put("message","GROUP NOT EXISTS");
            return ans_map;
        }

        //插入数据库信息
        int true_cnt=0;
        sql="insert into user_join_test (uid,username,class_id,test_name,test_id,test_ans) values(?,?,?,?,?,?)";
        for(int i=0;i<pre_list.size();i++){
            args=new Object[]{(int)map.get("uid"),username,class_id,(String)map.get("group_name"),
                    (int)pre_list.get(i).get("test_id"),question_result.get(i)};
            if(((String)pre_list.get(i).get("ans")).equals(question_result.get(i)))true_cnt++;
            int flag=jdbcTemplate.update(sql,args);
            if(flag<=0){
                ans_map.put("message","ERROR");
                return ans_map;
            }
        }

        ans_map.put("question_count",question_result.size());
        ans_map.put("true_count",true_cnt);
        return ans_map;
    }
}