/**
 * [GetDateDiff 判断天数间隔]
 * @param {[type]} startTime [开始时间]
 * @param {[type]} endTime   [结束时间]
 * @param {[type]} diffType  [时间类型]
 */
function GetDateDiff(startTime, endTime, diffType) {
    //将xxxx-xx-xx的时间格式，转换为 xxxx/xx/xx的格式 
    startTime = startTime.replace(/\-/g, "/");
    endTime = endTime.replace(/\-/g, "/");
    //将计算间隔类性字符转换为小写
    diffType = diffType.toLowerCase();
    var sTime =new Date(startTime); //开始时间
    var eTime =new Date(endTime); //结束时间
    //作为除数的数字
    var timeType =1;
    switch (diffType) {
        case"second":
            timeType =1000;
        break;
        case"minute":
            timeType =1000*60;
        break;
        case"hour":
            timeType =1000*3600;
        break;
        case"day":
            timeType =1000*3600*24;
        break;
        case"month":
            timeType =1000*3600*24*30;
        break;
        case"year":
            timeType =1000*3600*24*365;
        break;
        default:
        break;
    }
    return parseInt((eTime.getTime() - sTime.getTime()) / parseInt(timeType));
}

function bindCustomBtnEvent(obj){
	$(".searchBtn").on("click",function(){
		if($(".datail .layui-table").length>0){
			$(".datail").empty();
		}
		
		
		//校验起始日期不能大于到期日期
		var flag = true;
	    if($("#startDate").length>0){
	        var startDate = $("#startDate").val();
	        var endDate = $("#endDate").val();
	        
	        startDate = parseInt(new Date(startDate).getTime()/1000);
	        endDate = parseInt(new Date(endDate).getTime()/1000);

	        if(startDate>endDate){
	        	flag = false;
	        }
	    }
	    if($("#startMonth").length>0){
	        var startMonth = $("#startMonth").val();
	        var endMonth = $("#endMonth").val();
	        
	        startMonth = parseInt(new Date(startMonth).getTime()/1000);
	        endMonth = parseInt(new Date(endMonth).getTime()/1000);

	        if(startMonth>endMonth){
	        	flag = false;
	        }
	    }
	    if($("#startYear").length>0){
	        var startYear = $("#startYear").val();
	        var endYear = $("#endYear").val();
	        
	        startYear = parseInt(new Date(startYear).getTime()/1000);
	        endYear = parseInt(new Date(endYear).getTime()/1000);

	        if(startYear>endYear){
	        	flag = false;
	        }
	    }
	    if(flag){
 
	    }else{
	       layerMsg("起始日期应小于到期日期");
	       return ;
	    }
		
		
		
		var msg,url,data = $("form").serializeJSON();
		if(data.search_EQ_formReport == ("1")){
			data.search_GTE_updateTime_DATE = data.day0;
			data.search_LTE_updateTime_DATE = data.day1;
			var day = GetDateDiff(data.search_GTE_updateTime_DATE, data.search_LTE_updateTime_DATE, "day");
			msg = "按日统计需小于7天！"
		}else if(data.search_EQ_formReport == ("2")){
			data.search_GTE_updateTime_DATE = data.month0;
			data.search_LTE_updateTime_DATE = data.month1;
			var month = GetDateDiff(data.search_GTE_updateTime_DATE, data.search_LTE_updateTime_DATE, "month");
			msg = "按月统计需小于12个月！"
		}else if(data.search_EQ_formReport == ("3")){
			data.search_GTE_updateTime_DATE = data.year0;
			data.search_LTE_updateTime_DATE = data.year1;
			var year = GetDateDiff(data.search_GTE_updateTime_DATE, data.search_LTE_updateTime_DATE, "year");
			msg = "按年统计需小于10年！"
		}

		if( day && day > 6 ){ // 按日
			layerAlert(msg);
		}else if( month && month > 12 ){ // 按月
			layerAlert(msg);
		}else if( year && year > 10 ){ // 按年
			layerAlert(msg);
		}else{
			// type = 0为按用户统计，type = 1为按机构统计。
			if(obj.type == 0){
				url = obj.queryByUser;
			}else if(obj.type == 1){
				url = obj.queryByOrg;
			}

			var statisticsData = getAjaxData(url, data);
			if(statisticsData.code == "00000001"){
				layerMsg(statisticsData.msg);
			}else{
				userOrOrgStatistics(statisticsData,obj.type);
			}
		}
		
	})
}
/**
 * [arrSort 日期字符串排序]
 * @param  {[type]} arr [description]
 * @return {[type]}     [description]
 */
/*function arrSort(arr){
	for(var i=0;i<arr.length;i++){
	    var time=arr[i].split("-");
	    if(time.length == 1){
	    	arr[i]=time[0];
	    }else if(time.length == 2){
	    	arr[i]=time[0]+"-"+time[1];
	    }else if(time.length == 3){
	    	arr[i]=time[0]+"-"+time[1]+"-"+time[2];
	    }
	}
	return arr.sort(function(a,b){return a<b?0:1});
}*/

/**
 * [echartsUserOrOrgStatistics 生成按用户/按机构统计折线图]
 * @param  {[Object]} result [生成表格所需数据项]
 * @param  {[Array]} col  [表格项配置数组]
 */
function echartsUserOrOrgStatistics(result,col){
         // 基于准备好的dom，初始化echarts实例
        var dom = document.getElementById('chartWrap');
		var myChart = echarts.init(dom);
		var colArr = [];
		for(var i = 0 ; i < col.length; i++){
			colArr.push(col[i]);
		}
		var orgArr = [];
		var obj = {};
		for(var i = 0 ; i < result.list.length; i++){
			orgArr.push(result.list[i].deptCodeName);
			for(var j = 0 ; j < col.length; j++){
				if(!obj[col[j].field]){
					obj[col[j].field]=[];
				}
				obj[col[j].field].push(parseInt(result.list[i][col[j].field]));
			}
		}
		var option = null;
		option = {
		    title: {
		        text: '违规用户统计'
		    },
		    tooltip: {
		        trigger: 'axis'
		    },
		    legend: {
		        data:colArr,
		        top:30
		    },
		    grid: {
		        left: '3%',
		        right: '4%',
		        bottom: '3%',
		        containLabel: true
		    },
		    toolbox: {
		        feature: {
		            saveAsImage: {}
		        }
		    },
		    xAxis: {
		        type: 'category',
		        boundaryGap: false,
		        data: orgArr
		    },
		    yAxis: {
		        type: 'value'
		    },
		    series: []
		};
		for(var i = 0; i<col.length;i++){
			if(!(col[i].field =='deptCodeName')){
				if(!(col[i].field =='sum')){	
					option.series.push({
						name:col[i],
			            type:'line',
			            stack: '总量',
			            data:obj[col[i].field]
					})
				}
			}

		}
		if (option && typeof option === "object") {
		    myChart.setOption(option, true);
		}
}

/**
 * [userOrOrgStatistics 组装数据生成按用户/按机构统计表格]
 * @param  {[Object]} data [生成表格所需数据项]
 * @param  {[Array]} col [生成表格所需表头数据项]
 */
function userOrOrgStatistics(data,type){
	// 获取查询原因字典
	var qryReasonDic = {
		url : "common/cpqDic",
		type : "qryReason"
	};
	var dicObj = getDic(qryReasonDic.url, qryReasonDic.type);
	// 对单用户单日次数求和userCount
	$.each(data.data,function(i0,e0){
		e0.userCount = [];
		$.each(data.date,function(i1,e1){
			var userCount = 0;
			$.each(e0.dataList,function(i2,e2){
				userCount += e2.count[i1];
			})
			e0.userCount.push(userCount);
		})
	})
	var title = table = thead = tbody = "";
	if(type == 0){
		type = "用户";
	}else if(type == 1){
		type = "机构";
	};
	thead = "<th style=\"width:240px\"><div class='divide'>查询原因\\"+ type +"姓名\\时间</div></th>"
	$.each(data.date,function(i,e){
		thead +="<th lay-data='{field:\"td\", width:100}'>"+ e +"</th>";

	});
	thead +="<th>合计</th>";
	var newData = data;
	
	var sumArr = [];//总计数据
	// [[0,1],[1,1]]
	$.each(newData.data,function(i3,e3){
	    var nameData =  e3.userName;
	    if(type == "用户"/*用户统计*/){
	        
	    }else if(type == "机构"/*机构统计*/){
	        var qryReasonDic = {
	                url : "common/getDeptCodeName",
	                type : ""
	            };
	            var orgObj = getDic(qryReasonDic.url, qryReasonDic.type);
	        nameData = orgObj[nameData];
	    };
		// 拼接用户行数据
		tbody += "<tr class='userTr'><td><div class='name'>"+ nameData +"</div></td>";
		var sum1 = 0;
		sumArr.push(e3.userCount);
		$.each(e3.userCount,function(i4,e4){
			tbody += "<td>"+ e4 +"</td>";
			sum1 += e4;

		})
		tbody += "<td>"+ sum1 +"</td></tr>";
		$.each(e3.dataList,function(i5,e5){
			tbody += "<tr><td><div class='reason'>"+ dicObj[e5.queryReason] +"</div></td>";
			var sum2 = 0;
			$.each(e5.count,function(i6,e6){
				tbody += "<td>"+ e6 +"</td>";
				sum2 += e6;
			})
			tbody += "<td>"+ sum2 +"</td></tr>";
		})
	})
	tbody += "<tr class=\"totalTr\"><td>总计</td>";
	// [[0,1],[1,1]]
	// 计算总计行
	var totalArr = [];
	$.each(data.date,function(i7,e7){
	// for(var i = 0 ; i < data.date.length; i++){
		var sum3 = 0;
		$.each(sumArr,function(i8,e8){
			sum3 += e8[i7];
			// sum3 += e8[i];
		});
		totalArr.push(sum3);
		sum3 = 0;
	// }
	})
	var sum4 = 0;
	$.each(totalArr,function(i9,e9){
		tbody += "<td><div class=\"total\">"+ e9 +"</div></td>";
		sum4 += e9;
	});
	tbody += "<td><div class=\"total\">"+ sum4 +"</div></td></tr>";
	var table = "<table class=\"layui-table\" lay-even lay-skin=\"line\"><thead><tr>"+ thead +"</tr></thead><tbody>"+ tbody +"</tbody></table>";
	$(".datail").append(table);
	
}


/*function userOrOrgCount(i,data){
	$.each(data.date,function(i,e){
		var colsCount;
		$.each(e0.data,function(i1,e1){
			if(!e1.count[i]){
				e1.count[i] = 0;
			}
			colsCount = e1.count[i]
		})

	})
}*/

/*function userOrOrgStatistics(data,type){
	var col = arrSort(data.dateSet);
	var list = data.data;
	// type为1按机构统计
	if(type == 1){
		// 通过deptCode获取机构名称
		var url = "common/getDeptCodeName";
		var pName = window.sessionStorage.projectName;
		var deptCode = getDic(url,"deptCodeName",pName);
		for (var i = list.length - 1; i >= 0; i--) {
			list[i].deptCodeName=deptCode[list[i].deptCode];
		}
	}

	var name,head,title = "";
	if(type == 0){
		title = "用户姓名\\时间";
		name = "userName";
		head = "用户";
	}else if(type == 1){
		title = "机构名称\\时间";
		name = "deptCodeName";
		head = "机构";
	}
	// 为表头添加title和合计
	col.unshift(title);
	col.push("合计");
	// 表头
	var table = "<table class='statisticsTable'><thead><tr><th colspan="+(col.length)+">按"+head+"统计报表</th></tr>";
		table += "<tr><th colspan="+(col.length)+">&nbsp;</th></tr>";
		table += "<tr><th align='left' colspan="+(col.length-1)+">统计时间：" + data.startDate +" 至 "+ data.endDate +"</td><td>单位：次</th></tr>"; 
		table += "<tr>";
	for(var j = 0;j<col.length;j++){  
		table += "<td>"+col[j]+"</td>";
	};
	table += "</tr></thead><tbody>";
	for(var i = 0;i<list.length;i++){  
		for(var j = 0;j<list[i].data.length;j++){
			list[i][list[i].data[j].date]=list[i].data[j].count;
		}
	}
	// 表格内容
	for(var i = 0;i<list.length;i++){  
		table +="<tr>";
		var num=0,rowSum=0;
		for(var j = 0;j<col.length;j++){
			if(col[j] == "合计"){
				table +="<td>"+rowSum+"</td>";
			}else if(col[j] == title){
				table +="<td>"+list[i][name]+"</td>";
			}else{
				if(list[i][col[j]]){
					num = list[i][col[j]];
				}else{
					num = 0;
				}
				rowSum+=num;
				table +="<td>"+num+"</td>";
			}
		};
		table +="</tr>";
	}

	// 总计
	var total,totalSum = 0,arr = ["总计"];
	for(var i = 1; i < col.length;i++){
			if(col[i] != "合计"){
				total = totalTimes(list,col[i]);
				totalSum+=total;
				arr.push(total);
			}
	}
	arr.push(totalSum);
	table += "<tr>";
	for(var j = 0;j<arr.length;j++){  
		table += "<td>"+arr[j]+"</td>";
	};
	table +="</tr></tbody></table>"; 

	$(".datail").append(table); 
	// 生成折线图
	// echartsUserOrOrgStatistics(data,col);
}*/

/**
 * [totalTimes 计算list中相同key的合计]
 * @param  {[Array]} list  [后台返回表格数据对象]
 * @param  {[String]} date [每一列对应的日期]
 * @return {[Number]}       [每一列总计的数值]
 */
/*function totalTimes(list,date){
	var total = 0;
	for(var i = 0;i<list.length;i++){ 
		if(list[i][date]){
			total +=  list[i][date];
		}
	}
	return total;
}*/