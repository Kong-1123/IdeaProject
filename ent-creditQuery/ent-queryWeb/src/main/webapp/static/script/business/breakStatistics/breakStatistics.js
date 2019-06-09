

/**
 * [echartsBreakStatistics 生成违规统计折线图]
 * @param  {[Object]} result [生成表格所需数据项]
 * @param  {[Array]} col  [表格项配置数组]
 */
function echartsBreakStatistics(result,col){
         // 基于准备好的dom，初始化echarts实例
        var dom = document.getElementById('chartWrap');
		var myChart = echarts.init(dom);
		var colArr = [];
		for(var i = 0 ; i < col.length; i++){
			colArr.push(col[i].title);
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
						name:col[i].title,
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
 * [breakStatistics 组装数据生成违规统计表格]
 * @param  {[Object]} data [生成表格所需数据项]
 */
function breakStatistics(data){
	var list = data.list;
	// 通过deptCode获取机构名称
	var url = "common/getDeptCodeName";
	var pName = window.sessionStorage.projectName;
	var deptCode = getDic(url,"deptCodeName",pName);
	for (var i = list.length - 1; i >= 0; i--) {
		list[i].deptCodeName=deptCode[list[i].deptCode];
	}
	var col =[
		{
			field : 'deptCodeName',
			title : '机构名称'
		},{
			field : 'hourQuery',
			title : '一小时内查询超量'
		},{
			field : 'dateQuery',
			title : '单日查询超量'
		},{
			field : 'workTime',
			title : '非工作时间查询'
		},{
			field : 'businessQuery',
			title : '无业务背景查询'
		},{
			field : 'repeatedQuery',
			title : '日内重复查询'
		},{
			field : 'sum',
			title : '合计'
		},
	];

	// 表头
	var table = "<table class='statisticsTable'><thead><tr><th colspan="+(col.length)+">违规用户统计报表</th></tr>";
		table += "<tr><th colspan="+(col.length)+">&nbsp;</th></tr>";
		table += "<tr><th align='left' colspan="+(col.length-1)+">统计时间：" + data.startDate +" 至 "+ data.endDate +"</td><td>单位：次</th></tr>"; 
		table += "<tr>";
		for(var j = 0;j<col.length;j++){  
			table += "<td>"+col[j].title+"</td>";
		};
		table += "</tr></thead><tbody>";

	// 表格内容
	for(var i = 0;i<list.length;i++){  
		table +="<tr>";
		var num=0,rowSum=0;  
		for(var j = 0;j<col.length;j++){
			if(col[j].field == "sum"){
				table +="<td>"+rowSum+"</td>";
			}else{
				num = parseInt(list[i][col[j].field]);
				if(!isNaN(num)){
					rowSum+=num;
				}
				table +="<td>"+list[i][col[j].field]+"</td>";
			}
		};
		table +="</tr>";
	}

	// 总计
	var total,totalSum = 0,arr = ["总计"];
	for(var i = 1; i < col.length;i++){
		total = totalTimes(list,col[i].field);
		if(!isNaN(total)){
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

	$(".gridWrap").append(table); 
	// 生成折线图
	// echartsBreakStatistics(data,col);
}

/**
 * [totalTimes 计算list中相同key的合计]
 * @param  {[Array]} list  [description]
 * @param  {[String]} field [description]
 * @return {[type]}       [description]
 */
function totalTimes(list,field){
	var total = 0;
	for(var i = 0;i<list.length;i++){ 
		total +=  parseInt(list[i][field]);
	}
	return total;
}