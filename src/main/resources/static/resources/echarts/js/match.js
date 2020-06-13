var deptTree;
layui.extend({
  dtree: '/resources/layui_ext/dtree/dtree'   // {/}的意思即代表采用自有路径，即不跟随 base 路径
}).use(['dtree','layer','jquery','form'], function(){
  var dtree = layui.dtree;
  var layer = layui.layer;
  var form =layui.form;
  $ = layui.jquery;
  map();

  layer.msg("请选择征订期号！");
  //加载查询条件征订期号的下拉列表
  $.get("/order/loadAllOrderForSelect",function(res){
    var data=res.data;
    var dom=$("#search_orderid");
    var html='<option value="">请选择所要查询的征订期号信息</option>'
    $.each(data,function(index,item){
      html+='<option value="'+item.id+'">'+item.id+'-'+item.comment+'</option>'
    });
    dom.html(html);
    form.render("select");
  })
  form.on('select(doOrder)', function(data){
    var orderId = data.value;
    if(orderId == null || orderId == undefined || orderId == ''){
      layer.msg('请选择征订期号！');
      return 0;
    }
    //根据期号id拿到match表中各指标数据
    $.post("/match/getHeartInfo",{orderId:orderId},function(res) {
      $("#lNum").text("0");
      $("#bNum").text("0");
      $("#mRatio").text("0%");
      $("#lRatio").text("0%");
      var data = res.data;
      $("#lNum").text(data.lNum);
      $("#bNum").text(data.bNum);
      $("#mRatio").text(data.mRatio+'%');
      $("#lRatio").text(data.lRatio+'%');


    })
    //根据期号id拿到匹配结果中学校信息
    // 初始化树
    deptTree= dtree.render({
      elem: "#deptTree",
      dataStyle: "layuiStyle",  //使用layui风格的数据格式
      dataFormat: "list",  //配置data的风格为list
      response:{message:"msg",statusCode:0},  //修改response中返回数据的定义,
      request:{orderId:orderId},
      url: "/match/loadDeptTreeJsonByOrderId",// 使用url加载（可与data加载同时存在）
      ficon: "-1"
    });
    //根据期号id拿到match表中各学校余缺数量 给折线图
    $.post("/match/loadSchoolMatch",{orderId:orderId},function(res) {
      var data = res;
      line(data);
    })

    // 根据期号id拿到match表中各书目余缺数量 给条形图
    $.post("/match/loadBookMatch",{orderId:orderId},function(res) {
      var data = res;
      bar(data);
    })

    // 根据期号id拿到匹配结果中所有匹配结果 给  map
    $.post("/match/loadMatchResult",{orderId:orderId},function(res) {
      let data = res;
      map(data);
    })


  });


  // 绑定节点点击
  dtree.on("node(deptTree)" ,function(obj){
    var  orderId = $("#search_orderid").val();
    var deptId = obj.param.nodeId;
    // 根据期号和学校id拿到match表中各年级余缺数量 给折线图
    $.post("/match/loadSchoolMatch",{orderId:orderId,deptId:deptId},function(res) {
      var data = res;
      line(data);
    })
    // 根据期号和学校id拿到match表中各书目余缺数量 给条形图
    $.post("/match/loadBookMatch",{orderId:orderId,deptId:deptId},function(res) {
      var data = res;
      bar(data);
    })
    // 根据期号和学校id拿到相关匹配结果 给map
  });
});




// 柱状图
function bar(data) {
  var bookName=[];
  var lNum=[];
  var bNum=[];

  for(let i = 0;i < data.length ; i++){

    bookName.push(data[i].name);
    lNum.push(data[i].lnum);
    bNum.push(data[i].bnum);
  }
  var myColor = ["#1089E7", "#F57474", "#56D0E3", "#F8B448", "#8B78F6"];
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector(".bar .chart"));
  // 2. 指定配置和数据
  var option = {
    legend: {
          data: ['剩余数量','缺少数量'],
          textStyle: {
            color: "#4c9bfd"
          },
        },
    tooltip: {
      trigger: "axis"
    },
    grid: {
      top: "10%",
      left: "22%",
      bottom: "10%"
      // containLabel: true
    },
    // 不显示x轴的相关信息
    xAxis: {
      show: false
    },
    dataZoom: [//给x轴设置滚动条
      {
        start:0,//默认为0
        end: 100-1500/31,//默认为100
        type: 'slider',
        show: true,
        yAxisIndex: [0,1],
        handleSize: 0,//滑动条的 左右2个滑动条的大小

        left: '95%', //左边的距离
        right: '3%',//右边的距离

        borderColor: "#000",
        fillerColor: '#269cdb',
        borderRadius:5,
        backgroundColor: '#33384b',//两边未选中的滑动条区域的颜色
        showDataShadow: false,//是否显示数据阴影 默认auto
        showDetail: false,//即拖拽时候是否显示详细数值信息 默认true
        realtime:true, //是否实时更新
        filterMode: 'filter',
      },
      //下面这个属性是里面拖到
      {
        type: 'inside',
        show: true,
        yAxisIndex: [0,1],
        start: 0,//默认为1
        end: 100-1500/31,//默认为100
      },
    ],
    yAxis: [

      {
        type: "category",
        inverse: true,
        data: bookName,
        // 不显示y轴的线
        axisLine: {
          show: false
        },
        // 不显示刻度
        axisTick: {
          show: false
        },
        // 把刻度标签里面的文字颜色设置为白色
        axisLabel: {
          color: "#fff"
        },
        // axisPointer: {
        //   show:false //显示指示器
        //
        // },
      },
      {
        show: false,
        data: bookName,
        inverse: true,
        // 不显示y轴的线
        axisLine: {
          show: false
        },
        // 不显示刻度
        axisTick: {
          show: false
        },
        // 把刻度标签里面的文字颜色设置为白色
        axisLabel: {
          color: "#fff"
        }
      }
    ],
    series: [

      {
        name: "缺少数量",
        type: "bar",
        barCategoryGap: 50,
        barWidth: 15,
        yAxisIndex: 0,
        data: lNum,
        itemStyle: {
          color: "#777e96",
          borderColor: "#fff",
          opacity:0.45,
          borderWidth: 3,
          barBorderRadius: 15
        }
      },
      {
        name: "剩余数量",
        type: "bar",
        data: bNum,
        yAxisIndex: 1,
        // 修改第一组柱子的圆角
        itemStyle: {
          barBorderRadius: 20,
          // 此时的color 可以修改柱子的颜色
          color: '#1089E7'
        },
        // 柱子之间的距离
        barCategoryGap: 50,
        //柱子的宽度
        barWidth: 10,
        // 显示柱子内的文字
        label: {
          show: false,
          position: "inside",
          // {c} 会自动的解析为 数据  data里面的数据
          formatter: "{c}"
        }
      },
    ]
  };

  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function() {
    myChart.resize();
  });
  // myChart.on('click', function (params) {
  //   console.log(params);
  //   console.log(params.dataIndex);   //获取点击柱状图的第几个柱子 是从0开始的哦
  // });
};

// 折线图模块
function line(data) {
  var schoolName=[];
  var lNum=[];
  var bNum=[];
  for(let i = 0;i < data.length ; i++){

    schoolName.push(data[i].name);
    lNum.push(data[i].lnum);
    bNum.push(data[i].bnum);
  }
  // 1. 实例化对象
  var myChart = echarts.init(document.querySelector(".line .chart"));
  // 2.指定配置
  var option = {
    // 通过这个color修改两条线的颜色
    color: ["#ed3f35","#00f2f1"],
    tooltip: {
      trigger: "axis"
    },
    legend: {
      // 如果series 对象有name 值，则 legend可以不用写data
      // 修改图例组件 文字颜色
      textStyle: {
        color: "#4c9bfd"
      },
      // 这个10% 必须加引号
      right: "10%"
    },
    grid: {
      top: "20%",
      left: "3%",
      right: "4%",
      bottom: "3%",
      show: true, // 显示边框
      borderColor: "#012f4a", // 边框颜色
      containLabel: true // 包含刻度文字在内
    },

    xAxis: {
      type: "category",
      boundaryGap: false,
      data: schoolName,
      axisTick: {
        show: false // 去除刻度线
      },
      axisLabel: {
        color: "#4c9bfd",// 文本颜色
        interval:0,
        //
        // formatter:function(val){
        //   return val.split("").join("\n");
        // }//代表逆时针旋转90度
      },

      axisLine: {
        show: false // 去除轴线
      }
    },
    yAxis: {
      type: "value",
      axisTick: {
        show: false // 去除刻度线
      },
      axisLabel: {
        color: "#4c9bfd" // 文本颜色
      },
      axisLine: {
        show: false // 去除轴线
      },
      splitLine: {
        lineStyle: {
          color: "#012f4a" // 分割线颜色
        }
      }
    },
    series: [
      {
        name: "缺少数量",
        type: "line",
        // true 可以让我们的折线显示带有弧度
        smooth: true,
        data: lNum
      },
      {
        name: "剩余数量",
        type: "line",
        smooth: true,
        data: bNum
      }
    ]
  };

  // 3. 把配置给实例对象
  myChart.setOption(option);
  // 4. 让图表跟随屏幕自动的去适应
  window.addEventListener("resize", function() {
    myChart.resize();
  });
  // 5.监听折线点击事件
  myChart.on('click', function (params) {
    var  orderId = $("#search_orderid").val();
    var deptId = $(".dtree-theme-item-this").attr("data-id");
    if(deptId == null || deptId == undefined || deptId == ''){
      layer.msg('请在学校列表中选择学校！');
      return 0;
    }
    var grade = params.name;
    $.post("/match/loadBookMatch",{orderId:orderId,deptId:deptId,grade:grade},function(res) {
      var data = res;
      bar(data);
    })
  });
};




// 地图模块：模拟模拟匹配路线
function map(matchData) {
  var lineDates = [];
  var typeData = [];

  if(matchData!=null || matchData !=undefined ){
    if(matchData.length !=0){

      for (var i = 0; i < matchData.length; i++) {
        var heartString= "";
        var bottomString = [];
        heartString = matchData[i][0].fromName;
        typeData.push(matchData[i][0].fromName);
        for (var j = 0; j < matchData[i].length; j++){
          var temp = [];
          temp.push({name:matchData[i][j].fromName},{name:matchData[i][j].toName},matchData[i][j].books);
          bottomString.push(temp);
        }
        lineDates.push([heartString,bottomString]);

      }
    }
  }


  var myChart = echarts.init(document.querySelector(".map .chart"));
  var mapNames = ['china','fufeng'];
  var mapName = mapNames[1];
  // if(matchData == null || matchData == undefined || matchData == ''){
  //     mapName = mapNames[0];
  // }
  // 名称坐标map
  var geoCoordMap = {

    // 学校坐标
    召公初中:[108.004254,34.443154],
    召首初中:[108.000733,34.398368],
    天度初中:[107.976045,34.496635],
    新庄小学:[11992789.546,4041905.88089],
    作里小学:[13137011.9689,3993418.12774],
    天度中心小学:[107.929515,34.497665],
    三头小学:[12148453.2382,4021935.34669],
    聚粮小学 :[12025719.2189,4057682.99058],
    召首小学:[107.993028,34.398358],
    吴家小学:[11655563,3661820],
    召光小学:[107.998636,34.411993],
    灵护小学:[108.029105,34.408345],
    西张小学 :[107.975838,34.420065],
    吕宅小学:[107.98587,34.428272],
    召公中心小学 :[108.004199,34.443141],
    袁新小学 :[12023808.5474,4057061.74513],
    后董小学:[107.975722,34.404433],
    官道小学:[116.1546,39.710265],




    // 各城市坐标
    上海: [121.4648, 31.2891],
    东莞: [113.8953, 22.901],
    东营: [118.7073, 37.5513],
    中山: [113.4229, 22.478],
    临汾: [111.4783, 36.1615],
    临沂: [118.3118, 35.2936],
    丹东: [124.541, 40.4242],
    丽水: [119.5642, 28.1854],
    乌鲁木齐: [87.9236, 43.5883],
    佛山: [112.8955, 23.1097],
    保定: [115.0488, 39.0948],
    兰州: [103.5901, 36.3043],
    包头: [110.3467, 41.4899],
    北京: [116.4551, 40.2539],
    北海: [109.314, 21.6211],
    南京: [118.8062, 31.9208],
    南宁: [108.479, 23.1152],
    南昌: [116.0046, 28.6633],
    南通: [121.1023, 32.1625],
    厦门: [118.1689, 24.6478],
    台州: [121.1353, 28.6688],
    合肥: [117.29, 32.0581],
    呼和浩特: [111.4124, 40.4901],
    咸阳: [108.4131, 34.8706],
    哈尔滨: [127.9688, 45.368],
    唐山: [118.4766, 39.6826],
    嘉兴: [120.9155, 30.6354],
    大同: [113.7854, 39.8035],
    大连: [122.2229, 39.4409],
    天津: [117.4219, 39.4189],
    太原: [112.3352, 37.9413],
    威海: [121.9482, 37.1393],
    宁波: [121.5967, 29.6466],
    宝鸡: [107.1826, 34.3433],
    宿迁: [118.5535, 33.7775],
    常州: [119.4543, 31.5582],
    广州: [113.5107, 23.2196],
    廊坊: [116.521, 39.0509],
    延安: [109.1052, 36.4252],
    张家口: [115.1477, 40.8527],
    徐州: [117.5208, 34.3268],
    德州: [116.6858, 37.2107],
    惠州: [114.6204, 23.1647],
    成都: [103.9526, 30.7617],
    扬州: [119.4653, 32.8162],
    承德: [117.5757, 41.4075],
    拉萨: [91.1865, 30.1465],
    无锡: [120.3442, 31.5527],
    日照: [119.2786, 35.5023],
    昆明: [102.9199, 25.4663],
    杭州: [119.5313, 29.8773],
    枣庄: [117.323, 34.8926],
    柳州: [109.3799, 24.9774],
    株洲: [113.5327, 27.0319],
    武汉: [114.3896, 30.6628],
    汕头: [117.1692, 23.3405],
    江门: [112.6318, 22.1484],
    沈阳: [123.1238, 42.1216],
    沧州: [116.8286, 38.2104],
    河源: [114.917, 23.9722],
    泉州: [118.3228, 25.1147],
    泰安: [117.0264, 36.0516],
    泰州: [120.0586, 32.5525],
    济南: [117.1582, 36.8701],
    济宁: [116.8286, 35.3375],
    海口: [110.3893, 19.8516],
    淄博: [118.0371, 36.6064],
    淮安: [118.927, 33.4039],
    深圳: [114.5435, 22.5439],
    清远: [112.9175, 24.3292],
    温州: [120.498, 27.8119],
    渭南: [109.7864, 35.0299],
    湖州: [119.8608, 30.7782],
    湘潭: [112.5439, 27.7075],
    滨州: [117.8174, 37.4963],
    潍坊: [119.0918, 36.524],
    烟台: [120.7397, 37.5128],
    玉溪: [101.9312, 23.8898],
    珠海: [113.7305, 22.1155],
    盐城: [120.2234, 33.5577],
    盘锦: [121.9482, 41.0449],
    石家庄: [114.4995, 38.1006],
    福州: [119.4543, 25.9222],
    秦皇岛: [119.2126, 40.0232],
    绍兴: [120.564, 29.7565],
    聊城: [115.9167, 36.4032],
    肇庆: [112.1265, 23.5822],
    舟山: [122.2559, 30.2234],
    苏州: [120.6519, 31.3989],
    莱芜: [117.6526, 36.2714],
    菏泽: [115.6201, 35.2057],
    营口: [122.4316, 40.4297],
    葫芦岛: [120.1575, 40.578],
    衡水: [115.8838, 37.7161],
    衢州: [118.6853, 28.8666],
    西宁: [101.4038, 36.8207],
    西安: [109.1162, 34.2004],
    贵阳: [106.6992, 26.7682],
    连云港: [119.1248, 34.552],
    邢台: [114.8071, 37.2821],
    邯郸: [114.4775, 36.535],
    郑州: [113.4668, 34.6234],
    鄂尔多斯: [108.9734, 39.2487],
    重庆: [107.7539, 30.1904],
    金华: [120.0037, 29.1028],
    铜川: [109.0393, 35.1947],
    银川: [106.3586, 38.1775],
    镇江: [119.4763, 31.9702],
    长春: [125.8154, 44.2584],
    长沙: [113.0823, 28.2568],
    长治: [112.8625, 36.4746],
    阳泉: [113.4778, 38.0951],
    青岛: [120.4651, 36.3373],
    韶关: [113.7964, 24.7028]
  };

  var planePath =
    "path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z";
  //var planePath = 'arrow';
  var convertData = function(data) {
    var res = [];
    for (var i = 0; i < data.length; i++) {
      var dataItem = data[i];
      var fromCoord = geoCoordMap[dataItem[0].name];
      var toCoord = geoCoordMap[dataItem[1].name];
      let bookvalue ="";
      for(var j=0;j<dataItem[2].length;j++){
        bookvalue += dataItem[2][j].bookname +':'+dataItem[2][j].value+'本</br>';
      }

      if (fromCoord && toCoord) {
        res.push({
          fromName: dataItem[0].name,
          toName: dataItem[1].name,
          coords: [fromCoord, toCoord],
          // value:    dataItem[2].bookname +':'+dataItem[1].value+'本'
          value:  bookvalue,
        });
      }
    }
    return res;
  };

  var color = ["#a6c84c", "#ffa022", "#46bee9"]; //航线的颜色
  var series = [];
  function getRandomInt(max) {
    return Math.floor(Math.random() * Math.floor(max));
  }
  if(matchData!=null || matchData !=undefined ){
    if(matchData.length !=0){
      for(var i = 0;i<lineDates.length;i++){
        var j = getRandomInt(3);
        series.push(
            {
              name: lineDates[i][0],
              type: "lines",
              zlevel: 1,
              effect: {
                show: true,
                period: 6,
                trailLength: 0.7,
                color: "red", //arrow箭头的颜色
                symbolSize: 5
              },
              lineStyle: {
                normal: {
                  color: color[j],
                  width: 0,
                  curveness: 0.2
                }
              },
              data: convertData(lineDates[i][1])
            },
            {
              name: lineDates[i][0],
              type: "lines",
              zlevel: 2,
              symbol: ["none", "arrow"],
              symbolSize: 10,
              // effect: {
              //   show: true,
              //   period: 6,
              //   trailLength: 0,
              //   symbol: planePath,
              //   symbolSize: 15
              // },
              effect: {
                show: true,
                period: 3, //箭头指向速度，值越小速度越快
                trailLength: 0.01, //特效尾迹长度[0,1]值越大，尾迹越长重
                symbolSize: 9, //图标大小
              },
              lineStyle: {
                normal: {
                  color: color[j],
                  width: 1,
                  opacity: 0.6,
                  curveness: 0.2
                }
              },
              data: convertData(lineDates[i][1])
            },
            {
              name: lineDates[i][0],
              type: "effectScatter",
              coordinateSystem: "geo",
              zlevel: 2,
              rippleEffect: {
                // scale: 4,
                brushType: "stroke"
              },
              label: {
                normal: {
                  show: true,
                  position: "right",
                  formatter: "{b}"
                }
              },
              symbolSize: function(val) {
                return val[2] / 8;
              },
              itemStyle: {
                normal: {
                  color: color[j]
                },
                emphasis: {
                  areaColor: "#2B91B7"
                }
              },
              data: lineDates[i][1].map(function(dataItem) {
                return {
                  name: dataItem[1].name,
                  value: geoCoordMap[dataItem[1].name]
                };
              }),
            },
            {
              name:lineDates[i][0],
              type: 'scatter',
              coordinateSystem: 'geo',
              zlevel: 2,
              rippleEffect: {
                // period: 4,
                brushType: 'stroke',
                // scale: 4
              },
              label: {
                normal: {
                  show: true,
                  position: 'right',
                  //offset:[5, 0],
                  // color: '#0f0',
                  formatter: '{b}',
                  // textStyle: {
                  //   color: "#0f0"
                  // }
                },
                // emphasis: {
                //   show: true,
                //   color: "#f60"
                // }
              },
              // symbol: 'pin',
              symbolSize: function(val) {
                return val[2] / 8;
              },
              itemStyle: {
                normal: {
                  color: color[j]
                },
                emphasis: {
                  areaColor: "#2B91B7"
                },
              },
              data: [{
                name:  lineDates[i][0],
                value: geoCoordMap[ lineDates[i][0]],
              }],
            }

        );
      }
    }
  }
  var option = {

    tooltip: {
      trigger: "item",
      formatter: function(params, ticket, callback) {
        if (params.seriesType == "effectScatter") {
          return "线路：" + params.data.name + "" + params.data.value[2];
        } else if (params.seriesType == "lines") {
          return (
            params.data.fromName +
            ">" +
            params.data.toName +
            "<br />" +
            params.data.value
          );
        } else {
          return params.name;
        }
      }
    },
    toolbox: {
      show: true,
      feature: {
        // dataView: {readOnly: false},
        // restore: {},
        saveAsImage: {}
      },
    },

    legend: {
        orient: "vertical",
        selectedMode: "multiple",
        //下一版本修复 全选与反选功能
        // selector: ['all', 'inverse'],
        // selectorLabel:{
        //   show : true,
        //   color : "#fff",
        //   position :'end',
        //   distance : 15
        // },
        top: "bottom",
        left: "right",
        data: typeData,
        textStyle: {
          color: "#fff"
        },
    },
    geo: {
      map: mapName,
      label: {
        emphasis: {
          show: true,
          color: "#fff"
        }
      },

      // 把中国地图放大了1.2倍
      zoom: 1.2,
      roam: true,
      itemStyle: {
        normal: {
          // 地图省份的背景颜色
          areaColor: "rgba(20, 41, 87,0.6)",
          borderColor: "#195BB9",
          borderWidth: 1
        },
        emphasis: {
          show:true,
          areaColor: 'rgba(20, 41, 87,0.6)'
        }
      }
    },
    series: series
  };
  myChart.setOption(option);

  // 监听浏览器缩放，图表对象调用缩放resize函数
  window.addEventListener("resize", function() {
    myChart.resize();
  });
};
