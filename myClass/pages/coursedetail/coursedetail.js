// pages/coursedetail/coursedetail.js
Page({
  data: {
    activitydata:{},
    spaceimgs:[],
    currentIndex:1,
    stuInfoList:[],
    class_id:null,
    class_name:null,
    broadcast_list:null,
    class_type:null
  },
  onLoad: function (options) {
    console.log(options);
    console.log(options.class_type)
    this.setData({
      class_name:options.class_name,
      class_id:options.class_id,
      class_type:options.class_type,
      activitydata:{
        "name": "课程："+options.class_name+"",
        "date": "周二晚 19:20",
        "hasentered":60,
        "total":200,
        "address":'二基楼B303',
        "hoster": "江安校区"
      },
      spaceimgs:["http://139.196.218.128/SjPark/UserFile/Files/Thumbnail/46932530-4bc8-48dc-bf10-1e5e39d254b8_750x470.png","http://139.196.218.128/SjPark/UserFile/Files/Thumbnail/73efa039-6c54-43c6-8ad9-70f831723e2e_750x470.png","http://139.196.218.128/SjPark/UserFile/Files/Thumbnail/eb8bbf4d-e236-4c92-900c-67d8b941b02a_750x470.png"]
    })
    // setTimeout(()=>{
      wx.setNavigationBarTitle({
        title: this.data.activitydata.name
      })
    // },1000)
    this.getAllStuInfo(this.data.class_id)
    this.getBroadCast(this.data.class_id)
  },
  getAllStuInfo: function(class_id){
    const that = this
    wx.request({
      url: 'http://47.113.114.73:9911/getClassStudentList/'+class_id.toString(),
      data:{},
      header:{
        'content-type': 'application/json' //默认值
      },
      success(res){
        console.log('getAllStuInfo return: ',res.data)
        that.setData({
          stuInfoList: res.data
        })
        that.setData({
          stu_num: that.data.stuInfoList.length
        })
      }
    })
  },
  setCurrent: function(e){
    this.setData({
      currentIndex:e.detail.current+1
    })
  },
  imgPreview: function(){ //图片预览
    const imgs = this.data.spaceimgs;
    wx.previewImage({
      current: imgs[this.data.currentIndex-1], // 当前显示图片的http链接
      urls: imgs // 需要预览的图片http链接列表
    })
  },
  getBroadCast: function(class_id){
    var that = this
    wx.request({
      url: 'http://47.113.114.73:9911/return_all_broadcast/'+class_id.toString(),
      data:{},
      header:{
        'content-type': 'application/json' //默认值
      },
      success(res){
        console.log('getAllBroadCast return: ',res.data.data)
        that.setData({
          broadcast_list: res.data.data,
          boradcast_num: res.data.bc_number
        })
      }
    })
  },
  toJoin: function(){
    wx.navigateTo({
      url:"../apply/apply",
    })
  },
  alert_sign_in: function(){
    this.setData({
      isShowConfirm:true
    })
  },
  tapCancel: function(){

    this.setData({
      isShowConfirm:false
    })
  },
  tapConfirm: function(){
    // 发送数据
    var that = this
    wx.request({
      url: app.globalData.request_url+'/'+app.globalData.user_info.uid.toString()+'/'+that.data.class_id.toString(),
      data:{},
      header:{
        'content-type': 'application/json' //默认值
      },
      success(res){
        console.log('tapSign_in return: ',res.data)
      }
    })

    that.setData({
      isShowConfirm:false
    })
  },
  input_sign_in_code: function(e){
    console.log(e.detail.value)
    this.setData({
      input_sign_in_code: e.detail.value
    })
  },
  sign_in_scanQR: function () {
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
  }

})
