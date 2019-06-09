$(function(){
   var h = $("html").height();
   $("#form-wrap").css("height",h-107);
    // 窗口最大化时改变显示内容的高度及页签按钮显示状态
    $(window).resize(function() {
        h = $("html").height();
        $("#form-wrap").css("height",h-107);
        if($(this).width()>901){
        	parent.$(".creditBox>a").hide();
        }else{
        	parent.$(".creditBox>a").show();
        }
        $(".tabWrap",parent.document).height($(".creditBox",parent.document).height()-$(".headWrap",parent.document).height());
    });

    // 为form的action属性添加项目名称
    var projectName = getProjectName(location.href);
    var formAction =$("form").attr("action");
    if(formAction!=undefined){
        $("form").attr("action",projectName+formAction);
    }
    // 为金额等需要千分符输入项做操作
    if($(".numFmt").length>0){
        $(".numFmt").focus(function(){
            var val = $(this).val();
            var v = val.replace(/,/g,'');
            $(this).val(v);
        }).blur(function(){
            var val = $(this).val();
            if(val.indexOf(".") > -1){
                var v1 = val.split(".")[0].replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                $(this).val(v1+"."+val.split(".")[1]) ;
            }else{
                var v2 = val.split(".")[0].replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
                $(this).val(v2);
            }
        })
    }
    // 引入机构树
    orgTree();
    
    // 引入菜单树
    menuTree();
    
    

    // 引入行业树
    
    if($(".investTree").length>0){
        if($(".orgTree").length>0){
            pathArr = ["../static/script/zTree_v3/jquery.ztree.exhide.min.js","../static/script/zTree_v3/investTree.js"];
        }else{
            pathArr = ["../static/script/zTree_v3/jquery.ztree.core.min.js","../static/script/zTree_v3/jquery.ztree.exhide.min.js","../static/script/zTree_v3/investTree.js","../static/script/zTree_v3/style/static.css","../static/script/zTree_v3/style/zTreeStyle_01.css"];
        }
        for (var i = 0; i < pathArr.length; i++) {
            var file = pathArr[i];
            if (file.match(/.*.js$/)){
                // loadJs(file);
                $("body").append('<script type="text/javascript" src="' + file + '"></script>');
            }else if (file.match(/.*.css$/)){
                loadCss(file);
                // $("head").append('<link rel="stylesheet" href="' + file + '" type="text/css" />');
            }
        }
        investTreeFun(parent.parent.investTreeData);
    }

    // 引入菜单权限/按钮权限管理
    
    if($(".roleManage").length>0){
        pathArr = ["../static/script/zTree_v3/jquery.ztree.core.min.js","../static/script/zTree_v3/jquery.ztree.excheck.min.js","../static/script/zTree_v3/roleTree.js","../static/script/zTree_v3/style/static.css","../static/script/zTree_v3/style/zTreeStyle_01.css","../static/style/iconfont/iconfont.css"];
        for (var i = 0; i < pathArr.length; i++) {
            var file = pathArr[i];
            if (file.match(/.*.js$/)){
                // loadJs(file);
                $("body").append('<script type="text/javascript" src="' + file + '"></script>');
            }else if (file.match(/.*.css$/)){
                loadCss(file);
            }
        }
    }

    // 禁用回车默认事件
    document.onkeydown=function(evt){
        var event,kCode;
        if(window.event){
          event = window.event;
        }else{
          event = evt ;
        }
        if(event.keyCode){
          kCode = event.keyCode;
        }else{
          kCode = event.charCode ;
        }  
        if(kCode ==13){
          return false;
        }
      }

    // 第一个输入框获取焦点
    $("#form-wrap input[type='text']").not(".disabled").eq(0).focus();
})