/**
 * 	bindCustomBtnEvent 绑定自定义按钮事件  用于绑定领取审批任务按钮
 * @param obj
 */
function bindCustomBtnEvent(returnObj, urlObj) {
	//list页面的领取任务按钮
	$(".btnWrap .inquireReportBtn").bind("click", function() {
		var table = returnObj.table;
	    var ids = [],
        checkStatus = table.checkStatus('grid');
		var data = checkStatus.data;
		for ( var i = 0; i <data.length; i++){
		   var id = data[i].id;
		   ids.push(id);
		}
		if (ids.length == 1) {
			var listData = data[0];
			var status = data[0].status;
			if(status != '3'){
				layerMsg("只有审批通过的才能查询信用报告！！");
				return;
			}
			var projectName = getProjectName(location.href);
			var taskUrl = urlObj.inquireUrl;
			var flag = true; 
			if(flag){
			    flag = false;
			    var pre = "?pre=0";
			    ajaxFuncForJson(taskUrl+pre, function(data) {
	                var result = data;
	                
	                if ('10000' == result.resultCode) {
	                    flag = true;
	                    applyReport(listData,urlObj);
	                }else if(result.resultCode == '10013'){
	                    layer.confirm(result.resultMsg, {
	                          btn: ['是','否'], //按钮
	                          cancel:function(index){
	                              layer.close(layer.index); //如果设定了yes回调，需进行手工关闭
	                          }
	                      },function(index){
	                    	  
	                          layer.close(layer.index);
	                          applyReport(listData,urlObj);
	                      },function(index){
	                          applyReport(listData,urlObj);
	                      });
	                }else if(result.resultCode == '00000'){
	                	 alert(result.resultMsg);
	   	        	  	 var platFormUlr = sessionStorage.projectName;
	   	        	     window.location.href = platFormUlr + "/logout";
	                }else {
	                    flag = true;
	                   /**
	                    layerAlert(data.resultMsg, function() {
	                        reloadPage();
	                    });
	                    */
	                    if(result.resultCode =="01"){
	                    	applyReport(listData,urlObj);
	    					return;
	    				}else{
	    					layer.confirm("您已违法异常阻断规则，是否继续查询?", {
	  						  btn: ['是','否'], //按钮
	  						  cancel:function(index){
	  							  layer.close(layer.index); //如果设定了yes回调，需进行手工关闭
	  						  }
	  					  },function(index){
	  						  layer.close(layer.index);
	  						  if(result.resultCode =="00000"/*弹出提示，将用户踢出登陆*/){
	  					        	  alert(result.resultMsg);
	  					        	  var platFormUlr = sessionStorage.projectName;
	  					        	  window.location.href = platFormUlr + "/logout";
	  				            } else/*弹出提示，关闭页面*/{
	  				            	var pre = "?pre=1";
	  			            		ajaxFunc(urlObj.inquireUrl+pre,function(data){
	  				            		layerAlert(result.resultMsg,function(){
	  				            			result=data;
	  				        				var pName = getProjectName(location.href);
	  				        				// 弹出提示，将用户踢出登陆
	  					        			if(result.resultCode =="00000"){
	  				        			    	  alert(result.resultMsg);
	  				        			    	  var platFormUlr = sessionStorage.projectName;
	  				        			    	  window.location.href = platFormUlr + "/logout";
	  				        				}else{
	  				        					$(".layui-tab .layui-this .layui-tab-close",parent.document).click();
	  				        				}
	  					        		});
	  			            		});
	  					 		}
	  					  },function(index){
	  						 
	  						  $(".layui-tab .layui-this .layui-tab-close",parent.document).click();
	  					  });
	  				}
	                    
	                    
	                    
	                }
	            }, function() {
	                flag = true;
	                layerMsg("审批任务领取失败！");
	            }, JSON.stringify(ids), false);    
			}
		} else {
			layerMsg("请选中一条记录进行操作");
		}
	});
	//list页面的领取任务按钮
	$(".btnWrap .applyAgainBtn").bind("click", function() {
		var table = returnObj.table;
	    var ids = [],
        checkStatus = table.checkStatus('grid');
		var data = checkStatus.data;
		for ( var i = 0; i <data.length; i++){
		   var id = data[i].id;
		   ids.push(id);
		};
		if (ids.length == 1) {
			var status = data[0].status;
			if(status != '4'){
				layerMsg("只有审批拒绝的才能重新提交审批！！");
				return;
			}
			var projectName = getProjectName(location.href);
			var taskUrl = urlObj.applyTaskUrl;
			ajaxFuncForJson(taskUrl, function(data) {
				if ('00000000' == data.code) {
					layerAlert("已成功提交审批，请等待审批员审批。", function() {
						reloadPage();
					});
				} else {
					layerAlert(data.msg, function() {
						reloadPage();
					});
				}
			}, function() {
				layerMsg("审批任务领取失败！");
			}, JSON.stringify(ids), false);
		} else {
			layerMsg("请选中一条记录进行操作");
		}
	});
}


/**
 * 判断本地是否存在信用报告
 * @param urlObj
 * @param returnObj
 */
function applyReport(listData,urlObj){
	ajaxFunc(urlObj.applyReportUrl,function(data){
		//debugger
		var result = data;
		if(result.resultCode == "10401"){
			confirmMsg(result,urlObj.localReportUrl,urlObj.showReportUrl,listData);
		} else{
			inquireReport(urlObj.showReportUrl,listData);
		}
	},function(data) {layerMsg("请求出错！");
    },listData);
}
/**
 * 确认信息。
 * 
 */
function confirmMsg(result,localUrl,remoteUrl,listData){
	  layer.confirm(result.resultMsg, {
		  btn: ['是','否'], //按钮
		  cancel:function(index){
			  layer.close(layer.index); //如果设定了yes回调，需进行手工关闭
		  }
	  },function(index){
		  if(!$(".layui-layer-btn0").hasClass("layui-btn-disabled")){
			  $(".layui-layer-btn0").addClass("layui-btn-disabled");
			  $(".layui-layer-btn1").addClass("layui-btn-disabled");
			  layer.close(layer.index);
			  inquireReport(localUrl,listData);
		  }
	  },function(index){
		  if(!$(".layui-layer-btn1").hasClass("layui-btn-disabled")){
			  $(".layui-layer-btn1").addClass("layui-btn-disabled");
			  $(".layui-layer-btn0").addClass("layui-btn-disabled");
			  layer.close(layer.index);
			  inquireReport(remoteUrl,listData);
		  }
	  });
}

/**
 * 查询信用报告
 */
function inquireReport(url,listData){
	ajaxFunc(url,function(data){
		var result = data;
		if(result.resultCode == "00"){
			var projectName = getProjectName(location.href);
			var showReportUrl = projectName + result.url;
			window.open(showReportUrl); 
			reloadPage();
		}else if(result.resultCode == '00000'){
	       	 alert(result.resultMsg);
	   	  	 var platFormUlr = sessionStorage.projectName;
	   	     window.location.href = platFormUlr + "/logout";
	    }  else {
			layerMsg(result.resultMsg);
		}
	},function(data) {
          layerMsg("请求出错！");
    },listData);
}


function bindCustomBtnEvent4Detail(obj) {
	//详情页面的领取任务按钮
	$(".modal-footer #receiveTaskBtn").bind("click", function() {
		var id = obj.data.id;
		var ids = [ id ];
		var projectName = getProjectName(location.href);
		var taskUrl = obj.urlObj.receiveTask
		console.log(ids);
		console.log(taskUrl);
		ajaxFuncForJson(taskUrl, function(data) {
			if ('00000000' == data.code) {
				layerAlert("审批任务领取成功，请前往个人审批处理菜单查看处理。", function() {
					reloadPage();
				});
			} else {
				layerAlert(data.msg, function() {
					reloadPage();
				});
			}
		}, function() {
			layerMsg("审批任务领取失败！");
		}, JSON.stringify(ids), false);

	});
	//详情页面的显示档案按钮
	var archiveBtn = $(".modal-footer #showArchiveBtn");
	if (obj.data.archiveId == "" || obj.data.archiveId == undefined) {
		archiveBtn.hide();
		return;
	} else {
		archiveBtn.bind("click", function() {
			alertArchive(obj);
		});
	}
}
//弹出档案页面
function alertArchive(obj) {
	var urlList = obj.listObj;
	var titleName = "授权档案查看", showArchiveUrl = urlList.tabs[0].downLoad, getArchive = urlList.tabs[0].getArchiveUrl;
	var custom = {
		customName : "customUrl",
		url : showArchiveUrl,
		getValUrl : getArchive,
		id : obj.data.archiveId
	}

	var opt = {
		ele : "#form-wrap",
		name : titleName,
		state : 5,
		tabs : false,
		urlList : urlList,
		data : obj.data.id,
		pageName : obj.pageName,
		custom : custom,
		size : {
			w : 700,
			h : 350
		}
	};
	createBox(opt);
}