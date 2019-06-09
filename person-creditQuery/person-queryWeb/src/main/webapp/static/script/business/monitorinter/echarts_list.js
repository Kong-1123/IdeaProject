$(function(){
	// 获取所有接口的访问路径
	var path = getUrlList("../static/script/urlList/monitorinter.json");	


	// 获取全局配置 一
	var data_config = getAjaxData(path["tabs"][0]["configUrl"]);

	// 根据机构代码查询出银行名称
	var deptCodeName = {
		url : "common/getDeptCodeName",
		type : "deptCodeName"
	};
	// 根据状态码查询状态名称
	var queryTypeName = {	
		url : "common/cpqDic",
		type : "qryStatus"
	};
	var deptObj = getDic(deptCodeName.url, deptCodeName.type);
	var queryStateObj = getDic(queryTypeName.url, queryTypeName.type);


    //初始化echarts图
	 function echartsInit(){
		// 24小时查询量 二
		var data_oneday = getAjaxData(path["tabs"][0]["onedayUrl"]);
		// 近一周查询量 三
		var data_oneweek = getAjaxData(path["tabs"][0]["oneweekUrl"]);
		// 获取实时查询量 四	
		var data_realtime = getAjaxData(path["tabs"][0]["realtimeUrl"]);
		// 机构查询量排行榜 五
		var data_institu = getAjaxData(path["tabs"][0]["instituUrl"]);
		// 征信用户查询量排行榜 六
		var data_creditInfo = getAjaxData(path["tabs"][0]["creditInfoUrl"]);
		// 内部用户查询量排行榜 七
		var data_internalUser = getAjaxData(path["tabs"][0]["internalUserUrl"]);
		// 当天查询总量 八 数字自动增加
		var data_allLine = getAjaxData(path["tabs"][0]["alllineUrl"]);
		// 全行当天查询状态 九
		var data_querystate = getAjaxData(path["tabs"][0]["querystateUrl"]);
		// 征信用户查询异常 十一
		 var data_creditUserEx = getAjaxData(path["tabs"][0]["creditUserExUrl"]);
		// 内部用户查询异常 十一
		 var data_internalUserEx = getAjaxData(path["tabs"][0]["internalUserExUrl"]);


	 	/*第一层*/
	 	onedayQuery_echarts(data_oneday.oneDayQuery,queryStateObj);	// 近24小时查询量
	 	oneweekQuery_echarts(data_oneweek.weekQuery,queryStateObj);	// 近一周查询量
	 	justintime_echarts(data_realtime.realTimeQuery,data_config.param);	// 实时量
	 	/*第二层*/
	 	orgQueryRank_echarts(data_institu.orgQueryNumRank,deptObj);	// 机构查询量排行榜
	    creditUserQueryRank_echarts(data_creditInfo.creditUserQueryNumRank);	// 征信用户查询量排行榜
	    innerUserQueryRank_echarts(data_internalUser.innerUserQueryNumRank);	// 内部用户查询量排行榜
	    /*第三层*/    
		theday_numAnimate(data_allLine.allLineOfDay);	// 数字自动增加特效
	    queryState_echarts(data_querystate.queryState,queryStateObj);	// 全行当天查询状态
	    creditUserException_list(data_creditUserEx.creditExc.data);	// 征信用户查询异常
	    innerUserException_list(data_internalUserEx.innerExc.data);	// 内部用户查询异常
	   
	}

	// 判断是否是顶级机构(即总行)
	// 获取ajax数据
	 var istop = getAjaxData(path["tabs"][0]["istopcodeUrl"]);
	if(istop.code == '00000001'){	// 不是顶级机构
		$(".layui-fluid").hide();
		$(".layui-fluid").css('opacity', 0);

		// 获取监控界面的标题节点元素
		var ulParent = parent.$("#LAY_app_tabs #LAY_app_tabsheader")[0];
		var lidom = $(ulParent).find('.layui-this')[0];

		// 弹出提示对话框
		layui.use(["layer"],function(){
 			var layer = layui.layer;
 			layer.alert(istop.msg,{
 				yes:function(){
 					// 删除该菜单项标题标签元素，让上一个兄弟元素显示
 					// $(ulParent.getElementsByClassName('layui-this')[0]).prev().addClass("layui-this");
 					$($(ulParent).find('.layui-this')[0]).prev().addClass("layui-this");
 					ulParent.removeChild(lidom);

 					// 删除该iframe菜单项标签元素，让上一个兄弟元素显示
 					parent.$("#LAY_app_body .layadmin-tabsbody-item.layui-show").last().prev().addClass("layui-show");
 					parent.$("#LAY_app_body .layadmin-tabsbody-item.layui-show").last().remove();

 					// 关闭layui对话框弹出层
 					layer.closeAll();
 				}
 			});
 		});
	}else{	// 是顶级机构
		$(".layui-fluid").show();
		$(".layui-fluid").css('opacity', 1);

		// 页面加载时第一次运行
		echartsInit();

		// 每隔5s刷新一次数据
		var time = data_config.param.time * 1000;
	    setInterval(function () {
	  		echartsInit();
		},time);
	}
})

/********************第一层********************/

// 近24小时查询量
var onedayQueryChart = echarts.init(document.getElementById('onedayhQueryEc'));
function onedayQuery_echarts(data,queryStateObj){	
	//对象进行空值判断
	data = data || {};
	queryStateObj = queryStateObj || {};
	// console.log(data);

	var option = {
		color: ['#7DB1E1','#F9D395','#EF474E'],
	    // title: {
	    //     text: '近24小时查询量',
	    //     textStyle:{
	    //     	color: '#444',
	    //     	fontSize: '16',
	    //     	fontWeight: 'normal'
	    //     },
	    //     x: 10,
	    //     y: 5
	    // },
	    tooltip: {
	        trigger: 'axis'
	    },
	    legend: {
	    	// 设置图形
	    	// icon: "rect ",  
	    	// itemWidth: 10,
	    	// itemHeight: 10,
	    	// itemGap: 10, // 设置间距
	    	top: '10',
	        data:[queryStateObj[1], queryStateObj[2], queryStateObj[3]]
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: true,
	        data : data.date,
	    },
	    yAxis: {
	        type: 'value'
	    },
	    series: [
	        {
	            name:queryStateObj[1],
	            type:'line',
	            symbolSize: '6',
	            data:data.series[0][1] || [0]
	        },
	        {
	            name:queryStateObj[2],
	            type:'line',
	            symbolSize: '6',
	            data:data.series[0][2] || [0]
	        },
	        {
	            name:queryStateObj[3],
	            type:'line',
	            symbolSize: '6',
	            data:data.series[0][3] || [0]
	        }
	    ]
	};

	onedayQueryChart.setOption(option);
};


// 近一周查询量
var oneweekQueryChart = echarts.init(document.getElementById('oneweekQueryEc'));
function oneweekQuery_echarts(data,queryStateObj){	
	//对象进行空值判断
	data = data || {};
	queryStateObj = queryStateObj || {};

	var option = {
		color: ['#7DB1E1','#F9D395','#EF474E'],
	    legend: {
	    	top: '10px',
	        // data:['查询成功','查无此人','查询失败']
	        data:[queryStateObj[1], queryStateObj[2], queryStateObj[3]]
	    },
	    tooltip: {
	        trigger: 'axis'
	    },
	    grid: {
	        left: '3%',
	        right: '4%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'category',
	        boundaryGap: true,
	        data: data.date
	    },
	    yAxis: {
	        type: 'value'
	    },
	    series: [
	        {
	            name:queryStateObj[1],
	            type:'line',
	            symbolSize: '6',
	            data: data.series[0][1]
	        },
	        {
	            name:queryStateObj[2],
	            type:'line',
	            symbolSize: '6',
	            data: data.series[0][2]
	        },
	        {
	            name:queryStateObj[3],
	            type:'line',
	            symbolSize: '6',
	            data: data.series[0][3]
	        }
	    ]
	};

	oneweekQueryChart.setOption(option);
};


// 实时量
var justinTimeChart = echarts.init(document.getElementById('justinTimeEc'));
function justintime_echarts(data, data_config){
	//对对象进行空值判断
	data = data || {};
	data_config = data_config || {};

	var option = {
		title : {
	        text: (data_config.time || 5) + '秒内实时量(次)',
	        textStyle:{
	        	color: '#444',
	        	fontSize: '16',
	        	fontWeight: 'normal'
	        },
	        x:'center',
	        y: 10
	    },
	    tooltip : {
	        formatter: "{a} <br/>{b}次 : {c}"
	    },
	    series: [
	        {
	            name: '实时量',
	            type: 'gauge',
	            radius: '70%',
	            fontSize: '14px',
	            min: 0,
	            max: Number(data_config.maxNum) || 100,
	            center: ['50%', '52%'],
	            detail: {
	            	formatter:'{value}'+'次',
	            	offsetCenter: [0, "100%"],
	            	textStyle: { // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    fontSize: '24',
	                    color: '#F2705B'
	                }
	            },
	            data: [{value: data.value || 0}],
	            axisLine: { // 坐标轴线
	                lineStyle: { // 属性lineStyle控制线条样式
	                    width: 10,
	                    shadowColor: '#fff', //默认透明
	                    shadowBlur: 10
	                }
	            },
	            splitLine: { // 分隔线
	                length: 8, // 属性length控制线长
	                lineStyle: { // 属性lineStyle（详见lineStyle）控制线条样式
	                    width: 3,
	                    color: '#fff',
	                    shadowColor: '#fff', //默认透明
	                    shadowBlur: 10
	                }
	            },
	        }
	    ]
	};
	justinTimeChart.setOption(option);
};


/********************第二层********************/

// 机构查询量排行榜
var orgQueryRankChart = echarts.init(document.getElementById('orgQueryRankEc'));
function orgQueryRank_echarts(data,deptObj){
	//对象进行空值判断
	// data = null;
	data = data || {};
	deptObj = deptObj || {};

	// 根据机构代码获取机构名称
	var deptArr = [];
	for(var i=0; i<data.orgCode.length; i++){
		// deptcode = Number(data.orgCode[i]);
		deptcode = data.orgCode[i];
		deptArr.push(deptObj[deptcode]);
	}

	var option = {
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'shadow'
	        }
	    },
	    grid: {
	        left: '0%',
	        right: '8%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'value',
	        splitLine: { //网格线
				show: true
			},
	        boundaryGap: [0, 0.01]
	    },
	    yAxis: {
	    	show: false,
	        type: 'category',
	        axisTick:{ //y轴刻度线
				show: false
			},
			splitLine: { //网格线
				show: false
			},
			//机构代码
	        data: deptArr
	    },
	    series: [
	        {
	            type: 'bar',
	            barMaxWidth: 20,//最大宽度
	            //机构代码对应数据
	            data: data.series || [0],
	            //顶部数字展示pzr
                itemStyle: {
                    //柱形图圆角，鼠标移上去效果
                    emphasis: {
                        barBorderRadius: [5, 5, 5, 5]
                    },
                    normal: {
                    	//每根柱子设置不同的颜色 
                    	color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#BEACD4','#FFBF5B','#FEE37C','#B5DFAF','#80BEE3', '#B452CD','#F78E57','#D45477','#83A8C3','#EEAEEE'
                            ];
                            return colorList[params.dataIndex]
                        },
                    	label:{
		                    show: true,
		                    position: 'right',
		                    formatter: '{b}'
		                },
		                labelLine :{show:true},
                        //柱形图圆角，初始化效果
                        barBorderRadius:[5, 5, 5, 5]
                    }
                },
	        }
	    ]
	};

	orgQueryRankChart.setOption(option);
};

// 征信用户查询量排名
var creditUserQueryRankChart = echarts.init(document.getElementById('creditUserQueryRankEc'));
function creditUserQueryRank_echarts(data){	
	//对象进行空值判断
	data = data || {};
	// 判断机构码与查询量为[],则赋值为[0]
	if(data.orgCode.length <= 0){ data.orgCode = [0]; }
	if(data.series.length <= 0){ data.series = [0]; }	

	var option = {
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'shadow'
	        }
	    },
	    grid: {
	        left: '0%',
	        right: '8%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'value',
	        splitLine: { //网格线
				show: true
			},
	        boundaryGap: [0, 0.01]
	    },
	    yAxis: {
	    	show: false,
	        type: 'category',
	        axisTick:{ //y轴刻度线
				show: false
			},
			splitLine: { //网格线
				show: false
			},
	        data: data.orgCode
	    },
	    series: [
	        {
	            type: 'bar',
	            barMaxWidth: 20,//最大宽度
	            // data: [93, 89, 134, 70, 74],
	            data: data.series,
	            //顶部数字展示pzr
                itemStyle: {
                    //柱形图圆角，鼠标移上去效果
                    emphasis: {
                        barBorderRadius: [5, 5, 5, 5]
                    },
                     
                    normal: {
                    	//每根柱子设置不同的颜色 
                    	color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#709FEF','#A4DCF3','#FDBE7B','#F7E799','#FD8D8C', '#FF83FA','#DD9C80','#89A396','#7F7F7F','#F98355'
                            ];
                            return colorList[params.dataIndex]
                        },
                    	label:{
		                    show: true,
		                    position: 'right',
		                    formatter: '{b}'
		                },
		                labelLine :{show:true},
                        //柱形图圆角，初始化效果
                        barBorderRadius:[5, 5, 5, 5]
                    }
                },
	        }
	    ]
	};

	creditUserQueryRankChart.setOption(option);
};

// 内部用户查询量排名
var innerUserQueryRankChart = echarts.init(document.getElementById('innerUserQueryRankEc'));
function innerUserQueryRank_echarts(data){	
	//对象进行空值判断
	data = data || {};
	// 判断机构码与查询量为[],则赋值为[0]
	if(data.orgCode.length <= 0){ data.orgCode = [0]; }
	if(data.series.length <= 0){ data.series = [0]; }	

	var option = {
	    tooltip: {
	        trigger: 'axis',
	        axisPointer: {
	            type: 'shadow'
	        }
	    },
	    grid: {
	        left: '0%',
	        right: '8%',
	        bottom: '10%',
	        containLabel: true
	    },
	    xAxis: {
	        type: 'value',
	        splitLine: { //网格线
				show: true
			},
	        boundaryGap: [0, 0.01]
	    },
	    yAxis: {
	    	show: false,
	        type: 'category',
	        axisTick:{ //y轴刻度线
				show: false
			},
			splitLine: { //网格线
				show: false
			},
	        data: data.orgCode
	    },
	    series: [
	        {
	            type: 'bar',
	            barMaxWidth: 20,//最大宽度
	            data: data.series, 
	            //顶部数字展示pzr
                itemStyle: {
                    //柱形图圆角，鼠标移上去效果
                    emphasis: {
                        barBorderRadius: [5, 5, 5, 5]
                    },
                     
                    normal: {
                    	//每根柱子设置不同的颜色 
                    	color: function(params) {
                            // build a color map as your need.
                            var colorList = [
                              '#F98355','#F5E293','#C8D8A9','#A7D1E1','#AAB6DE', '#C0E0EF','#FABF00','#CDAA7D','#F98700','#F9644E'
                            ];
                            return colorList[params.dataIndex]
                        },
                    	label:{
		                    show: true,
		                    position: 'right',
		                    formatter: '{b}'
		                },
		                labelLine :{show:true},
                        //柱形图圆角，初始化效果
                        barBorderRadius:[5, 5, 5, 5]
                    }
                },
	        }
	    ]
	};

	innerUserQueryRankChart.setOption(option);
};


/********************第三层********************/

// 当天查询总量 数字动画特效
var numObj={
	flag: true,	// 定义一个标识变量用于监控数据第一次变化，一旦数据发生变化，标识则变为false,动画不在执行
	value: 0  	// 用于实时接收后端传送过来的数据
};
function theday_numAnimate(data){
	// 对象进行空值判断
	if(!data){ data = {}; data.value = 0; }
	// 数字动画每次执行的时间
	var time = data.value <= 1000 ? 2000 : 4000;
	// 实时接收数据
	numObj.value = data.value;	

	$('#thedayEc .sum-data').each(function(){
		$(this).prop('Counter',0).animate({
			// Counter: $(this).text()
			Counter: data.value
		},{
			duration: time,
			easing: 'swing',
			step: function (now){
				// 进行动画判断
				if(numObj.flag){	// 页面加载时数据刷新第一次执行动画
					$(this).text(Math.ceil(now));
				}else{	// 数据刷新后第二次则不在执行动画
					$(this).text(numObj.value);
				}
			}
		});
	});

	// 动画执行完成一次之后，让flag变为false，动画不在执行
	setTimeout(function(){
		numObj.flag = false;
	},time);
};

// 全行当天查询状态
var queryStateChart = echarts.init(document.getElementById('queryStateEc'));
function queryState_echarts(data,queryStateObj){
	//对象进行空值判断
	data = data || {};
	queryStateObj = queryStateObj || {};

	var option = {
		color: ['#01ADEF','#F5AF40','#FD8D8C'],
	    tooltip : {
	        trigger: 'item',
	        position: 'top',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        orient: 'vertical',
	        // x: 'right',
	        // y: 'middle',
	        left: '60%',
	        top: '30%',
	        data:[queryStateObj[1], queryStateObj[2], queryStateObj[3]]
	    },
	    series : [
	        {
	            name: '查询结果',
	            type: 'pie',
	            radius : '70%',
	            center: ['35%','50%'],
	            label:{
	                normal:{
	                    position:'inner',
	                    show: false
	                }
	            },   
	            data:[
	                {value: data['1'] || 0, name: queryStateObj[1]},
	                {value: data['2'] || 0, name: queryStateObj[2]},
	                {value: data['3'] || 0, name: queryStateObj[3]}
	            ],
	        }
	    ]
	};

	queryStateChart.setOption(option);
};


// 征信用户查询异常
function creditUserException_list(data){
	//对象进行空值判断
	data = null;
	data = data || ['暂无数据'];

	var strli ='';
	var len = data.length;
	$("#creditUserExceptionEc .dowebok ul").html('');
	for(var i=0; i<len; i++){
		strli += '<li>'+data[i]+'</li>';
	}
	$("#creditUserExceptionEc .dowebok ul").html(strli);
	creditUserExTips();
};
// 鼠标悬停提示
function creditUserExTips(){
	$("#creditUserExceptionEc .dowebok ul li").each(function(){
		//获取鼠标悬浮位置元素的内容
		var text = $(this).text();
		var id = $(this).attr('id');
		// 获取元素的宽度
		var li_width = $(this).width();
		//获取元素的高度
		var li_height = $(this).height();
		// 弹出提示层信息
		$(this).tipso({
	        useTitle: false,
	        position: 'top',
	        content: text,
	        offsetX: -(li_width/2),
	        // offsetY: -li_height,
	        width: 'auto',
	        speed: 50
	    });
	});

	// 清除多余生成的提示层元素(鼠标悬浮或来回移动时会生成多个提升层)
	$("#creditUserExceptionEc .dowebok ul li").mouseover(function(){
		if($("body").children(".tipso_bubble").length>=0){
			$("body").children(".tipso_bubble").remove();
		}
	});
	$("#creditUserExceptionEc .dowebok ul li").hover(function(){
		if($("body").children(".tipso_bubble").length>=0){
			$("body").children(".tipso_bubble").remove();
		}
	});

	//当鼠标离开时删除提示层元素
	$("#creditUserExceptionEc .dowebok ul li").mouseout(function(){
		$("body").children(".tipso_bubble").remove();
	});
}


// 内部用户查询异常
function innerUserException_list(data){	
	//对象进行空值
	data = null;
	data = data || ['暂无数据'];

	var li_str ='';
	var len = data.length;
	$("#innerUserExceptionEc .dowebok ul").html('');
	for(var i=0; i<len; i++){
		li_str += '<li>'+data[i]+'</li>';
	}
	$("#innerUserExceptionEc .dowebok ul").html(li_str);
	innerUserExTips();
};
// 鼠标悬停提示
function innerUserExTips(){
	$("#innerUserExceptionEc .dowebok ul li").each(function(){
		//获取鼠标悬浮位置元素的内容
		var text = $(this).text();
		//获取顶级父级元素的宽度
		var div_width = $(".exception-list").width();
		// 获取元素的宽度
		var li_width = $(this).width();
		//获取元素的高度
		var li_height = $(this).height();
		// 弹出提示层
		$(this).tipso({
	        useTitle: false,
	        position: 'top',
	        content: text,
	        offsetX: -(li_width/2),
	        // offsetY: -li_height,
	        width: 'auto',
	        speed: 0,
	        // 执行前的回调函数
	        onBeforeShow: function(){
	        	// 当文本的长度>40且li的宽度<360调整offsetX的偏移距离
	        	if(div_width < 380 & this.content.length >= 40){
	        		// this.offsetX = -li_width;
	        		this.offsetX = -(li_width/2 + this.content.length*2);
	        	}
	        }
	    });
	});
	// 清除多余生成的提示层(鼠标悬浮或来回移动时会生成多个提升层)
	$("#innerUserExceptionEc .dowebok ul li").mouseover(function(){
		if($("body").children(".tipso_bubble").length>=0){
			$("body").children(".tipso_bubble").remove();
		}
	});
	$("#innerUserExceptionEc .dowebok ul li").hover(function(){
		if($("body").children(".tipso_bubble").length>=0){
			$("body").children(".tipso_bubble").remove();
		}
	});

	//当鼠标离开时删除提示层元素
	$("#innerUserExceptionEc .dowebok ul li").mouseout(function(){
		$("body").children(".tipso_bubble").remove();
	});
}


/********************echats图表随屏幕的缩放********************/

//使图表跟着屏幕尺寸的变化而变化
window.onresize = function(){
	/*第一层*/
	onedayQueryChart.resize();
	oneweekQueryChart.resize();
	justinTimeChart.resize();
    /*第二层*/
    orgQueryRankChart.resize();
    creditUserQueryRankChart.resize();
    innerUserQueryRankChart.resize();
    /*第三层*/
    queryStateChart.resize();
    
    //myChart1.resize();    //若有多个图表变动，可多写
}