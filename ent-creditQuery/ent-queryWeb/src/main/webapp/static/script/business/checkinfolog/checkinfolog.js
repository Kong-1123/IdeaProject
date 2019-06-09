/**
 * bindCustomBtnEvent 绑定自定义按钮事件 用于绑定领取审批任务按钮
 * 
 * @param obj
 */
function bindCustomBtnEvent(obj) {
	// list页面 重新发起审批请求按钮
	$(".btnWrap .reSubCheckTaskBtn").bind("click", function() {
		var ids = transmitId();
		if (transmitId().length != 0) {
			var taskUrl = obj.urlObj.reSubCheckTask;
			ajaxFunc(taskUrl, function(data) {
				if ('00000' == data.code) {
					layerAlert("审批请求提交成功",function(){
						reloadPage();
					});
				} else {
					layerAlert(data.msg,function(){
						reloadPage();
					});
				}
			}, function(data) {
				layerMsg("审批请求提交失败！" + data.msg);
			}, {id:ids[0]}, false);
		} else {
			layerMsg("请选中一条数据进行操作");
		}
	});
	
	//查询报告按钮
    $(".btnWrap .queryBtn").bind("click", function() {
        queryReport(obj);
        
    });
	
}

//审批通过后，查询员本人可以进行报告查询操作
function queryReport(obj){
    var ids = transmitId();
    if(ids.length == 1){
        //检查是否可以查询
        var checkCanQuery = obj.urlObj.checkCanQuery;
        ajaxFunc(checkCanQuery, function(data) {
            if ('00000' == data.code) {
                //TODO  成功，进行查询
                alert("TODO 进行页面跳转")
            } else {
                layerAlert(data.msg,function(){
                    reloadPage();
                });
            }
        }, function(data) {
            layerMsg("审批请求提交失败！");
        }, {id:ids[0]}, false);
        
    }else{
        layerMsg("请选中一条数据进行操作"); 
    }
}



function bindArchiveIdBtnEvent(obj) {
	// 详情页面的显示档案按钮
	var archiveBtn = $(".modal-footer #showArchive");
	if (obj.data.archiveId == "" || obj.data.archiveId == undefined) {
		archiveBtn.hide();
		return;
	} else {
		archiveBtn.bind("click", function(){
    		alertArchive(obj);
    	} );
	}
}

// 弹出档案页面
function alertArchive(obj) {
	var urlList = obj.listObj;
	var titleName = "授权档案查看", showArchiveUrl = urlList.tabs[0].downLoad, getArchive = urlList.tabs[0].getArchiveUrl;
	var custom = {
		customName : "customUrl",
		url : showArchiveUrl,
		getValUrl :getArchive, 
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
