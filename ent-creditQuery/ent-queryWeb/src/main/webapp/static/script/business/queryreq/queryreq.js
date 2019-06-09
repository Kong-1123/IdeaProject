/**
      * [bindCustomBtnEvent 绑定list页中“信用报告查询”按钮点击事件]
      * @param  {[Object]} obj [list页面配置的tabBox组件参数对象]
      */
    function bindCustomBtnEvent(obj){
      $(".btnWrap .reportQueryBtn").bind("click",function(){
        window.location.href = obj.listObj.tabs[0].flowUrl;
        // window.location.href = "/creditpersonqueryweb/queryreq/create";

      });
      $(".btnWrap .showReportBtn").bind("click",function(){
         if (transmitId().length == 1) {
            var index = $("#grid-data .active").parents("tr").index();
            var parentObj = gridData.list[index];
            var statusUrl = 'getStatus?id=' + parentObj.id;
            ajaxFunc(statusUrl,function(data){
            	result = data;
            	if("00000000" == result.code){
            		url = 'revealReport?creditId=' + parentObj.creditId + '&recordId='+ parentObj.id;
            		window.open(url);
            	} else {
            		layerMsg("此记录不存在有效的信用报告，请选择其他记录查看信用报告");
            	}
            });
        }else{
            layerMsg("请选择1条数据进行操作");
        }
      });
    }

    function dataInit(url,t,submitData,archiveObj){
    	var platFormUlr = sessionStorage.projectName;  
        var result,projectName = getProjectName(location.href);
        if($("#iframeWrap iframe").contents().find("form").length>0){
          if(!submitData){
            var submitData = $("#iframeWrap iframe").contents().find("form").serializeJSON();
          }
          if(archiveObj){
            submitData.autharchiveId = archiveObj.archiveId;
          }else if(parent.formData && parent.formData.autharchiveId){
            submitData.autharchiveId = parent.formData.autharchiveId ;
          }
          parent.formData = submitData;
        }
        var str = JSON.stringify(submitData);

        ajaxFunc(projectName + url, function(data) {
          result = data;
           $(".stepIconWrap div").eq(result.currentPage-1).addClass("on");
           if(result.queryReq && result.queryReq.checkId){
        	   submitData.checkId = result.queryReq.checkId;
           }

          // 00 表示查询成功，弹出信用报告； 01 表示查询失败，弹出提示信息；0 表示忽略此步骤执行下一步； 1 表示 
         if(result.resultCode =="03"){
        	 alert(result.resultMsg);
       	  	 window.location.href = platFormUlr + "/logout";
          }
          if(result.resultCode == "00"){
              window.open(projectName+result.url);
              if(archiveObj){
            	  window.location.href = projectName + "archive/list";
              } else {
            	  window.location.href = projectName + "queryreq/list";
              }
          }else if(result.resultCode == "01"){
        	  if(archiveObj){
        		  layerAlert(result.resultMsg,function(){
        			  window.location.href = projectName + "archive/list";
        		  });
        	  }else{
        		  layerAlert(result.resultMsg,function(){
        			  window.location.href = projectName + "queryreq/list";
        		  });
        	  }
          }else if(result.resultCode == "02"){
        	  if(result.modulaName == "quantityControl"){
        		  layer.confirm(result.resultMsg, {
        			  btn: ['是','否'], //按钮
        			  cancel:function(index){
        				  layer.close(layer.index); //如果设定了yes回调，需进行手工关闭
        			  }
        		  },function(index){
        			  layer.close(layer.index);
        			  dataInit(result.submitUrl,t,submitData,archiveObj);
        		  },function(index){
        			  window.location.href = projectName + "queryreq/list";
        		  });
        	  } else{
        		  layerMsg(result.resultMsg);
        	  }
              if(result.modulaName == "localQuery" ){
                  $("#iframeWrap iframe")[0].contentWindow.setReadonly(["rekUser","rekPasword","checkWay"],true);
              }
          }else if(result.resultCode == "0"){         
              dataInit(result.submitUrl,t,submitData,archiveObj);
          }else if(result.resultCode == "1"){
            if(result.url == ""){
              layerAlert(result.resultMsg,function(){
                 window.location.href = projectName + "queryreq/list";
              });
            }else if(result.url == "queryreq/showLocalReport"){
                      layer.confirm(result.resultMsg, {
                          btn: ['是','否'], //按钮
                          cancel:function(index){
                            layer.close(layer.index); //如果设定了yes回调，需进行手工关闭
                            window.location.href = projectName + "queryreq/list";
                        }
                      },function(index){
                          dataInit(result.url,t,submitData,archiveObj);
                      },
                      function(index){
                          dataInit(result.submitUrl,t,submitData,archiveObj);
                      });
            }else{
              $("#iframeWrap iframe").attr("src",projectName+result.url).load(function(){
                if(result.modulaName =="recheckControl"){
                   var ipt,form = $("#iframeWrap iframe").contents().find("form");
                   var formData = parent.formData;
                   setValue(form,formData);
                   $("#iframeWrap iframe")[0].contentWindow.setReadonly(["rekUser","rekPasword","checkWay"],true);
                }
              });
              if(t){
              // t.click(function () { 
              t.unbind("click").bind("click",function(){
                    if(result.modulaName == "delegationControl"){
                        var active = $("#iframeWrap iframe").contents().find("#grid-data td .active");
                        var id = active.attr("for");
                        if(active.length ==1){
                          submitData.autharchiveId = id ;
                          parent.formData = submitData;
                          nextStep(result,t,submitData,archiveObj);
                        }else{
                          layerAlert("请选择1条进行操作！");
                        }
                    }else if(result.modulaName == "recheckControl"){
                        var validate =$("#iframeWrap iframe").contents().find("#form_id").validationEngine('validate');
                        if(validate){
                          $("#iframeWrap iframe")[0].contentWindow.removeReadonly();
                          var active = $("#iframeWrap iframe").contents().find("#grid-data td .active");
                          var id = active.attr("for");
                          submitData = $("#iframeWrap iframe").contents().find("form").serializeJSON();
                          submitData.autharchiveId = parent.formData.archiveId ;
                          var password = submitData.rekPasword;
                              password=hex_md5(password);
                          submitData.rekPasword = password;
                          nextStep(result,t,submitData,archiveObj);
                        }else{
                          onFailure();
                        }
                    }
                });
              }
            }
          }
        }, function(data) {
          layerMsg("请求出错！");
        }, submitData);
        return result;
    }

    function reportQueryBtn(obj){
        var urlList = obj.listObj;
        var titleName = "信用报告查询",
            reportUrl = urlList.tabs[0].reportUrl;
        var custom ={
            customName:"customUrl", 
            url:reportUrl
           /* ,
            id:123456*/
        }
        var opt = {
              ele : ".contentWrap",
              name : titleName,
              state: 5, 
              tabs : false, 
              urlList: urlList,
              pageName:obj.pageName,
              custom:custom,
              size:{
                w:800,
                h:500
              }
        };
        createBox(opt);
      }

      // page页绑定事件对应方法
     function nextStep(result,t,submitData,archiveObj){
        var result = dataInit(result.submitUrl,t,submitData,archiveObj);
     }
     
     /**
     * [returnObj 获取通过get传值的参数对象]
     * @param  {[type]} str [通过location.search获取的参数字符串]
     * @return {[type]}     [参数对象]
     */
    function returnObj(str1){
      var theRequest = new Object();  
      if ( str1.indexOf( "?" ) != -1 ) {  
        var str = str1.substr( 1 ); //substr()方法返回从参数值开始到结束的字符串；  
        var strs = str.split( "&" );  
        for ( var i = 0; i < strs.length; i++ ) {  
          theRequest[ strs[ i ].split( "=" )[ 0 ] ] = ( strs[ i ].split( "=" )[ 1 ] );  
        }  
        return theRequest; //此时的theRequest就是我们需要的参数；  
      }
    }

    /**
     * [setValue 对表单元素进行赋值]
     * @param {[Object]} form     [form表单对象]
     * @param {[Object]} formData [服务器返回json对象]
     */
    function setValue(form,formData){
    	console.log("setValue");
      var ipt,tagName;
//      var formData1={};
//      if(formData.resultType===undefined){
//    	  formData1=handleFormData(formData);
//      }else{
//    	  formData1=formData;
//      }
      for(var k in formData){
          ipt = form.find("[name='"+k+"']");
          tagName = ipt.prop("tagName");
          if(tagName=="SELECT"){
             ipt.find("[value='"+formData[k]+"']").prop("selected",true);
          }else if(tagName=="INPUT"){
             ipt.attr("value",formData[k]);
          }
      };
    }
    
    function handleFormData(formData){
    	var type;
    	var code;
    	var formData1={};
    	var loancardCode=formData.loancardCode;
    	var creditCode=formData.creditCode;
    	formData1.archiveId=formData.archiveId;
    	formData1.companyName=formData.companyName;
    	formData1.creditCode=formData.creditCode;
    	formData1.loancardCode=formData.loancardCode;
    	if(loancardCode!=null&&creditCode==null){
    		type="0";
    		code=loancardCode;
    	}else if(loancardCode==null&&creditCode!=null){
    		type="1";
    		code=creditCode;
    	}else if (loancardCode!=null&&creditCode!=null){
    		type="0";
    		code=loancardCode;
    	}else{
    		type="";
    		code="";
    	}
    	formData1.type=type;
    	formData1.code=code;
    	return formData1;
    }