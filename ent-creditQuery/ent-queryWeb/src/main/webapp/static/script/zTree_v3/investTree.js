var investTreeObj;
function investCodeSetName(code,data){
    if(code == data.id){
      $(".investTree").val(data.name);
      // investTreeObj.selectNode(data);
      return false;
    }else{
      if(data.children){
        data.children.forEach(function(item , index , array){     
            investCodeSetName(code,item);                               
        });
      }
    }
}
function investTreeQuery(val){
        var nodes = investTreeObj.getNodesByParamFuzzy("name", val, null);  
        if (nodes.length>0) {  
            investTreeObj.selectNode(nodes[0]);  
        }  
}

function investTreeFun(treeData){  
      if($(".investTree").length<1){
    	  return false;
      };
      var investTreeWrap = '<div class="investTreeWrap" ><div class="investQuery" ><input type="text" value="请输入行业名称"/></div><ul id="investTree" style="" class="ztree"></ul></div>';
      $(".modal-body").append(investTreeWrap);
      // $("#query_form_id .modal-body").append(investTreeWrap);
      $(".investTreeWrap").css({
           "display": "none",
           "height": "100%",
           "overflow":"hidden"
      });
      $(".investQuery").css({
           "width" : "100%",
           "height" : "30px",
           "line-height" : "30px",
           "padding" : "10px 0",
           "margin-bottom" : "5px",
           "border-bottom" : "1px solid #eef"
      });
      $(".investQuery input").css({
           "color":"#ccc",
           "width":"90%",
           "height": "28px",
           "display":"block",
           "line-height":"28px",
           "border":"1px solid #333",
           "padding":"0 5px",
           "margin":"0 auto"
      });
      $(".investTreeWrap #investTree").css({
         "width": "90%",
         "height": "75%",
         "background": "#fff",
         "overflow": "auto"
      });
       $(".investQuery input").focus(function(){
          if(!$(this).val() == ""){
            $(this).val("").css({
           "color":"#333"});
          }
       }).blur(function(){
          if($(this).val() == ""){
            $(this).val("请输入行业名称").css({
           "color":"#ccc"});
          }
       });
       // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
       var setting = {
            isSimpleData:true,
            view: {  
                showLine: true
            },  
            treeObj: $("#investTree"),
            callback : {
                beforeAsync : null,
                beforeCheck : null,
                beforeClick : null,
                beforeCollapse : null,
                beforeDblClick : null,
                beforeDrag : null,
                beforeDragOpen : null,
                beforeDrop : null,
                beforeEditName : null,
                beforeExpand : null,
                beforeMouseDown : null,
                beforeMouseUp : null,
                beforeRemove : null,
                beforeRename : null,
                beforeRightClick : null,
                onAsyncError : null,
                onAsyncSuccess : null,
                onCheck : null,
                onClick : onClick,
                onCollapse : null,
                onDblClick : null,
                onDrag : null,
                onDragMove : null,
                onDrop : null,
                onExpand : null,
                onMouseDown : null,
                onMouseUp : null,
                onNodeCreated : null,
                onRemove : null,
                onRename : null,
                onRightClick : null
            },
       };
      // zTree 的点击跳转
      function onClick(event, treeId, treeNode) {
          $(".investTree").val(treeNode.name);
          $(".investQuery input").val(treeNode.name).css({"color":"#333"});
           $("input[name='actInvest']").val(treeNode.id);
      };
      if($("form").attr("id") == "create_form_id" || $("form").attr("id") == "query_form_id"){
          $(".investTree").val(treeData.name);
          $("input[name='actInvest']").val(treeData.id);
      }

      $(".investTree").click(function(){
        $(this).blur();
        var val = $(this).val();
        $("#investTree").show();
          var index = layerTree(600,320,"行业分类树",$(".investTreeWrap"),1,function(){
            $(".layui-layer-btn").css({"border-top":"1px solid #ccc"});
          },function(){
            $(".investTreeWrap").hide();
          });
          layer.full(index);
          if(val != ""){
             $(".investQuery input").val(val).css({"color":"#333"});
             investTreeQuery(val);
          }
      });

      investTreeObj = $.fn.zTree.init(setting.treeObj, setting, treeData);

      var nodes = investTreeObj.getNodes();
      for (var i = 0; i < nodes.length; i++) { 
          //设置节点展开
          investTreeObj.expandNode(nodes[i], true, false, false);
      }
      // investTreeObj.expandAll(true);
      $(".investQuery input").bind("input",function(){
        var val = $(this).val();
        investTreeQuery(val);
        $(this).css({"color":"#333"})
      });
}


