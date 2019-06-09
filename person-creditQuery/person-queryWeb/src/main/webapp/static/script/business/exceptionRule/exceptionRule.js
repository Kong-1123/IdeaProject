
	/* 客户三项标识按钮 */
    $(".userSymbolBtn").bind("click", function() {
    	userSymbolBtn(urlObj);
	});
	function userSymbolBtn(obj) {
	    parent.layerOpen(900, 500,obj.icon,obj.name + "-客户三项标识页", obj.getSymbolPage, 2,
	    		   function() {},
	    true);
	}
//启停状态
	function gridFormBtnEvent(form,dataTable,urlObj){
	 	form.on('switch(stopFlag)', function(obj){
	 		var url = urlObj.startOrStop;
	 		var stopFlag;
			if(obj.elem.checked){
				stopFlag = 0;//启用
				startOrStop(obj, stopFlag,url);
			}else{
				stopFlag = 1;//停用
				startOrStop(obj, stopFlag,url);
			}
		});
	}

	function startOrStop(obj,stopFlag,url) {
		layer.confirm("确定进行此操作？", {
			btn : [ '确认', '取消' ], // 按钮
			yes : function(index) {
				var data = getAjaxData(url,{'id':obj.value,'stopFlag':stopFlag});
				if (data.code == "00000000") {
					layerAlert(data.msg,reloadPage);
				} else if (data.code == "00000001") {
					layerAlert(data.msg,reloadPage);
				}
			},
			btn2 : function(index) {
				reloadPage();
			},
			cancel : function(index) {
				reloadPage();
			}
		});

	}
	
	/*function startOrStop(obj, stopFlag,url){
		layerConfirm("确定进行此操作？", function() {
			var data = getAjaxData(url,{'id':obj.value,'stopFlag':stopFlag});
			layerMsg(data.msg);
	  	},function(){
	  		var switch_ = obj.othis;
	  		var checkedClass = 'layui-form-onswitch';
	  		if(switch_.hasClass(checkedClass)){
	  			switch_.removeClass(checkedClass);
	  			switch_.find('>em').text('停用');
	  			obj.elem.checked = false;
	  			reloadPage();
	  		}else{
	  			switch_.addClass(checkedClass);
	  			switch_.find('>em').text('启用');
	  			obj.elem.checked = true;
	  			reloadPage();
	  		}
	  	});
	}*/