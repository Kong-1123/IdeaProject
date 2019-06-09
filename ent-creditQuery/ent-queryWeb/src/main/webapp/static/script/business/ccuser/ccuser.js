
/**
 * [bindCustomBtnEvent “测试提交”按钮点击事件]
 * 
 */

      function bindCustomBtnEvent(obj){
    	  $("#testBtn").bind("click", function() {
    	      testBtn(obj);
    	  });
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
  	    	        				//$('#submitPage').removeAttr('disabled');
  	    	        	        });
  	    	        }else {
  	    				layerMsg(result.msg);
  	    			}
  	    	    },
  	    	    function(result) {
  	    	        layerMsg("error:" + JSON.stringify(result));
  	    	    },
  	    	    formData);
    }
      
