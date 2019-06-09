function checkExpireDate(field, rules, i, options) {
	var arr = returnObject(field, options, "checkExpireDate");
	//console.log(arr);
	//获取档案到期时间
	var expireDate = arr[1].value;
	// 获取查询时间
	var queryTime = arr[0].queryTime.value;
	if(new Date(expireDate.toString())<new Date(queryTime.toString()))
		return arr[2].alertText;
}

function checkStartDate(field, rules, i, options) {
	var arr = returnObject(field, options, "checkStartDate");
	//console.log(arr);
	//获取档案起始时间
	var startDate = arr[1].value;
	// 获取查询时间
	var queryTime = arr[0].queryTime.value;
	if(new Date(startDate.toString())>new Date(queryTime.toString()))
		return arr[2].alertText;
}

/**
 * [bindCustomBtnEvent 绑定档案补录页中“补录”按钮点击事件]
 * 
 */
      function bindCustomBtnEvent(obj){
    	  $(".btnWrap .reviseBtn").bind("click", function() {
    		  revise(obj);
    		});
    	  $(".btnWrap .checkedBtn").bind("click", function() {
    		  checked(obj);
    	  });
    	  $(".btnWrap .revisecreateBtn").bind("click", function() {
    		  create(obj);
    	  });
      }
      
      function create(obj){
    	  var titleName = "补录档案",
    	  revisecreateUrl = obj.urlObj.revisecreate;
    	  var custom = {
  				customName : "customUrl",
  				url : revisecreateUrl
  			}
  			var opt = {
  				ele : ".contentWrap",
  				name : titleName,
  				state : 5,
  				tabs : false,
  				urlList : obj.listObj,
  				pageName : obj.pageName,
  				custom : custom,
  				size : {
  					w : 700,
  					h : 400
  				}
  			};
  			createBox(opt);
      }
      
      function checked(obj){
    	    var urlList = parent.listObj;
    		id = transmitId(), 
    		archiveUrl = urlList.tabs[0].checkedArchive;
    		if (id.length == 0) {
    			layerMsg("至少选择一条数据");
    		}else if(id.length == 1){
    			ajaxFunc(archiveUrl + "?id=" + obj.parentObj.id + "&archiveId=" + id + "&archiveRevise=" + obj.parentObj.archiveRevise, 
    					function(result){
    						if(result.code == "00000000"){
    							layerMsg(result.msg, reloadParentPage());
    						}else{
    							layerMsg(result.msg);
    						}
    					});
    		} else {
    			layerMsg("最多只能选择一条数据");
    		}
      }
      
      function revise(obj){
    	  var urlList = obj.listObj;
    		var titleName = "补录档案", 
    			id = transmitId(), 
    			archiveUrl = urlList.tabs[0].archive;
    		var resultUrl;
    		 $.ajaxSetup({  
                 async : false  
             });  
             $.get(archiveUrl + id, function(data, status) {  
               if (status == "success") {  
            	   resultUrl = getProjectName(location.href) + data;
               }else{
            	   
               }
               });
    		if (id.length == 0) {
    			layerMsg("至少选择一条数据");
    		}else if(id.length == 1){
    			var custom = {
    				customName : "customUrl",
    				url : resultUrl,
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
    				    w : 1000,
                        h : 600
    				}
    			};
    			createBox(opt);
    		} else {
    			layerMsg("最多只能选择一条数据");
    		}
      }