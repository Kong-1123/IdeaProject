      /**
       * [reloadPage 刷新当前页面]
       */
      function reloadPage(){
           window.location.reload();
      }

      /**
       * [reloadParentPage 刷新父级窗体页面]
       */
      function reloadParentPage(){
           parent.location.reload();
      }
      
      /**
       * [deleteBtnCallback list页面删除按钮回调方法]
       * @param  {[Object]} obj [list页面定义的页签组件配置对象]
       */
      function deleteBtnCallback(obj){
              if(obj.tabs){
                  var id = transmitId()+"";
                  var del =obj.listObj.tabs[0].delUrl
                  $.post(del+id,function(data){
                    json = strToJson(data);
                      layerAlert(json.msg,reloadPage)
                  }); 
              }else{
                  var id = transmitId()+"";
                  var urlList = returnOneUrlObj(obj.pageName,obj.listObj);
                  var del = urlList.delUrl;
                  $.post(del+id,function(data){
                      json = strToJson(data);
                      layerAlert(json.msg,reloadPage)
                  }); 
              }
      }
      
      /**
       * [oneCreatePage 在单页面中list页面新建按钮的回调函数]
       * @param  {[String]} value [输入框中输入的值，
       *                          将其发到后台进行查询并做相应处理]
       * @param  {[Object]} obj   [list页面中的页签组件配置项对象]
       */
      function oneCreatePage(value,obj){ 
          var json1,json2 = null;
          var getValUrl1 = obj.listObj.tabs[0].getValUrl;
          var getValUrl2 = obj.urlObj.getValUrl;
          ajaxFunc(getValUrl1+value,function(result){
              if(result != null){
                formData = result;
              }
              json1 = result;
          },function(result){
              layerMsg("error:"+JSON.stringify(result));
          });
          if(obj.keyNameIsOnly){
             ajaxFunc(getValUrl2+value,function(result){
                json2 = result;
            },function(result){
                layerMsg("error:"+JSON.stringify(result));
            });
          }
          if(json1 != null && json2 == null){
              var opt = {
                        ele : ".contentWrap",
                        name : obj.urlObj.name+"-新增页",
                        state: 0, 
                        tabs : obj.tabs,
                        urlList: obj.listObj,
                        data: value,
                        pageName:obj.pageName,
                        flagName:flagName
              };
              if(obj.acctType){
                  var flag = acctType(json1.acctType);
                  if(flag){
                     createBox(opt);
                  }
              }else{
                  createBox(opt);
              }
          }else{
              if(json1 == null){
                    layerMsg("基础段未查询到此"+obj.title);
              }else if(json2 != null){
                    layerMsg("此"+obj.title+"已存在");
              }
          }
      }

      /**
       * [submitTabsMsg 在页签中page页提交方法回调函数非查询页，含页签权限控制]
       * @param  {[Object]} result [提交后返回对象]
       * @param  {[Object]} iframe [父级页面的iframe的jquery对象]
       */
      function submitTabsMsg(result,iframe){ 
              var li = $(".tabHead li.active",parent.document);
              var index = li.index()-1;
              var tabs =parent.listObj.tabs;
              var flagName = parent.flagName;
              li.addClass("complete");
              iframe.attr({
                "data-state":1,
                "src":tabs[index].updUrl,
                "data-id":$.parseJSON(result.comData)[flagName]
              });
 /*             var arr= parent.authorityArr;
              if(arr){
                  isAllow(arr);
              }else{
                  isAllow();
              }*/
      }

      /**
       * [delectCallback page页面删除按钮的回调函数]
       * @param  {[Array]} list   [在页签中全部url对象的数组]
       * @param  {[Object]} urlObj [在单页面中当前页面所在的url对象]
       */
      function delectCallback(list,urlObj){
            var json,del,parentBody =$("body",parent.document);
            var iframe = parentBody.find("#iframeWrap");
            // var id = $("input[name='"+ parent.flagName +"']").val();
            var id = parent.formData.id;
            if(iframe.hasClass("iframePage")){
                var pageName = parentBody.attr("name");
                del = urlObj.delUrl;
                $.post(del+id,function(data){
                  json = strToJson(data);
                    layerAlert(json.msg,
                    function(){
                        reloadParentPage();
                    })
                }); 
            }else{
                var li = parentBody.find(".tabHead li.active");
                var index = li.index()-1;
                var tabs =list.tabs; 
                del =tabs[index].delUrl;
                $.post(del+id,function(data){
                    json = strToJson(data);
                    layerAlert(json.msg,function(){
                        if(index==0){
                          reloadParentPage();
                        }else{
                          reloadPage();
                          li.removeClass("allow");
                          li.removeClass("complete");
                          $("#iframeWrap",parent.document).attr("src",tabs[index].creUrl);
                        }
                    })
                }); 
            }
      }
