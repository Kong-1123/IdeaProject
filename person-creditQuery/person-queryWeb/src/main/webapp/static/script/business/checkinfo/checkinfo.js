/**
 * 	bindCustomBtnEvent 绑定自定义按钮事件  用于绑定领取审批任务按钮
 * @param obj
 */
function bindCustomBtnEvent(returnObj, urlObj) {
	//list页面的领取任务按钮
	$(".btnWrap .receiveTaskBtn").bind("click", function() {
		var table = returnObj.table;
	    var ids = [],
        checkStatus = table.checkStatus('grid');
		var data = checkStatus.data;
		for ( var i = 0; i <data.length; i++){
		   var id = data[i].id;
		   ids.push(id);
		}
		if (data.length != 0) {
			var projectName = getProjectName(location.href);
			var taskUrl = urlObj.receiveTask
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
		} else {
			layerMsg("请选中一条数据");
		}
	});
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
