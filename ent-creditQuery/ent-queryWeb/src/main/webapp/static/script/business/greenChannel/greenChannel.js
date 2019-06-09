function gridFormBtnEvent(form,dataTable,urlObj) {
 	form.on('switch(status)', function(obj){
 		id = this.value;
 		if(obj.elem.checked){
 			$(this).attr('checked',false);
 			$(this).attr('disabled','disabled');
 			form.render();
 			layerMsg("无法手动解锁用户，请联系管理员。");
 			
		}else{
			url = urlObj.stopGreen;
			stopBtn(url, id);
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

	
	
