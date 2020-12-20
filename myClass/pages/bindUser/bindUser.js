const app = getApp();

Page({
  data: {
    ip: app.globalData.request_url,
    already_login: false,
    wxInfo: {},
    hasWxInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  onLoad: function () {
    console.log(!this.data.hasWxInfo && this.data.canIUse)
    this.getOpenid()
    if (app.globalData.wxInfo) {
      this.setData({
        wxInfo: app.globalData.wxInfo,
        hasWxInfo: true
      })
    } else if (this.data.canIUse){
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          wxInfo: res.userInfo,
          hasWxInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.wxInfo = res.userInfo
          this.setData({
            wxInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  onShow: function () {
  },
  applySubmit: function () {
    this.addStudent()
  },

  userLogin: function () {
    var that = this
    var post_url = this.data.ip + '/wx_login/' + app.globalData.openid
    wx.request({
      url: post_url,
      data: {
        'username': that.data.username,
        'password': that.data.password,
      },
      method: 'POST',
      header: {
        'content-type': 'application/json' //默认值
      },
      success(res) {
        if(res.data!=-1) app.globalData.uid = res.data
        wx.showToast({
          title: res.data==-1?'失败':'成功',
          duration: 2000,
          success: function () {
            setTimeout(function () {
              if (res.data != -1) {
                that.setData({
                  already_login: true,
                })
              }
            }, 2000)
          }
        })
      }
    })
  },

  //由于异步的问题，转为回调实现
  getOpenid: function () {
    var that = this
    wx.login({
      success(res) {
        // console.log(res)
        app.globalData.userCode = res.code // 发送 res.code 到后台换取 openId, sessionKey, unionId
        wx.request({
          url: app.globalData.request_url + '/openid/' + res.code,
          method: 'POST',
          header: {
            'content-type': 'application/json' //默认值
          },
          success(res) {
            console.log('code_parse ret:',res)
            app.globalData.openid = res.data.openid
            console.log('openid:', app.globalData.openid)
            that.userNoLogin(res.data.openid)
          }
        })
      },
    })
  },
  userNoLogin: function (openid) {
    var that = this
    var post_url = app.globalData.request_url + '/wx_no_login/' + openid
    wx.request({
      url: post_url,
      method: 'POST',
      header: {
        'content-type': 'application/json' //默认值
      },
      success(res) {
        console.log('no_login_ret:',res.data)
        if (res.data != -1) {
          app.globalData.uid = res.data
          wx.showToast({
            title: '已绑定',
            duration: 2000,
            success: function () {
              setTimeout(function () {
                that.setData({
                  already_login: true
                })
              }
                , 2000);
            }
          })
        }
      },
      fail(res) {
        console.log('no_login_ret fail:', res)
      }
    })
  },
  inputUsername: function (e) {
    this.setData({
      username: e.detail.value
    })
  },
  inputPassword: function (e) {
    this.setData({
      password: e.detail.value
    })
  },

  scanQR: function () {
    var that = this
    console.log('scan')
    wx.scanCode({
      // onlyFromCamera: false,
      success: (res) => {
        console.log(res)
        var uuid = res.result
        var url = that.data.ip + '/qr_code/qr_scaned/' + uuid + '/' + app.globalData.openid
        wx.request({
          url: url,
          method: 'POST',
          header: {
            'content-type': 'application/json' //默认值
          },
          success(res) {
            wx.showToast({
              title: res.data,
            })
          }
        })
      }
    })
  },
  getWxInfo: function(e) {
    console.log('wxInfo:',e)
    app.globalData.wxInfo = e.detail.userInfo
    this.setData({
      wxInfo: e.detail.userInfo,
      hasWxInfo: true
    })
  }
})