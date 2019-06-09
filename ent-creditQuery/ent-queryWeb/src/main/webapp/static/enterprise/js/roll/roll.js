/******************************************************************
	Author:       
	Create Date : 2004-04-25
	Function    : ��ҳ�ؼ�

	Explain			:	�漰����:1.��ǰҳ��ȱʡֵΪ0����û��ҳ			_CurPage     
	              				 2.��ҳ��    												_PageCount     
	              				 3.ÿҳ��¼����ȱʡֵΪ5						_PageSize      
	              				 4.�ܼ�¼��													_TotalCount    
	
	Example			������� rolldemo.htm
	
 ******************************************************************/




//----------------------------------------------------/
// ȫ�ֱ���
//----------------------------------------------------/
//��ѯ����
var _WhereClause = "";
//��ǰҳ��ȱʡֵΪ0����û��ҳ
var _CurPage = 0;
//��ҳ��
var _PageCount = 0;
//3.ÿҳ��¼����ȱʡֵΪ5
var _PageSize = 5;
//�ܼ�¼��
var _TotalCount = 0;
//----------------------------------------------------/

/**
 * ��ʾ��ҳ�ؼ�
 * @param  _canSearch   �Ƿ������ѯ
 *				 _currentPage ��ǰҳ��
 *         _pageCount   ��ҳ��
 *				 _pageSize		ÿҳ��¼��
 * 				 _totalCount	�ܼ�¼��
 *				 _updateNum   ��������ʱ��䶯��������(����Ϊ1,ɾ��Ϊ-1)
 */				
function showRoll(_taskID, _canSearch, _whereClause, _currentPage, _pageCount, _pageSize, _totalCount, _updateNum) {
	
	if(parent != null && parent._ROLLWS != null) {
		//��ѯ����
		if(_whereClause != null && _whereClause != "undefined") {
			parent._ROLLWS._WhereClause = _whereClause;
		} 
		if(parent._ROLLWS._WhereClause != null) {
			_WhereClause = parent._ROLLWS._WhereClause;
		}
		
		//��ǰҳ
		if(_currentPage != null && _currentPage != "undefined") {
			parent._ROLLWS._CurPage = _currentPage;
		} 
		if(parent._ROLLWS._CurPage != null) {
			_CurPage = parent._ROLLWS._CurPage;
		}
		
		
		//��ҳ��
		if(_pageCount != null && _pageCount != "undefined") {
			parent._ROLLWS._PageCount = _pageCount;
		} 
		if(parent._ROLLWS._PageCount != null) {
			_PageCount = parent._ROLLWS._PageCount;
		}
		
		//ÿҳ��¼����ȱʡֵΪ5
		if(_pageSize != null && _pageSize != "undefined") {
			parent._ROLLWS._PageSize = _pageSize;
		}
		if(parent._ROLLWS._PageSize != null) {
			_PageSize = parent._ROLLWS._PageSize;
		}
		
		//�ܼ�¼��
		if(_totalCount != null && _totalCount != "undefined") {
			parent._ROLLWS._TotalCount = _totalCount;
		}
		if(parent._ROLLWS._TotalCount != null) {
			//����и���������
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
	
	//��ʾ��ҳ�ؼ���TAG�ַ�
	var str = "";
  
	//���㵱ǰҳ�ļ�¼��ʼ���
	var _snArray = _getSN(_currentPage, _pageCount, _pageSize, _totalCount);
	
	str += "\t <table border='0' width='600' height='16' align='right' style='font-size: 12px;'> \r\n";
	str += "\t\t <tr align='right' valign='bottom'> \r\n";
	str += "\t\t\t <td class='pageText'>("+_snArray[0]+"-"+_snArray[1]+")</td><td>/</td><td class='pageText'>��"+_TotalCount+"</td>	<td class='pageText' id='navigatestatus' nowrap>��"+_CurPage+"ҳ/��"+_PageCount+"ҳ&nbsp \r\n";
	str += "\t\t\t </td> \r\n";
	str += "\t\t\t <td align='left'>&nbsp;&nbsp;<a onclick='javascript:pagefirst("+_CurPage+", "+_PageCount+")'><label align='middle' style='cursor:hand;color:#000000' alt='��ҳ'>��&nbsp;ҳ</label></a> \r\n";
	str += "\t\t\t\t <a onclick='javascript:pageprevious("+_CurPage+", "+_PageCount+")'><label align='middle' style='cursor:hand;color:#000000' alt='��һҳ'>��һҳ</label></a> \r\n";
	str += "\t\t\t\t <a onclick='javascript:pagenext("+_CurPage+", "+_PageCount+")'><label style='cursor:hand;color:#000000' align='middle' alt='��һҳ'>��һҳ</label></a> \r\n";
	str += "\t\t\t\t <a onclick='javascript: pagelast("+_CurPage+", "+_PageCount+");'><label style='cursor:hand;color:#000000' align='middle' alt='βҳ'>β&nbsp;ҳ</label></a> \r\n";
	
	//ƴ��ҳ��
	var curPageStr = "<option value=''>-</option>";
	var selectStr = "";
	for(var i = 1; i < new Number(_PageCount)+1; i++) {
	  if(i == new Number(_currentPage)) {
	    selectStr = "selected";
	  } else {
	    selectStr = "";  
	  }
	  
		if(i == 1){
			//����Ĭ��ֵ
			curPageStr = "<option value='1' "+selectStr+">1</option>";
		} else {
			curPageStr += "<option value='"+i+"' "+selectStr+">"+i+"</option>";
		}
	}
	
	str += "\t\t\t\t &nbsp;&nbsp;&nbsp;��ת���� <select name='somePage' onchange='javascript:goCentainPage( this.value , "+_CurPage+", "+_PageCount+")'>"+curPageStr+"</select> \r\n";
	
	//�Ƿ������ѯ�����������ʾ��ѯͼ��
	if(_canSearch == "true") {
		str += "\t\t\t\t <a href='javascript:search()'><img src='/InfoServiceWebProject/images/roll/query.gif' style='cursor:hand'  alt='��ѯ'  align='absmiddle'  border='0'></a> \r\n";
	}
	str += "\t\t\t </td> \r\n";
	str += "\t\t </tr> \r\n";
	str += "\t </table> \r\n";
	
	document.write(str);
	document.body.insertAdjacentHTML("AfterBegin", "<form id='rollForm' name='rollForm' method='post'><input type='hidden' name='whereClause' value=\""+_WhereClause+"\"><input type='hidden' name='currentPage' value='"+_CurPage+"'><input type='hidden' name='PageCount' value='"+_PageCount+"'><input type='hidden' name='pageSize' value='"+_PageSize+"'><input type='hidden' name='totalCount' value='"+_TotalCount+"'><input type='hidden' name='_RollTaskID' value='"+_taskID+"'></form>");
}


/**
 * ��ȡ��ǰ��¼���
 * @return array array[0]:��ǰ��¼�����array[1]:��ǰ���ֹ
 */
function _getSN(currentPage, pageCount, pageSize, totalCount) {
	//��¼�������
	var snArray = new Array();
	
	var _strsn = -1;//��ʼ���
	var _endsn = totalCount;//��ֹ���

	if(currentPage != 0) {
  	var _abssn = currentPage * pageSize;//������ֹ���
  	
  	//�����ǰҳ����������ҳ��
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

//������ҳ�ؼ��е�"��ҳ"��ť
function pagefirst(curPage, pageCount)  {
	if( parseInt(pageCount) == 0 )  { 
		alert( "���ܷ�ҳ!" ) ;
		return ;
	}
	if( parseInt(curPage) == 1 )  { 
		alert( "���Ѿ�����ҳ!" ) ;
 		return ;
	}
	document.all("currentPage").value = 1;
	execRollAction();
}
	
	
//������ҳ�ؼ��е�"��һҳ"��ť
function pageprevious(curPage, pageCount)  {
	if( parseInt(pageCount) == 0 )  { 
		alert( "���ܷ�ҳ!" ) ;
		return ;
	}
	if( parseInt(curPage) == 1 )  { 
		alert( "���Ѿ�����ҳ!" ) ;
 		return ;
	}
	document.all("currentPage").value =  parseInt(curPage) - 1;
	execRollAction();
}

//������ҳ�ؼ��е�"��һҳ"��ť
function pagenext(curPage, pageCount)  {
	if( parseInt(pageCount) == 0)  { 
		alert( "���ܷ�ҳ!" ) ;
		return ;
	}
	if( parseInt(curPage) >= parseInt(pageCount) )  { 
		alert( "���Ѿ���βҳ!" ) ;
		return ;
	}
	document.all("currentPage").value = parseInt(curPage) + 1;
	execRollAction();
}

//������ҳ�ؼ��е�"���һҳ"��ť
function pagelast(curPage, pageCount)  {
	if( parseInt(pageCount) == 0 )  { 
		alert( "���ܷ�ҳ!" ) ;
		return ;
	}
	if( parseInt(curPage) >= parseInt(pageCount) )  { 
		alert( "���Ѿ���βҳ!" ) ;
	return ;
	}
	document.all("currentPage").value = pageCount;
	execRollAction();
}

//������ҳ�ؼ��е�"��ת��"��ť
function goCentainPage( p, curPage, pageCount )  {
	
	if( parseInt(pageCount) == 0 )  { 
		alert( "���ܷ�ҳ!" ) ;
		return ;
	}
	
	if (p=="")      
		p=""+curPage;
	
	//�ж�����ҳ����Ч��
	var pInt = parseInt(p);
	if(isNaN(pInt)) {
		alert("����������!");
		//document.getElementById("somePage").select();
		document.getElementById("somePage").focus();
		return;	
	}
	
 	if( parseInt(curPage) == parseInt(p) )  { 
		alert( "���Ѿ����������ҳ!" ) ;
		//document.getElementById("somePage").select();
		document.getElementById("somePage").focus();
		return;
	}

	if( 1 > parseInt(p) || parseInt(pageCount) < parseInt(p) )  { 
		alert( "�������ҳ�����˷�Χ!" ) ;
		//document.getElementById("somePage").select();
		document.getElementById("somePage").focus();
		return ;
	}
	document.all("currentPage").value = p;
	execRollAction();

}

//ִ��Roll���ύ
function execRollAction() {
	var mainForm = document.all("rollForm");

	mainForm.action = mainForm._RollTaskID.value;
	mainForm.submit();

}