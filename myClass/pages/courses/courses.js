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
    navTab: ['加入的课程', '创建的课程'],
    currentNavtab: 0
  },
  onLoad: function () { //加载数据渲染页面
    console.log(app.globalData.uid)
    this.getClassList(app.globalData.uid);
  },
  onReady: function () {
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
        that.fetchJoinClassList();
      }
    })
  },

  fetchJoinClassList: function () { //获取加入的课程
    const perpage = 5;
    this.setData({
      page: this.data.page + 1
    })
    const page = this.data.page;
    const create_class = this.data.list_create;
    const len = create_class.length;
    console.log('len', len)
    var appendList = [];
    for (var i = (page - 1) * perpage; i < page * perpage && i < len; i++) {
      appendList.push({
        "id": i + 1,
        "name": create_class[i].class_name,
        "status": util.getRandomArrayElement(["上课中", "下课了", "已结课"]),
        "time": "周二晚 19:20",
        "coments": Math.floor(Math.random() * 1000),
        "address": "二基楼B303",
        "imgurl": "https://img2020.cnblogs.com/blog/1976122/202011/1976122-20201130235455287-1660721123.png"
      })
    }
    this.setData({
      show_list_create: this.data.show_list_create.concat(appendList)
    })
    console.log(this.data.show_list_create)
  },
  setSortBy: function (e) { //选择排序方式
    const d = this.data;
    const dataset = e.currentTarget.dataset;
    this.setData({
      sortindex: dataset.sortindex,
      sortid: dataset.sortid
    })
    console.log('排序方式id：' + this.data.sortid);
  },
  setStatusClass: function (e) { //设置状态颜色
    console.log(e);
  },
  scrollHandle: function (e) { //滚动事件
    console.log(e.detail.scrollTop)
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
    this.fetchJoinClassList();
  },
  onPullDownRefresh: function () { //下拉刷新
    this.setData({
      page: 0,
      show_list_create: []
    })
    this.fetchJoinClassList();
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