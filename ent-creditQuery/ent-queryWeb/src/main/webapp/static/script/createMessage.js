// JavaScript Document
$(function(){
 var msg = function (ele, opt) { 
  this.defaults = {
   //id
   id: "",
   //表头格式
   columns: null,
   // 表格数据
   data: null
  }
  this.settings = $.extend({}, this.defaults, opt); 
 }
  msg.prototype = {
  _id:null,
  _op:null,
  init: function () {
     var json,ele;
     this._id=this.settings.id;
     _op=this; 
     ele = $("#"+this._id);
     json = this.settings.data;
     this.create(json,ele);
     ele.undelegate();
     this.bindEvent(ele);
  },
  create: function (json,ele) {
   //初始化元素
   this.InitializeElement(json,ele);
   //初始化表头
   this.createTableHead(ele);
   //初始化动态行
   this.createTableBody(json,ele);
  },
  bindEvent: function (ele) { 
   //添加鼠标悬浮事件
   this.registermousehover(ele);
  },
  //初始化元素
  InitializeElement: function (json,table) {
   var title = "<p class='msgTitle'>"+ json.typeName +"</p>";
   table.append(title,"<table><thead><tr></tr></thead><tbody></tbody><TFOOT></TFOOT></table>");
  },
  //循环添加表头
  createTableHead: function (table) {
   var headcols = this.settings.columns;
   table.find("thead tr").empty();
   for (var i = 0; i < headcols.length; i++) { 
      table.find("thead tr").append("<th width=" + headcols[i].width + " align='center'>" + headcols[i].title + "</th>");
   } 
  },
  //循环添加行
  createTableBody: function (json,table) {
   var color,barW,rate,field,columns = _op.settings.columns; 
   var rowsdata = "";
   _op.setBarAndComplateNum(json);
   for (var row = 0; row < json.list.length; row++) {
      rowsdata += "<tr>";
      for (var colindex = 0; colindex < columns.length; colindex++) {
          field = json.list[row][columns[colindex].field];
          if(columns[colindex].field == "msgStat"){
              if( field == null){
                field = "";
              }else if ( field == "1"){
                field = "<div class='stat stat1'>处理中</div>";
              }else if ( field == "2"){
                field = "<div class='stat stat2'>处理失败</div>";
              }else if ( field == "3"){
                field = "<div class='stat stat3'>回滚中</div>";
              }else if ( field == "4"){
                field = "<div class='stat stat4'>回滚失效</div>";
              }else if ( field == "5"){
                field = "<div class='stat stat5'>处理完成</div>";
              }else if ( field == "6"){
                field = "<div class='stat stat6'>文件写入中</div>";
              }else if ( field == "7"){
                field = "<div class='stat stat7'>文件写入失败</div>";
              }else if ( field == "8"){
                field = "<div class='stat stat8'>文件写入完成</div>";
              }else if ( field == "9"){
            	  field = "<div class='stat stat9'>回滚完成</div>";
              }else if ( field == "10"){
            	  field = "<div class='stat stat10'>预处理中</div>";
              }
          }
          if ( columns[colindex].field == "bar"){
            rate = 100*parseInt(json.list[row].handledNum)/parseInt(json.list[row].rptNum);
            if(rate != parseInt(rate)){
               rate =rate.toFixed(2);
            }
            barW = 3*rate;
            if(rate>50){
              color = "color:#fff;";
            }else{
              color = "";
            }
            field = "<div class='progressBar'><div style='width:"+barW+"px;' id='bar'"+row+"' class='bar'></div><span style='"+color+"'> "+rate+"% </span></div>";
          }
          if( columns[colindex].field == "rate"){
            field = json.list[row].handledNum + "/" + json.list[row].rptNum;
          }
          if(field == (undefined ||null)){
              field = "";
          }
          rowsdata += '<td align="center">' + field + '</td>';
      }
      rowsdata += "</tr>";
   }
   table.find("tbody").empty().append(rowsdata);
  },
  // 设置回滚完成进度条和完成数
  setBarAndComplateNum: function(json){
    for(var i= 0;i<json.list.length;i++){
      if(json.list[i].msgStat == 9){
        json.list[i].handledNum = json.list[i].rptNum;
      }else{
        return;
      }
    }
  },
  //添加鼠标悬浮事件
  registermousehover: function (table) {
   table.find("tbody tr").mouseover(function () {
    $(this).addClass("mouseover");
   }).mouseleave(function () {
    $(this).removeClass("mouseover");
   });
  }
 }
 $.fn.createMsg = function (options) {
    var createMsg = new msg(this, options); 
    createMsg.init();
 }
})
  // 组装表格数据
  function createBar(json) {
      if(json.messageArr != undefined && json.messageArr.length>0){
        for (var i = 0; i < json.messageArr.length; i++) {
          var msgOpt = {
            id : "typeWrap" + i,
            columns : [ {
              field : 'msgNum',
              title : '报文号',
              width : 50
            }, {
              field : 'bar',
              title : '进度条',
              width : 300
            }, {
              field : 'checkSuccessNum',
              title : '校验通过数',
              width : 100
            }, {
              field : 'rate',
              title : '已完成数/总数',
              width : 100
            }, {
              field : 'msgStat',
              title : '处理状态',
              width : 100
            } ],
            data : json.messageArr[i],
            index : i
          };
          if(json.messageArr[i].list.length>0){
            $("#listWrap").append("<div id='typeWrap"+i+"'></div>");
            $("#typeWrap" + i).createMsg(msgOpt);
          }
        }
      }
  }
  // 发送生成报文请求
  function msgCreate(url,data) {
    $.post(url,data,function(result) {
      return false;
    });
  }
  
  function importdata(url,data) {
	    $.post(url,data,function(result) {
	    		 layerAlert(result);
	      return false;
	    });
	  }
  // 发送请求返回值判断按段删除和整笔删除是否可用
  function msgButton(url) {
    $.post(url, function(result) {
      result = strToJson(result);
      if (result.someDel == true) {
        $(".someDelBtn").attr("disabled",false).removeClass("disabled").on("click",function() {
          location.href = "somedel";
        });
      }
      if (result.allDel == true) {
        $(".allDelBtn").attr("disabled",false).removeClass("disabled").on("click", function() {
          location.href = "alldel";
        });
      }
    });
  }
  // 发送请求返回报文数据
  function msgTimer(url,data,arr) {
      setTimeout(function(){
         msgAjax(url,data,arr)
      }, 5000);
/*    var timer = setInterval(function() {
        msgAjax(url,data,arr);
     }, 1000);*/
  }
  // 发送请求返回报文数据
  function msgAjax(url,data,arr) {
      $.ajax({
        type : 'post',
        url : url,
        cache : false,
        dataType : 'json',
        data : data,
        async : true,
        beforeSend : function() {
        },
        success : function(result, textStatus) {
        	 $("#listWrap").empty();
             createBar(result);
          if (result.StopFlag != true ) {
            msgTimer(url,data,arr);
            $(".startBtn").each(function(){
              if(!$(this).hasClass("disabled")){
                $(this).addClass("disabled").attr("disabled", true).text("处理中..");
              }
            })
          } else {
            $(".startBtn").eq(0).removeClass("disabled").attr("disabled", false).text(arr[0]);
            $(".startBtn").eq(1).removeClass("disabled").attr("disabled", false).text(arr[1]);
          }
        },
        error : function(textStatus) {
        },
        complete : function(result, textStatus) {
        }
      });
  }