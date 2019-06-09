/******************************************************************
	Author:       
	Create Date : 2004-04-25
	Function    : 界面功能

	Explain			:	
	Explain			:	包括两种类型功能方法:  
								1. 提供客户端的请求提交时涉及的公共接口；
								2. 可独立使用的功能接口；
	FunctionList：
			 ___________________________________________________________________________________________
		  | 		   |	 functionType		|					  name  |                                             |
			|_____________________________________________|_____________________________________________|
			|    	1	 |			public			|					 			  |
			|				 |									|					  execSearchAction        |
			|				 |									|					  execSearchByPKAction    |
			|				 |									|					  execUploadAction		  |
			|				 |									|					  execRollAction	      |
			|				 |									|					  execListAction          |
			|				 |									|					  execTreeAction          |
			|________|__________________________________|_____________________________________________|
			|		2		 |			public					|			            myround               |
			|				 |									|						formatround           |
			|				 |									|						Keypress              |
			|				 |									|						trim                  |
			|				 |									|						lTrim                 |
			|				 |									|						rTrim                 |
			|				 |									|						reflectValue          |
			|				 |									|						reflectValue_onKeyUp  |
			|				 |									|						formatToNum           |
			|				 |									|						touchOnblur           |
			|				 |									|						touchOnClick          |
			|				 |									|						touchOnChange         |
			|				 |									|						touchOnKeyPress       |
			|				 |									|						touchOnKeyUp          |
			|				 |									|						touchOnMouseDown      |
			|				 |									|						buttonOnclick         |
			|				 |									|						setButtonEles         |
			|				 |									|						alliedObjs            |
			|				 |									|						alliedObjs_more       |
			|				 |									|						alliedObjs_bit        |
			|				 |									|						addSpeedKeyScript     |
			|________________|__________________________________|_____________________________________________|
			|				 |		  private  		            |						_showButtonPanel	  |
			|				 |									|						_controlScroll        |
			|				 |									|						_moveButtonPanel      |
			|				 |									|						_addChildOption       |
			|				 |									|						_addChildOption_more  |
			|				 |									|						_touchSpeedKey        |
			|________________|__________________________________|_____________________________________________|
	
 ******************************************************************/
function jumpToPage( frameName , url , img , label ) {
	
	if( frameName == "_blank" )  {
		openForm( url , 1 , img , label ) ;
		return ;
	}

  var parameterStr = "";
  var parameterName = new Array();
  var parameterValue = new Array();
  var _tempHideParent = new Array();
  var _tempHideHTML = new Array();
  var hiddenHtml = "";
  if(url.indexOf("?") > 0) {
    parameterStr = url.substring(url.indexOf("?") + 1);
    url = url.substring(0, url.indexOf("?"));
    
    //使用递归来解析参数
    _parseParameter(parameterStr, parameterName, parameterValue);
    
    for(var i = 0; i < parameterName.length; i++) {
      //如果页面中已经存在标签对象，先隐藏
      /*var _curParameterObj = eval("document.all('"+parameterName[i]+"')");
      if(_curParameterObj != null) {
        _tempHideHTML[_tempHideHTML.length++] = _curParameterObj.outerHTML;
        _tempHideParent[_tempHideParent.length++] = _curParameterObj.parentNode;
        _curParameterObj.parentNode.removeChild(_curParameterObj);
      }*/
      hiddenHtml += "<INPUT TYPE='HIDDEN' NAME='"+parameterName[i]+"' VALUE='"+parameterValue[i]+"'/>";
    }
  }

	str = "<form name='tempForm' target='" + frameName + "' action='" + url + "' method='get'>"+hiddenHtml+"</form>" ;

	for( var i in document.all )  {
		if ( document.all[i].tagName == "BODY" )  {
			document.all[i].insertAdjacentHTML( "BeforeEnd" , str ) ;
			break ;
		}
	}

	document.all.tempForm.submit() ;
	document.all.tempForm.outerHTML = "" ;
	
  onOpenState(true);  
	
	//将隐藏的标签对象显示出来
	/*for(var i = 0; i < _tempHideHTML.length; i++) {
	  _tempHideParent[i].insertAdjacentHTML("BeforeEnd", _tempHideHTML[i]);
	}*/

}

//执行跳转。为页面控件之间关联引起的跳转
function jumpToPage_allied(frameName , url) {
	
	if( frameName == "_blank" )  {
		openForm( url) ;
		return ;
	}

  var parameterStr = "";
  var parameterName = new Array();
  var parameterValue = new Array();
  var _tempHideParent = new Array();
  var _tempHideHTML = new Array();
  var hiddenHtml = "";
  if(url.indexOf("?") > 0) {
    parameterStr = url.substring(url.indexOf("?") + 1);
    url = url.substring(0, url.indexOf("?"));
    
    //使用递归来解析参数
    _parseParameter(parameterStr, parameterName, parameterValue);
    
    for(var i = 0; i < parameterName.length; i++) {
      //如果页面中已经存在标签对象，先隐藏
      hiddenHtml += "<INPUT TYPE='HIDDEN' NAME='"+parameterName[i]+"' VALUE='"+parameterValue[i]+"'/>";
    }
  }

	str = "<form name='tempForm' target='" + frameName + "' action='" + url + "' method='get'>"+hiddenHtml+"</form>" ;

	for( var i in document.all )  {
		if ( document.all[i].tagName == "BODY" )  {
			document.all[i].insertAdjacentHTML( "BeforeEnd" , str ) ;
			break ;
		}
	}

	document.all.tempForm.submit() ;
	document.all.tempForm.outerHTML = "" ;
  
}


//解析参数
function _parseParameter(parameterStr, parameterName, parameterValue) {
    var pos = parameterStr.indexOf("&");
    
    if(pos > 0) {
      var _curParameter = parameterStr.substring(0, pos);
      parameterStr = parameterStr.substring(pos+1);
      pos = _curParameter.indexOf("=");
      if(pos > 0) {
        parameterName[parameterName.length++]   = _curParameter.substring(0, pos);
        parameterValue[parameterValue.length++] = _curParameter.substring(pos+1);
      }

      _parseParameter(parameterStr, parameterName, parameterValue);
    } else if(parameterStr.indexOf("=") > 0) {
        pos = parameterStr.indexOf("=");
        parameterName[parameterName.length++]   = parameterStr.substring(0, pos);
        parameterValue[parameterValue.length++] = parameterStr.substring(pos+1);
    }
}
//******************************************************

//======================================================
//设置页面title
//当文档被加载时执行
//======================================================
/**
  * 设置当前模块的名称
  * @ param  titleStr    ：模块名称   string
  */
function setTitle(titleStr) {
  
  var str = "<center><TABLE width='700' border='1' cellspacing='2' height='20' bordercolordark='#333333' bordercolorlight='#FFFFFF' class='normalText'>"
          + " <tr align='center'><td width='15%'></td><TD height='20' bgcolor='#003366'><FONT color='#FFFFFF'><strong>"
          + titleStr
          + " </FONT><strong></TD><td width='15%'></td></tr>"
          + "</TABLE></center>";
  
  document.body.insertAdjacentHTML( "afterBegin" , str ) ;  
  
}
//******************************************************


//======================================================
//在状态栏显示信息
//当文档被加载时执行
//======================================================
function showStatus()  {

	theFlag = 0 ;

	for(var i in document.all)  {
		if (document.all[i].tagName == "MESSAGE")  {
			theFlag = 1 ;
			type = document.all[i].type ;
			message = document.all[i].value ;
			break ;
		}
	}

	if( theFlag == 0 )
		return ;

	if( type == "success" )
		showSuccessMessage( message ) ;

	if( type == "failure" )
		showFailureMessage( message ) ;

}
//******************************************************



//======================================================
//给出操作提示
function showOperationMessage( _message ) {
	
		//如果此时有操作提示，说明此时正向服务器请求服务。
		//现在的新请求不能再被响应
		if( document.all('operatingFlagPanel') != null ) {
			alert("当前的操作未完成，不能进行其他的操作！");
			return false;
		}
	
        //加入正在操作提示
        var iStr = "<div ID='operatingFlagPanel' style='position:absolute;z-index:1000;width:250;height:40;display:none;border-style:solid;border-width:1;border-color:#555555;font-size:9pt;background-color:#5555EE' align='center'><table border='0' width='100%' height='100%'><tr><td align='center' style='font-size:9pt;color:#FFFFFF'>请稍候，正在 "+_message+" ......</td></tr></table></div>";
        document.body.insertAdjacentHTML('AfterBegin', iStr);
        sTop = document.body.scrollTop + 50 ;
        sLeft = document.body.offsetWidth/2 - 125 ;
        if( document.body.offsetHeight < 130 ) {
        		document.all('operatingFlagPanel').style.height = 23;
        		sTop = (document.body.offsetHeight-23)/2; 
        }		
        if( document.body.offsetWidth < 300 ) {
        		document.all('operatingFlagPanel').style.width = 150;
		        sLeft = document.body.offsetWidth/2 - 75 ;
        }		
        document.all('operatingFlagPanel').style.pixelLeft = sLeft;
        document.all('operatingFlagPanel').style.pixelTop = sTop;
        document.all('operatingFlagPanel').style.display='';
        
        return true;
}        





//*****************************************************************


/*
 * 在提交请求前，对一些值进行前置处理
 * 1.去掉TextArea中回车换行
 × 2.替换返回页面中的&为$
 */
function preActionHandle(mainForm){
	var _textAreas = mainForm.getElementsByTagName("TEXTAREA");
	for(var i = 0; i < _textAreas.length; i++) {
          var curAreaObj = _textAreas[i];
     	  var enterStr = curAreaObj.value;
     	  while( enterStr.indexOf("\r") != -1 && enterStr.indexOf("\n") != -1) {
    		 enterStr = enterStr.replace('\r', '');
    		 enterStr = enterStr.replace('\n', '');
    	  }
          curAreaObj.value = enterStr;       
     }
     //-E-Modified by 李俊超 2003/05/22
     //while(ReturnPage.indexOf("&") != -1) {
     //    ReturnPage = ReturnPage.replace('&', '$');//将符号'&'转换为'$'
     //}

}

/**
 * 执行查询业务时的请求提交
 */
function execSearchAction() {
	var mainForm = document.all("mainForm");
	mainForm.action = "/servlet/org.apache.struts.action.ActionServlet?_TaskID=searchList";
	mainForm.target = "hideArea";
	mainForm.submit();
  if(self.onOpenState != null) {
    onOpenState(true);  
  }
	
}

//针对主键进行查询的请求提交
function execSearchByPKAction(taskId) {
	var mainForm = document.all("mainForm");
	mainForm.action = "/servlet/org.apache.struts.action.ActionServlet?_TaskID="+taskId;
	mainForm.target = "hideArea";
	mainForm.submit();
  if(self.onOpenState != null) {
    onOpenState(true);  
  }
	
}

//执行上传时提交
//saveDirID: 保存路径编号，需要在配置文件中进行定义
function execUploadAction(ReturnPage, ReturnArea, saveDirID, maxSize) {
	
	mainForm.enctype= "multipart/form-data";
	mainForm.action = "/view/share/upload/commonupload.jsp?ReturnPage="+ReturnPage+"&ReturnArea="+ReturnArea+"&saveDirID="+saveDirID+"&maxSize="+maxSize;
	mainForm.target = "hideArea";
	mainForm.submit();
	
}

//执行Roll的提交
function execRollAction() {
	var mainForm = document.all("rollForm");
/*	var rollTaskID = mainForm._RollTaskID.value;
	if(rollTaskID == null) {
		alert("请设置提交的ACTION别名");
	}
*/	

	alert(mainForm);
	mainForm.action = mainForm._RollTaskID+".do";
	mainForm.submit();
  if(self.onOpenState != null) {
    onOpenState(true);  
  }

}

//执行列表提交
function execListAction() {
	var mainForm = document.all("mainForm");
	mainForm.action = "/servlet/org.apache.struts.action.ActionServlet?_TaskID=onSelectItem";
	mainForm.target = "hideArea";
	mainForm.submit();
  if(self.onOpenState != null) {
    onOpenState(true);  
  }
	
}

//执行树列表提交
function execTreeAction() {
	var mainForm = document.all("mainForm");
	mainForm.action = "/servlet/org.apache.struts.action.ActionServlet?_TaskID=onSelectNode";
	mainForm.target = "hideArea";
	mainForm.submit();
  if(self.onOpenState != null) {
    onOpenState(true);  
  }
}

//====================================================== 









//======================================================
//------------------------四舍五入,n为小数位数,返回数字---------------------------------//
function myround(m,n) {
 var myrd = Math.round(m*Math.pow(10,n))/Math.pow(10,n); 
 return myrd;
}

//------------------------四舍五入,n为小数位数,格式化数字,返回字符---------------------------------//
function formatround(m,n) {
 var myrd = Math.round(m*Math.pow(10,n))/Math.pow(10,n)+"";
 
 var strTmp = ""; 
 var index = myrd.indexOf("."); 
 var strAfter = myrd.substring(index+1);
 if(index>0){
  for(var j=0; j<n-strAfter.length;j++){   
   myrd = myrd + "0";   
  }
 }else{
  if(n>0){
   myrd = myrd + ".";
   for(var i= 0;i<n;i++){
    myrd = myrd + "0"; 
   }      
  }
 }
 return myrd;
}


//======================================================
//去掉空格,类似于java中的"trim()"方法
//去掉字串两边的空格
function trim(str) {
 return lTrim(rTrim(str));
}
 
//去掉字串左边的空格
function lTrim(str) {
 if (str.charAt(0) == " ") {
  //如果字串左边第一个字符为空格
  str = str.slice(1);//将空格从字串中去掉
  //这一句也可改成 str = str.substring(1, str.length);
  str = lTrim(str); //递归调用
 }
 return str;
}
 
//去掉字串右边的空格
function rTrim(str) {
 var iLength;
 
 iLength = str.length;
 if (str.charAt(iLength - 1) == " ") {
  //如果字串右边第一个字符为空格
  str = str.slice(0, iLength - 1);//将空格从字串中去掉   
  //这一句也可改成 str = str.substring(0, iLength - 1);
  str = rTrim(str); //递归调用
 }
 return str;
}
//======================================================



//======================================================
//由输入的参数值，设置下拉框中相应值为当前焦点。如果没有，给予提示，并返回焦点
function reflectValue( inputObj, reflectObj, objCaption ) {
  
 if(inputObj.value == "" || reflectObj.children.length == 1) {
  reflectObj.children(0).selected = true;//如果没有输入值，或者下拉菜单只有一项，选择下拉菜单的第一项并返回。
  return;
 }
 /*if(inputObj.value.length != reflectObj.children(1).value.length) {
  return;//如果没有输入值，或者下拉菜单只有一项，或者输入值长度与下拉菜单中的值长度不一致，返回。
 }*/
 
 for(var i = 0; i < reflectObj.children.length; i++) {
  if(inputObj.value == reflectObj.children(i).value) {
   reflectObj.children(i).selected = true;
   return;
  }
 }
 
 if(inputObj.value.length > 1 || inputObj.value.length == inputObj.maxLength) {
   alert("代码为["+inputObj.value+"]的"+objCaption+"不存在!");
   reflectObj.children(0).selected = true;
   inputObj.focus();
   inputObj.select();
  }
  
} 
 
 
//由输入的参数值，匹配下拉框中相应值为当前焦点。
function reflectValue_onKeyUp( inputObj, reflectObj ) {

 if(inputObj.value == "") {
  inputObj.blur();
  inputObj.focus();
  return;
 }
 
 for(var i = 1; i < reflectObj.children.length; i++) {
  if(inputObj.value == reflectObj.children(i).value) {
   reflectObj.children(i).selected = true;
   return;
  }
 }
 
 if(inputObj.value.length == inputObj.maxLength) {
 	inputObj.blur();
 }
 
} 
//======================================================



/**
  * 格式化数字：将科学计数法转化为通常形式
  * 说明：
  *   1.如果用科学计数法表示的数值(以下简称"科学计数数")的指数为正数[如3.3E9]，又分为下列几种处理方式
  *	    1.1  科学计数数的尾数为正数
  *     1.2  科学计数数的尾数为负数
  *
  *   2.如果用科学计数法表示的数值(以下简称"科学计数数")的指数为负数[如3.3E-9]，又分为下列几种处理方式
  *	    2.1  科学计数数的尾数为正数
  *     2.2  科学计数数的尾数为负数
  *
  *   注意：因为如果没有人为的因素(现在也不可能存在)，不可能存在科学计数数的尾数为0.000..的情况，在此忽略
  * parameter:  curNumber 需要格式化的数字，字符串类型
  *             dec       保留的小数位数，整数类型
  */
function formatToNum(curNumber, dec) {

	//如果不是科学计数法，直接返回
	if (curNumber == null) {
		return curNumber;
	}

	//检查参数是否为数字，如果不是，直接返回
	if(isNaN(parseFloat(curNumber))) {
		return curNumber;
	}

	if(curNumber == "0" || (curNumber.indexOf("e") == -1 && curNumber.indexOf("E") == -1)) {
		//判断小数位是否和传入的参数dec一致，如果不一致，补足小数位
		var pos = curNumber.indexOf(".");
		if (pos != -1 && dec != null && dec > -1) {

			if (dec == 0) { //如果dec为0，首先进行四舍五入，去掉小数位
				curNumber = formatround(curNumber.substring(0, pos), 0);

				if (curNumber.indexOf(".") != -1) {
					curNumber = curNumber.substring(0, curNumber.indexOf("."));
				}
			} else {
				var decBit = curNumber.substring(pos); //小数位
				var bitMis = dec - (decBit.length - 1); //dec与小数位的差值
				if (bitMis > 0) {
					while (bitMis > 0) {
						curNumber = curNumber + "0";
						bitMis--;
					}
				} else if (bitMis < 0) {
					curNumber = formatround(curNumber, dec);
				}
			}
		}

		return curNumber;
	}


	//判断是否为负数
	var sufTag = ""; //负数标志
	if (curNumber.length > 1 && curNumber.substring(0, 1) == "-") {
		sufTag = "-";
		curNumber = curNumber.substring(1);
	}

	var curValue = "";
	var pos = curNumber.indexOf("E");
	if(pos == -1) {
		pos = curNumber.indexOf("e");
	}

	var tail = "1"; //尾数
	var exponent = 1; //指数
	if (pos != -1) {
		exponent = parseInt(curNumber.substring(pos + 1));
		tail = curNumber.substring(0, pos);
	}

	//如果exponent为正数，说明为整数 
	var pointPos = tail.indexOf(".");
	var minLocation = 0; //共有多少个小数位
	var lastMinLocation = 0; //小数位后的数字个数
	if (pointPos > 0) {
		minLocation = tail.substring(0, pointPos).length;
		lastMinLocation = tail.substring(pointPos + 1).length;
		tail = tail.substring(0, pointPos) + tail.substring(pointPos + 1);
	}

	//循环设置数字后的"0"
	var zeroStr = ""; //所有的零位
	var exponentNum = exponent;
	if (exponent < 0) {
		zeroStr = "0.";
		exponentNum = -exponentNum;
	}

	for (var i = 0; i < exponentNum; i++) {
		zeroStr += "0";
	}

	if (exponent > 0) {
		//1.1 指数位大于小数点后数字个数
		if (exponentNum > lastMinLocation) {
			curValue = tail + zeroStr.substring(0, exponentNum - lastMinLocation);
		} else if (exponentNum == lastMinLocation) { //1.2 等于
			curValue = tail;
		} else { //1.3 小于
			var curLastIndex = minLocation + exponentNum;
			curValue = tail.substring(0, curLastIndex) + "." + tail.substring(curLastIndex);
		}
	} else {
		//2.1 指数位大于小数点后数字个数
		if (exponentNum > minLocation) {
			zeroStr = zeroStr.substring(0, zeroStr.length - minLocation);
			if (zeroStr == "0") {
				zeroStr = "0.0";
			}
			curValue = zeroStr + tail;
		} else if (exponentNum == minLocation) { //2.2 等于
			curValue = "0." + tail;
		} else { //2.3 小于
			if (minLocation - exponentNum > 0) {
				minLocation = minLocation - exponentNum;
				curValue = tail.substring(0, minLocation) + "." + tail.substring(minLocation);
			} 
		}
	}

	//判断小数位是否和传入的参数dec一致，如果不一致，补足小数位
	pos = curValue.indexOf(".");
	if (pos != -1 && dec != null && dec > -1) {
		if (dec == 0) { //如果dec为0，返回整数
			curValue = formatround(curValue.substring(0, pos), 0);

			if (curValue.indexOf(".") != -1) {
				curValue = curValue.substring(0, curValue.indexOf("."));
			}
		} else {
			var decBit = curValue.substring(pos); //小数位
			var bitMis = dec - decBit.length; //dec与小数位的差值
			if (bitMis > 0) {
				while (bitMis > 0) {
					curValue = curValue + "0";
					bitMis--;
				}
			} else if (bitMis < 0) {
				curValue = formatround(curValue, dec);
			}
		}
	}

	return sufTag + curValue;
}
//======================================================



//======================================================
// 借款人地区管理
//======================================================

var provs = new Array();//省级地区
var towns = new Array();//市级地区
var countrys = new Array();//县级地区
var errorArea = new Array();//错误地区信息

/**
 * 借款人地区对象
 * @param areaCode 地区代码
 * @param areaName 地区名称
 * @param areaLevel 地区级别 1：省 2：市 3：县
 **/
function Area(areaCode, areaName, areaLevel) {
  //分级别存储于不同的数组中
  if(areaLevel == 1) {
    provs[provs.length++] = new Array(areaCode, areaName);
  } else if(areaLevel == 2) {
    towns[towns.length++] = new Array(areaCode, areaName);
  } else if(areaLevel == 3) {
    countrys[countrys.length++] = new Array(areaCode, areaName);
  } else {
    //放入错误的地区数组
    errorArea[errorArea.length++] = new Array(areaCode, areaName, areaLevel);
  } 
}

/**
 * 设置省编码
 * @param provName 代表"省"的标签名称
 */
function setProv(provName, defvalue) {
    var provObj = document.getElementById(provName);
    var defaultSign = '';
    if(defvalue != undefined)
    	defaultSign = defvalue.substring(0, 2 );

    //构造省数据项
    for(var i = 0; i < provs.length; i++) {
      var curProv = provs[i];
      //新标签对象
      var optObj = document.createElement("OPTION");
      optObj.value = curProv[0];
      optObj.innerText = curProv[1];

     	if( defaultSign != '' &&  curProv[0].indexOf(defaultSign) == 0) {
      	optObj.selected = 'selected';
      }
      //添加到省标签下
      provObj.appendChild(optObj);
    }

    //集中显示错误地区编码
    var error = "";
    for(var i = 0; i < errorArea.length; i++) {
      error += "=>"+ errorArea[i][0] +","+ errorArea[i][1] +","+ errorArea[i][2];
      if(i == errorArea.length-1) {
        alert("错误级次地区："+error);
      }
    }
}

/**
 * 省到市的关联
 * @param provName 代表"省"的标签名称
 * @param townName 代表"市"的标签名称
 */
function provTOtown(provName, townName, countryName, defvalue) {
  var provObj = document.getElementById(provName);
  var townObj = document.getElementById(townName);
  var countryObj = document.getElementById(countryName);

  //清除市的地区编码
  _removeChild(townObj);
  //清除县的地区编码
  _removeChild(countryObj);
  _appendOpt(provObj.value, townObj, towns, 2,defvalue,4);
  if( townObj.options.length == 0 ){
      var optObj = document.createElement("OPTION");
      optObj.value = provObj.value;
     	optObj.innerText = (provObj.options( provObj.selectedIndex  )).innerText;

      //添加到市标签下
      townObj.appendChild(optObj);
  }

  //进行市到县的关联
  townTOcountry(townName, countryName,defvalue);
}

/**
 * 市到县的关联
 * @param townName 代表"市"的标签名称
 * @param countryName 代表"县"的标签名称
 */
function townTOcountry(townName, countryName,defvalue) {
  var townObj = document.getElementById(townName);
  var countryObj = document.getElementById(countryName);

  //清除县的编码
  _removeChild(countryObj);
  _appendOpt(townObj.value, countryObj, countrys, 4,defvalue,6);
  if( countryObj.options.length == 0 ) {
      var optObj = document.createElement("OPTION");
      optObj.value = townObj.value;
      optObj.innerText = (townObj.options( townObj.selectedIndex )).innerText;

      //添加到市标签下
      countryObj.appendChild(optObj);
  }

}

/**
 * 添加选择项
 * @param retValue     主关联项值
 * @param parentObj    需添加选择项的对象
 * @param parentArray  需添加选择项的值数组
 * @param characterLeg 特征值长度
 */
function _appendOpt(retValue, parentObj, parentArray, characterLeg, defvalue, deflen) {
  if(retValue.length == 0) return;

  //根据省代码关联到市的地区编码
  for(var i = 0; i < parentArray.length; i++) {
    var curParent = parentArray[i];
    var sign = retValue.substring(0, characterLeg);//省标志码:省前四位编码
    var defaultSign = '';
    if(defvalue != undefined)
    	defaultSign = defvalue.substring(0, deflen );
    
    //根据代码过滤出满足条件的市
    if(curParent[0].indexOf(sign) == 0) {
      //构造市数据项
      var optObj = document.createElement("OPTION");
      optObj.value = curParent[0];
      optObj.innerText = curParent[1];
      if( defaultSign != '' &&  curParent[0].indexOf(defaultSign) == 0) {
      	  optObj.selected = 'selected';
      }
      //添加到市标签下
      parentObj.appendChild(optObj);
    }
  }
}


/**
 * 删除SELECT选择框的子节点
 * 如果第一个子节点的value为空字符,则保留第一个子节点，否则全部删除
 * @param obj SELECT选择框对象
 */
function _removeChild(obj) {
  
  if(obj != undefined) {
    var objChild = obj.children;
    var valueChildSN = 0;//带值子节点序号
    if(objChild.length > 0 && objChild[0].value == "") {
      valueChildSN = 1;
    }
    while(objChild.length != valueChildSN) {
      obj.removeChild(objChild[valueChildSN]);
    }
  }
}
 
//======================================================
