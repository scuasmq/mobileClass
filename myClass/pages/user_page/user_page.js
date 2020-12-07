// pages/user_page/user_page.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    ip: app.globalData.request_url,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  scanQR:function(){
    var that = this
    console.log('scan')
    wx.scanCode({
      // onlyFromCamera: false,
      success: (res)=>{
        console.log(res)
        var uuid = res.result
        var url = that.data.ip+'/qr_code/qr_scaned/'+uuid+'/'+app.globalData.openid
        wx.request({
          url: url,
          method:'POST',
          header:{
            'content-type': 'application/json' //默认值
          },
          success(res){
            wx.showToast({
              title: res.data,
            })
          }
        })
      }
    })
  }
})