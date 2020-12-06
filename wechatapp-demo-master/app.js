//app.js
App({
  onLaunch: function () {
    //小程序初始化完成只执行一次
    //调用API从本地缓存中获取数据
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    console.log('onLaunch')
    this.getOpenid();

  },
  onShow: function(){
    //启动小程序或者从后台进入前台
  },
  onHide: function(){
    //从前台进入后台
  },
  getUserInfo:function(cb){
    var that = this
    if(this.globalData.userInfo){
      typeof cb == "function" && cb(this.globalData.userInfo)
    }else{
      //调用登录接口
      wx.login({
        success: function () {
          wx.getUserInfo({
            success: function (res) {
              that.globalData.userInfo = res.userInfo
              typeof cb == "function" && cb(that.globalData.userInfo)
            }
          })
        }
      })
    }
  },
  getOpenid:function(){
    var that = this
    wx.login({
      success(res) {
        // console.log(res)
        that.globalData.userCode = res.code // 发送 res.code 到后台换取 openId, sessionKey, unionId
        wx.request({
          url: that.globalData.request_url+'/openid/'+res.code,
          method:'POST',
          header:{
            'content-type': 'application/json' //默认值
          },
          success(res){
            that.globalData.openid = res.data.openid
            console.log('openid:',that.globalData.openid)
          },
          fail(res){
            console.log('fail',res)
          }
        })
      },
    })
  },
  
  getClassInfo(){
    var that = this
    
  },

  globalData:{
    //全局信息
    userCode:null,
    userInfo:null,
    openid:null,
    username:null,
    userid:null,

    // request_url: 'http://47.113.114.73:9911' //项目
    request_url: 'http://127.0.0.1:9911' //开发
  },
})