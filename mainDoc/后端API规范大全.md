# 后端API规范大全

***

## 1.用户注册

URL：/register

Request:

```
{
    "username":"zt",
    "password":"991211",
    "mailbox":"814943622@qq.com",
    "school":"SCU",
    "school_id":"2018"
}
```

Response:Result对象

## 2.用户登录

URL：/login/{username}/{password}

Request：路径参数

Response:

```
{
    "uid":1(-1代表无此用户)
}
```

## 3.得到用户参加和创建的课程列表

URL：/get_class_list/{uid}

Request：路径参数

Response：

```
{
    "list1":[{class_name,class_id},{}]
    "list2":[{class_name,class_id},{}]
}
```

## 4.创建课程

URL：/insertClass

Request:

```
{
   "user_id":1,
   "class_name":"springboot"
}
```

Response:Result对象

## 5.加入课程

URL：/joinClass/{user_id}/{class_id}

Request:路径参数

Response:Result对象

## 6.得到参加某课程的人的名字

URL：/getClassNameList/{class_id}

Request:路径参数

Response:

```
{
    "data":["lzl","zt",......]
}
```

## 7.得到某课程下发布过的试卷

URL：/getClassGroupList/{class_id}

Request:路径参数

Response:

```
[{group_name,publish_time},{}]
```

## 8.上传一套试题

URL：/upload_problems/{class_id}

Request:

```
{
   "group_name":"dsada",
   "question_count":1,
   "data":[]
}
```

Response:Result

## 9.点击某套试题得到信息

URL：/get_one_group_info/{class_id}

Request:

```
{
    "group_name":"dasda",
    "uid":1
}
```

Response:

```
{
     "question_count":1,
     "problemlist":[{test_id,content,a,b,c,d,ans,publish_time,class_id,group_name},{},......],
     "submitlist":["a","b","c"](null表示user未提交过)
}
```

## 10.提交答题结果

URL：/submit_one_group/{class_id}

Request:

```
{
    "question_result":[],
    "uid":1,
    "group_name":"dasda"
}
```

Response:

```
{
    "question_count":1,
    "true_count":2
}
```

## 11.发布一个投票

URL：/publish_one_vote/{class_id}

Request:

```
{
    "vote_theme":"dadas",
    "option_num",10,
    "data":[]
}
```

Reponse:Result

## 12. 查看某个class下已经发布过的投票

URL：/check_vote/{class_id}

Request:路径参数

Response:

```
[{vote_theme,vote_id,publish_time},{},{},.....]
```

## 13.得到某个投票的信息

URL：/get_one_vote_info/{class_id}

Request:

```
{
    "vote_id":1,
    "user_id":1
}
```

Response:

```
{
   "vote_theme":"dasda",
   "option_num":1,
   "option_result":["1","2"],
   "option_selected":["1","2"]
}
```

## 14.提交一个投票结果

URL：/submit_one_vote/{class_id}

Request:

```
{
    "vote_id":1,
    "user_id":2,
    "vote_result":"1-2-4"
}
```

Response:Result