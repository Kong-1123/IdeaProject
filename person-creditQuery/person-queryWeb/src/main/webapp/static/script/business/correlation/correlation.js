	/**
      * [bindCustomBtnEvent 绑定list页中“菜单权限”和“按钮权限”按钮点击事件]
      * @param  {[Object]} obj [list页面配置的tabBox组件参数对象]
      */
    	  function bindCustomBtnEvent(obj){
              var tabs = obj.listObj.tabs[0];
              $(".buttonWrap .stopFlag").bind("click",function(){
                var t = $(this);
                var url,id = t.parents("tr").find("label").attr("for");
                if(t.hasClass("stopFlag0")){
                  // 停用
                	 layerConfirm("确定停用此关联吗？",function(){
                         url = tabs.stop;
                         $.post(url+id,function(data){
                             data = strToJson(data);
                             var msg;
                             if (data.code == "00000000") {
                               msg ="操作成功！";
                             }else if (data.code == "00000001"){
                               msg = "操作失败！";
                             }
                             layerAlert(msg,reloadPage);
                         })
                       })
                }else{
                  // 启用
                  layerConfirm("确定启用此关联吗？",function(){
                    url = tabs.start;
                    $.post(url+id,function(data){
                        data = strToJson(data);
                        var msg;
                        if (data.code == "00000000") {
                          msg ="操作成功！";
                        }else if (data.code == "00000001"){
                          msg = "操作失败！";
                        }
                        layerAlert(msg,reloadPage);
                    })
                  })
                }
              });
    	  }