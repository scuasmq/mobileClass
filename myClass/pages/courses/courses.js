import util from './../../utils/util.js';
const app = getApp();
Page({
  data: {
    sortindex: 0,  //排序索引
    sortid: null,  //排序id
    sort: [],
    scrolltop: null, //滚动位置
    page: 0,  //分页
    list_create: [],
    list_join: [],
    show_list_create: [],
    show_list_join: [],
    navTab: ['加入的课程', '创建的课程'],
    currentNavtab: 0
  },
  onLoad: function () { //加载数据渲染页面
  },
  onReady: function () {
  },
  onShow: function(){
    this.getClassList(app.globalData.uid);
  },

  getClassList: function (uid) {
    const that = this
    var url = 'http://47.113.114.73:9911/get_class_list/' + uid.toString()
    //向后端请求课程数据
    wx.request({
      url: url,
      method: 'POST',
      data: { url },
      header: {
        'content-type': 'application/json' //默认值
      },
      success(res) {
        console.log('getClassList return: ', res.data)
        that.setData({
          list_create: res.data.list1,
          list_join: res.data.list2
        })
        that.fetch_Join_Create_ClassList();
      }
    })
  },

  fetch_Join_Create_ClassList: function () { //获取加入的课程
    const perpage = 5;
    this.setData({
      page: this.data.page + 1
    })
    const page = this.data.page;
    const create_class = this.data.list_create;
    const join_class = this.data.list_join;
    const len_create = create_class.length;
    const len_join = join_class.length;
    var appendList_create = [];
    var appendList_join = [];
    for (var i = (page - 1) * perpage; i < page * perpage && i < len_create; i++) {
      appendList_create.push({
        "id": i + 1,
        "name": create_class[i].class_name,
        "status": util.getRandomArrayElement(["上课中", "下课了", "已结课"]),
        "time": "周二晚 19:20",
        "coments": Math.floor(Math.random() * 1000),
        "address": "二基楼B303",
        "imgurl": "https://img2020.cnblogs.com/blog/1976122/202011/1976122-20201130235455287-1660721123.png",
        "class_name": create_class[i].class_name,
        "class_id": create_class[i].class_id,
      })
    }
    for (var i = (page - 1) * perpage; i < page * perpage && i < len_join; i++) {
      appendList_join.push({
        "id": i + 1,
        "name": join_class[i].class_name,
        "status": util.getRandomArrayElement(["上课中", "下课了", "已结课"]),
        "time": "周二晚 19:20",
        "coments": Math.floor(Math.random() * 1000),
        "address": "二基楼B303",
        "imgurl": "https://img2020.cnblogs.com/blog/1976122/202011/1976122-20201130235455287-1660721123.png",
        "class_name": join_class[i].class_name,
        "class_id": join_class[i].class_id,
      })
    }
    this.setData({
      show_list_create: this.data.show_list_create.concat(appendList_create),
      show_list_join: this.data.show_list_join.concat(appendList_join)
    })
  },

  scrollHandle: function (e) { //滚动事件
    // console.log(e.detail.scrollTop)
    this.setData({
      scrolltop: e.detail.scrollTop
    })
  },
  goToTop: function () { //回到顶部
    this.setData({
      scrolltop: 0
    })
  },
  scrollLoading: function () { //滚动加载
    console.log('loadScroll')
    this.fetch_Join_Create_ClassList();
  },
  onPullDownRefresh: function () { //下拉刷新
    this.getClassList(app.globalData.uid);
    this.setData({
      page: 0,
      show_list_create: [],
      show_list_join: []
    })
    this.fetch_Join_Create_ClassList();
    setTimeout(() => {
      wx.stopPullDownRefresh()
    }, 1000)
  },

  switchTab: function(e){
    this.setData({
      currentNavtab: e.currentTarget.dataset.idx
    });
  },
})