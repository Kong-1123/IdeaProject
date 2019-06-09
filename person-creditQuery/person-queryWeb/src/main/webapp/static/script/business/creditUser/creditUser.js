
/**
 * [bindCustomBtnEvent 绑定征信用户list页中“修改密码”按钮点击事件]
 * 
 */
      function bindCustomBtnEvent(obj){
    	  $("#testBtn").bind("click", function() {
    		  testBtn(obj);
    	  });
      }
      function submitFun(n, e) {
    		ajaxFunc(n, function(e) {
    			if (n.indexOf("systemOrg") > -1) {
    				var t = getProjectName(location.href);
    				t = t.replace(/\//g, "");
    				var i = window.sessionStorage;
    				i.removeItem(t + "_orgTreeData")
    			}
    			submitMsg(e)
    		}, function(e) {
    			layerMsg("请求失败！");
    			window.hasSubmit = false
    		}, e)
    	}
      function testBtn(obj){
    	  	var testUrl = obj.test;
    	  	var formData = $('form').serializeJSON();
    	  	if(window.jsonData){
		  	  	 formData = updateSubmitData(jsonData,formData);
		  	}
    	    ajaxFunc(testUrl,function(result) {
    	    	        if(result.code == '00000000'){
    	    	        	layerAlert('认证成功',
    	    	        	        function() {
    	    	        				$('#submitPage').removeAttr('disabled');
    	    	        	        });
    	    	        }else{
    	    	        	layerMsg(result.msg);
    	    	        }
    	    	    },
    	    	    function(result) {
    	    	    	layerMsg("认证失败");
    	    	        //layerMsg("error:" + JSON.stringify(result));
    	    	    	
    	    	    },
    	    	    formData);
      }
      
