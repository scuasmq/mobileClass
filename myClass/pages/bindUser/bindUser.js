const app = getApp();

Page({
  data: {
    ip: app.globalData.request_url,
  },
  onLoad: function() {
    console.log(this.data.ip)
  },
  applySubmit: function(){
    this.addStudent()
  },
  userLogin: function() {
    var that = this
    var post_url = this.data.ip+'/wx_login/'+app.globalData.openid
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
        console.log('addStudent return:', res)
        wx.showToast({
          title: res.data,
        })
        if(res.data=="登陆成功"){
          console.log('jump')
          wx.navigateTo({
            url: '../user_page/user_page'
          })
        }
      }
      
    })
  },

  inputUsername: function(e){
    console.log('输入名字:', e.detail.value)
    this.setData({
      username: e.detail.value
    })
  },
  inputPassword: function(e){
    this.setData({
      password: e.detail.value
    })
  },

})