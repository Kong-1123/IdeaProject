//校验函数
ERR_NUMBER = "非法的数值！";
ERR_BIG_NUMBER = "数额太大，系统无法接受！";
ERR_EMAIL = "Email地址输入不正确！";
ERR_STRING_NULL = "必须输入";

//性别常量。依据数据字典定义。如果数据字典发生变化，需修改。
GENDER_MALE = "1"; // 男性
GENDER_FEMALE = "2"; // 女性

//读写中征码时使用的初始变量：六个0
NULLLOANCARDNO = "000000";

/**
 * 校验字符串中包括的字符是否在指定的字符串包括的字符之中
 * @param  s :被校验的字符串
 * @param  bag :范围字符串
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
/**
 * 将数据去掉空格
 * @param val 要校验的数据
 */
function trim(val)
{
	var str = val+"";
	if (str.length == 0) return str;
	var re = /^\s*/;
	str = str.replace(re,'');
	re = /\s*$/;
	return str.replace(re,'');
}

/**
 * 校验数据是否为整数
 * @param data 要校验的数据
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
 * 校验数据是否为大于零的整数
 * @param data 要校验的数据
 */
function checkNoZeroInt(data)
{
   if (data == "") return true;
   
   if (checkint(data)) {
      if (parseFloat(data) > 0) {return true;} 
   } else {
      return false;
   }
   
}

/**
 * 校验数据值是否为零
 * @param data 要校验的数据
 * @return true - 为零；false - 不为零
 */
function checkZero(data) {
    if (data==""||parseFloat(data) == 0)
        return true;
    else 
        return false;
}

/**
 * 校验数据是否为空,不可全为空格
 * @param data 要校验的数据
 */
function checkstring_null(data)
{
    if (data==null || trim(data)==""){
	      return false;
    }
    if (!checkstring_allSpace(data)) {
	      return false;
    }
    
    return true;
}

/**
 * 检验数据是否全是空格
 * @param data 要检验的数据
 */
function checkstring_allSpace(data) {
    if (data.length>0 && trim(data)=="") {
        return false;
    }
    for (var i = 0; i < data.length; i++) {
        if (data.substring(i,i+1) == "'" || data.substring(i,i+1) == '"') {
            alert("输入项中不能含有单引号或双引号！");
            return false;
        }
    }
    return true;
}

/**
 * 检验行业代码
 * @param data 要检验的数据
 */
function checkIndustryCode(obj,msg) {
    var data = obj.value;
    if (data.length>0 && trim(data)=="") {
        alert(msg+"不能全是空格！");
        obj.focus();
        return;
    }
    for (var i = 0; i < data.length; i++) {
        if (data.substring(i,i+1) == "'" || data.substring(i,i+1) == '"' || data.substring(i,i+1) == '%' || data.substring(i,i+1) == '％') {
            alert(msg+"不能含有单引号、双引号或百分号！");
            obj.focus();
            return;
        }
    }
}

/**
 * 校验数据是否为空，允许全空格
 * @param data 要校验的数据
 */
function checkNull(data)
{
    if (data==null || trim(data)=="")
	      return false;
    
    return true;
}

function checkemail(obj)
{
 
 if (!checkstring_allSpace(obj)) return false;
 
 if(obj!= "")
 {
  var ok1=obj.indexOf("@");
  var ok2=obj.indexOf(".");
  if(!((ok1!=-1)&&(ok2!=-1)))
  {
   return false;
  }
  var allowstrlist = "&#%<>";
  var endvalue = true;
  for (i=0;i<obj.length;i++) 
  {
   if (allowstrlist.indexOf(obj.substr(i,1))!=-1) 
   {
    endvalue=false;   
    break;
   }
  }
  if(endvalue==false)
  {
   return false;   
  }
  //邮件地址正确
  return true;
 }
 else
 {
  return true;
 }
} 

/**
 * 校验网址是否正确
 */
function checkwebaddress(obj)
{
 
 if (!checkstring_allSpace(obj)) return false;
 
 if(obj!= "")
 {
  var ok2=obj.indexOf(".");
  if(!((ok2!=-1)))
  {
   return false;
  }
  var allowstrlist = "&#%<>";
  var endvalue = true;
  for (i=0;i<obj.length;i++) 
  {
   if (allowstrlist.indexOf(obj.substr(i,1))!=-1) 
   {
    endvalue=false;   
    break;
   }
  }
  if(endvalue==false)
  {
   return false;   
  }
  //地址正确
  return true;
 }
 else
 {
  return true;
 }
}

/**
 * 校验是否含有中文
 * true － 不含有；false － 含有
 */
function isChinese(name,msg) //中文值检测
{
 if (!checkstring_allSpace(name)) return false;
 
 if(name.length == 0) return true;
 
 for(i = 0; i < name.length; i++) {
  if(name.charCodeAt(i) > 128) {
      alert(msg+"不能含有中文！");
      return false;
  }
 }
 
 return true;
}


/**
 * 校验普通电话、传真号码：可以“+”开头，除数字外，可含有“-”
 * @param telno 要检验的数据
 */
function checkTel(telno)
{
    if (!checkstring_allSpace(telno)) return false;

    telno = trim(telno);
    if (telno.length == 0) return true;
    var patrn=/^[+]?\d{1,3}?([-]?\d{1,12})*$/;
    if (!patrn.exec(telno)) return false;
    return true;
}

/**
 * 校验邮政编码
 * @param zip 要检验的数据
 */
function checkPostalCode(zip)
{
    if (!checkstring_allSpace(zip)) return false;
    zip = trim(zip);
    if (zip.length == 0) return true;
    
    if (zip.length < 6) return false;
    
    var patrn=/^[0-9 ]{3,12}$/;
    if (!patrn.exec(zip)) return false;
    return true;
}

/**
 * 校验数据是否为大于零的数字
 * @param data 要校验的数据
 */
function checknumber(data){
	var tmp ;
	if (data == "") return true;
	if (!checkstring_allSpace(data)) return false;
	
	var re = /^[\-\+]?([0-9]\d*|0|[1-9]\d{0,2}(,\d{3})*)(\.\d+)?$/;

  if (re.test(data)){
    if (parseInt(data,10) < 0 || parseInt(data) == 0) {
      return false;
    } else {
      return true;
    }
	}else{
   	return false;
	}
}

/**
 * 校验数据是否为合法的金额，
 * @param data 要校验的数据
 */
/**
 * 校验数据是否为合法的金额，
 * @param data 要校验的数据
 */
function isNotNegativeNubmer(data){
	var tmp ;
	if (data == "") return true;
	if (!checkstring_allSpace(data)) return false;
	if(parseFloat(data)<1){
	   if(data.substring(1,2)=="0"){
	     alert("金额不要以数字0开头！");
	     return false;
	   }
	}else{
	  if(data.substring(0,1) == "0"){
	  	 alert("金额不要以数字0开头！");
	     return false;
	  }
	}
	
	var re = /^[\-\+]?([0-9]\d*|0|[1-9]\d{0,2}(,\d{3})*)(\.\d+)?$/;

  if (re.test(data)){
      var ok = data.indexOf(".");
      if (ok != -1 && data.substring(ok).length > 3) {
        alert("小数点后最多两位");
        return false;
      }
      if (ok == -1 && data.length > 17) {
        alert("整数部分不许超过17位！");
        return false;
      }
      return true;
	}else{
   	  return false;
	}
	
}

/**
 * 校验是否为合法的年份
 * @param data 要校验的数据
*/
function checkyear(year)
{
  if (!checkstring_allSpace(year)) return false;

  if (year== "") return true;
  var temp = parseInt(year);
  if (!isNaN(temp)){
 //   if (year == 0) return true;
    low = 1000;
   // high = 2050;
    if (year >= low) return true;
  }
  return false;
}
/**
 * 校验是否为合法的月份
 * @param data 要校验的数据
*/
function checkmonth(month)
{
   if (!checkstring_allSpace(month)) return false;

   if(month == "") return true;
    var temp = parseInt(month);
	if (!isNaN(temp)){
    low = 1;
		 high = 12;
		if ((month >= low) && (month <=high)) return true;
	}
	return false;
}
/**
 * 校验是否为合法的天
 * @param data 要校验的数据
*/
function checkday(day,year,month)
{
	err = false;
    if((year == 0) || (month == 0) || (day == 0)){
        return true;
    }
    if((year == "") || (month == "") || (day == "")){
        return true;
    }
	if (!checkint(year) || (year < 1900)) {
		return false;
	}
	if (!checkint(month) || (month < 1) || (month > 12)){
		return false;
	}
	if (!checkint(day) || (day < 1) || (day > 31)){
		return false;
	}

	switch (parseInt(month)){
		case 2:
			high =28;
			if ((year % 4 == 0) && (year % 100 != 0))
				{high =29;}
			else if (year % 400 == 0) {high=29;}
			break;
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			high =31;
			break;
		default:
			high =30;
	}
	if ((day < 1) || (day > high)){
		return false;
	}
	return true;
}

/**
 * 校验数据是否为合法的日期
 * @param bdate 要校验的数据
 */
function checkdate(vdate){

  if (!checkstring_allSpace(vdate)) return false;

  vdate = trim(vdate);
  if (vdate == "")
    return true;

	var datePat=/^(\d{2}|\d{4})(\-)(\d{1,2})(\-)(\d{1,2})$/;

  var dateStr= vdate;
    // is the format ok?
  var matchArray = dateStr.match(datePat);

    if (matchArray==null)
    {
     return false;
    }
    year=matchArray[1];
    month=matchArray[3];
    day=matchArray[5];

    if (year.length!=4 || month.length!=2 || day.length!=2)
        return false;
    if (month < 1 || month > 12)
        return false;
    if (day < 1 || day > 31)
        return false;

    if ((month==4 || month==6 || month==9 || month==11) && day==31)
        return false;

    if (month==2)
    {
        var isleap=(year % 4==0 && (year % 100 !=0 || year % 400==0));
        if (day>29 || ((day==29) && (!isleap)))
            return false;
    }
    
    // 校验日期中的年份是否合法
    if (!checkyear(vdate.substring(0,4))) {
        alert("日期中的年份非法！");
        return false;
    }  
    return true;
}

/**
 * 校验数据是否为合法的日期
 * @param bdate 要校验的数据yyyy-mm
 */
function checkdate2(vdate){

  if (!checkstring_allSpace(vdate)) return false;

  vdate = trim(vdate);
  if (vdate == "")
    return true;

  var datePat=/^(\d{2}|\d{4})(\-)(\d{1,2})$/;
  var dateStr= vdate;
    // is the format ok?
  var matchArray = dateStr.match(datePat);
    if (matchArray==null)
    {
     return false;
    }
    year=matchArray[1];
    month=matchArray[3];
    
    if (year.length!=4 || month.length!=2)
         return false;
    if (month < 1 || month > 12)
        return false;
    // 校验日期中的年份是否合法
    if (!checkyear(vdate.substring(0,4))) {
        alert("日期中的年份非法！");
        return false;
    }    
    return true;
}

/**
 * 日期大小判断(格式：yyyy-mm-dd)
 * @param beginDate,endDate
 * @return boolean true - endDate大于等于beginDate
 */
function judgetwodate(beginDate,endDate)
{
 var eva = checkdate(beginDate) && checkdate(endDate);
 
    if(beginDate !="" && endDate!="" & eva != false)
    {
		var myDate1 = Date.parse(beginDate.replace("-","/"));
        var myDate2 = Date.parse(endDate.replace("-","/"));
        if(myDate1 > myDate2)
           return false;
        else
           return true;
    }
    else
    {
      return false;
    }
}

/**
 * 日期大小判断(格式：yyyy-mm)
 * @param beginDate,endDate
 * @return boolean true - endDate大于等于beginDate
 */
function judgetwodate2(beginDate,endDate)
{
 var eva = checkdate2(beginDate) && checkdate2(endDate);
    if(beginDate !="" && endDate!="" & eva != false)
    {
			  var myDate1 = beginDate.replace("-","");
        var myDate2 = endDate.replace("-","");

        if(myDate1 > myDate2)
           return false;
        else
           return true;
    }
    else
    {
      return false;
    }
}

 /**
 * 日期在一年之间
 * @param beginDate,endDate
 * @return boolean true -
 *
 */
  function judgetwodateoneyear(beginDate,endDate)
{
		var year= beginDate.substr(0, 4) ;
		var  cmpDate  =parseInt(year)+1;
		var  newDate =   cmpDate+beginDate.substr(4,8);
		var   myDate1 = Date.parse(newDate.replace("-","/"));
		var   myDate2 = Date.parse(endDate.replace("-","/"));
 
      if( (myDate1 - myDate2)<0) 
           return false;
        else
           return true;
}

/**
 * 校验输入的中征码校验位是否合法
 *
 */
 function checkLoanCardno(loanCardNo) {
  
     if (trim(loanCardNo).length != 16)
        return false;
     
     var checkCode;
     var weightValue = new Array();
     var checkValue = new Array();
     var totalValue = 0;
     var c = 0;
     // 生成的校验码
     var str1;

     checkCode = trim(loanCardNo).substring(0, 14);
     
     for (var i = 0; i < 14; i++) {
        var tempvalue = checkCode.charCodeAt(i);
        if (tempvalue >= "a".charCodeAt() && tempvalue <= "z".charCodeAt()) {
            //alert("中征码中不能有小写字母");
            return false;
        }
     }
     
     weightValue[0] = 1;
     weightValue[1] = 3;
     weightValue[2] = 5;
     weightValue[3] = 7;
     weightValue[4] = 11;
     weightValue[5] = 2;
     weightValue[6] = 13;
     weightValue[7] = 1;
     weightValue[8] = 1;
     weightValue[9] = 17;
     weightValue[10] = 19;
     weightValue[11] = 97;
     weightValue[12] = 23;
     weightValue[13] = 29;
     
     for (var j = 0; j < 3; j++) {
         var tempValue = checkCode.substring(j, j+1);
         if (tempValue >= "A" && tempValue <= "Z") {
             checkValue[j] = tempValue.charCodeAt() - 55;
         } else {
             checkValue[j] = tempValue;
         }
         
         totalValue = totalValue + weightValue[j] * checkValue[j];
     }
     
     for (var j = 3; j < 14; j++) {
     	checkValue[j] = checkCode.substring(j, j+1);
     	totalValue = totalValue + weightValue[j] * checkValue[j];
     }
     
     c = 1 + (totalValue % 97);
     str1 = String(c);
     
     if (str1.length == 1) {
     	str1 = '0' + str1;
     }
     
     if (str1 == trim(loanCardNo).substring(14))  
       return true;
     else 
       return false;
     
 }
 
/*
==================================================================
功能：验证身份证号码是否有效
提示信息：输入身份证号不正确！
使用：isIDno(obj)
返回：bool
==================================================================
*/
function isIDno(idcard) {
    var Errors=new Array("验证通过!",
                         "身份证号码位数不对!",
                         "身份证号码出生日期超出范围或含有非法字符!",
                         "身份证号码校验错误!",
                         "身份证地区非法!"
                        );
  
    var area={11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
    
    var idcard,Y,JYM;
    var S,M;
    var idcard_array = new Array();
    idcard_array = idcard.split("");
    //地区检验
    if(area[parseInt(idcard.substr(0,2))]==null) {
        alert(Errors[4]);
        return false;
    }
    //身份号码位数及格式检验
    switch(idcard.length){
    case 15:
        //15位身份号码检测
        if ( (parseInt(idcard.substr(6,2))+1900) % 4 == 0 || ((parseInt(idcard.substr(6,2))+1900) % 100 == 0 && (parseInt(idcard.substr(6,2))+1900) % 4 == 0 )){
            ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;//测试出生日期的合法性
        } else {
            ereg=/^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;//测试出生日期的合法性
        }
        if(!ereg.test(idcard)) {
            alert(Errors[2]);
            return false;
        } else {
            return true;
        }
    break;
    case 18:
        //18位身份号码检测
        //出生日期的合法性检查 
        //闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
        //平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
        if ( parseInt(idcard.substr(6,4)) % 4 == 0 || (parseInt(idcard.substr(6,4)) % 100 == 0 && parseInt(idcard.substr(6,4))%4 == 0 )){
            ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
        } else {
            ereg=/^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
        }
        if(ereg.test(idcard)) { //测试出生日期的合法性
            //计算校验位
            S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
            + (parseInt(idcard_array[1]) + parseInt(idcard_array[11])) * 9
            + (parseInt(idcard_array[2]) + parseInt(idcard_array[12])) * 10
            + (parseInt(idcard_array[3]) + parseInt(idcard_array[13])) * 5
            + (parseInt(idcard_array[4]) + parseInt(idcard_array[14])) * 8
            + (parseInt(idcard_array[5]) + parseInt(idcard_array[15])) * 4
            + (parseInt(idcard_array[6]) + parseInt(idcard_array[16])) * 2
            + parseInt(idcard_array[7]) * 1 
            + parseInt(idcard_array[8]) * 6
            + parseInt(idcard_array[9]) * 3 ;
            Y = S % 11;
            M = "F";
            JYM = "10X98765432";
            M = JYM.substr(Y,1);//判断校验位
            if(M !== idcard_array[17]) {
                alert(Errors[3]);
                return false;
            }
            return true;
        } else {
            alert(Errors[2]);
            return false;
        } 
        break;
    default:
        alert(Errors[1]);
        return false;
        break;
    }
}

/*
==================================================================
功能：验证组织机构代码是否有效
使用：isCorpno(obj)
返回：bool
==================================================================
*/
function isCorpNo(financecode) {

  if (!checkstring_allSpace(financecode)) {
	layerMsg("错误：您输入的都是空格！");
    return false;
  }
  var financecode=trim(financecode);
   var code = financecode;
   if (code == "00000000-0") {
	 layerMsg("组织机构代码错误!");
     return false;
   }

   var re = /[A-Z0-9]{8}-[A-Z0-9]/;    
   var r = code.match(re);   
   if (r == null) {
	 layerMsg("组织机构代码错误!");
     return false;
   }      
     
   return true;
}


/** 
 * 写卡器写卡时做境外企业的中征码转换。
 * 把境外企业的中征码前三个国别字母转换为数字（相应的charCodeAt()值）并返回。
 */
function transLoanCardNoWhenWrite(loanCardNo) {
  
    // 如果中征码不合法或不是境外企业中征码，直接返回原参数。
    if (!checkLoanCardno(loanCardNo) || !isNaN(loanCardNo.substring(0,1)))  return loanCardNo;
    
    var charStr;
    var numStr = "";
    for (i = 0; i < 3; i++) {
        charStr = loanCardNo.substring(i,i+1);
        numStr += String(charStr.charCodeAt());
    }

    return numStr + loanCardNo.substring(3);
}

/**
 * 读卡器读卡时做境外企业的中征码转换。
 * 把前六位数字转换为对应的字母。
 * 单独使用时请注意对临界条件的判断。
 */
function transLoanCardNoWhenRead(loanCardNo) {

    var idx = loanCardNo.indexOf("#");
    
    if (idx == -1)  return loanCardNo;
    
    // 2磁道的数据
    var track2Data = loanCardNo.substring(0,idx);
    // 3磁道的数据
    var track3Data = loanCardNo.substring(idx+1);

    var parLoanCardNo;
    
    if (track3Data.length == 16 || track3Data.length == 19) {
        parLoanCardNo = track3Data;
    }
    else if (track3Data == NULLLOANCARDNO) {
        parLoanCardNo = track2Data;
    }
    else if (track3Data.length == 18) {
        if (track2Data == NULLLOANCARDNO)
            parLoanCardNo = track3Data.substring(0,16);
        else
            parLoanCardNo = track2Data;
    }

    // 如果不是境外借款人的中征码，直接返回原参数。
    if (trim(parLoanCardNo).length <= 16)  return parLoanCardNo;
    
    // 如果是老系统发的18位中征码，截取前16返回。
    if (trim(parLoanCardNo).length == 18)  return parLoanCardNo.substring(0,16);
    
    // 取得大写字母和数字的对照表
    var charArr = getTransArr();
    // 进行转换
    var charIdx;
    var charStr = "";
    for (i = 0; i < 5; i = i+2) {
        charIdx = parseInt(parLoanCardNo.substring(i,i+2),10);
        charStr += charArr[charIdx];
    }
    
    return charStr + parLoanCardNo.substring(6);
}

/**
 * 建立26个大写英文字母和数字的对照数组。
 * 使用charCodeAt返回的值作为数组中存储对应字母的下标(65--90)。
 * return@ 建立好的对照数组
 */
function getTransArr() {
    var charStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    var charLen = charStr.length;
    var str;
    var buf = new Array();
    buf.push("var charArr = {");
    for (var i = 0; i < charLen; i++) {
        str = charStr.substring(i,i+1);
        buf.push(str.charCodeAt() + ":'" + str + "'");
        if (i == charLen-1)
            buf.push("}");
        else
            buf.push(",");
    }
    var arrStr = buf.join("");
    eval(arrStr);
    return charArr;
}

/**
 * 本方法只用在支付信用系统
 * 校验输入的中征码校验位是否合法
 *
 */
 function checkLoanPcisCardno(loanCardNo) {
  
     if (trim(loanCardNo).length != 16)
        return false;
     
     var checkCode;
     var weightValue = new Array();
     var checkValue = new Array();
     var totalValue = 0;
     var c = 0;
     // 生成的校验码
     var str1;

     checkCode = trim(loanCardNo).substring(0, 14);
     
     for (var i = 0; i < 14; i++) {
        var tempvalue = checkCode.charCodeAt(i);
        if (tempvalue >= "a".charCodeAt() && tempvalue <= "z".charCodeAt()) {
            //alert("中征码中不能有小写字母");
            return false;
        }
     }
     
     weightValue[0] = 1;
     weightValue[1] = 3;
     weightValue[2] = 5;
     weightValue[3] = 7;
     weightValue[4] = 11;
     weightValue[5] = 2;
     weightValue[6] = 13;
     weightValue[7] = 1;
     weightValue[8] = 1;
     weightValue[9] = 17;
     weightValue[10] = 19;
     weightValue[11] = 97;
     weightValue[12] = 23;
     weightValue[13] = 29;
     
     for (var j = 0; j < 3; j++) {
         var tempValue = checkCode.substring(j, j+1);
         if (tempValue >= "A" && tempValue <= "Z") {
             checkValue[j] = tempValue.charCodeAt() - 55;
         } else {
             checkValue[j] = tempValue;
         }
         
         totalValue = totalValue + weightValue[j] * checkValue[j];
     }
     
     for (var j = 3; j < 14; j++) {
     	checkValue[j] = checkCode.substring(j, j+1);
     	totalValue = totalValue + weightValue[j] * checkValue[j];
     }
     
     c = 1 + (totalValue % 97);
     str1 = String(c);
     
     if (str1.length == 1) {
     	str1 = '0' + str1;
     }
     
     if (str1 == trim(loanCardNo).substring(14))  
       return true;
     else 
       return false;
     
 }


function isCorpNo(financecode) {

  if (!checkstring_allSpace(financecode)) {
	  layerMsg("错误：您输入的都是空格！");
    return false;
  }
  var financecode=trim(financecode);
  if(financecode=="")
    return true;

   var fir_value, sec_value;
   var w_i = new Array(8);
   var c_i = new Array(8);
   var j, s = 0;
   var c, i;

   var code = financecode;
   if (code == "00000000-0") {
	   layerMsg("组织机构代码错误!");
     return false;
   }

   re = /[A-Z0-9]{8}-[A-Z0-9]/;    
   r = code.match(re);   
   if (r == null) {
	   layerMsg("组织机构代码错误!");
     return false;
   }        

	   w_i[0] = 3;
	   w_i[1] = 7;
	   w_i[2] = 9;
	   w_i[3] = 10;
	   w_i[4] = 5;
	   w_i[5] = 8;
	   w_i[6] = 4;
	   w_i[7] = 2;

	   if (financecode.charAt(8) != '-') {
		   layerMsg("组织机构代码错误!");
		   return false;
	   }

	   for (i = 0; i < 10; i++) {
		   c = financecode.charAt(i);
		   if (c <= 'z' && c >= 'a') {
			   layerMsg("组织机构代码错误!");
			   return false;
		   }
	   }

	   fir_value = financecode.charCodeAt(0);
	   sec_value = financecode.charCodeAt(1);

	   if (fir_value >= 'A'.charCodeAt(0) && fir_value <= 'Z'.charCodeAt(0)) {
		   c_i[0] = fir_value + 32 - 87;
	   } else {
			if (fir_value >= '0'.charCodeAt(0) && fir_value <= '9'.charCodeAt(0)) {
			c_i[0] = fir_value - '0'.charCodeAt(0);
			} else {
				layerMsg("组织机构代码错误!");
			  return false;
			}
	   }

	   s = s + w_i[0] * c_i[0];

	   if (sec_value >= 'A'.charCodeAt(0) && sec_value <= 'Z'.charCodeAt(0)) {
		   c_i[1] = sec_value + 32 - 87;
	   } else if (sec_value >= '0'.charCodeAt(0) && sec_value <= '9'.charCodeAt(0)) {
		   c_i[1] = sec_value - '0'.charCodeAt(0);
	   } else {
		   layerMsg("组织机构代码错误!");
		   return false;
	   }

	   s = s + w_i[1] * c_i[1];
	   for (j = 2; j < 8; j++) {
		   if (financecode.charCodeAt(j) >= 'A'.charCodeAt(0) && financecode.charCodeAt(j) <= 'Z'.charCodeAt(0)) {
             c_i[j] = financecode.charCodeAt(j) + 32 - 87;
           } else if (financecode.charCodeAt(j) >= '0'.charCodeAt(0) && financecode.charCodeAt(j) <= '9'.charCodeAt(0)) {
             c_i[j] = financecode.charCodeAt(j) - '0'.charCodeAt(0);
           } else {
        	   layerMsg("组织机构代码错误!");
            return false;
           }
		   s = s + w_i[j] * c_i[j];
	   }

	   c = 11 - (s % 11);

	   if (!((financecode.charAt(9) == 'X' && c == 10) ||
			 (c == 11 && financecode.charAt(9) == '0') || (c == financecode.charCodeAt(9) - '0'.charCodeAt(0)))) {
		     layerMsg("组织机构代码错误!");
				return false;
	   }
   
   return true;
}

function isPcisCorpNo(financecode) {

  if (!checkstring_allSpace(financecode)) {
    //alert("错误：您输入的都是空格！");
    return false;
  }
  var financecode=trim(financecode);
  if(financecode=="")
    return true;

   var fir_value, sec_value;
   var w_i = new Array(8);
   var c_i = new Array(8);
   var j, s = 0;
   var c, i;

   var code = financecode;
   if (code == "00000000-0") {
     //alert("组织机构代码错误!");
     return false;
   }

   re = /[A-Z0-9]{8}-[A-Z0-9]/;    
   r = code.match(re);   
   if (r == null) {
	   //alert("组织机构代码错误!");
     return false;
   }        

	   w_i[0] = 3;
	   w_i[1] = 7;
	   w_i[2] = 9;
	   w_i[3] = 10;
	   w_i[4] = 5;
	   w_i[5] = 8;
	   w_i[6] = 4;
	   w_i[7] = 2;

	   if (financecode.charAt(8) != '-') {
	     //alert("组织机构代码错误!");
		   return false;
	   }

	   for (i = 0; i < 10; i++) {
		   c = financecode.charAt(i);
		   if (c <= 'z' && c >= 'a') {
		    // alert("组织机构代码错误!");
			   return false;
		   }
	   }

	   fir_value = financecode.charCodeAt(0);
	   sec_value = financecode.charCodeAt(1);

	   if (fir_value >= 'A'.charCodeAt(0) && fir_value <= 'Z'.charCodeAt(0)) {
		   c_i[0] = fir_value + 32 - 87;
	   } else {
			if (fir_value >= '0'.charCodeAt(0) && fir_value <= '9'.charCodeAt(0)) {
			c_i[0] = fir_value - '0'.charCodeAt(0);
			} else {
			  //alert("组织机构代码错误!");
			  return false;
			}
	   }

	   s = s + w_i[0] * c_i[0];

	   if (sec_value >= 'A'.charCodeAt(0) && sec_value <= 'Z'.charCodeAt(0)) {
		   c_i[1] = sec_value + 32 - 87;
	   } else if (sec_value >= '0'.charCodeAt(0) && sec_value <= '9'.charCodeAt(0)) {
		   c_i[1] = sec_value - '0'.charCodeAt(0);
	   } else {
	     //alert("组织机构代码错误!");
		   return false;
	   }

	   s = s + w_i[1] * c_i[1];
	   for (j = 2; j < 8; j++) {
		   if (financecode.charCodeAt(j) >= 'A'.charCodeAt(0) && financecode.charCodeAt(j) <= 'Z'.charCodeAt(0)) {
             c_i[j] = financecode.charCodeAt(j) + 32 - 87;
           } else if (financecode.charCodeAt(j) >= '0'.charCodeAt(0) && financecode.charCodeAt(j) <= '9'.charCodeAt(0)) {
             c_i[j] = financecode.charCodeAt(j) - '0'.charCodeAt(0);
           } else {
        	layerMsg("组织机构代码错误!");
            return false;
           }
		   s = s + w_i[j] * c_i[j];
	   }

	   c = 11 - (s % 11);

	   if (!((financecode.charAt(9) == 'X' && c == 10) ||
			 (c == 11 && financecode.charAt(9) == '0') || (c == financecode.charCodeAt(9) - '0'.charCodeAt(0)))) {
			  //alert("组织机构代码错误!");
				return false;
	   }
   
   return true;
}