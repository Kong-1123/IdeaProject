/**
 * bindCustomBtnEvent 绑定自定义按钮事件 用于绑定领取审批任务按钮
 * 
 * @param obj
 */
function bindCustomBtnEvent(obj) {
    // list 页面 审批通过按钮
    $(".modal-footer #checkPassBtn").bind("click", function() {
    	var data = obj.jsonData;
    	var ids = [];
    	ids.push(data.id);
        if (ids.length == 1) {
            var projectName = getProjectName(location.href);
            var taskUrl = obj.urlObj.checkinfoPass;
            confirmMsg(taskUrl,ids);
        } else {
            layerMsg("请选中一条数据进行操作");
        }
    });

    // list 审批拒绝按钮
    $(".modal-footer #checkRefusalBtn").bind("click", function() {
    	var data = obj.jsonData;
    	var ids = [];
    	ids.push(data.id);
        if (ids.length == 1) {
            var taskUrl = obj.urlObj.checkRefusal;
                layerPrompt("请输入拒绝原因", function(value) {

                    if(value.length < 450){
                        var val = {
                            "id" : ids[0],
                            "refuseReasons" : value
                        };
                        ajaxFunc(taskUrl, function(data) {
                            if ('00000000' == data.code) {
                                layerAlert("审批拒绝成功", function() {
                                    reloadParentPage();
                                });
                            } else {
                                layerMsg(data.msg);
                            }
                        }, function(data) {
                            layerMsg("操作失败！" + data.msg);
                        }, val, false);
                    }else{
                        layerMsg("输入内容长度小于450个字");
                    }
                    
            });
        } else {
            layerMsg("请选中一条数据进行操作");
        }
    });

    // list 任务回退按钮
    $(".modal-footer #backTaskBtn").bind("click", function() {
    	var data = obj.jsonData;
    	var ids = [];
    	ids.push(data.id);
        if (ids.length == 1) {
            var taskUrl = obj.urlObj.backTask;
            ajaxFunc(taskUrl, function(data) {
                if ('00000000' == data.code) {
                    layerAlert("任务退回成功", function() {
                    	reloadParentPage();
                    });
                } else {
                    layerAlert(data.msg, function() {
                    	reloadParentPage();
                    });
                }
            }, function(data) {
                layerMsg("任务退回失败！" + data.msg);
            }, data, false);
        } else {
            layerMsg("请选中一条数据进行操作");
        }
    });

}

function bindArchiveIdBtnEvent(obj) {
    // 详情页面的显示档案按钮
    var archiveBtn = $(".modal-footer #showArchive");
    if (obj.data.archiveId == "" || obj.data.archiveId == undefined) {
        archiveBtn.hide();
    } else {
        archiveBtn.bind("click", function() {
            selectArchive(obj);
        });
    }
}
// 弹出档案页面
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

function selectArchive(obj) {
    var selectArchiveUrl = obj.listObj.tabs[0].selectArchiveUrl;
    var id = obj.data.id;
    var data = {
        id : id
    };
    ajaxFunc(selectArchiveUrl, function(data) {
        if ('00000000' == data.code) {
            alertArchive(obj);
        } else {
            layerMsg(data.msg);
        }
    }, function(data) {
        layerAlert(data.msg, function() {
            reloadPage();
        });
    }, data, false);
}


function confirmMsg(taskUrl,ids){
	  layer.confirm("是否确定审批成功!", {
		  btn: ['确定','取消'], //按钮
		  cancel:function(index){
			  layer.close(layer.index); //如果设定了yes回调，需进行手工关闭
		  }
	  },function(index){
		  layer.close(layer.index);
		  checkPass(taskUrl,ids);
	  },function(index){
		  layer.close(layer.index);
	  });
}

function checkPass(taskUrl,ids){
	ajaxFuncForJson(taskUrl, function(data) {
        if ('00000000' == data.code) {
            layerAlert("审批成功", function() {
                reloadParentPage();
            });
        } else {
            layerAlert(data.msg, function() {
            	reloadPage();
            });
        }
    }, function(data) {
        layerMsg("审批失败！" + data.msg);
    }, JSON.stringify(ids), false);
}