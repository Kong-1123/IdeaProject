/******************************************************************
    Author:       
    Create Date : 2004-04-25
    Function    : 界面验证

    Explain            :    包括两种类型的页面验证方法:  
                                1. 页面请求访问的公共接口涉及的页面验证方法，但也可以进行单独使用；
                                2. 单独使用的页面验证方法；
    FunctionList：
             ________________________________________________________________________________________________
            |        sn       |         functionType               |                                name     |
            |______________________________________________________|_________________________________________|
            |        1        |                     public         |                        checkForm        |
            |                 |                                    |                        self_check       |
            |                 |                                    |                        ChkCoDate        |
            |                 |                                    |                        CompareDate      |
            |                 |                                    |                        checkNull        |
            |                 |                                    |                        checkLength      |
            |                 |                                    |                        ChkNsrCode       |
            |                 |                                    |                        ChkDigit         |
            |                 |                                    |                        isDigit          |
            |                 |                                    |                        isInt            |
            |                 |                                    |                        checkNumber      |
            |                 |                                    |                        checkDate        |
            |                 |                                    |                        checkDateInput   |
            |                 |                                    |                        ChkDTFormat      |
            |_________________|____________________________________|_________________________________________|
            |                 |                    private         |                       _getFieldProperty |
            |        2        |                                    |                        _checkTabButton  |
            |________________________________________________________________________________________________|
            |        3        |                    public          |                        isPostNum        |
            |                 |                                    |                        isEmail          |
            |                 |                                    |                        ChkTelCode       |
            |                 |                                    |                        isBetween        |
            |_________________|____________________________________|_________________________________________|
    
 ******************************************************************/
//******************************************************
// 输入验证的公共函数，为工具方法
//******************************************************
//验证指定form中的控件的输入值是否符合要求    
function checkForm( obj ) {
    //首先调取用户重载的方法
    if( !self_check() ) {
        return false;
    }    
    // 然后调用框架实现form所有输入项的检查。
    for(var i in obj.all)  {
		//检查tagName是否正确。
	    if(obj.all[i]==null||obj.all[i]=="undefined") continue;
		//检查范围只能是INPUT,text,SELECT,TEXTAREA
   	    if(obj.all[i].tagName!="INPUT" && obj.all[i].type!= "text" && obj.all[i].tagName!= "SELECT" && obj.all[i].tagName!= "TEXTAREA"){
		    continue;
	    }
        //获取该Tag对应的配置对象。如果没有，说明不是真正的配置对象，不能进行自动检查
        var _curObjProperty = _getFieldProperty(obj.all[i]);
		//不存在配置属性，那么就跳过，不需要检查。
        if(_curObjProperty == null ||_curObjProperty == "undefined" ) {
            continue;
        } 
		//以下检查 text input,要求该对象是激活的
        if ( obj.all[i].tagName == "INPUT" && (obj.all[i].type=="text") && !obj.all[i].disabled )  {
            curObj = obj.all[i] ;
			//检查是否必输项，notnull==true
            if (_curObjProperty.notnull == "true"){
                 if( checkNull( curObj , _curObjProperty.caption ) == false ) {
                      return false ;
				 }
			}
			if(curObj.value!=null&&curObj.value.length>0) {
				//如果是数字，那么检查取值范围和精度；如果是字符串，那么检查长度
	            if (_curObjProperty.type == "byte"||_curObjProperty.type == "int"||_curObjProperty.type == "long"||_curObjProperty.type == "float"||_curObjProperty.type == "double")  {
		            if( checkNumber( curObj , _curObjProperty.caption , _curObjProperty.min , _curObjProperty.max , _curObjProperty.precLen ) == false ){
			             return false ;
					}
				}
				else if(_curObjProperty.type == "String"||_curObjProperty.type == "java.lang.String"){
					if (checkLength(curObj , _curObjProperty.caption , _curObjProperty.minlen , _curObjProperty.maxlen)==false)
						return false;	
				}
	            //如果是日期控件，而且有日期值，还要进行验证格式是否正确
		        if(_curObjProperty.type== "java.util.Date" ) {
			        if(ChkDTFormat(curObj.value) == false) {
				      return false;
					}
				}
			}
          } 
		  //其余格式，只是检查是否为空
		  else if(_curObjProperty.notnull== "true") {
              if( checkNull( obj.all[i] , _curObjProperty.caption ) == false )
                  return false ;
          }
    }
    return true;
}

//======================================================
//可供重载的方法，该方法对页面进行整体控制，必须有返回
//======================================================
function self_check() {
    return true;
}
//======================================================

//------------------------------------------------------/
// 获取该Tag对应的配置对象。如果没有，说明不是真正的配置
// 对象，进行提示
//------------------------------------------------------/
function _getFieldProperty(curObj) {
	//在框架中使用id;在struts中使用name
	var itemName = curObj.name;
    //没有id和name，确定没有相应的配置，返回null
    if(itemName == null) {
        return null;
    }

	//alert("getFieldProperty:"+curObj.name);
	var _itemArray = null;
	//说明当前控件是单据的部分，应该取单据对应的VO配置文件来验证。
	if(itemName.indexOf('$')!=-1&& itemName.indexOf('_')!=-1) {
		//voucherName = itemName.substring(0,itemName.indexOf('_'));
		//itemName = itemName.substring(itemName.indexOf('_')+1,itemName.length-1)
		
		//因为字段可能存在"_",所以不能通过"_"来判断,现在通过"orm"来判断,所以子ActionForm类的名字必须为"××Form",此为暂时办法
		voucherName = itemName.substring(0,itemName.indexOf('Form_')+4);
		itemName = itemName.substring(itemName.indexOf('Form_')+5,itemName.length-1)
		while(voucherName.indexOf('$')!=-1)
			voucherName = voucherName.replace('$','.');
		_itemArray = parent.getVOITEMS(voucherName);
	}
	else {
		_itemArray = parent.getVOITEMS();
	}
	//以下取最近一层的VOITEMS,一共取三层，如果还取不到，就返回错误信息。
	
	
	//如果没有配置对象数组，确定是没有解析
    if(_itemArray == null) {
        alert("没有相应的配置信息，请检查配置文件!");
        return "undefined";
    }
    
    //获取对应属性的配置信息。
    for(var i = 0; i < _itemArray.length ; i++) {
        var _curItem = _itemArray[i];
		//alert(itemName+":"+_curItem.name);
        if(_curItem != null && _curItem.name == itemName) {
            return _curItem;
        }
    }
    return null;
}



//-----------------------检测整数,不对外---------------------------------------//
function isInt(theInt){

    if(theInt=="") {
        return true;
    } else {
        for(var i=0;i<theInt.length;i++){
            if(isDigit(theInt.charAt(i))==false){
                return false;
            }
        }
        return true;
    }
}

//        检测日期格式'YYYY-MM-DD',并比较,参数(Osdate)为开始日期，参数(Oedate)为终止日期。
//        用例：onblur="ChkCoDate(thisform.gsbeg,this)"
//----------------------检测日期格式,并比较----yyyy-mm-dd----------------------//
function ChkCoDate(Osdate,Oedate) {
  if(checkDateInput(Osdate)) {
    if(checkDateInput(Oedate)) {
      if(Osdate.value == "" || Oedate.value == "") return true;
  
      if(CompareDate(Osdate.value,Oedate.value)>=0) {
        return true;
      } else {
        alert("日期关系不正确!\n比如:终止日期应该大于或等于开始日期!");
        return false;
      }
    } else {
      Oedate.focus();
      Oedate.select();
      return false;
    }
  } else {
    Osdate.focus();
    Osdate.select();
    return false;
  }
}

//-----------------------------比较日期的大小-----------------------------------//
function CompareDate(startDate,endDate) { //yyyy-mm-dd
    var sYear=eval(startDate.substring(0,4));
    var sMonth=eval(startDate.substring(5,7));
    var sDay=eval(startDate.substring(8,10));
    
    var eYear=eval(endDate.substring(0,4));
    var eMonth=eval(endDate.substring(5,7));
    var eDay=eval(endDate.substring(8,10));

    if(eYear>sYear) {
        return 1;
    } else if(eYear<sYear) {
        return -1;
    } else {
        if(eMonth>sMonth) {
            return 1;
        } else if(eMonth<sMonth) {
            return -1;
        } else {
            if(eDay>sDay) {
                return 1;
            } else if(eDay<sDay) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}

//检验控件值是否为空
function checkNull( obj , name ) {
    if(name=="undefined") return true;
    
    if ( obj.value.length == 0 )  {
        alert( name + "不能为空！" ) ;
        if(obj.tagName=="SELECT") {
          _checkTabButton(obj);
          obj.focus();  
        } else {
          _checkTabButton(obj);
          obj.focus() ;
          obj.select() ;
      }
        return false ;
    }
    return true;
}

//检验控件值长度
function checkLength( obj , name , minLens , maxLens ) {
	if(minLens==null||minLens=="undefined") minLens = 0;
	if(maxLens==null||maxLens=="undefined") maxLens = 100000;
    //return true;
    var tlen = obj.value.length ;
    if(maxLens=="null" || tlen == 0 || (tlen>=minLens && tlen<=maxLens )) {
       return true ;
    }
    if (minLens<maxLens) {
        alert( name + "的长度必须在" + minLens + "位和" + maxLens + "位之间！" ) ;
    } else {
        alert( name + "的长度必须是" + maxLens + "位！" ) ;
    }
    _checkTabButton(obj);
    obj.focus() ;
    obj.select() ;
    return false ;
}

//----------检查纳税人8位识别号，该方法在税务系统中可能有用-------------------------//
//规范：最后一位的值等于前7位之和的个位数
function ChkNsrCode(obj) {
    var sum=0;
    var ssum="";
    if(obj.value=="") return true;//忽略空值情况
    if(ChkDigit(obj,8,true)) {
        for(i=0;i<7;i++) {
            sum=sum+eval(obj.value.charAt(i));
        }
        ssum=sum.toString();
        if(ssum.charAt(ssum.length-1)==obj.value.charAt(7)) {
            return true;
        } else {
            alert("请输入正确的纳税人识别号！");
            _checkTabButton(obj);
            obj.focus();
            obj.select();
            return false;
        }
    } else {
      _checkTabButton(obj);
      obj.focus();
      obj.select();
        return false;
    }
}

//-------------------检查不超过（或等于）n位的数字----------------------------//
function ChkDigit(obj,n,flag) {        //flag=true 等于;flag=false 不超过.
    var num=obj.value;
    if(num=="") return true;
    for(var i=0;i<num.length;i++){
        if(isDigit(num.charAt(i))==false){
            alert("请输入数字！");
            return false;
        }
    }
    if(flag) {
        if(num.length != n) {
            alert("请输入"+n+"位数字！");
            return false;
        }
    } else {
        if(num.length > n) {
            alert("请输入不超过"+n+"位的数字！");
            return false;
        }
    }
    return true;
}

//-----------------------------------------------------------------------------//
function isDigit(theNum){
    var theMask="0123456789";
    if(theNum==""){
        return true;
    }
    else if(theMask.indexOf(theNum)==-1){
        return false;
    }
    return true;
}



//检验控件数字
function checkNumber( obj , name , min , max , decLens ) {
    if(name=="undfined") return true;
    tlen = obj.value.length ;
    var dPoint = obj.value.indexOf( "." ) ;
    if( dPoint != -1 && decLens == 0 )  {
        alert( name + "的值必须为整数！" ) ;
        _checkTabButton(obj);
        obj.focus() ;
        obj.select() ;
        return false;
    }

    str1 = "+-0123456789" ;
    str2 = "0123456789." ;

    for ( i = 0 ; i <= obj.value.length - 1 ; i++ )     {
        char1 = obj.value.substring( i , i+1 ) ;
        if ( i == 0 )  {
            if ( str1.indexOf( char1 ) == -1 )  {
                alert( name + "的值不是合法的数字！" ) ;
                _checkTabButton(obj);
                obj.focus() ;
                obj.select() ;
                return false;
            }
        }
        else     {
            if ( str2.indexOf( char1 ) == -1 )  {
                alert( name + "的值不是合法的数字！" ) ;
                _checkTabButton(obj);
                obj.focus() ;
                obj.select() ;
                return false;
            }
        }
    }

    var str = obj.value ;
    if( parseFloat( str ) > parseFloat( max ) || parseFloat( str ) < parseFloat( min ) )  { 
        alert( name + "的值必须在" + formatToNum(min) + "和" + formatToNum(max) + "之间！" ) ;
        _checkTabButton(obj);
        obj.focus() ;
        obj.select() ;
        return false;
    }


    var dPoint = str.indexOf( "." ) ;
    if( dPoint != -1 )
        if( tlen - dPoint - 1 > decLens )  {
            alert( name + "的小数位数不能超过" + decLens + "位！" ) ;
            _checkTabButton(obj);
            obj.focus() ;
            obj.select() ;
            return false;
        }

    return true;

}

//检验控件是否为日期（年、月、日为单独的控件）
function checkDate( year , month , day ) {
    
    yearValue = parseInt( year.value ) ;
    monthValue = parseInt( month.value ) ;
    dayValue = parseInt( day.value ) ;
    
    
    if( isNaN( yearValue ) )  {
        alert( "您输入的年份不合法！" ) ;
        year.select() ;
        year.focus() ;
        return ;
    }

    
    yearValue -= 1900 ;
    monthValue -= 1 ;
    NewDate = new Date( yearValue , monthValue , dayValue ) ;
    year.value = NewDate.getYear() + 1900 ;
    month.value = NewDate.getMonth() + 1 ;
    day.value = NewDate.getDate() ; 

}

//检验控件是否为日期
function checkDateInput( ymd ) {
    ymdString = ymd.value ;
    if( ymdString == "" )
        return true;
    pos = ymdString.indexOf( "-" ) ;
    if( pos != 4 )  {
        //alert( "日期的格式必须为 YYYY-M-D , 请重新输入 ！" ) ;    
        _checkTabButton(ymd);
        ymd.select() ;
        ymd.focus() ;
        return false;
    }
    _yearValue = ymdString.substring( 0 , pos ) ; 
    ymdString = ymdString.substring( pos + 1 , ymdString.length ) ; 
    pos = ymdString.indexOf( "-" ) ;
    if( pos != 1 && pos != 2 )  {
        //alert( "日期的格式必须为 YYYY-M-D , 请重新输入 ！" ) ;    
        _checkTabButton(ymd);
        ymd.select() ;
        ymd.focus() ;
        return false;
    }
    _monthValue = ymdString.substring( 0 , pos ) ; 
    _dayValue = ymdString.substring( pos + 1 , ymdString.length ) ; 
    if( _dayValue.length != 1 && _dayValue.length != 2 )  {
        //alert( "日期的格式必须为 YYYY-M-D , 请重新输入 ！" ) ;    
        _checkTabButton(ymd);
        ymd.select() ;
        ymd.focus() ;
        return false;
    }
    yearValue = parseInt( _yearValue ) ;
    //去掉前面的0
    while(true){
        if(_monthValue.indexOf("0")==0)
            _monthValue= _monthValue.substring(1,_monthValue.length);
        else
            break;
    }
    while(true){
        if(_dayValue.indexOf("0")==0)
            _dayValue= _dayValue.substring(1,_dayValue.length);
        else
            break;
    }
    monthValue = parseInt( _monthValue ) ;
    dayValue = parseInt( _dayValue ) ;
    
    if( isNaN( yearValue ) || isNaN( monthValue ) || isNaN( dayValue ) )  {
        //alert( "您输入的日期不合法！" ) ;
        _checkTabButton(ymd);
        ymd.select() ;
        ymd.focus() ;
        return false;
    }
    
    yearValue -= 1900 ;
    monthValue -= 1 ;
    NewDate = new Date( yearValue , monthValue , dayValue ) ;
    if( yearValue != NewDate.getYear() || monthValue != NewDate.getMonth() || dayValue != NewDate.getDate() )  {
        //alert( "您输入的日期不合法！" ) ;
        _checkTabButton(ymd);
        ymd.select() ;
        ymd.focus() ;
        return false;
    }
  
  return true;
}

//检查日期控件中的输入格式，直接调用DTPicker.js中的方法，此时一定同时调用了DTPicker.js
function ChkDTFormat(dataStr) {
  //调用DTPicker.js中的方法
  if(_dt_CheckDate != null) {
    return _dt_CheckDate(true, dataStr, true);
  } else {
    return true;
  }
}

//如果当前控件的父类中有"tabcontrol"，触发点击该父类的事件，从而使当前控件可视
var _curOldTabButtonObj = null;
function _checkTabButton(obj) {
  //循环                  
  var i = 5;
  var curObj = obj;
  while(i < 20) {//循环十五次，如果当前控件的一个父类为"tabcontrol"，触发点击该父类的事件，从而使当前控件可视
    if(curObj.parentElement != null && curObj.parentElement.isTabControl == true) {//如果父类为TABCONTROL，首先将该父类设为可视
      var curTabButton = document.getElementById("tabButton"+curObj.parentElement.id)
      if(curTabButton != _curOldTabButtonObj) {
        curTabButton.click();
        _curOldTabButtonObj = curTabButton;
      }
      return true;
    } else if(curObj.parentElement != null) {
      curObj = curObj.parentElement;
    } else if(curObj.parentElement == null) {
      return true;
    }
    i++;
  }
  
  return true;
}



//******************************************************








//******************************************************
// 输入验证的公共函数（工具方法）
//******************************************************
//----------------------------检测邮政编码-----------------------------------//
function isPostNum(_this){
    if(_this == null || _this.value.length == 0){
        return true;
    }
    else if(isBetween(_this.value.length,6,6)==false){
        alert("邮政编码应为6个字符长度!");
        _this.focus();
        return false;
    }
    else if(isInt(_this.value)==false){
        alert("邮政编码应为数字!");
        _this.focus();
        return false;
    }
    return true;
}

//----------------------------检测Email地址------------------------------------//
function isEmail(_theStr){
    //仅检查"."和"@"
    var theStr=_theStr.value;
    var atIndex=theStr.indexOf("@");
    var doIndex=theStr.indexOf(".",atIndex);
    var theSub=theStr.substring(0,doIndex+1);
    if(!checkNull(_theStr, _theStr.name)){
        return true;
    }
    if((atIndex<1)||(atIndex!=theStr.lastIndexOf("@"))||(doIndex<atIndex+2)||(theStr.length<=theSub.length)){
        alert("输入Mail地址格式有误！");
        _theStr.focus();
        return false;
    }
    return true;
}
//----------------------------检测电话号码------------------------------------//
function ChkTelCode(num){
  //if(!checkNull(num, num.name)){
  if(num == null || num.value.length == 0){
      return true;
  }
  else{
  for(i_loop=0;i_loop<num.value.length;i_loop++){
     if (!(((num.value.charAt(i_loop)>=0)&&(num.value.charAt(i_loop)<=9))||(num.value.charAt(i_loop)=="(")||(num.value.charAt(i_loop)==")")||(num.value.charAt(i_loop)=="-")||(num.value.charAt(i_loop)==","))){
        alert("电话号码的格式不正确，它只能包括数字、‘，’、‘-’和括号！");
          num.focus();
        return false;   
     }
     if( ((num.value.charAt(i_loop)==")")&&(num.value.charAt(i_loop+1)!="-" )&&(num.value.charAt(i_loop+1)!=","))||(num.value.charAt(i_loop)=="-")||(num.value.charAt(i_loop)==",")){
        if (((num.value.length-i_loop-1)>8)||((num.value.length-i_loop-1)<7)){
//           alert("电话号码的长度不正确，在区号之后应该是7位或者8位！");
//           return false;
          }
        }
   }
   if (!((num.value.length>=7)&&(num.value.length<=20))){
        alert("电话号码的长度不正确，它只能大于或等于7位并且小于或等于20位！");
          num.focus();
        return false;   
    }
  }
  return true;
}

//--------------------------------------检查是否在两个值之间----------------------------------//
function isBetween(val,lo,hi){
    if( val<lo || val>hi ){
        return false;
    }
    else{
        return true;
    }
}

