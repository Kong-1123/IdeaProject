/******************************************************************
	Author:       
	Create Date : 2004-04-25
	Function    : 翻页控件

	Explain			:	涉及参数:1.当前页，缺省值为0，即没有页			_CurPage     
	              				 2.总页数    												_PageCount     
	              				 3.每页记录数，缺省值为5						_PageSize      
	              				 4.总记录数													_TotalCount    
	
	Example			：请参照 rolldemo.htm
	
 ******************************************************************/




//----------------------------------------------------/
// 全局变量
//----------------------------------------------------/
//查询条件
var _WhereClause = "";
//当前页，缺省值为0，即没有页
var _CurPage = 0;
//总页数
var _PageCount = 0;
//3.每页记录数，缺省值为5
var _PageSize = 5;
//总记录数
var _TotalCount = 0;
//----------------------------------------------------/

/**
 * 显示翻页控件
 * @param  _canSearch   是否允许查询
 *				 _currentPage 当前页码
 *         _pageCount   总页码
 *				 _pageSize		每页记录数
 * 				 _totalCount	总记录数
 *				 _updateNum   更新数据时候变动的数据量(新增为1,删除为-1)
 */				
function showRoll(_taskID, _canSearch, _whereClause, _currentPage, _pageCount, _pageSize, _totalCount, _updateNum) {
	
	if(parent != null && parent._ROLLWS != null) {
		//查询条件
		if(_whereClause != null && _whereClause != "undefined") {
			parent._ROLLWS._WhereClause = _whereClause;
		} 
		if(parent._ROLLWS._WhereClause != null) {
			_WhereClause = parent._ROLLWS._WhereClause;
		}
		
		//当前页
		if(_currentPage != null && _currentPage != "undefined") {
			parent._ROLLWS._CurPage = _currentPage;
		} 
		if(parent._ROLLWS._CurPage != null) {
			_CurPage = parent._ROLLWS._CurPage;
		}
		
		
		//总页数
		if(_pageCount != null && _pageCount != "undefined") {
			parent._ROLLWS._PageCount = _pageCount;
		} 
		if(parent._ROLLWS._PageCount != null) {
			_PageCount = parent._ROLLWS._PageCount;
		}
		
		//每页记录数，缺省值为5
		if(_pageSize != null && _pageSize != "undefined") {
			parent._ROLLWS._PageSize = _pageSize;
		}
		if(parent._ROLLWS._PageSize != null) {
			_PageSize = parent._ROLLWS._PageSize;
		}
		
		//总记录数
		if(_totalCount != null && _totalCount != "undefined") {
			parent._ROLLWS._TotalCount = _totalCount;
		}
		if(parent._ROLLWS._TotalCount != null) {
			//如果有更新数据量
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
	
	//显示翻页控件的TAG字符
	var str = "";
  
	//计算当前页的记录起始序号
	var _snArray = _getSN(_currentPage, _pageCount, _pageSize, _totalCount);
	
	str += "\t <table border='0' width='600' height='16' align='right' style='font-size: 12px;'> \r\n";
	str += "\t\t <tr align='right' valign='bottom'> \r\n";
	str += "\t\t\t <td class='pageText'>("+_snArray[0]+"-"+_snArray[1]+")</td><td>/</td><td class='pageText'>共"+_TotalCount+"</td>	<td class='pageText' id='navigatestatus' nowrap>第"+_CurPage+"页/共"+_PageCount+"页&nbsp \r\n";
	str += "\t\t\t </td> \r\n";
	str += "\t\t\t <td align='left'>&nbsp;&nbsp;<a onclick='javascript:pagefirst("+_CurPage+", "+_PageCount+")'><label align='middle' style='cursor:hand;color:#000000' alt='首页'>首&nbsp;页</label></a> \r\n";
	str += "\t\t\t\t <a onclick='javascript:pageprevious("+_CurPage+", "+_PageCount+")'><label align='middle' style='cursor:hand;color:#000000' alt='上一页'>上一页</label></a> \r\n";
	str += "\t\t\t\t <a onclick='javascript:pagenext("+_CurPage+", "+_PageCount+")'><label style='cursor:hand;color:#000000' align='middle' alt='下一页'>下一页</label></a> \r\n";
	str += "\t\t\t\t <a onclick='javascript: pagelast("+_CurPage+", "+_PageCount+");'><label style='cursor:hand;color:#000000' align='middle' alt='尾页'>尾&nbsp;页</label></a> \r\n";
	
	//拼接页码
	var curPageStr = "<option value=''>-</option>";
	var selectStr = "";
	for(var i = 1; i < new Number(_PageCount)+1; i++) {
	  if(i == new Number(_currentPage)) {
	    selectStr = "selected";
	  } else {
	    selectStr = "";  
	  }
	  
		if(i == 1){
			//覆盖默认值
			curPageStr = "<option value='1' "+selectStr+">1</option>";
		} else {
			curPageStr += "<option value='"+i+"' "+selectStr+">"+i+"</option>";
		}
	}
	
	str += "\t\t\t\t &nbsp;&nbsp;&nbsp;跳转至： <select name='somePage' onchange='javascript:goCentainPage( this.value , "+_CurPage+", "+_PageCount+")'>"+curPageStr+"</select> \r\n";
	
	//是否允许查询，如果允许，显示查询图标
	if(_canSearch == "true") {
		str += "\t\t\t\t <a href='javascript:search()'><img src='/InfoServiceWebProject/images/roll/query.gif' style='cursor:hand'  alt='查询'  align='absmiddle'  border='0'></a> \r\n";
	}
	str += "\t\t\t </td> \r\n";
	str += "\t\t </tr> \r\n";
	str += "\t </table> \r\n";
	
	document.write(str);
	document.body.insertAdjacentHTML("AfterBegin", "<form id='rollForm' name='rollForm' method='post'><input type='hidden' name='whereClause' value=\""+_WhereClause+"\"><input type='hidden' name='currentPage' value='"+_CurPage+"'><input type='hidden' name='PageCount' value='"+_PageCount+"'><input type='hidden' name='pageSize' value='"+_PageSize+"'><input type='hidden' name='totalCount' value='"+_TotalCount+"'><input type='hidden' name='_RollTaskID' value='"+_taskID+"'></form>");
}


/**
 * 获取当前记录序号
 * @return array array[0]:当前记录序号起；array[1]:当前序号止
 */
function _getSN(currentPage, pageCount, pageSize, totalCount) {
	//记录序号数组
	var snArray = new Array();
	
	var _strsn = -1;//起始序号
	var _endsn = totalCount;//截止序号

	if(currentPage != 0) {
  	var _abssn = currentPage * pageSize;//绝对终止序号
  	
  	//如果当前页数不等于总页数
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

//触发翻页控件中的"首页"按钮
function pagefirst(curPage, pageCount)  {
	if( parseInt(pageCount) == 0 )  { 
		alert( "不能翻页!" ) ;
		return ;
	}
	if( parseInt(curPage) == 1 )  { 
		alert( "这已经是首页!" ) ;
 		return ;
	}
	document.all("currentPage").value = 1;
	execRollAction();
}
	
	
//触发翻页控件中的"上一页"按钮
function pageprevious(curPage, pageCount)  {
	if( parseInt(pageCount) == 0 )  { 
		alert( "不能翻页!" ) ;
		return ;
	}
	if( parseInt(curPage) == 1 )  { 
		alert( "这已经是首页!" ) ;
 		return ;
	}
	document.all("currentPage").value =  parseInt(curPage) - 1;
	execRollAction();
}

//触发翻页控件中的"后一页"按钮
function pagenext(curPage, pageCount)  {
	if( parseInt(pageCount) == 0)  { 
		alert( "不能翻页!" ) ;
		return ;
	}
	if( parseInt(curPage) >= parseInt(pageCount) )  { 
		alert( "这已经是尾页!" ) ;
		return ;
	}
	document.all("currentPage").value = parseInt(curPage) + 1;
	execRollAction();
}

//触发翻页控件中的"最后一页"按钮
function pagelast(curPage, pageCount)  {
	if( parseInt(pageCount) == 0 )  { 
		alert( "不能翻页!" ) ;
		return ;
	}
	if( parseInt(curPage) >= parseInt(pageCount) )  { 
		alert( "这已经是尾页!" ) ;
	return ;
	}
	document.all("currentPage").value = pageCount;
	execRollAction();
}

//触发翻页控件中的"跳转到"按钮
function goCentainPage( p, curPage, pageCount )  {
	
	if( parseInt(pageCount) == 0 )  { 
		alert( "不能翻页!" ) ;
		return ;
	}
	
	if (p=="")      
		p=""+curPage;
	
	//判断输入页码有效性
	var pInt = parseInt(p);
	if(isNaN(pInt)) {
		alert("请输入数字!");
		//document.getElementById("somePage").select();
		document.getElementById("somePage").focus();
		return;	
	}
	
 	if( parseInt(curPage) == parseInt(p) )  { 
		alert( "这已经是您申请的页!" ) ;
		//document.getElementById("somePage").select();
		document.getElementById("somePage").focus();
		return;
	}

	if( 1 > parseInt(p) || parseInt(pageCount) < parseInt(p) )  { 
		alert( "您申请的页超过了范围!" ) ;
		//document.getElementById("somePage").select();
		document.getElementById("somePage").focus();
		return ;
	}
	document.all("currentPage").value = p;
	execRollAction();

}

//执行Roll的提交
function execRollAction() {
	var mainForm = document.all("rollForm");

	mainForm.action = mainForm._RollTaskID.value;
	mainForm.submit();

}