       var creditBox = function(){};
       creditBox.prototype={
            opt:{
                // 组件使用对象
                ele: null,
                // 直接跳转
                url : null,
                // 页签名称
                name: "",
                // tab页签内页状态 0为新建，1为修改，2为详情，4为树结构列表，5为自定义弹框
                state: null, 
                // 判断是否为多页签
                tabs : null,
                // 获取urlList对象
                urlList: null,
                // 查询数据字段名，如custId/bussNum
                fieldName:null,
                // 查询数据字段值
                fieldValue: null,
                // 页签内页名
                pageName:null,
                // 自定义业务配置
                custom:{}
                // 关键标识名称
                //flagName:null
            },
            // 初始化组件
            init:function(){
                if($('.creditBox').length<1){
                    var opt =this.opt;
                    var box = "<div class='creditBox'></div>";
                    $(opt.ele).append(box);
                    var tabs = this.returnOne(opt.urlList.tabs); 
                    if(opt.tabs){
                        tabs = opt.urlList.tabs; 
                        this.createHead(opt,tabs);
                      };
                    this.createWrap(tabs);
                }
            },
            // 创建页签菜单
            createHead:function(opt,tabs){
                var head = "<div class='headWrap'><ul class='tabHead clearfix'>";
                    head +="<li class='active' data-page="+tabs[0].eName+" >"+tabs[0].name+"</li>";
                for(var i= 1; i<tabs.length;i++){
                     head +="<li data-page="+tabs[i].eName+">"+tabs[i].name+"</li>";
                }
                head +="</ul></div>";
                var btnL = "<div class='tabBtn tabBtnL'><span></span></div>";
                var btnR = "<div class='tabBtn tabBtnR'><span></span></div>";
                $(".creditBox").append(btnL);
                $(".creditBox").append(head);
                $(".creditBox").append(btnR);
          /*      $(".headWrap").prepend(btnL);
                $(".headWrap").append(btnR);*/
                var $w = $(".headWrap");
                this.scrollContrl($w);
                var $li = $(".tabHead li").not(".empty");
                this.bindEvents($li,tabs);
            },
            // 创建页面容器
            createWrap:function(tabs){
                var t = this;
                var num = 0;
                var id = t.opt.fieldValue;
                var state = t.opt.state;
                var json,tabsClass,url;
                if(t.opt.url){
                    url =t.opt.url;
                }else if(t.opt.custom == undefined ){
                  url = t.changePage(tabs,id);
                }else{
                  if(t.opt.custom.updateErrPageUrl != undefined){
                    url = t.opt.custom.updateErrPageUrl;
                  }else if(t.opt.custom.importPageUrl != undefined){
                    url = t.opt.custom.importPageUrl;
                  }else if(t.opt.custom.thFgUpdatePageUrl != undefined){
                    url = t.opt.custom.thFgUpdatePageUrl;
                  }else if(t.opt.custom.rolePageUrl != undefined){
                    url = t.opt.custom.rolePageUrl;
                  }else if(t.opt.custom.stopReasonUrl != undefined){
                    url = t.opt.custom.stopReasonUrl;
                  }else{
                    url = t.changePage(tabs,id);
                  }
                }
                if(t.opt.tabs){
                  tabsClass = "iframeTabs";
                }else{
                  tabsClass = "iframePage";  
                }
                var pageWrap ='<div class="tabWrap"><iframe id="iframeWrap" class="'+ tabsClass +'" src='+ url +' data-index='+ num +' data-state='+ state +' frameborder="0" scrolling="no"></iframe></div>';
                $(".creditBox").append(pageWrap);
                if(t.opt.tabs && (state == 1||state == 2)){ 
                  if(t.opt.authority == undefined){ 
                    var $li = $(".tabHead li").not(".empty");
                    $li.addClass("allow");
                  }
                }
                $("#iframeWrap").load(function(){
                    var content = $("#iframeWrap").contents();
                    state = $(this).attr("data-state");
                    if(t.opt.tabs){
                        $(".tabWrap").height($(".creditBox").height()-$(".headWrap").height());
                        /*if(content.find("body").attr("name")!=undefined){
                            $("#iframeWrap")[0].contentWindow.formData = formData;
                            if(t.opt.clickBtn == "detail"){
                               $("#iframeWrap").contents().find(".createBtn,.updateBtn,.deleteBtn").attr("disabled","true").addClass("disabled");
                            }
                        }*/
                        if(state == 1||state == 2){
                            var index = $(this).attr("data-index");
                            if(index != undefined){ 
                              num = index;                    
                            }
                            if(!id){
                              id =$(this).attr("data-id");
                            };
                            t.isReturn(tabs,num);
                            // 具体业务赋值由页面进行操作或调整setReadonly(arr)
                            // content.find("#"+t.opt.flagName).val(id);
                        }else if(state == 3){
                             $("#iframeWrap")[0].contentWindow.formData = formData;
                            if(t.opt.clickBtn == "detail"){
                               $("#iframeWrap").contents().find(".createBtn,.updateBtn,.deleteBtn").attr("disabled","true").addClass("disabled");
                            }
                        }else if(state == 0){
                            return false;
                        }
                    }else{
                        $(this).attr("data-id",id); 
                        // 具体业务赋值由页面进行操作或调整setReadonly(arr)
                        // content.find("#"+t.opt.flagName).val(id);
                        if(state == 1||state == 2){
                            t.isReturn(tabs,num);
                        }else if(t.opt.custom && (state == 0)){
                            if(t.opt.custom.customName == "errorinfoshow"){
                              $("#iframeWrap").contents().find("[name='custId'],[name='bussNum']").val(t.opt.custom.key);
                            }else if(t.opt.custom.customName == "button"){
                               $("#iframeWrap").contents().find("[name='stopId']").val(id);
                            }
                            return false;
                        }else if(state == 4){
                          $("#iframeWrap")[0].contentWindow.roleManage(tabs,id,t.opt.custom.type);
                        }else if( state == 5 && t.opt.custom && t.opt.custom.id){
                          t.isReturn(tabs,0);
                        }
                    }
                })
            },
            // 判断后台是否有数据
            isReturn:function(tabs,num){
                var opt = this.opt;
                var content = $("#iframeWrap").contents();
                var t=this;
                var json,getValUrl;
                if(t.opt.tabs){
                  getValUrl = tabs[num].getValUrl;
                }else{
                  if(t.opt.custom == undefined ){
                    getValUrl = tabs.getValUrl;
                  }else{
                    if(t.opt.custom.customName == "errorinfoshow"){
                      getValUrl = t.opt.custom.updateErrDataUrl;
                      content.find("[name='"+t.opt.custom.errorEle+"']").addClass("errorEle");
                      var reason = "<p class='errText'>出错原因："+ t.opt.custom.reason +"</p>";
                      content.find(".modal-body").prepend(reason);
                    }else if(t.opt.custom.customName == "finance"){
                      getValUrl = t.opt.custom.thFgUpdateDataUrl;
                    }else if(t.opt.custom.getValUrl){
                      getValUrl = t.opt.custom.getValUrl;
                    }else{
                      getValUrl = tabs.getValUrl;
                    }
                  }
                }
              if(getValUrl){
                 // 通过ID判断页面为新建或修改
                var fieldName,fieldValue,url,opt=this.opt;
                if(opt.fieldName && opt.fieldValue){
                    fieldName = opt.fieldName;
                    fieldValue = opt.fieldValue;
                }else{
                  var num = $(".creditBox li.active").index();
                  fieldName = opt.fieldName[num].fieldName;
                  fieldValue = opt.fieldName[num].fieldValue;
                }
                  json = getAjaxData(getValUrl+"?"+fieldName + "="+ fieldValue,null);
                  return json;
                }else{
                  return null;
                }
            },
            // 表单元素赋值
            setVal:function(json){
                var opt = this.opt;
                if(json!=null){
                    var table = $("#iframeWrap").contents().find(".grayTable");
                    for(var key in json){
                      if(json[key] == null){
                         json[key]="";
                      }
                      var o = table.find("[name='"+key+"']");
                      // 具体业务由页面操作
               /*       if(key == "noRpt" && json[key] == "3"){
                         o.addClass("readonly").attr("disabled","disabled");
                      }*/
                      if(o.hasClass("dateFmt")){
                        // 日期/时间格式化转为后台操作
                        /*  var date = changeDatetoString(json[key]);
                          table.find("[name='"+key+"']").val(date);*/
                      }else{
                          var numFloat,tagName = o.prop("tagName");
                          if(tagName=="SELECT"){
                            o.find("[value='"+json[key]+"']").prop("selected",true);
                          }else if(tagName=="INPUT"){
                             if(o.hasClass("numFmt")){
                                if(opt.custom == undefined){
                                  numFloat = undefined;
                                }else{
                                  numFloat = opt.custom.numFloat;
                                }
                                var numData = fmtNumForShow(json[key],numFloat);
                                o.attr("value",numData);
                             }else{
                                o.attr("value",json[key]);
                             }
                          }else if(tagName=="TEXTAREA"){
                              if(o.hasClass("numFmt")){
                                if(opt.custom == undefined){
                                  numFloat = undefined;
                                }else{
                                  numFloat = opt.custom.numFloat;
                                }
                                var numData = fmtNumForShow(json[key],opt.custom.numFloat);
                                o.text(numData);
                             }else{
                                o.text(json[key]);
                             }
                          }
                      }
                    }
                    if($("#iframeWrap")[0].contentWindow.init){
                        $("#iframeWrap")[0].contentWindow.init(json);
                    }
                }else{
                  return false;
                }
            },
            // 表单元素赋值
            changePage:function(tabs,id){
                // 通过ID判断页面为新建或修改
                var fieldName,fieldValue,url,opt=this.opt;
                if(opt.fieldName && opt.fieldValue){
                    fieldName = opt.fieldName;
                    fieldValue = opt.fieldValue;
                }else if(opt.fieldName){
                  var num = $(".creditBox li.active").index();
                  fieldName = opt.fieldName[num].fieldName;
                  fieldValue = opt.fieldName[num].fieldValue;
                }

                if(opt.tabs){
                    if(id || (!id && fieldName)){
                        if(opt.state == 0){
                          url = tabs[0].creUrl ;
                        }else if(opt.state == 2){
                          url = tabs[0].detailUrl + "?" + fieldName + "=" + fieldValue;
                        }else{
                          url = tabs[0].updUrl + "?" + fieldName + "=" + fieldValue;
                        }
                    }else{
                        url = tabs[0].creUrl;
                    };
                }else{
                    if(opt.state == 5){
                        url = opt.custom.url;
                    }else if(opt.state == 0){
                        url = tabs.creUrl;
                    }else if(opt.state == 1){
                        url = tabs.updUrl+"?" + fieldName + "="+ fieldValue;
                    }else if(opt.state == 2){
                        url = tabs.detailUrl+"?" + fieldName + "="+ fieldValue;
                    }
                }
                return url;
            },
            // 添加菜单点击事件load页面 
            bindEvents:function(ele,tabs){
                var clickObj,t = this,opt=this.opt;
                var name,num,url,id = t.opt.fieldValue;
                ele.bind("click",function(){
                  if(t.opt.state == 0){
                      id = $("#iframeWrap").attr("data-id");
                  }
                  if($(this).hasClass("active")){
                        return false;
                  }else{
                    if($(this).hasClass("allow")){
                        // $("#iframeWrap").attr({"data-state":t.opt});
                        num = $(this).index();
                        var fieldName = opt.fieldName[num].fieldName;
                        var fieldValue = opt.fieldName[num].fieldValue;
                        /*var fieldName = t.opt.fieldName;
                        var fieldValue = t.opt.fieldValue;
                        if(!fieldName){
                          fieldName = "id";
                          fieldValue = id;
                        }*/
                      /*  if(typeof fieldName == "object"){
                          $.each(fieldName,function(index, value){
                            if(fieldName[index]["tabNum"] == num){
                              fieldName = fieldName[index]["name"];
                              fieldValue = parent["tabsData_"+num];
                            }

                          })
                        }*/
                        for(var i = 0; i<tabs.length;i++){
                          if($(this).attr("data-page") == tabs[i].eName){
                             clickObj = tabs[i];
                          }
                        }
                        $(this).addClass("active").siblings().removeClass("active");
                        if($.inArray(clickObj.eName,t.opt.showList)>-1 && t.opt.showList != undefined){
                                 url = clickObj.listUrl; 
                                 $("#iframeWrap").attr("data-state","3");
                        }else{
                          var jsonData = t.isReturn(tabs,num);
                           if(t.opt.state == 2){
                                url = clickObj.detailUrl+"?"+ fieldName + "="+ fieldValue; 
                                $("#iframeWrap").attr("data-state","2");
                            }else if(jsonData !=null || $(this).hasClass("complete")){
                                url = clickObj.updUrl+"?"+ fieldName + "="+ fieldValue;
                                $("#iframeWrap").attr("data-state","1");
                            }else if(jsonData ==null){
                                url = clickObj.creUrl+"?"+ fieldName + "="+ fieldValue;
                                $("#iframeWrap").attr("data-state","0");
                            }else{
                                return;
                            }
                        }
                        $("#iframeWrap").attr({"src":url,"data-index":num});
                    }else{
                        name = $(this).attr("data-page");
                        t.tabAlert(name);
                    }
                  }
              });
            },
            tabAlert:function(name){
                var custom =this.opt.custom;
                var txt = "未获得权限，请优先完善其他信息";
                if(custom != undefined && custom.allow != undefined){
                  var allow = custom.allow;
                  for(var k in allow){
                    if(name == k){
                      txt = allow[k].msg;
                    }
                  }
                }
                layerMsg(txt);
            },
            // 控制菜单滚动条           
            scrollContrl:function(w){
               // var num = $(".tabHead li").length;
               var width = $(".tabHead li").width();
               // var boxWidth = $(".creditBox").width();
               var scrollLeft,moveSize,tabHeadWidth=0;
       /*        var btnW = $(".tabBtnL").width();
               var showNum = (boxWidth-btnW*2)/width;*/

               // 为横向滚动设置tabHead宽度
               $(".tabHead li").each(function(){
                var t =$(this);
                tabHeadWidth+= t.width()+parseInt(t.css('padding-left'))*2;
               })
               $(".tabHead").css({"width":tabHeadWidth+10})
               // END
               if(tabHeadWidth<800){
                  $(".tabBtnL,.tabBtnR").addClass("btnOff");
                  return false;
               }
               if(w.scrollLeft()==0){
                    $(".tabBtnL").addClass("btnOff");
               }
               $(".tabBtnL").bind("click",function(){
                   scrollLeft = w.scrollLeft();
                   if(scrollLeft!=0){
                      $(this).attr("disabled",true);
                       w.stop().animate({scrollLeft:(scrollLeft-width)},300,function(){
                          $(".tabBtnL").attr("disabled",false);
                       });
                       $(".tabBtnR").removeClass("btnOff"); 
                   }else{
                       $(this).addClass("btnOff"); 
                   }
    	          });
    	          $(".tabBtnR").bind("click",function(){
	                   scrollLeft = w.scrollLeft();
	                    var reg =/^\d+$/;
	                   if(reg.test(scrollLeft/width)||scrollLeft==0){
	                	   $(this).attr("disabled",true);
	                       w.stop().animate({scrollLeft:(scrollLeft+width)},300,function(){
                            $(".tabBtnR").attr("disabled",false);
                         });
	                       $(".tabBtnL").removeClass("btnOff");
	                   }else{
	                       $(this).addClass("btnOff"); 
	                   } 
	               });
            },
            // 单页面时返回urlList对象
            returnOne:function(tabs){
                var opt =this.opt;
                var name = opt.pageName;
                for(var i = 0; i<tabs.length;i++){
                  if(name == tabs[i].eName){
                      return tabs[i];
                  }
                }
            }
        }

        // 创建页签并初始化
        // 第二个参数为关闭按钮是否刷新，true为不刷新
        function createBox(opt,flag){ 
                var box =new creditBox();
                box.opt= opt;
                box.init(); 
                
                var w,h,iframeClassName = $("#iframeWrap",parent.document).attr("class");
                if(!opt.size){
                  if(iframeClassName =="iframeTabs"){
                    w = 800;
                    h = 360;
                  }else{
                    w = 900;
                    h = 500;
                  }
                }else{
                   w = opt.size.w;
                   h = opt.size.h;
                }
                var index = layerOpen(w,h,"",opt.name,$(".creditBox"),1,false,flag);
                return index;
        }
