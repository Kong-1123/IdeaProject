
function gridFormBtnEvent(form,dataTable,urlObj) {
	form.on('switch(delegationPolicy)', function(obj){
		id = this.value;
		console.log(obj.elem);
		if(obj.elem.checked){
			url = urlObj.startDelegationPolicy;
			startBtn(url, id);
		}else{
			url = urlObj.stopDelegationPolicy;
			stopBtn(url, id);
		}
	});
	
	form.on('switch(recheckPolicy)', function(obj){
		console.log(obj.elem);
		id = this.value;
 		if(obj.elem.checked){
 			url = urlObj.startRecheckPolicy;
 			startBtn(url, id);
 		}else{
 			url = urlObj.stopRecheckPolicy;
 			stopBtn(url, id);
 			
 		}
  	});
	
	
 	
}


function startBtn(url, id) {
	layer.confirm("确定进行此操作？", {
		btn : [ '确认', '取消' ], // 按钮
		yes : function(index) {
			$.post(url + "?id=" + id, function(data) {
				data = strToJson(data);
				var msg;
				if (data.code == "00000000") {
					msg = "操作成功！";
				} else if (data.code == "00000001") {
					msg = "操作失败！";
				}
				layerAlert(msg, reloadPage);
			})
		},
		btn2 : function(index) {
			reloadPage();
		},
		cancel : function(index) {
			reloadPage();
		}
	});

}

// 停用用户
function stopBtn(url, id) {
	layer.confirm("确定进行此操作？", {
		btn : [ '确认', '取消' ], // 按钮
		yes : function(index) {
			$.post(url + "?id=" + id, function(data) {
				data = strToJson(data);
				var msg;
				if (data.code == "00000000") {
					msg = "操作成功！";
				} else if (data.code == "00000001") {
					msg = "操作失败！";
				}
				layerAlert(msg, reloadPage);
			})
		},
		btn2 : function(index) {
			reloadPage();
		},
		cancel : function(index) {
			reloadPage();
		}
	});
}
/* 停用审批
function lockBtn() {
	 	layerConfirm("确定进行此操作？", function() {
			
		var msg,data = getAjaxData(url,{id:id})
			
		if (data.code == "00000000") {
				msg = "操作成功！";
		} else if (data.code == "00000001") {
				msg = "操作失败！";
		}
		layerMsg(msg,reloadPage);
		})
	}
启用审批
function unlockBtn(url,checkbox) {
		layerConfirm("确定进行此操作？", function() {
			
			var msg,data = getAjaxData(url,{id:id})
				
			if (data.code == "00000000") {
					msg = "操作成功！";
			} else if (data.code == "00000001") {
					msg = "操作失败！";
			}
			layerMsg(msg,reloadPage);
			})
	}*/