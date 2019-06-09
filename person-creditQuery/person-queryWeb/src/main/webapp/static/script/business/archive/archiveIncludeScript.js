      // 判断浏览器低于IE9引入兼容js文件
      //判断是否是IE浏览器，包括Edge浏览器 
      function IEVersion(){  
            var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串  
            var isOpera = userAgent.indexOf("Opera") > -1;
            var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera; //判断是否IE浏览器  
            var isEdge = userAgent.indexOf("Windows NT 6.1; Trident/7.0;") > -1 && !isIE; //判断是否IE的Edge浏览器  
            if(isIE){  
                 var reIE = new RegExp("MSIE (\\d+\\.\\d+);");  
                 reIE.test(userAgent);  
                 var fIEVersion = parseFloat(RegExp["$1"]);  
                 if(fIEVersion == 7 ||fIEVersion == 8){ return false;}  
                 else if(fIEVersion == 9)  
                 { return "IE9";}  
                 else if(fIEVersion == 10)  
                 { return "IE10";}  
                 else if(fIEVersion == 11)  
                 { return "IE11";}  
                 else  
                 { return false;}//IE版本过低  
            }else if(isEdge){  
                return "Edge";  
            }else{  
                return "-1";//非IE  
            }  
      } 
      if(!IEVersion()){
            LoadMyFile([
                "../static/script/ieCompatible.js"
                  ,"../static/script/json2.min.js"    
        ]);
      }

        // 引入大金额bigDecimal的运算
      var locationUrl = location.href;
      var nameArr=["cashflows","iaes","balancesheet","ispa","ibs"];
      for(var i=0; i<nameArr.length; i++){
        if(locationUrl.indexOf(nameArr[i])> -1){
           LoadMyFile([
                  "../static/script/BigDecimal.js"
          ]);
          break;
        }
      }

      // 统一引入js文件
      LoadMyFile([
                  //"../static/script/jquery-1.9.1.min.js"
            "../static/script/jquery-1.11.1.min.js"
            ,"../static/script/jquery.serializejson.js"
            // 兼容ie9上传文件
             ,"../static/script/ajaxfileupload.js"
            // ,"../static/script/My97DatePicker/WdatePicker.js"
            // ,"../static/script/layui/layui.js"
            // 弹出层的JS
            // ,"../static/script/layer/layer.js"
            // ,"../static/script/layer/layer.cu.js"
            // ,"../static/script/layer/layer.callback.js"
            ,"../static/script/layui/layui.js"
            ,"../static/script/layui/layui.cu.js"
            ,"../static/script/layui/layui.callback.js"
            // 校验的JS
            ,"../static/script/jQuery-Validation-Engine/js/jquery.validationEngine.js"
            ,"../static/script/jQuery-Validation-Engine/js/jquery.validationEngine-zh_CN.js"
            ,"../static/script/jQuery-Validation-Engine/js/jquery.validationEngine.custom.js"
            ,"../static/script/jQuery-Validation-Engine/js/jquery.validationEngine.init.js"
            // 公共方法
            ,"../static/script/common.js"
            // 内页方法
            ,"../static/script/form.js"
            ,"../static/script/page.js"
            ,"../static/script/business/archive/archive.js"
        ]);
