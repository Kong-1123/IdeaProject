function roleReason(obj,id,type){
      $.ajaxSetup({  
          async : false  
      });  
      var datadAll,urlAll,datadUser,urlUser,submitUrl;
      if(type == 0){
        urlAll = obj.getAllReason+id;
        urlUser=obj.getUserReason+id;
        submitUrl = obj.saveUserReason;
      }else if(type == 1){
        urlAll = obj.getAllReason+id;
        urlUser=obj.getUserReason+id;
        submitUrl = obj.saveUserReason;
      }else if(type == 2){
        urlAll = obj.allReason+id;
        urlUser=obj.userReaso+id;
        submitUrl = obj.Ids;
      }
      // 左侧树数据
      $.post(urlAll,function(data){
          datadAll = strToJson(data);
      })
      // 左侧树数据
      $.post(urlUser,function(data){       
          datadUser = strToJson(data);
      })
      //树1 数据 生成
      Tree1 = $.fn.zTree.init($("#srcTree"),setting, datadAll);  
      Tree1.expandAll(true);
      //树2 数据null
      Tree2 = $.fn.zTree.init($("#tarTree"),setting, datadUser);
      Tree2.expandAll(true);
      var flag1 = false,flag2 = false;
      $("#btnAllL").on("click",function(argument) {
        var t = $(this);
        if(flag1){
          flag1 = false;
          t.text("左侧全选");
        }else{
          flag1 = true;
          t.text("左侧全取消");
        }
        Tree1.checkAllNodes(flag1);
      });
      $("#btnAllR").on("click",function(argument) {
        var t = $(this);
        if(flag2){
          flag2 = false;
          t.text("右侧全选");
        }else{
          flag2 = true;
          t.text("右侧全取消");
        }
        Tree2.checkAllNodes(flag2);
      });
      $("#save").on("click",function(argument) {
        var idArr = [],nodeArr = Tree2.transformToArray(Tree2.getNodes());
        for(var i=0;i<nodeArr.length;i++){
            idArr.push(nodeArr[i].id);
        }
        var str = idArr.join(",");
        roleSubmitFun(submitUrl+id+","+str);
      });
  }


function roleSubmitFun(url){
    ajaxFunc(url,function(result){
        console.log(result.code);
        console.log(result.msg);
        submitMsg(result);
    },function(result){
        layerMsg("error:"+JSON.stringify(result));
    });
}