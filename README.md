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