/**
 * [bindCustomBtnEvent 按钮点击事件]
 * 
 */
      function bindCustomBtnEvent(urlObj){
    	  $(".btnWrap .relevantBtn").bind("click", function() {
    		  relevant(urlObj);
    		});
      }
      function relevant(urlObj){
    	  var data = $("form").serializeJSON();
    	  
    	  //日期校验
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
    	    if(flag){
    	    	
    	    }else{
    	       layerMsg("起始日期应小于到期日期");
    	       return ;
    	    }
    	  
    	  ajaxFunc(urlObj.getRelieve, function(result) {
  			if (result.code == "00000001") {
  				layerMsg(result.msg);
  				$(".datail").empty();
  			} else {
  				breakStatistics(result);
  			}
  		}, function(result) {
  			layerMsg("请求出错！");
  		}, data, false);
    	 
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
            			field : 'c0000',
            			title : '征信用户每日查询量上限'
            		},{
            			field : 'c0001',
            			title : '内部用户每日查询量上限'
            		},{
            			field : 'c0002',
            			title : '工作日查询控制'
            		},{
            			field : 'c0003',
            			title : '工作时间控制'
            		},{
            			field : 'c0004',
            			title : '疑似未授权查询'
            		},{
            			field : 'c0005',
            			title : '征信用户多次失败查询控制'
            		},{
            			field : 'c0006',
            			title : '内部用户多次查无此人查询控制'
            		},{
            			field : 'c0007',
            			title : '征信用户短时高频查询控制'
            		},{
            			field : 'c0008',
            			title : '内部用户短时高频查询控制'
            		},{
              			field : 'c0009',
              			title : '异地查询限制'
            		},{
            			field : 'c0010',
            			title : '异常查询次数控制'
            		},{
            			field : 'sum',
            			title : '合计'
            		},
            	];

      	// 表头
      	/*var table = "<table class=\"layui-table statisticsTable\" lay-even lay-skin=\"line\"><thead><tr><th colspan="+(col.length)+">违规用户统计报表</th></tr>";
      		table += "<tr><th colspan="+(col.length)+">&nbsp;</th></tr>";*/
          var table = "<table class=\"layui-table statisticsTable\" lay-even lay-skin=\"line\"><thead>";
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

      	$(".datail").empty().append(table); 
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

function searchBtn(returnObj, obj) {
    // 查询
    var dataTable = returnObj.dataTable;
    var searchData = $("form").serializeJSON();
    var data = returnObj.searchData;
    if(data){
        for(var i in data){
            if(searchData[i] != "" && !searchData[i]){
                searchData[i] = data[i]; 
            }
        }
    }
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

    if(flag){
        dataTable.reload({
            where: searchData,
            page: {
                curr: 1 //重新从第 1 页开始
            }
        }); 
    }else{
       layerMsg("起始日期应小于到期日期");
    }
}