/**
 * bindCustomBtnEvent 绑定自定义按钮事件 用于绑定领取审批任务按钮
 * 
 * @param obj
 */
function bindCustomBtnEvent(obj) {
	$(".btnWrap .creditBtn").bind("click", function() {
		creditBtn(obj);
	});
	$(".btnWrap .checkInfoBtn").bind("click", function() {
		checkInfoBtn(obj);
	});
	$(".btnWrap .downloadfileBtn").bind("click", function() {
		downloadfileBtn(obj);
	});
	$(".btnWrap .archiveBtn").bind("click", function() {
		archiveBtn(obj);
	});
	$(".btnWrap .exportBtn").bind("click", function() {
		exportBtn(obj);
	});
}

/**
 * <导出页面>
 */
function exportBtn(obj){
    var urlObj = obj.urlObj, idArr = transmitId();
    var taskUrl =  urlObj.exportUrl;
    window.location.href = taskUrl+"?ids="+idArr;
}

// 弹出显示审批记录页面
function checkInfoBtn(obj) {
	var urlList = obj.listObj;
	var titleName = "显示审批记录",
	ids = transmitId(),
    checkInfoById = urlList.tabs[0].checkInfoById, checkDetailUrl = urlList.tabs[0].checkDetailUrl;
	if (ids.length == 1 ) {
	    var index = $("#grid-data .active").parents("tr").index();
	    var checkId = gridData.list[index].checkId;
	    if(checkId == null){
	        layerMsg("该查询记录无相关审批信息");
	        return;
	    }
		var custom = {
			customName : "customUrl",
			url : checkDetailUrl,
			id : checkId,
			getValUrl : checkInfoById
		}
		var opt = {
			ele : ".contentWrap",
			name : titleName,
			state : 5,
			tabs : false,
			urlList : urlList,
			pageName : obj.pageName,
			custom : custom,
			size : {
				w : 700,
				h : 500
			}
		};
		createBox(opt);
		$("#iframeWrap").contents().find(".modal-footer #showArchive").hide();
		$("#iframeWrap").contents().find(".modal-footer #receiveTask").hide();
	} else {
		layerMsg("请选择1条数据进行操作");
	}
}

function creditBtn(obj){
		var id = transmitId();
  	    if (id.length != 1) {
			layerMsg("请选择1条数据进行操作");
		}else{
			var index = $("#grid-data .active").parents("tr").index();
			var parentObj = gridData.list[index];
			var projectName = getProjectName(location.href);
			var statusUrl = projectName+'/queryreq/getStatus?creditId=' + parentObj.creditId;
            ajaxFunc(statusUrl,function(data){
            	result = data;
            	if("00000" == result.code){
            		url = projectName+'/queryreq/revealReport?creditId=' + parentObj.creditId;
            		window.open(url);
            	} else {
            		layerMsg("此记录不存在信用报告，请选择其他记录查看信用报告");
            	}
            });
		
		}
  }


//弹出查看档案页面
function archiveBtn(obj) {
	var urlList = obj.listObj;
	var titleName = "档案信息",
	id = transmitId(),
	findByArchive = urlList.tabs[0].getArchiveUrl, 
	archiveDetailUrl = urlList.tabs[0].archiveDetailUrl;
	if (id.length == 1) {
	    var index = $("#grid-data .active").parents("tr").index();
	    var autharchiveId = gridData.list[index].autharchiveId;
	    if(autharchiveId == null){
	        layerMsg("该查询记录无相关档案信息");
            return;
	    }
		var custom = {
			customName : "customUrl",
			url : archiveDetailUrl,
			id : autharchiveId,
			getValUrl : findByArchive
		}
		var opt = {
			ele : ".contentWrap",
			name : titleName,
			state : 5,
			tabs : false,
			urlList : urlList,
			pageName : obj.pageName,
			custom : custom,
			size : {
				w : 990,
				h : 450
			}
		};
		createBox(opt);
	} else {
		layerMsg("最多只能选择一条数据");
	}
}

// 查看上传档案资料
function downloadfileBtn(obj) {
	var urlList = obj.listObj;
	var titleName = "查看上传档案资料",
	id = transmitId(),
	findByArchiveId = urlList.tabs[0].getArchiveUrl
	downLoadUrl = urlList.tabs[0].downLoad;
	if (id.length == 0) {
		layerMsg("至少选择一条数据");
	} else if (id.length == 1) {
	    
	    var index = $("#grid-data .active").parents("tr").index();
	    var autharchiveId = gridData.list[index].autharchiveId;
	    if(null == autharchiveId){
	        layerMsg("该查询记录无档案信息！");
	        return;
	    };
	    
		var custom = {
			customName : "customUrl",
			url : downLoadUrl,
			getValUrl : urlList.tabs[0].getArchiveUrl,
			id : autharchiveId
		}
		var opt = {
			ele : ".contentWrap",
			name : titleName,
			state : 5,
			tabs : false,
			urlList : urlList,
			pageName : obj.pageName,
			custom : custom,
			size : {
				w : 700,
				h : 400
			}
		};
		createBox(opt);
	} else {
		layerMsg("最多只能选择一条数据");
	}
}
