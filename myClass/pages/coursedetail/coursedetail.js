// pages/coursedetail/coursedetail.js
Page({
  data: {
    activitydata:{},
    spaceimgs:[],
    currentIndex:1,
    stuInfoList:[]
  },
  onLoad: function (options) {
    console.log(options);
    this.setData({
      activitydata:{
        "name": "课程："+options.id+"",
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
    this.getAllStuInfo()
  },
  getAllStuInfo: function(class_id = 9){
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
  reserveHandle: function(){
    wx.navigateTo({
      url: '../spacereserve/spacereserve'
    })
  },
  toJoin: function(){
    wx.navigateTo({
      url:"../apply/apply",
    })
  }
  // formateNumber:function(n){
  //   return n>9?n:'0'+n
  // }
})
