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
  addStudent: function(class_id, class_name, data) {
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
        if (res.status_code == '500') {
          console.log('add failure', res.message)
        }
        if (res.status_code == '200') {
          console.log('add success', res.message)
        }
      }
    })
  },
  fetchData: function() {
    this.setData({
      industryarr: ["请选择", "移动互联网", "手机游戏", "互联网金融", "O2O", "智能硬件", "SNS社交", "旅游", "影视剧", "生活服务", "电子商务", "教育培训", "运动和健康", "休闲娱乐", "现代农业", "文化创意", "节能环保", "新能源", "生物医药", "IT软件", "硬件", "其他"],
      statusarr: ["请选择", "初创时期", "市场扩展期", "已经盈利"],
      jobarr: ["请选择", "创始人", "联合创始人", "产品", "技术", "营销", "运营", "设计", "行政", "其他"]
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
  applySubmit: function() {
    wx.navigateTo({
      url: '../service/service'
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