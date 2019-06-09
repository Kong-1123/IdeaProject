$(function(){
    // 第一个输入框获取焦点
    $("#layui-form input[type='text']").not(".disabled").eq(0).focus();
    // 窗口内list放大时控制高度
    if($("#iframeWrap",parent.document).attr("class") == "iframeTabs"){
        var h = $("html",parent.document).height();
        $("#form-wrap",parent.document).css("height",h-107);
        $(window).resize(function() {
           h = $("html",parent.document).height();
           $("#form-wrap",parent.document).css("height",h-107);
           $(".tabWrap",parent.document).height($(".creditBox",parent.document).height()-$(".headWrap",parent.document).height());
            if(layerIndex){
              if($(this).width()>901){
                layer.full(layerIndex);
              }else{
                layer.restore(layerIndex);
              }
            }
        });
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
      if(kCode ==13 ){
      // 13表示回车键 32表示空格键
      // if(kCode ==13 || kCode == 32 ){
        return false;
      }
    }
  // 引入机构树
  orgTree();

  // 数组去重复
  Array.prototype.unique = function(){
     var res = [];
     var json = {};
     for(var i = 0; i < this.length; i++){
      if(!json[this[i]]){
       res.push(this[i]);
       json[this[i]] = 1;
      }
     }
     return res;
  }
  searchFormBtnEvent();
})

// 查询框下拉/刷新/关闭
function searchFormBtnEvent(){
    var $btn = $(".main-list .list-top_right li a");
    $btn.each(function(i){
      $(this).removeAttr("href").addClass("seachWrapBtn"+i);
    })
    var consH = $(".main-list .cons").height();
    $(".main-list .list-top_right").on("click",".seachWrapBtn0",function(){
       /*var t = $(this);
      var img = t.find("img");
      var h = $(".main-list .cons").height();
     if(h>0){
        $(".main-list .cons").stop().animate({height:"0px"});
        $(".main-list .cons").css({"opacity":"0","overflow":"hidden"});
        $(".main-list .btnWrap").hide();
        img.attr("src","../static/images/table/btn_icon0.png");
      }else{
        $(".main-list .cons").stop().animate({height:consH +"px"},function(){
          $(".main-list .btnWrap").fadeIn(300);
          img.attr("src","../static/images/table/btn_icon1.png");
        });
        $(".main-list .cons").css({"opacity":"1","overflow":"inherit"});
      }*/
      var cons = $(".main-list .cons");
      var t = $(this);
      var img = t.find("img");
      if(cons.is(":hidden")){
        cons.stop().slideDown(500,function(){
            $(".main-list .btnWrap").fadeIn(300);
            img.attr("src","../static/images/table/btn_icon1.png");
        });
      }else{
        cons.stop().slideUp(500,function(){
            img.attr("src","../static/images/table/btn_icon0.png");
        });
        $(".main-list .btnWrap").hide();
      }
    })
    $(".main-list .list-top_right").on("click",".seachWrapBtn1",function(){
      reloadPage();
    })
    $(".main-list .list-top_right").on("click",".seachWrapBtn2",function(){
      $(".layui-tab .layui-this .layui-tab-close",parent.document).click();
    })
  }
