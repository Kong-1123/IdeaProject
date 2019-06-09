/**
 * [bindBtn 绑定list页中“信用报告查询”按钮点击事件]
 * 
 * @param {[Object]}
 *            obj [list页面配置的tabBox组件参数对象]
 */

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function bindBtn(obj) {
	$(".btnWrap .uploadBtn").bind("click", function() {
		uploadBtn(obj);
	});
	$(".btnWrap .exportBtn").bind("click", function() {
		exportBtn(obj);
	});
	$(".btnWrap .downloadfileBtn").bind("click", function() {
		downloadfileBtn(obj);
	});
	$(".btnWrap .reportsearchBtn").bind("click", function() {
		if ($("#grid-data tbody .active").length !=1 ) {
			layerMsg("只能选择一条数据");
		}else{
			var index = $("#grid-data .active").parents("tr").index();
			var parentObj = gridData.list[index];
			if(parentObj.status == 2){
			    layerMsg("该档案无档案资料，禁止查询");
			    return;
			}
			var thisDate = new Date(new Date().Format("yyyy-MM-dd"));
			var date = parentObj.extendDate;
			if(date == "" || date == undefined ){
			    date = parentObj.expireDate;
			}
			var expireDate = new Date(date);
			if(thisDate > expireDate){
			   layerMsg("该档案已失效，不可进行查询");
			   return;
			}
			var creditUrl = encodeURI( obj.listObj.tabs[0].flowUrl + '?signCode=' + parentObj.signCode + '&archiveId=' + parentObj.id);
	        window.location.href =creditUrl;
		}
	});
}

function exportBtn(obj){
    var urlObj = obj.urlObj, idArr = transmitId();
    var taskUrl =  urlObj.exportUrl;
    window.location.href = taskUrl+"?ids="+idArr;
}

function downloadfileBtn(obj){
	var urlList = obj.listObj;
	var titleName = "查看上传档案资料", 
		id = transmitId(), 
		downLoadUrl = urlList.tabs[0].downLoad;
	if (id.length == 0) {
		layerMsg("至少选择一条数据");
	}else if(id.length == 1){
		var custom = {
			customName : "customUrl",
			url : downLoadUrl,
			getValUrl : urlList.tabs[0].getValUrl,
			id : id + ""
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

function uploadBtn(obj) {
	var urlList = obj.listObj;
	var titleName = "上传档案资料", 
		id = transmitId(), 
		upLoadUrl = urlList.tabs[0].uploadRepairs,
		index = $("#grid-data .active").parents("tr").index();
	if (id.length == 0) {
		layerMsg("至少选择一条数据");
	}else if(id.length == 1){
	    var queryNum = gridData.list[index].queryNum;
	    if(queryNum > 0){
	        layerMsg("该档案已关联信用报告，禁止修改档案文件"); 
	        return;
	    }
		var custom = {
			customName : "customUrl",
			url : upLoadUrl,
			getValUrl : urlList.tabs[0].getValUrl,
			id : id + ""
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
function validateExpireDate(field, rules, i, options) {
	var msg ="授权到期日应大于等于当前时间";
	var expireDate = $("#expireDate").val();
	var queryTime = new Date().Format("yyyy-MM-dd");
	if(new Date(expireDate.toString())< new Date(queryTime.toString()))
		return msg;
}

function validateStartDate(field, rules, i, options) {
	var msg ="授权起始日应小于等于当前时间";
	var startDate = $("#startDate").val();
	var queryTime = new Date().Format("yyyy-MM-dd");
	if(new Date(startDate.toString())>new Date(queryTime.toString()))
		return msg;
}

// 补录校验 查询起始日期小于等于查询日期
function checkStartDate(field, rules, i, options) {
	 var arr = returnObject(field, options, "checkStartDate");
	 var startDateStr = arr[1].value;
	 if ($.trim(startDateStr) != "" && $.trim(reviseInfo.queryTime) != "") {
			var startDate = new Date(startDateStr);
			var queryTime = new Date(/\d{4}-\d{1,2}-\d{1,2}/g.exec(reviseInfo.queryTime));
			if (startDate>queryTime) {
				return arr[2].alertText;
			}
		}
	 return true;
}

//补录校验 查询到期日期大于等于查询日期
function checkExpireDate(field, rules, i, options) {
	 var arr = returnObject(field, options, "checkExpireDate");
	 var expireDateStr = arr[1].value;
	 if ($.trim(expireDateStr) != "" && $.trim(reviseInfo.queryTime) != "") {
			var expireDate = new Date(expireDateStr);
			var queryTime = new Date(/\d{4}-\d{1,2}-\d{1,2}/g.exec(reviseInfo.queryTime));
			if (expireDate<queryTime) {
				return arr[2].alertText;
			}
		}
	 return true;
}

// 数组移除某个值
Array.prototype.remove = function(val) { 
	var index = this.indexOf(val); 
	if (index > -1) { 
		this.splice(index, 1); 
	} 
};
//预览方法
function view(name,result,id,file){
	 //获取预览流路径
	 if(id){
	 	var result = urlObj.getPictureFile + "?id=" + id;
	 }
	 // if(id){
	 	var flag = false;
	 	var version = BrowserType(); 
	 	if(version.indexOf("IE")==0){
	 		flag = true;
	 		
	 	}
	 	if(flag && name.indexOf("pdf") > 0 && !isAcrobatPluginInstall()){
	 		layerAlert("对不起,您还没有安装PDF阅读器软件,为了方便预览PDF文档,请安装！"); 
	 		return;
	 	}
	 /*	var result = urlObj.getPictureFile + "?id=" + id;

	 	if(version == "IE9" || version == "IE10"){
	 		flag = true;
	 	}
	 	if(flag && name.indexOf("pdf") > 0){
	 		var pName = getProjectName(location.href);
	 		var url = urlObj.getPreviewPathInIE + "?id=" + id;
	 		var data = getAjaxData(url,null);
	 		result = pName + data.msg;
	 		result = result.replace(/\\/g,"/");
 		 opt = {
	     		  type: 2,
	     		  title: "文件预览",
	     		  maxmin: true,
	     		  area: ['800px','500px'],
	     		  skin: 'layui-layer-preview', //没有背景色
	     		  shadeClose: true,
	     		  // content: $(".pdf",parent.document)
	     		  content: pName+"pdfjs/web/viewer.html?file=" + result
	     		} 
   	 		parent.layer.open(opt); 
   	 		return;
	 	}*/
	 // }
     var opt;
	 if(name.indexOf("pdf")<0){
		 var img ="<img style='max-height:100%;' id='imgLook' src='"+ result +"'/>";
		 opt = {
	     		  type: 1,
	     		  title: "图片预览",
	     		  maxmin: true,
	     		  area: ['800px','500px'],
	     		  skin: 'layui-layer-preview', //没有背景色
	     		  shadeClose: true,
	     		  content: img
	     		}
     		parent.layer.open(opt);   
	 }else{
	 	if(!id){
	 		layer.load(2);
	 		var pName = getProjectName(location.href);
	 		// var fileObj = fileInput[0].files[0]; // js 获取文件对象
            var formFile = new FormData();
            formFile.append("action", "UploadVMKImagePath");
            console.log(file);  
            formFile.append("file", file);
            $.ajax({
                url:  requestAddRandom(urlObj.previewPdf),
                type: 'post',
                data: formFile,
               	async: false,
                processData: false, //禁止序列化data，默认为true
                contentType: false, //避免JQuery对contentType做操作
                success: function(res) {
                	layer.closeAll('loading');
                    res = res.replace(/\\/g,"/");
                    result = pName+"static/script/pdfjs/web/viewer.html?file=" + pName + res;
                     opt = {
		     		  type: 2,
		     		  title: "文件预览",
		     		  maxmin: true,
		     		  area: ['800px','500px'],
		     		  skin: 'layui-layer-preview', //没有背景色
		     		  shadeClose: true,
		     		  // content: result
		     		  content:  result
		     		}
	     			parent.layer.open(opt);   
                },
                error: function(err) {
                    layer.closeAll('loading');
                    layerMsg('预览失败!')
                }
            })
        }else{
        	opt = {
     		  type: 2,
     		  title: "文件预览",
     		  maxmin: true,
     		  area: ['800px','500px'],
     		  skin: 'layui-layer-preview', //没有背景色
     		  shadeClose: true,
     		  content: result
     		  }
	    	parent.layer.open(opt);   
        }
	 }

}
//删除方法
function cut(btn){
	var fileId = $(btn).parents("tr").children('.selfId').attr("data-type");
		layerConfirm('确认此项操作？',function() {
    		if(fileId){
    			ajaxFunc(urlObj.deleteArchiveFile+fileId, function(result){
					if ('00000000' == result.code) {
						addItemFlag = true;
						$(btn).parents("tr").remove();
					} else {
						layerMsg(result.msg);
					}
				})
    		}else{
    			addItemFlag = true;
				$(btn).parents("tr").remove();
    		}
        });
}

// ie9 电子文件2 新增档案文件
function addItem(n){
	var trStr = '<tr>'+
			'<td class="typeName" data-type="200">'+dicData[200]+'</td>'+
			'<td class="fileTypeName" width="300px"></td>'+
			'<td>'+
			'<button type="button" class="layui-btn layui-btn-xs layui-btn-danger demo-delete" onclick="cut(this)">删除</button>'+
			'<input id="file'+ n +'" class="fileUpload" type="file" name="file">'+
			'</td>'+
			'</tr>';
	$('#uploadList').append(trStr);
	$(".fileUpload").show();
}

// IE中判断文件名是否重复,是否包含不支持后缀文件类型
function checkFileRepeatOrNonSupport(ele){
	if(BrowserType() == "IE9"){
		var len = ele.length
		var fileUpload  = ele.filter(function(){
			return Boolean($(this).val()) == true;
		});
		var nameArr = [];
		var imgType = ['pdf','png','jpg','gif','bmp','tif'];
		for(var i = 0; i<len;i++){
			if(fileUpload[i]){
				var val = fileUpload[i].value;
				if(nameArr.indexOf(val) == -1){
					nameArr.push(val);
				}
				var valArr = val.split(".");
				var valType = valArr[valArr.length-1];
				if(imgType.indexOf(valType)<0){
					$(".modal-footer button").attr("disabled", false);
					window.hasSubmit = false;
					layerMsg("不支持."+valType+"后缀文件，无法上传！");
					return false;
				}
			}
		}
		if(nameArr.length<len){
			$(".modal-footer button").attr("disabled", false);
			window.hasSubmit = false;
			layerMsg("上传文件包含相同文件名，无法上传！");
			return false;
		}
	}
	return true;
}
	