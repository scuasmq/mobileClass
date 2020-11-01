#  研开代码的说明文档
## 文件夹说明

testDBCoonection 测试本地的数据库连接

mainproject主项目

## Entity与Controller说明

### Entity

- Classtable

  userid 与 class_name

- GroupInfo

  question_count, List<Map<String,Object>>problemlist, List\<String>submitlist;

  每个群组的信息

- Problem

  question a,b,c,d option，answer

  储存每个问题的信息

- ProblemList

  group_name, question_count, ProblemList:data

  question_count是问题的数目，即data的len

- Result

  status_code, message

  与前端的响应信息，**建议更名为Reseponse**

- ReturnUserClassList

  List<\<Map\<String,Object>>> list1,list2;

  返回两个列表，每个list里一连串的map，一个map就是一个课程的信息，(map=json)

- User

  username, password, mailbox, school, school_id

  保存用户信息

### Controller

- UserController

  ```
  @Autowired 采用properties里的配置，不用额外配置了
  @CrossOrigin 跨域
  @ResponseBody 响应为json格式
  @RequestBody 参数为json格式
  
  //注册界面，映射为请求网址
  @RequestMapping(value = "/register")
  
  //list的每一个元素都是mysql的一个记录
  List<Map<String,Object>> list=jdbcTemplate.queryForList(pre_sql);
  list.get(xxx).get("xxx");
  
  //sql查询语句的参数用法
  String sql="insert into userinfo (username,password,mailbox,school,school_id,uid) values(?,?,?,?,?,?)";
  Object args[]={username,password,mailbox,school,school_id,uid};
  int flag = jdbcTemplate.update(sql,args);
  
  return
  {
      "status_code": "200",
      "message": "SUCCESS"
  }
  ```

  ```
  //登陆请求 {username}和{password}是可变的参数，由springboot识别，可以看见函数参数里面的是
  //@PathVariable
  @RequestMapping(value = "/login/{username}/{password}")
  public Map<String,Object> Login(@PathVariable String username,@PathVariable String password){}
  return 
  {
  	"uid":xx,
  }
  ```

  ```
  //获取用户的上课表和授课表,返回两个json？
  @RequestMapping(value = "/get_class_list/{uid}")
  
  ```

- ClassController

  ```
  //classTable user_id,class_name
  //传入用户id和课程名，创建课程
  @RequestMapping(value = "/insertClass")
  public Result insertClass(@RequestBody ClassTable jsondata) {}
  return:
  {
      "status_code": "200",
      "message": "SUCCESS"
  }
  ```

  ```
  //通过用户id和课程id加入课程
  @RequestMapping(value = "/joinClass/{user_id}/{class_id}")
  public Result JoinClass(@PathVariable int user_id,@PathVariable int class_id){}
  {
      "status_code": "200",
      "message": "SUCCESS"
  }
  ```

  ```
  //通过课程号查询课程里的人名
  @RequestMapping(value = "/getClassNameList/{class_id}")
  return:
  {
  	"data":[
  		"user1",
  		"user2"
  	]
  }
  ```

  ```
  //返回有哪些组
  @RequestMapping(value = "/getClassGroupList/{class_id}")
  public List<Map<String,Object>> getClassGroupList(@PathVariable int class_id){}
  return:{
  	[{
  		"group_name":"xxx",
  		"pubish_time":"xxx"
  	},
  	{
  		"group_name":"xxx",
  		"pubish_time":"xxx"
  	},
  	...]
  }
  ```
  
- QuestionController

  ```
  //传过来问题和课程id
  //数据格式:
  @RequestMapping(value = "/upload_problems/{class_id}")
  public Result GetProblems(@RequestBody ProblemList jsondata, @PathVariable int class_id){}
  return:
  <Result>
  ```

  ```
  //好像group是问卷...
  @RequestMapping(value = "/get_group/{class_id}")
  public List<Map<String,Object>> returnGroup(@PathVariable int class_id){}
  ```

  ```
  @RequestMapping(value = "/get_problemslist_by_group/{class_id}")
  public List<Map<String,Object>> returnProblemsListByGroup(@RequestBody Map<String,Object>map,@PathVariable int class_id){}
  ```

  ```
  @RequestMapping(value = "/get_one_group_info/{class_id}")
  public GroupInfo returnOneGroup(@RequestBody Map<String,Object>map, @PathVariable int class_id){}
  ```

  ```
  @RequestMapping(value = "/submit_one_group/{class_id}")
  public Map<String,Object> submitOneGroup(@RequestBody Map<String,Object>map,@PathVariable int class_id){}
  ```