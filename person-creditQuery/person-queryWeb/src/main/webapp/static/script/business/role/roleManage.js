      /**
      * [bindCustomBtnEvent 绑定list页中“菜单权限”和“按钮权限”按钮点击事件]
      * @param  {[Object]} obj [list页面配置的tabBox组件参数对象]
      */
      function bindCustomBtnEvent(obj){
          $(".menuRoleBtn").bind("click",function(){roleBtn(obj,"menu")});
          $(".buttonRoleBtn").bind("click",function(){roleBtn(obj,"button")});
      }


      function roleBtn(obj,page){
        var urlList = obj.listObj;
        var rolePageUrl,titleName,type;
        if(page == "menu"){
            titleName = "菜单";
            type = 0;
            rolePageUrl = urlList.tabs[0].menuRoleList;
        }else if(page == "button"){
            titleName = "按钮";
            rolePageUrl = urlList.tabs[0].buttonRoleList;
            type = 1;
        }
        var custom ={
          customName:"roleManage", 
          rolePageUrl:rolePageUrl,
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
        if(transmitId().length == 1){
          createBox(opt);
        }else{
          layerMsg("请选择1条进行操作！");
        }
      }