/**
      * [bindCustomBtnEvent 绑定list页中“菜单权限”和“按钮权限”按钮点击事件]
      * @param  {[Object]} obj [list页面配置的tabBox组件参数对象]
      */
      function bindCustomBtnEvent(obj){
          var tabs = obj.listObj.tabs[0];
     /*     $(".buttonWrap .stopFlag").bind("click",function(){
            var t = $(this);
            var url,id = t.parents("tr").find("label").attr("for");
            if(t.hasClass("stopFlag0")){
              // 停用
              stopBtn(obj,id);
            }else{
              // 启用
              layerConfirm("确定进行此操作？",function(){
                url = tabs.startUser;
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

          $(".buttonWrap .lockFlag").bind("click",function(){
            var t = $(this);
            var id = t.parents("tr").find("label").attr("for");
            if(t.hasClass("lockFlag1")){
              layerConfirm("确定进行此操作？",function(){
                url = tabs.unlockUser;
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

          $(".buttonWrap .role").bind("click",function(){
            var t = $(this);
            roleBtn(obj)
          });
*/
          $(".buttonWrap .stopFlag").bind("click",function(){
            var t = $(this);
            var id = t.parents("tr").find("label").attr("for");
            layerConfirm("确定进行此操作？",function(){
                url = tabs.stopUrl;
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
          });
      }
/*
      function stopBtn(obj,id){
        var urlList = obj.listObj;
        var titleName = "用户停用原因",
            type = 3,
            stopReasonUrl = urlList.tabs[0].stopReason;
        var custom ={
            customName:"button", 
            stopReasonUrl:stopReasonUrl,
            type:type
        }
        var opt = {
              ele : ".contentWrap",
              name : titleName,
              state: 0, 
              tabs : false, 
              urlList: urlList,
              data : id,
              pageName:obj.pageName,
              custom:custom,
              size:{
                w:500,
                h:300
              }
        };
        createBox(opt);

      }

      function roleBtn(obj){
        var urlList = obj.listObj;
        var titleName = "用户角色",
            type = 2,
            grantRoleUrl = urlList.tabs[0].grantRole;
        var custom ={
            customName:"roleManage", 
            rolePageUrl:grantRoleUrl,
            type:type
        }
        var data = transmitId()+"";
        var opt = {
              ele : ".contentWrap",
              name : titleName+"权限页",
              state: 4, 
              tabs : false, 
              urlList: urlList,
              data : data,
              pageName:obj.pageName,
              custom:custom,
              size:{
                w:800,
                h:400
              }
        };
        createBox(opt);
      }

*/