/**
 * bindCustomBtnEvent 绑定自定义按钮事件 用于绑定领取审批任务按钮
 * 
 * @param obj
 */
/*function bindCustomBtnEvent(obj) {
	var checkStatus = table.checkStatus('idTest')
    ,data = checkStatus.data;
	$(".btnWrap .relieveBtn").bind("click", function() {
		var index = $("#grid-data .active").parents("tr").index();
		var list = gridData.list[index];
		var checkStatus = list.checkStatus;
		if ("0" == checkStatus) {
			relieveBtn(obj);
		} else {
			handleUserStatus(obj, list);
		}
	});
	$(".btnWrap .exportBtn").bind("click", function() {
        exportBtn(obj);
    });
}
*/
/**
 * [bindCustomBtnEvent 绑定list页中“菜单权限”和“按钮权限”按钮点击事件]
 * 
 * @param {[Object]}
 *            obj [list页面配置的tabBox组件参数对象]
 */
function bindCustomBtnEvent(returnObj, urlObj) {
/*	$(".btnWrap .bingdingBtn").bind("click", function() {
		bingdingBtn(returnObj,opt);
	});*/

	$(".btnWrap .relieveBtn").bind("click", function() {
		var index = $("#grid-data .active").parents("tr").index();
		//var list = gridData.list[index];
		var checkStatus = returnObj.table.checkStatus('grid'),data = checkStatus.data[0];
		console.log(checkStatus.data);
		if (checkStatus.data.length !=1 ) {
			layerMsg("只能选择一条数据");
		}else{
			//debugger
			if ("0" == data.checkStatus) {
				relieveBtn(urlObj,data);
			} else {
				handleUserStatus(urlObj, data);
			}
		}
	});
	/*$(".btnWrap .exportBtn").bind("click", function() {
        exportBtn(obj);
    });*/
}

// 弹出解除预警原因页面



function relieveBtn(urlObj,data) {
	
	var id = data.id , userStatus = data.userStatus , url = urlObj.relieveReason;
	layerOpen(600, 300, "", "解除预警原因", url + "?id=" + id + "&userStatus=" + userStatus,2,function() {});
	
//	var titleName = "解除预警原因", id = data.id, downLoadUrl = urlObj.relieveReason;
//		var custom = {
//			customName : "customUrl",
//			url : downLoadUrl
//		}
//		var opt = {
//			ele : ".contentWrap",
//			name : titleName,
//			state : 5,
//			tabs : false,
//			urlList : urlList,
//			pageName : opt.pageName,
//			custom : custom,
//			size : {
//				w : 500,
//				h : 300
//			}
//		};
//		createBox(opt);
}

// 处理用户状态
function handleUserStatus(urlObj, data) {
	var locked = data.userStatus;
	var url, msg;
	if ("1" == locked) {
		url = urlObj.unLock + "?id=" + data.id;
		msg = "当前状态锁定，是否需要解锁？";
	} else {
		url = urlObj.addLock + "?id=" + data.id;
		
		msg = "当前状态未锁定，是否需要锁定？";
	}
	layer.confirm(msg, {
		btn : [ '是', '否' ], // 按钮
		cancel : function(index) {
			layer.close(layer.index); // 如果设定了yes回调，需进行手工关闭
		}
	}, function(index){
		ajaxFunc(url, function(result) {
			var data = result;
			if (result.code == "00000000") {
				layerAlert(result.msg, function() {
					reloadPage();
				});
			} else {
				layerMsg(result.msg);
			}
		});
	}, function(index) {
		reloadPage();
	});
}

/*function exportBtn(obj){
    var urlObj = obj.urlObj, idArr = transmitId();
    var taskUrl =  urlObj.exportUrl;
    window.location.href = taskUrl+"?ids="+idArr;
}
*/