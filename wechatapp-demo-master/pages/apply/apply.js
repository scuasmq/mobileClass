Page({
  data: {
    industryarr: [],
    industryindex: 0,
    statusarr: [],
    statusindex: 0,
    jobarr: [],
    jobindex: 0,
    hasfinancing: false, //是否已融资
    isorg: false, //是否是机构
    ip: '',
    imgPath:null
  },
  onLoad: function() {
    this.fetchData()
  },
  applySubmit: function(){
    this.addStudent()
  },
  addStudent: function(class_id=9, class_name="研究与开发", data="zhangtong") {
    wx.request({
      url: 'http://47.113.114.73:9911/grab_students',
      data: {
        'class_id': class_id,
        'class_name': class_name,
        'data': data
      },
      method: 'POST',
      header: {
        'content-type': 'application/json' //默认值
      },
      success(res) {
        console.log('addStudent return:', res)
        if (res.data.status_code == '500') {
          console.log('add failure', res.message)
          wx.showToast({
            title: '加入失败',
          })
        }
        if (res.data.status_code == '200') {
          console.log('add success', res.message)
          wx.showToast({
            title: '加入成功',
          })
        }
      }
    })
  },
  fetchData: function() {
    this.setData({
      statusarr: ["请选择", "计算机学院", "文新学院", "数学学院"]
    })
  },
  bindPickerChange: function(e) { //下拉选择
    const eindex = e.detail.value;
    const name = e.currentTarget.dataset.pickername;
    // this.setData(Object.assign({},this.data,{name:eindex}))
    switch (name) {
      case 'industry':
        this.setData({
          industryindex: eindex
        })
        break;
      case 'status':
        this.setData({
          statusindex: eindex
        })
        break;
      case 'job':
        this.setData({
          jobindex: eindex
        })
        break;
      default:
        return
    }
  },
  setFinance: function(e) { //选择融资
    this.setData({
      hasfinancing: e.detail.value == "已融资" ? true : false
    })
  },
  setIsorg: function(e) { //选择投资主体
    this.setData({
      isorg: e.detail.value == "机构" ? true : false
    })
  },
  // applySubmit: function() {
  //   wx.navigateTo({
  //     url: '../service/service'
  //   })
  // },
  inputName: function(e){
    console.log('输入名字:', e.detail.value)
    this.setData({
      username: e.detail.value
    })
  },
  inputId: function(e){
    this.setData({
      id: e.detail.value
    })
  },
  inputIntro: function(e){
    this.setData({
      intro: e.detail.value
    })
  },

  uploadImage: function() {
    const that = this
    wx.chooseImage({
      count: 1,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success(res) {
        // tempFilePatxh可以作为img标签的src属性显示图片
        const tempFilePaths = res.tempFilePaths[0]
        console.log(tempFilePaths[0])
        that.setData({
          userHeaderImage: tempFilePaths,
          imgPath: tempFilePaths
        })
        console.log(tempFilePaths)
        //上传图片
        wx.showLoading({
          title: '上传图片中...',
        })
        wx.uploadFile({
          url: that.data.ip + '/uploader',
          filePath: tempFilePaths,
          name: 'photo', //后台要绑定的名称
          // header: {
          //   "content-type": "multipart/form-data",
          //   'content-type': 'application/x-www-form-urlencoded' //表单提交
          // },
          header: {
            "content-type": "multipart/form-data"
          },
          formData: {
            'id': that.data.id //参数绑定
          },
          success(res) {
            const data = res.data
            console.log(res);
            console.log('**************')
            console.log('upload_img:' + res.data)
            console.log('**************')
            that.setData({
              img: true,
              id: res.data
            })
            console.log('tttttttttt')
            console.log(that.data.id)
            //do something
          },
          complete() {
            wx.hideLoading()
          }
        })
      },
      fail(err) {
        console.log("img上传失败：" + err.errMsg)
      }
    })
  }
})