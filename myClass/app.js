//app.js
App({
  onLaunch: function () {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)
    
    this.getOpenid()
    // // 登录
    // wx.login({
    //   success: res => {
    //     // 发送 res.code 到后台换取 openId, sessionKey, unionId
    //     console.log(res)
    //   }
    // })
    // // 获取用户信息
    // wx.getSetting({
    //   success: res => {
    //     if (res.authSetting['scope.userInfo']) {
    //       // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
    //       wx.getUserInfo({
    //         success: res => {
    //           // 可以将 res 发送给后台解码出 unionId
    //           this.globalData.userInfo = res.userInfo
    //           console.log(res)
    //           // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
    //           // 所以此处加入 callback 以防止这种情况
    //           if (this.userInfoReadyCallback) {
    //             this.userInfoReadyCallback(res)
    //           }
    //         }
    //       })
    //     }
    //   }
    // })
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
  globalData: {
    userCode:null,
    userInfo:null,
    openid:null,
    username:null,
    userid:null,

    // request_url: 'http://47.113.114.73:9911' //项目
    request_url: 'http://127.0.0.1:9911' //开发
  }
})