/**
 * [bindCustomBtnEvent 绑定list页中“授权资料”和“复合”按钮点击事件]
 * 
 */
      function bindCustomBtnEvent(obj){
          var tabs = obj.listObj.tabs[0];
          //授权
          $(".buttonWrap .delegationPolicy").bind("click",function(){
            var t = $(this);
            var url,id = t.parents("tr").find("label").attr("for");
            //url = tabs.delegationPolicy;
            if(t.hasClass("stopFlag0")){
              // 停用
              layerConfirm("确定进行此操作？",function(){
                  url = tabs.stopDelegationPolicy;
                  console.log(url);
                  $.post(url+id,function(data){
                      data = strToJson(data);
                      var msg;
                      if (data.code == "00000") {
                        msg ="操作成功！";
                      }else if (data.code == "00001"){
                        msg = "操作失败！";
                      }
                      layerAlert(msg,reloadPage);
                      
                  })
                })
              
            }else{
              // 启用
              layerConfirm("确定进行此操作？",function(){
                url = tabs.startDelegationPolicy;
                console.log(tabs);
                console.log(url);
                $.post(url+id,function(data){
                    data = strToJson(data);
                    var msg;
                    if (data.code == "00000") {
                      msg ="操作成功！";
                    }else if (data.code == "00001"){
                      msg = "操作失败！";
                    }
                    layerAlert(msg,reloadPage);
                })
              })
            }
          });
          
          
          
          //复合
          $(".buttonWrap .recheckPolicy").bind("click",function(){
              var t = $(this);
              var url,id = t.parents("tr").find("label").attr("for");
              //url = tabs.recheckPolicy;
              if(t.hasClass("stopFlag0")){
            	// 停用
                layerConfirm("确定进行此操作？",function(){
                    url = tabs.stopRecheckPolicy;
                    console.log(url);
                    $.post(url+id,function(data){
                        data = strToJson(data);
                        var msg;
                        if (data.code == "00000") {
                          msg ="操作成功！";
                         
                        }else if (data.code == "00001"){
                          msg = "操作失败！";
                        }
                        layerAlert(msg,reloadPage);
                    })
                  })
                
              }else{
                // 启用
                layerConfirm("确定进行此操作？",function(){
                  url = tabs.startRecheckPolicy;
                  console.log(tabs);
                  console.log(url);
                  $.post(url+id,function(data){
                      data = strToJson(data);
                      var msg;
                      if (data.code == "00000") {
                        msg ="操作成功！";
                      }else if (data.code == "00001"){
                        msg = "操作失败！";
                      }
                      layerAlert(msg,reloadPage);
                  })
                })
              }
            });
          
          
      }
          
          

         
      /*
      function stopBtn(obj,id){
          //点击后跳入启动
    	  ajaxFunc(url,function(data){
    		if ('00000' == data.code) {
    			layerConfirm("审批请求提交成功");
				reloadPage();//刷新当前页面
			} else {
				layerConfirm(data.msg);
				reloadPage();
			}
		}, function(data) {
			layerConfirm("审批请求提交失败！" + data.msg);
		}, {id:ids[0]}, false);
    	  
        }*/