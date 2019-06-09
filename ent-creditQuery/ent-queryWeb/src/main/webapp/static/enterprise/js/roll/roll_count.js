/******************************************************************
	Author:       
	Create Date : 2004-04-25
	Function    : \u7ffb\u9875\u63a7\u4ef6

	Explain			:	\u6d89\u53ca\u53c2\u6570:1.\u5f53\u524d\u9875\uff0c\u7f3a\u7701\u503c\u4e3a0\uff0c\u5373\u6ca1\u6709\u9875			_CurPage     
	              				 2.\u603b\u9875\u6570    												_PageCount     
	              				 3.\u6bcf\u9875\u8bb0\u5f55\u6570\uff0c\u7f3a\u7701\u503c\u4e3a5						_PageSize      
	              				 4.\u603b\u8bb0\u5f55\u6570													_TotalCount    
	
	Example			\uff1a\u8bf7\u53c2\u7167 rolldemo.htm
	
 ******************************************************************/




//----------------------------------------------------/
// \u5168\u5c40\u53d8\u91cf
//----------------------------------------------------/
//\u67e5\u8be2\u6761\u4ef6
var _WhereClause = "";
//\u5f53\u524d\u9875\uff0c\u7f3a\u7701\u503c\u4e3a0\uff0c\u5373\u6ca1\u6709\u9875
var _CurPage = 0;
//\u603b\u9875\u6570
var _PageCount = 0;
//3.\u6bcf\u9875\u8bb0\u5f55\u6570\uff0c\u7f3a\u7701\u503c\u4e3a5
var _PageSize = 5;
//\u603b\u8bb0\u5f55\u6570
var _TotalCount = 0;
//----------------------------------------------------/

/**
 * \u663e\u793a\u7ffb\u9875\u63a7\u4ef6
 * @param  _canSearch   \u662f\u5426\u5141\u8bb8\u67e5\u8be2
 *				 _currentPage \u5f53\u524d\u9875\u7801
 *         _pageCount   \u603b\u9875\u7801
 *				 _pageSize		\u6bcf\u9875\u8bb0\u5f55\u6570
 * 				 _totalCount	\u603b\u8bb0\u5f55\u6570
 *				 _updateNum   \u66f4\u65b0\u6570\u636e\u65f6\u5019\u53d8\u52a8\u7684\u6570\u636e\u91cf(\u65b0\u589e\u4e3a1,\u5220\u9664\u4e3a-1)
 */				
function showRoll(_taskID, _canSearch, _whereClause, _currentPage, _pageCount, _pageSize, _totalCount, _updateNum) {
	if(parent != null && parent._ROLLWS != null) {
		//\u67e5\u8be2\u6761\u4ef6
		if(_whereClause != null && _whereClause != "undefined") {
			parent._ROLLWS._WhereClause = _whereClause;
		} 
		if(parent._ROLLWS._WhereClause != null) {
			_WhereClause = parent._ROLLWS._WhereClause;
		}
		
		//\u5f53\u524d\u9875
		if(_currentPage != null && _currentPage != "undefined") {
			parent._ROLLWS._CurPage = _currentPage;
		} 
		if(parent._ROLLWS._CurPage != null) {
			_CurPage = parent._ROLLWS._CurPage;
		}
		
		
		//\u603b\u9875\u6570
		if(_pageCount != null && _pageCount != "undefined") {
			parent._ROLLWS._PageCount = _pageCount;
		} 
		if(parent._ROLLWS._PageCount != null) {
			_PageCount = parent._ROLLWS._PageCount;
		}
		
		//\u6bcf\u9875\u8bb0\u5f55\u6570\uff0c\u7f3a\u7701\u503c\u4e3a5
		if(_pageSize != null && _pageSize != "undefined") {
			parent._ROLLWS._PageSize = _pageSize;
		}
		if(parent._ROLLWS._PageSize != null) {
			_PageSize = parent._ROLLWS._PageSize;
		}
		
		//\u603b\u8bb0\u5f55\u6570
		if(_totalCount != null && _totalCount != "undefined") {
			parent._ROLLWS._TotalCount = _totalCount;
		}
		if(parent._ROLLWS._TotalCount != null) {
			//\u5982\u679c\u6709\u66f4\u65b0\u6570\u636e\u91cf
			if(_updateNum != "undefined" && _updateNum != null) parent._ROLLWS._TotalCount = parseInt(parent._ROLLWS._TotalCount) + parseInt(_updateNum);
			_TotalCount = parent._ROLLWS._TotalCount;
		}
	} else {
		_WhereClause = _whereClause;
		_CurPage =  _currentPage;
		_PageCount =  _pageCount;
		_PageSize = _pageSize;
		_TotalCount = _totalCount;
	}
	
	//\u663e\u793a\u7ffb\u9875\u63a7\u4ef6\u7684TAG\u5b57\u7b26
	var str = "";
  
	//\u8ba1\u7b97\u5f53\u524d\u9875\u7684\u8bb0\u5f55\u8d77\u59cb\u5e8f\u53f7
	var _snArray = _getSN(_currentPage, _pageCount, _pageSize, _totalCount);
	
	str += "\t <table border='0' width='700' height='16' align='right' style='font-size: 12px;'> \r\n";
	str += "\t\t <tr align='right' valign='bottom'> \r\n";
	str += "\t\t\t <td class='pageText'>("+_snArray[0]+"-"+_snArray[1]+")</td><td>/</td><td class='pageText'>\u5171"+_TotalCount+"</td>	<td class='pageText' id='navigatestatus' nowrap>\u7b2c"+_CurPage+"\u9875/\u5171"+_PageCount+"\u9875&nbsp \r\n";
	str += "\t\t\t </td> \r\n";
	str += "\t\t\t <td align='left'>&nbsp;&nbsp;<a onclick='javascript:pagefirst("+_CurPage+", "+_PageCount+")'><label align='middle' style='cursor:hand;color:#000000' alt='\u9996\u9875'>\u9996&nbsp;\u9875</label></a> \r\n";
	str += "\t\t\t\t <a onclick='javascript:pageprevious("+_CurPage+", "+_PageCount+")'><label align='middle' style='cursor:hand;color:#000000' alt='\u4e0a\u4e00\u9875'>\u4e0a\u4e00\u9875</label></a> \r\n";
	str += "\t\t\t\t <a onclick='javascript:pagenext("+_CurPage+", "+_PageCount+")'><label style='cursor:hand;color:#000000' align='middle' alt='\u4e0b\u4e00\u9875'>\u4e0b\u4e00\u9875</label></a> \r\n";
	str += "\t\t\t\t <a onclick='javascript: pagelast("+_CurPage+", "+_PageCount+");'><label style='cursor:hand;color:#000000' align='middle' alt='\u5c3e\u9875'>\u5c3e&nbsp;\u9875</label></a> \r\n";
	
	//\u62fc\u63a5\u9875\u7801
	var curPageStr = "<option value=''>-</option>";
	var selectStr = "";
	
	str += "\t\t\t\t &nbsp;&nbsp;&nbsp;\u8df3\u8f6c\u81f3\uff1a<input type='text' style='width: 60' name='somePage' maxlength='7' class='input-text'><input type='button' name='Go' value='Go' class='input-button' onclick='javascript:goCentainPage( document.getElementById(\"somePage\").value , "+_CurPage+", "+_PageCount+")'> \r\n";
	
	//\u662f\u5426\u5141\u8bb8\u67e5\u8be2\uff0c\u5982\u679c\u5141\u8bb8\uff0c\u663e\u793a\u67e5\u8be2\u56fe\u6807
	if(_canSearch == "true") {
		str += "\t\t\t\t <a href='javascript:search()'><img src='/InfoServiceWebProject/images/roll/query.gif' style='cursor:hand'  alt='\u67e5\u8be2'  align='absmiddle'  border='0'></a> \r\n";
	}
	str += "\t\t\t </td> \r\n";
	str += "\t\t </tr> \r\n";
	str += "\t </table> \r\n";
	
	document.write(str);
	document.body.insertAdjacentHTML("AfterBegin", "<form id='rollForm' name='rollForm' method='post'><input type='hidden' name='whereClause' value=\""+_WhereClause+"\"><input type='hidden' name='currentPage' value='"+_CurPage+"'><input type='hidden' name='PageCount' value='"+_PageCount+"'><input type='hidden' name='pageSize' value='"+_PageSize+"'><input type='hidden' name='totalCount' value='"+_TotalCount+"'><input type='hidden' name='_RollTaskID' value='"+_taskID+"'></form>");
	
}


/**
 * \u83b7\u53d6\u5f53\u524d\u8bb0\u5f55\u5e8f\u53f7
 * @return array array[0]:\u5f53\u524d\u8bb0\u5f55\u5e8f\u53f7\u8d77\uff1barray[1]:\u5f53\u524d\u5e8f\u53f7\u6b62
 */
function _getSN(currentPage, pageCount, pageSize, totalCount) {
	//\u8bb0\u5f55\u5e8f\u53f7\u6570\u7ec4
	var snArray = new Array();
	
	var _strsn = -1;//\u8d77\u59cb\u5e8f\u53f7
	var _endsn = totalCount;//\u622a\u6b62\u5e8f\u53f7

	if(currentPage != 0) {
  	var _abssn = currentPage * pageSize;//\u7edd\u5bf9\u7ec8\u6b62\u5e8f\u53f7
  	
  	//\u5982\u679c\u5f53\u524d\u9875\u6570\u4e0d\u7b49\u4e8e\u603b\u9875\u6570
  	if(currentPage != pageCount) {
  		_endsn = _abssn;
  	}
  	_strsn = (_abssn + 1) - pageSize;
  } else {
  	_strsn = 0;
	} 
	
	snArray[0] = _strsn;
	snArray[1] = _endsn;
	return snArray;
}

//\u89e6\u53d1\u7ffb\u9875\u63a7\u4ef6\u4e2d\u7684"\u9996\u9875"\u6309\u94ae
function pagefirst(curPage, pageCount)  {
	if( parseInt(pageCount) == 0 )  { 
		alert( "\u4e0d\u80fd\u7ffb\u9875!" ) ;
		return ;
	}
	if( parseInt(curPage) == 1 )  { 
		alert( "\u8fd9\u5df2\u7ecf\u662f\u9996\u9875!" ) ;
 		return ;
	}
	document.all("currentPage").value = 1;
	execRollAction();
}
	
	
//\u89e6\u53d1\u7ffb\u9875\u63a7\u4ef6\u4e2d\u7684"\u4e0a\u4e00\u9875"\u6309\u94ae
function pageprevious(curPage, pageCount)  {
	if( parseInt(pageCount) == 0 )  { 
		alert( "\u4e0d\u80fd\u7ffb\u9875!" ) ;
		return ;
	}
	if( parseInt(curPage) == 1 )  { 
		alert( "\u8fd9\u5df2\u7ecf\u662f\u9996\u9875!" ) ;
 		return ;
	}
	document.all("currentPage").value =  parseInt(curPage) - 1;
	execRollAction();
}

//\u89e6\u53d1\u7ffb\u9875\u63a7\u4ef6\u4e2d\u7684"\u540e\u4e00\u9875"\u6309\u94ae
function pagenext(curPage, pageCount)  {
	if( parseInt(pageCount) == 0)  { 
		alert( "\u4e0d\u80fd\u7ffb\u9875!" ) ;
		return ;
	}
	if( parseInt(curPage) >= parseInt(pageCount) )  { 
		alert( "\u8fd9\u5df2\u7ecf\u662f\u5c3e\u9875!" ) ;
		return ;
	}
	document.all("currentPage").value = parseInt(curPage) + 1;
	execRollAction();
}

//\u89e6\u53d1\u7ffb\u9875\u63a7\u4ef6\u4e2d\u7684"\u6700\u540e\u4e00\u9875"\u6309\u94ae
function pagelast(curPage, pageCount)  {
	if( parseInt(pageCount) == 0 )  { 
		alert( "\u4e0d\u80fd\u7ffb\u9875!" ) ;
		return ;
	}
	if( parseInt(curPage) >= parseInt(pageCount) )  { 
		alert( "\u8fd9\u5df2\u7ecf\u662f\u5c3e\u9875!" ) ;
	return ;
	}
	document.all("currentPage").value = pageCount;
	execRollAction();
}

//\u89e6\u53d1\u7ffb\u9875\u63a7\u4ef6\u4e2d\u7684"\u8df3\u8f6c\u5230"\u6309\u94ae
function goCentainPage( p, curPage, pageCount )  {
	
	if( parseInt(pageCount) == 0 )  { 
		alert( "\u4e0d\u80fd\u7ffb\u9875!" ) ;
		return ;
	}
	
	if (p=="")      {
		alert("\u8bf7\u8f93\u5165\u8df3\u8f6c\u7684\u9875\u6570!");
		document.getElementById("somePage").focus();
		return;	
	}
		//p=""+curPage;
	
	//\u5224\u65ad\u8f93\u5165\u9875\u7801\u6709\u6548\u6027
	var pInt = parseInt(p);
	if(isNaN(pInt)) {
		alert("\u8bf7\u8f93\u5165\u6570\u5b57!");
		//document.getElementById("somePage").select();
		document.getElementById("somePage").focus();
		return;	
	}
	
 	if( parseInt(curPage) == parseInt(p) )  { 
		alert( "\u8fd9\u5df2\u7ecf\u662f\u60a8\u7533\u8bf7\u7684\u9875!" ) ;
		//document.getElementById("somePage").select();
		document.getElementById("somePage").focus();
		return;
	}

	if( 1 > parseInt(p) || parseInt(pageCount) < parseInt(p) )  { 
		alert( "\u60a8\u7533\u8bf7\u7684\u9875\u8d85\u8fc7\u4e86\u8303\u56f4!" ) ;
		//document.getElementById("somePage").select();
		document.getElementById("somePage").focus();
		return ;
	}
	
	if (!checkint(p)) {
	  alert("\u60a8\u8981\u8c03\u8f6c\u7684\u9875\u7801\u5fc5\u987b\u662f\u6570\u5b57!");
	  document.getElementById("somePage").focus();
	  return;
	}
	
	document.all("currentPage").value = p;
	execRollAction();

}

//\u6267\u884cRoll\u7684\u63d0\u4ea4
function execRollAction() {
	var mainForm = document.all("rollForm");

	mainForm.action = mainForm._RollTaskID.value;
	mainForm.submit();

}


/**
 * \u6821\u9a8c\u6570\u636e\u662f\u5426\u4e3a\u6574\u6570
 * @param data \u8981\u6821\u9a8c\u7684\u6570\u636e
 */
function checkint(data)
{
   if (data == "") return true;
    var validChar = "0123456789";
    if (isCharsInBag(data, validChar))
       return true;
    return false;
}

/**
 * \u6821\u9a8c\u5b57\u7b26\u4e32\u4e2d\u5305\u62ec\u7684\u5b57\u7b26\u662f\u5426\u5728\u6307\u5b9a\u7684\u5b57\u7b26\u4e32\u5305\u62ec\u7684\u5b57\u7b26\u4e4b\u4e2d
 * @param  s :\u88ab\u6821\u9a8c\u7684\u5b57\u7b26\u4e32
 * @param  bag :\u8303\u56f4\u5b57\u7b26\u4e32
 */
function isCharsInBag (s, bag)
{
  var i;
  for (i = 0; i < s.length; i++)
  {
      var c = s.charAt(i);
      if (bag.indexOf(c) == -1) return false;
  }
  return true;
}
