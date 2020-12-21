var app = getApp()
Page({
  data: {
    motto: 'Hello World',
    userInfo: {},
    isShowConfirm: true
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: ''
    })
  },
  onLoad: function () {
    console.log('onLoad')
    var that = this
    //调用应用实例的方法获取全局数据
      that.setData({
        userInfo:app.globalData.wxInfo
      })
  },
    //data      
 
    setValue: function (e) {
      this.setData({
        walletPsd: e.detail.value
      })
    },
    cancel: function () {
      var that = this
      that.setData({
        isShowConfirm: false,
      })
    },
    confirmAcceptance:function(){
      var that = this
      that.setData({
        isShowConfirm: false,
      })
    },
})