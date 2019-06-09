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
}

// 弹出解除预警原因页面



function relieveBtn(urlObj,data) {
	
	var id = data.id , userStatus = data.userStatus , url = urlObj.relieveReason;
	layerOpen(600, 300, "", "解除预警原因", url + "?id=" + id + "&userStatus=" + userStatus,2,function() {});
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
		reloadParentPage();
	});
}