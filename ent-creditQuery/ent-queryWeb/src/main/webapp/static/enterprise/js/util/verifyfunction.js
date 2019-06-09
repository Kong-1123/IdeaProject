//校验函数
ERR_NUMBER = "非法的数值！";
ERR_BIG_NUMBER = "数额太大，系统无法接受！";
ERR_EMAIL = "Email地址输入不正确！";
ERR_STRING_NULL = "必须输入";

//性别常量。依据数据字典定义。如果数据字典发生变化，需修改。
GENDER_MALE = "1"; // 男性
GENDER_FEMALE = "2"; // 女性

//读写贷款卡时使用的初始变量：六个0
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
 * 校验输入的贷款卡编码校验位是否合法
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
            alert("贷款卡编码中不能有小写字母");
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
    alert("错误：您输入的都是空格！");
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
     alert("组织机构代码错误!");
     return false;
   }

   re = /[A-Z0-9]{8}-[A-Z0-9]/;    
   r = code.match(re);   
   if (r == null) {
	   alert("组织机构代码错误!");
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
	     alert("组织机构代码错误!");
		   return false;
	   }

	   for (i = 0; i < 10; i++) {
		   c = financecode.charAt(i);
		   if (c <= 'z' && c >= 'a') {
		     alert("组织机构代码错误!");
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
			  alert("组织机构代码错误!");
			  return false;
			}
	   }

	   s = s + w_i[0] * c_i[0];

	   if (sec_value >= 'A'.charCodeAt(0) && sec_value <= 'Z'.charCodeAt(0)) {
		   c_i[1] = sec_value + 32 - 87;
	   } else if (sec_value >= '0'.charCodeAt(0) && sec_value <= '9'.charCodeAt(0)) {
		   c_i[1] = sec_value - '0'.charCodeAt(0);
	   } else {
	     alert("组织机构代码错误!");
		   return false;
	   }

	   s = s + w_i[1] * c_i[1];
	   for (j = 2; j < 8; j++) {
		   if (financecode.charAt(j) < '0' || financecode.charAt(j) > '9') {
		     alert("组织机构代码错误!");
		     return false;
		   }
		   c_i[j] = financecode.charCodeAt(j) - '0'.charCodeAt(0);
		   s = s + w_i[j] * c_i[j];
	   }

	   c = 11 - (s % 11);

	   if (!((financecode.charAt(9) == 'X' && c == 10) ||
			 (c == 11 && financecode.charAt(9) == '0') || (c == financecode.charCodeAt(9) - '0'.charCodeAt(0)))) {
			  alert("组织机构代码错误!");
				return false;
	   }
   
   return true;
}


/*
==================================================================
功能：校验两个字段是否相等
使用：isEqual(obj1,obj2)
返回：true - 相等；false - 不相等。
==================================================================
*/
function isEqual(obj1,obj2) {
    
    var value1 = trim(obj1.value);
    var value2 = trim(obj2.value);
    
    if (value1==null || value1=="" || value2==null || value2=="") return false;
    
    if (value1 == value2) {
        obj1.focus();
        return true;
    }
    
    return false;
}

/**
==================================================================
功能：校验字段是否超长，一个汉字占两个字符
使用：countRealLeng(obj,length,msg)
返回：true - 不超长；false - 超长。
==================================================================
*/
function countRealLeng(obj,maxLength,msg) {
  var value = obj.value;
  var lengthTemp = value.replace(/[^\x00-\xff]/g,"**").length;
  
  if (lengthTemp > maxLength) {
    alert(msg+"长度不能超过"+maxLength+"个字符");
    obj.focus();
    return false;
  } else {
    return true;
  }
  
}

/**
==================================================================
功能：把一个页面元素项设置为无效
使用：disabledObj(obj)
==================================================================
*/
function disabledObj(obj) {
    if (obj.disabled == false) {
        obj.value = "";
        obj.disabled = true;
    }
}

/**
==================================================================
功能：把一个页面元素项恢复为无效
使用：enabledObj(obj)
==================================================================
*/
function enabledObj(obj) {
    if (obj.disabled == true) {
        obj.disabled = false;  
    }
}
function checkFin(orgcode){
		var codelength=orgcode.length;
		//校验金融机构代码长度为11位
		if(codelength!=11){
			return false;
		}
		//校验机金融机构代码必须为数字或字母（没经过确认）
		for(var i=0; i<codelength; i++)
		{
			var charValue = orgcode.charAt(i);
			if ( !(charValue >= 'A' && charValue <= 'Z') && !(charValue>='0' && charValue<='9') && !(charValue >= 'a' && charValue <= 'z')) {
				return false;
			}
		} 
		//按金融机构代码的生成规则校验校验位
        var s, M, i, temp, k;
		M = 10;
        s = M;
        k = 9;
        for (i = k; i >= 0; i--) {
            temp = orgcode.charAt(k - i);
            if (temp >= '0' && temp <= '9') {
                temp = temp - '0';
            } else {
                temp = 0;
            }            
            if (((s + temp) % M) == 0) {
                s = 9;
            } else {
                s = (((s + temp) % M) * 2) % (M + 1);
            }
        }

        s = M + 1 - s;
        if (s == 10) {
            s = 0;
        }

        if ( ( s == 11 && orgcode.charAt(10) == 'X' ) || ( s == orgcode.charAt(10) - '0' )) {
            return true;
        } else {
            return false;
        }
  }
  
  
  //转换千分位  
function  commafy(num){  
  num  =  num+"";  
  var  re=/(-?\d+)(\d{3})/  
  while(re.test(num)){  
  num=num.replace(re,"$1,$2")  
  }  
  return  num;  
}  
//如果没有小数点，增加小数点后两位
function convertFloat(objStr)
{objStr=trim(objStr);
	if(objStr=="")
     objStr="0.00" ;
  if (objStr.indexOf(".") == -1)
   	{
     objStr = objStr + ".00";
    }
  else
  	{
     if (objStr.indexOf(".") == objStr.length - 1)
       	{
        objStr = objStr + "00";
       	}
       	else if (objStr.indexOf(".") == objStr.length - 2)
       	{
        objStr = objStr + "0";
       	}
        else if (objStr.indexOf(".") < objStr.length - 2)
          { objStr=objStr.substring(0,objStr.indexOf(".")+3);
          }	
	}
  return objStr;
}
function format(){
  tags=document.all.tags("a");
  for(i=0;i<=tags.length-1;i++){		
    if(tags[i].name=="format"){ 
    			if(tags[i].innerText!='---')     
  		tags[i].innerText=commafy(convertFloat(tags[i].innerText));      		
    }	
  }
}

// 转换千分位，且如果是整数增加小数点后两位
function cardNumberFormat(obj) {
		var valueStr = obj.value;
    if(valueStr!=""){
       valueStr = commafy(convertFloat(valueStr));
       obj.value = valueStr;
    }
}

// 去掉数据字符串中的千分位；如果小数点后是"00"，去掉小数部分。
function formatToNoComma(obj) {
    // 去掉千分位
    obj.value= replaceAll( obj.value , ",", "" );
    // 如果小数点后是"00"，去掉小数部分
    var index = obj.value.indexOf(".");
    if (index != -1 && obj.value.substring(index+1) == "00") {
        obj.value = obj.value.substring(0,index);
    }
}

// 替换字符串中的特定字符
function replaceAll(str, from, to) {
  var idx = str.indexOf(from);
  while(idx>-1) {
    str = str.replace(from, to);
    idx = str.indexOf(from);
  }
  return str;
}

/** 检查是奇偶数
 *
 * return true - 偶数；false - 奇数。
 */
function checkOdd(valueStr) {
    if (valueStr%2 == 0)
        return true;
    else 
        return false;
}

/**
 * 校验所选的文件是否是Excel文件；以及文件路径是否为空。
 */
function checkPath(obj) {
    var path = trim(obj.value);
    if (path == "") {
        alert("请选择文件路径");
        obj.select();
        return false;
    }
    if (!/^.+\.(xls)$/.test(path)) {
        alert("所选文件类型必须是.xls");
        obj.select();
        return false;
    }
    return true;
}

//返回一个页面对象
function get(objName) {
    return document.getElementById(objName);
}

/**
 * 贷款卡子系统概况信息、资本构成等信息、关注信息和财务报表页面数据导入的方法。
 */
function importDataFromExcel(inObj,tabId,addArr,addArr2,changeArr) {
    var FILE_FLAG = "#*_CFCC_*#";
    // 取得输入文件路径
    if(!checkPath(inObj))  return;
    var fileDir = trim(inObj.value).replace("\\","/");
    // 取得输入数据的文件
    var oXL = new ActiveXObject("Excel.Application");
    var workbooks = oXL.WorkBooks;
    var inputBook = workbooks.Open(fileDir);
    var sheets = inputBook.Worksheets;

    var count,flagIndex;
    if (tabId == "finance") {
        count = 3;
        flagIndex = 5;
    } else {
        count = 9;
        flagIndex = 4;
    }
    if (sheets.Count < count) {
        alert("所选文件的sheet不足"+count+"个，请选择正确的文件！");
        inputBook.Close();
        return;
    }
    if (sheets.item(1).Cells(1,flagIndex).value != FILE_FLAG) {
        alert("所选文件不是指定的模板文件，请选择正确的文件！");
        inputBook.Close();
        return;
    }

    var sheet;
    var rows;
    var rowCount;
    var defaultLen;
    var msg = "<br>";
    var err = 0;
    // ---------- 财务报表页面 ----------
    if (tabId == "finance") {
        importFinancialData(sheets);
    }
    // ---------- 概况信息页面 ----------
    else if (tabId == "general") {
        sheet = sheets.item(1);
        rows = get("general");
        var errArr = importGeneralData(sheet,rows,msg,err);
        err = errArr[0];
        msg = errArr[1];
    }
    // ---------- 资本构成等信息页面 ----------
    else if (tabId == "capStru") {
        // 注册资本
        sheet = sheets.item(2);
        var changeFlag = false;
        if (typeof sheet.Cells(2,2).value != "undefined") {
            var optionIndex = reflectSelect(sheet.Cells(2,2).value,get("currencycode1"));
            if (optionIndex == -1) {
                msg += "Excel文件\""+sheet.name+"\"sheet中\""+sheet.Cells(2,1).value+"\"下拉列表输入数值不正确！<br>";
                err++;
            }
            changeFlag = true;
        }
        if (typeof sheet.Cells(3,2).value != "undefined") {
            get("contmoney1").value = sheet.Cells(3,2).value;
            changeFlag = true;
        }
        if (changeFlag) {if (get(changeArr[0]).value == "0")  get(changeArr[0]).value = "1";};
        // 资本构成
        sheet = sheets.item(3);
        rowCount = getSheetRowsCount(sheet);
        defaultLen = document.getElementsByName(addArr[1]).length;//addArr元素请参见addCapStru.jsp的importData()方法
        if (rowCount > defaultLen) {
            for (i = 1; i <= rowCount-defaultLen; i++)
                addCapStru(addArr[0],addArr[1],addArr[2],addArr[3]);
        }
        rows = get("capTable").rows;
        var msgArr = importStruData(sheet,rows,rowCount,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[1]).value == "0")  get(changeArr[1]).value = "1";}
        // 对外投资
        sheet = sheets.item(4);
        rows = document.getElementsByName("list")[1].rows; //对外投资是ID为list的第二个table
        msgArr = importStruData(sheet,rows,undefined,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[2]).value == "0")  get(changeArr[2]).value = "1";}
        // 高级管理人信息
        sheet = sheets.item(5);
        rows = document.getElementsByName("list")[2].rows; //高级管理人信息是ID为list的第三个table
        msgArr = importStruData(sheet,rows,undefined,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[3]).value == "0")  get(changeArr[3]).value = "1";}
        // 法人家族企业成员信息
        sheet = sheets.item(6);
        rows = document.getElementsByName("list")[3].rows; //法人家族企业成员信息是ID为list的第四个table
        msgArr = importStruData(sheet,rows,undefined,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[4]).value == "0")  get(changeArr[4]).value = "1";}
        // 集团公司
        sheet = sheets.item(7);
        if (typeof sheet.Cells(2,2).value != "undefined") {
            get("upcorpname").value = sheet.Cells(2,2).value;
            changeFlag = true;
        }
        if (typeof sheet.Cells(3,2).value != "undefined") {
            get("upcorpcardno").value = sheet.Cells(3,2).value;
            changeFlag = true;
        }
        if (typeof sheet.Cells(4,2).value != "undefined") {
            get("upcorpborrcode").value = sheet.Cells(4,2).value;
            changeFlag = true;
        }
        if (changeFlag) {if (get(changeArr[5]).value == "0")  get(changeArr[5]).value = "1";};
    }
    // ---------- 关注信息页面 ----------
    else if (tabId == "concern") {
        // 重大诉讼信息
        sheet = sheets.item(8);
        rowCount = getSheetRowsCount(sheet);
        if (rowCount > 10) rowCount = 10; // 最多取10条记录
        defaultLen = document.getElementsByName(addArr[1]).length;//addArr元素请参见addConcern.jsp的importData()方法
        if (rowCount > defaultLen) {
            for (i = 1; i <= rowCount-defaultLen; i++)
                addSue(addArr[0],addArr[1],addArr[2]);
        }
        rows = get("sueTable").rows;
        var msgArr = importStruData(sheet,rows,rowCount,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[0]).value == "0")  get(changeArr[0]).value = "1";}
        // 其他大事信息
        sheet = sheets.item(9);
        rowCount = getSheetRowsCount(sheet);
        if (rowCount > 10) rowCount = 10; // 最多取10条记录
        defaultLen = document.getElementsByName(addArr2[1]).length;//addArr2元素请参见addConcern.jsp的importData()方法
        if (rowCount > defaultLen) {
            for (i = 1; i <= rowCount-defaultLen; i++)
                addIncident(addArr2[0],addArr2[1]);
        }
        rows = get("incidentTable").rows;
        msgArr = importStruData(sheet,rows,rowCount,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[1]).value == "0")  get(changeArr[1]).value = "1";}
    }

    inputBook.Close();
    if (err > 0)
        window.showModelessDialog("/webroot/enterprise/ImportErrMsg.jsp",msg,'dialogWidth:500px');
}

/**
 * 导入概况信息页面数据的方法。
 */
function importGeneralData(sheet,obj,msg,err) {
    // 取得概况信息页面的输入框、下拉列表、单选按钮、文本域对象
    var eles = getArrayByType(getArrayByTagName(obj,"INPUT","SELECT","TEXTAREA"),"text","select-one","textarea","radio");
    var rowCount = getSheetRowsCount(sheet);
    var k;
    var ele;
    var optionIndex;
    for (var i = 0; i < eles.length; i++) {
        ele = eles[i];
        k = getGeneralRowNo(sheet,ele.name,rowCount);
        if (k != -1) {
            var inputValue = sheet.Cells(k,2).value; // 取得文件中的输入值
            var generalErrMsg = "Excel文件\""+sheet.name+"\"sheet中第"+k+"行数据\""+sheet.Cells(k,1).value+"\"下拉列表输入数值不正确！<br>";
            if (typeof inputValue == "undefined")
                continue;
            // ---------- 下拉框 ----------
            if (ele.type == "select-one") {
                if (ele.name == "province") {
                    // 地区下拉列表需要从省做集联处理
                    optionIndex = reflectSelect(inputValue,ele);
                    if (optionIndex == -1) {
                        msg += generalErrMsg;
                        err++;
                    } else {
                        provTOtown("province","city","country",sheet.Cells(k+2,2).value);
                    }
                } else if (ele.name == "country" || ele.name == "city")
                    continue; // 市和县的下拉框不用处理
                else {
                    optionIndex = reflectSelect(inputValue,ele);
                    if (optionIndex == -1) {
                        msg += generalErrMsg;
                        err++;
                    }
                }
            }
            // ---------- 单选按钮 ----------
            else if (ele.type == "radio") {
                if (ele.name == eles[i-1].name) // 单选按钮不是第一个，不必判断i-1是否下标越界。
                    continue;
                if (inputValue == 1)
                    ele.checked = true;
                else if (inputValue == 2)
                    eles[i+1].checked = true;
                else {
                    msg += "Excel文件\""+sheet.name+"\"sheet中第"+k+"行数据\""+sheet.Cells(k,1).value+"\"单选按钮输入数值不正确！<br>";
                    err++;
                }
            }
            // ---------- 文本框或文本域 ---------- 
            else
                ele.value = inputValue;
        }
    }
    var errArr = new Array();
    errArr[0] = err;
    errArr[1] = msg;
    return errArr;
}

/**
 * 导入资本构成等信息和关注信息页面数据的方法。
 */
function importStruData(sheet,rows,rowCount,msg,err) {
    if (typeof rowCount == "undefined") {
        rowCount = getSheetRowsCount(sheet);
        if (rowCount > 10) rowCount = 10;
    }
    var datas;
    var msg;
    var flag = false;
    var optionIndex;
    if (rowCount > 0) flag = true;
    for (var i = 1; i <= rowCount; i++) {
        var row = rows[i];
        // 取得该行的文本框和下拉列表对象的数组
        datas = getArrayByType(getArrayByTagName(row,"INPUT","SELECT","TEXTAREA"),"text","select-one","textarea");
        for (var j = 0; j < datas.length; j++) {
            if (typeof sheet.Cells(i+1,j+1).value == "undefined")
                continue;
            if (datas[j].type == "select-one") {
                optionIndex = reflectSelect(sheet.Cells(i+1,j+1).value,datas[j]);
                if (optionIndex == -1) {
                    msg += "Excel文件\""+sheet.name+"\"sheet中第"+i+"行数据\""+sheet.Cells(1,j+1).value+"\"下拉列表输入数值不正确！<br>";
                    err++;
                }
            }
            else
                datas[j].value = sheet.Cells(i+1,j+1).value;
        }
    }
    var msgArr = new Array();
    msgArr[0] = err;
    msgArr[1] = msg;
    msgArr[2] = flag;
    return msgArr;
}

/**
 * 
 */
function importFinancialData(sheets) {
    // 资产负债表
    var inputSheet = sheets.item(1);
    var desData = document.getElementById("tab1_Content").getElementsByTagName("input");
    var len = desData.length;
    var inputValue;
    var j = -1;
    var k;
    for (var i = 0; i < len; i++) {
        if (checkOdd(i)) {
            if (i%4 == 0) {
              k = i/2 - j;
              j = j + 1;
            } else {
              k = k + 44;
            }
            inputValue = inputSheet.Cells(k+1,3).value;//期初数
        } else {
            inputValue = inputSheet.Cells(k+1,4).value;//期末数
        }
        if (typeof inputValue != "undefined")
            desData[i].value = inputValue;



    }
    // 由于页面倒数第二行缺一文本框，需要掉换最后四个输入框的值。
    var temp = desData[len-1].value;
    // 掉换倒数第一和倒数第三
    desData[len-1].value = desData[len-3].value;
    desData[len-3].value = temp;
    // 掉换倒数第二和倒数第四
    temp = desData[len-2].value;
    desData[len-2].value = desData[len-4].value;
    desData[len-4].value = temp;

    // 利润及利润分配表
    inputSheet = sheets.item(2);
    desData = document.getElementById("tab2_Content").getElementsByTagName("input");
    len = desData.length;
    for (var i = 0; i < len; i++) {
        if (checkOdd(i)) {
            inputValue = inputSheet.Cells((i/2)+2,3).value;//上期数
        } else {
            inputValue = inputSheet.Cells((i+1)/2+1,4).value;//本期数
        }
        if (typeof inputValue != "undefined")
            desData[i].value = inputValue;



    }

    // 现金流量表
    inputSheet = sheets.item(3);
    desData = document.getElementById("tab3_Content").getElementsByTagName("input");
    var count = getSheetRowsCount(inputSheet);
    for (var i=1,j=0; i <= count; i++) {
        if (i==1 || i==12 || i==23 || i==35 || i==36 || i==54 || i==59)
            continue;
        inputValue = inputSheet.Cells(i+1,3).value;
        if (typeof inputValue != "undefined")
            desData[j].value = inputValue;



        j++;
    }
}


/**
 * 贷款卡子系统概况信息、资本构成等信息、关注信息和财务报表页面数据导入的方法。
 */
function importDataFromExcelQ(inObj,tabId,addArr,addArr2,changeArr) {
    var FILE_FLAG = "#*_CFCC_*#";
    // 取得输入文件路径
    if(!checkPath(inObj))  return;
    var fileDir = trim(inObj.value).replace("\\","/");
    // 取得输入数据的文件
    var oXL = new ActiveXObject("Excel.Application");
    var workbooks = oXL.WorkBooks;
    var inputBook = workbooks.Open(fileDir);
    var sheets = inputBook.Worksheets;

    var count,flagIndex;
    if (tabId == "finance") {
        count = 3;
        flagIndex = 5;
    } else {
        count = 9;
        flagIndex = 4;
    }
    if (sheets.Count < count) {
        alert("所选文件的sheet不足"+count+"个，请选择正确的文件！");
        inputBook.Close();
        return;
    }
    if (sheets.item(1).Cells(1,flagIndex).value != FILE_FLAG) {
        alert("所选文件不是指定的模板文件，请选择正确的文件！");
        inputBook.Close();
        return;
    }

    var sheet;
    var rows;
    var rowCount;
    var defaultLen;
    var msg = "<br>";
    var err = 0;
    // ---------- 财务报表页面 ----------
    if (tabId == "finance") {
        importFinancialDataQ(sheets);
    }
    // ---------- 概况信息页面 ----------
    else if (tabId == "general") {
        sheet = sheets.item(1);
        rows = get("general");
        var errArr = importGeneralData(sheet,rows,msg,err);
        err = errArr[0];
        msg = errArr[1];
    }
    // ---------- 资本构成等信息页面 ----------
    else if (tabId == "capStru") {
        // 注册资本
        sheet = sheets.item(2);
        var changeFlag = false;
        if (typeof sheet.Cells(2,2).value != "undefined") {
            var optionIndex = reflectSelect(sheet.Cells(2,2).value,get("currencycode1"));
            if (optionIndex == -1) {
                msg += "Excel文件\""+sheet.name+"\"sheet中\""+sheet.Cells(2,1).value+"\"下拉列表输入数值不正确！<br>";
                err++;
            }
            changeFlag = true;
        }
        if (typeof sheet.Cells(3,2).value != "undefined") {
            get("contmoney1").value = sheet.Cells(3,2).value;
            changeFlag = true;
        }
        if (changeFlag) {if (get(changeArr[0]).value == "0")  get(changeArr[0]).value = "1";};
        // 资本构成
        sheet = sheets.item(3);
        rowCount = getSheetRowsCount(sheet);
        defaultLen = document.getElementsByName(addArr[1]).length;//addArr元素请参见addCapStru.jsp的importData()方法
        if (rowCount > defaultLen) {
            for (i = 1; i <= rowCount-defaultLen; i++)
                addCapStru(addArr[0],addArr[1],addArr[2],addArr[3]);
        }
        rows = get("capTable").rows;
        var msgArr = importStruData(sheet,rows,rowCount,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[1]).value == "0")  get(changeArr[1]).value = "1";}
        // 对外投资
        sheet = sheets.item(4);
        rows = document.getElementsByName("list")[1].rows; //对外投资是ID为list的第二个table
        msgArr = importStruData(sheet,rows,undefined,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[2]).value == "0")  get(changeArr[2]).value = "1";}
        // 高级管理人信息
        sheet = sheets.item(5);
        rows = document.getElementsByName("list")[2].rows; //高级管理人信息是ID为list的第三个table
        msgArr = importStruData(sheet,rows,undefined,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[3]).value == "0")  get(changeArr[3]).value = "1";}
        // 法人家族企业成员信息
        sheet = sheets.item(6);
        rows = document.getElementsByName("list")[3].rows; //法人家族企业成员信息是ID为list的第四个table
        msgArr = importStruData(sheet,rows,undefined,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[4]).value == "0")  get(changeArr[4]).value = "1";}
        // 集团公司
        sheet = sheets.item(7);
        if (typeof sheet.Cells(2,2).value != "undefined") {
            get("upcorpname").value = sheet.Cells(2,2).value;
            changeFlag = true;
        }
        if (typeof sheet.Cells(3,2).value != "undefined") {
            get("upcorpcardno").value = sheet.Cells(3,2).value;
            changeFlag = true;
        }
        if (typeof sheet.Cells(4,2).value != "undefined") {
            get("upcorpborrcode").value = sheet.Cells(4,2).value;
            changeFlag = true;
        }
        if (changeFlag) {if (get(changeArr[5]).value == "0")  get(changeArr[5]).value = "1";};
    }
    // ---------- 关注信息页面 ----------
    else if (tabId == "concern") {
        // 重大诉讼信息
        sheet = sheets.item(8);
        rowCount = getSheetRowsCount(sheet);
        if (rowCount > 10) rowCount = 10; // 最多取10条记录
        defaultLen = document.getElementsByName(addArr[1]).length;//addArr元素请参见addConcern.jsp的importData()方法
        if (rowCount > defaultLen) {
            for (i = 1; i <= rowCount-defaultLen; i++)
                addSue(addArr[0],addArr[1],addArr[2]);
        }
        rows = get("sueTable").rows;
        var msgArr = importStruData(sheet,rows,rowCount,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[0]).value == "0")  get(changeArr[0]).value = "1";}
        // 其他大事信息
        sheet = sheets.item(9);
        rowCount = getSheetRowsCount(sheet);
        if (rowCount > 10) rowCount = 10; // 最多取10条记录
        defaultLen = document.getElementsByName(addArr2[1]).length;//addArr2元素请参见addConcern.jsp的importData()方法
        if (rowCount > defaultLen) {
            for (i = 1; i <= rowCount-defaultLen; i++)
                addIncident(addArr2[0],addArr2[1]);
        }
        rows = get("incidentTable").rows;
        msgArr = importStruData(sheet,rows,rowCount,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[1]).value == "0")  get(changeArr[1]).value = "1";}
    }

    inputBook.Close();
    if (err > 0)
        window.showModelessDialog("/webroot/enterprise/ImportErrMsg.jsp",msg,'dialogWidth:500px');
}


function getGeneralRowNo(sheet,eleName,rowCount) {
    for (var i = 2; i <= rowCount+1; i++) {
        if (eleName == sheet.Cells(i,4))
            return i;
    }
    return -1;
}

/**
 * 返回Excel一个sheet中数据的行数
 */
function getSheetRowsCount(sheet) {
    var k = 0;
    // 第一行是标题，从第二行开始统计。
    for (var i = 2; i < 100; i++) {
        if (typeof sheet.Cells(i,1).value != "undefined")
            k++;
        else
            break;
    }
    return k;
}

/**
 * 获得对象内指定tagName类型的元素的数组。
 * 目前最多支持3个tagName类型，分别由tagName1、tagName2、tagName3指定。
 * 如需扩充支持类型，需要修改的代码在函数中注释标明了。
 * 调用此方法时，需保证参数obj内所包含的(javascript语法中obj.all返回的)每个元素都具有tagName属性，否则会出错。
 */
function getArrayByTagName(obj,tagName1,tagName2,tagName3) {
    var eles = obj.all;
    var len = eles.length;
    var arr = new Array();
    for (var i = 0,j = 0; i < len; i++) {
        // @TODO:若扩充类型，需修改if的布尔表达式。
        if ((typeof tagName1 != "undefined" && eles[i].tagName==tagName1) || 
            (typeof tagName2 != "undefined" && eles[i].tagName==tagName2) ||
            (typeof tagName3 != "undefined" && eles[i].tagName==tagName3)) {
            arr[j] = eles[i];
            j++;
            // 下拉列表后紧跟着很多OPTION选项，直接跳过他们以减少循环次数。
            if (eles[i].tagName=="SELECT") {
                i = i + eles[i].children.length;
                continue;
            }
        }
    }
    return arr;
}

/**
 * 从数组元素中挑选指定type类型的元素，组成新的数组并返回。
 * 目前最多支持4个type类型，分别由type1、type2、type3、type4指定。
 * 如需扩充支持类型，需要修改的代码在函数中注释标明了。
 * 调用此方法时，需保证arr数组中的每个元素有type属性，否则会出错。
 */
function getArrayByType(arr,type1,type2,type3,type4) {
    var newArr = new Array();
    for (var i = 0,j = 0; i < arr.length; i++) {
        // @TODO:若扩充类型，需修改if的布尔表达式。
        if ((typeof type1 != "undefined" && arr[i].type==type1) ||
            (typeof type2 != "undefined" && arr[i].type==type2) ||
            (typeof type3 != "undefined" && arr[i].type==type3) ||
            (typeof type4 != "undefined" && arr[i].type==type4)) {
            newArr[j] = arr[i];
            j++;
        }
    }
    return newArr;
}

/**
 * 根据输入值获得下拉列表的焦点。（仅支持单一选项下拉列表）
 */
function reflectSelect(inputValue,reflectObj) {
    var k = -1;
    for(var i = 0; i < reflectObj.children.length; i++) {
        if(inputValue == reflectObj.children(i).value) {
            k = i;
            break;
        }
    }
    if (k != -1)
        reflectObj.children(k).selected = true;
    return k;
}

/** 
 * 写卡器写卡时做境外企业的贷款卡编码转换。
 * 把境外企业的贷款卡编码前三个国别字母转换为数字（相应的charCodeAt()值）并返回。
 */
function transLoanCardNoWhenWrite(loanCardNo) {
  
    // 如果贷款卡编码不合法或不是境外企业贷款卡编码，直接返回原参数。
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
 * 读卡器读卡时做境外企业的贷款卡编码转换。
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

    // 如果不是境外借款人的贷款卡编码，直接返回原参数。
    if (trim(parLoanCardNo).length <= 16)  return parLoanCardNo;
    
    // 如果是老系统发的18位贷款卡编码，截取前16返回。
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

/* ---------------- 资本构成和关注信息页面在线增加、删除表格行的代码 ---------------- */
/**
 * 删除被一行文本记录。
 * 此方法资本构成信息、对外投资信息、高级管理人信息、法人家族企业成员信息、重大诉讼信息、其他大事信息通用。
 */
function delCapStru(tableId,radioName,changeFlagStr,isNotNullStr) {
    var radioValue = getRadioValue(radioName);
    if (document.getElementsByName(radioName).length <= 1) {
        alert("至少要保留一行记录！");
        return;
    }
    if (radioValue == "undefined" || radioValue == null) {
        alert("请您选择要删行前的单选按钮！");
        return;
    }
    if (!confirm("您确定要删除此行记录吗？"))  return;
    
    if (document.getElementsByName(isNotNullStr)[radioValue].value != null &&
        document.getElementsByName(isNotNullStr)[radioValue].value != "") {
        
        if (document.getElementById(changeFlagStr).value =="0") {
            document.getElementById(changeFlagStr).value ="1";
        }
    }

    document.getElementById(tableId).deleteRow(radioValue+1);
}

/**
 * 返回被选中的单选按钮的数组下标
 */
function getRadioValue(radioName) {
  for (var i = 0; i < document.getElementsByName(radioName).length; i++) {
      if (document.getElementsByName(radioName)[i].checked) 
          return i;
  }
}

/**
 * 增加资本构成信息的记录行
 */
function addCapStru(tableId,radioName,certTypeSelectStr,currencySelectStr) {
    var newTR = document.getElementById(tableId).insertRow();
    var ftTD = newTR.insertCell();
    var sdTD = newTR.insertCell();
    var thTD = newTR.insertCell();
    thTD.align = "center";
    var foTD = newTR.insertCell();
    ftTD.innerHTML = '<input type=\"radio\" name=\"capRadio\"><input type=\"text\" name=\"contname1\" maxlength=\"80\" class=\"input-text\" style=\"width: 200px\" size=\"20\" onchange=\"setChangeFlagCaptStruct();\">';
    sdTD.innerHTML = '<table><tr><td align="right">贷款卡编码</td><td align="center"><input type=\"text\" name=\"contcardno1\" maxlength=\"16\" class=\"input-text\" style=\"width: 150px\" size=\"20\" onchange=\"setChangeFlagCaptStruct();\"></td></tr><tr><td align="right">组织机构代码</td><td align="center"><input type=\"text\" name=\"contborrcode1\" maxlength=\"10\" class=\"input-text\" style=\"width: 150px\" size=\"20\" onchange=\"setChangeFlagCaptStruct();\"></td></tr><tr><td align="right">登记注册号</td><td align="center"><input type=\"text\" name=\"regino1\" maxlength=\"20\" class=\"input-text\" style=\"width: 150px\" size=\"20\" onchange=\"setChangeFlagCaptStruct();\"></td></tr><tr><td align="right">证件类型</td><td align="center">'+certTypeSelectStr+'</td></tr><tr><td align="right">证件号码</td><td align="center"><input type=\"text\" name=\"capcertno\" maxlength=\"18\" class=\"input-text\" style=\"width:150px\" size=\"20\" onchange=\"setChangeFlagCaptStruct();\"></td></tr></table>';
    thTD.innerHTML = currencySelectStr;
    foTD.innerHTML = '<input type="text" name="contmoney2" maxlength="8" onfocus="javascript:formatToNoComma(this);" onblur="javascript:this.value=commafy(this.value);" style="text-align: right;width: 100px" size="20" onchange="setChangeFlagCaptStruct();"> 万元';
}

/**
 * 增加对外投资信息的记录行
 */
function addInverst(tableId,radioName,currencySelectStr) {
  
    if (document.getElementsByName(radioName).length >= 10) {
        alert("最多10条对外投资信息记录");
        return;
    }
  
    var newTR = document.getElementById(tableId).insertRow();
    var ftTD = newTR.insertCell();
    var sdTD = newTR.insertCell();
    sdTD.align = "center";
    var thTD = newTR.insertCell();
    thTD.align = "center";
    var foTD = newTR.insertCell();
    foTD.align = "center";
    var fiTD = newTR.insertCell();
    fiTD.align = "center";
    ftTD.innerHTML = '<input type="radio" name="invRadio"><input type="text" name="contname2" maxlength="80" class="input-text" style="width: 160px" size="20" onchange="setChangeFlagInvest();">';
    sdTD.innerHTML = '<input type="text" name="contcardno2" maxlength="16" class="input-text" style="width: 150px" size="20" onchange="setChangeFlagInvest();">';
    thTD.innerHTML = '<input type="text" name="contborrcode2" maxlength="10" class="input-text" style="width: 150px" size="20" onchange="setChangeFlagInvest();">';
    foTD.innerHTML = currencySelectStr;
    fiTD.innerHTML = '<input type="text" name="contmoney3" maxlength="8" class="input-text" style="width: 80px" size="20" onchange="setChangeFlagInvest();"> 万元';
}

/**
 * 增加高级管理人信息的记录行
 */
function addMana(tableId,radioName,manaTypeSelectStr,manaGenderSelectStr,manaCertTypeSelectStr,manaEduSelectStr) {
  
    if (document.getElementsByName(radioName).length >= 10) {
        alert("最多10条高级管理人信息记录");
        return;
    }
  
    var newTR = document.getElementById(tableId).insertRow();
    var ftTD = newTR.insertCell();
    var sdTD = newTR.insertCell();
    sdTD.align = "center";
    var thTD = newTR.insertCell();
    thTD.align = "center";
    var foTD = newTR.insertCell();
    foTD.align = "center";
    var fiTD = newTR.insertCell();
    fiTD.align = "center";
    var siTD = newTR.insertCell();
    siTD.align = "center";
    var seTD = newTR.insertCell();
    seTD.align = "center";
    var eiTD = newTR.insertCell();
    eiTD.align = "center";
    
    ftTD.innerHTML = '<input type="radio" name="manaRadio">' + manaTypeSelectStr;
    sdTD.innerHTML = '<INPUT type="text" name="maname" maxlength="30" class="input-text" style="width: 120px" size="20" onchange="setChangeFlagManager();">';
    thTD.innerHTML = manaGenderSelectStr;
    foTD.innerHTML = manaCertTypeSelectStr;
    fiTD.innerHTML = '<INPUT type="text" name="macertno" maxlength="18" class="input-text" style="width: 100px" size="20" onchange="setChangeFlagManager();">';
    siTD.innerHTML = '<INPUT type="text" name="mabirthday"  maxlength="10" class="input-text" style="width: 60px" size="20" onchange="setChangeFlagManager();">';
    seTD.innerHTML = manaEduSelectStr;
    eiTD.innerHTML = '<textarea name="maresume" maxlength="500" cols="18" rows="2" wrap="IRTUAL" onchange="setChangeFlagManager();"></textarea>';
}

/**
 * 增加法人家族企业成员信息的记录行
 */
function addFamily(tableId,radioName,fmRalaSelectStr,fmCertTypeSelectStr) {
    
    if (document.getElementsByName(radioName).length >= 10) {
        alert("最多10条法人家族企业成员信息记录");
        return;
    }

    var newTR = document.getElementById(tableId).insertRow();
    var ftTD = newTR.insertCell();
    ftTD.align = "center";
    var sdTD = newTR.insertCell();
    sdTD.align = "center";
    var thTD = newTR.insertCell();
    thTD.align = "center";
    var foTD = newTR.insertCell();
    foTD.align = "center";
    var fiTD = newTR.insertCell();
    fiTD.align = "center";
    var siTD = newTR.insertCell();
    siTD.align = "center";
    
    ftTD.innerHTML = '<input type="radio" name="fmRadio">' + fmRalaSelectStr;
    sdTD.innerHTML = '<INPUT type="text" name="fmname" maxlength="30" class="input-text" style="width: 90px" size="20" onchange="setChangeFlagFamily();">';
    thTD.innerHTML = fmCertTypeSelectStr;
    foTD.innerHTML = '<INPUT type="text" name="fmcertno" maxlength="18" class="input-text" style="width: 150px" size="20" onchange="setChangeFlagFamily();">';
    fiTD.innerHTML = '<INPUT type="text" name="fmcorpname" maxlength="80" class="input-text" style="width: 140px" size="20" onchange="setChangeFlagFamily();"></TD>';
    siTD.innerHTML = '<INPUT type="text" name="fmcorpcardno" maxlength="16" class="input-text" style="width: 140px" size="20" onchange="setChangeFlagFamily();">';
}

/**
 * 增加重大诉讼信息的记录行
 */
function addSue(tableId,radioName,sueCurrencySelectStr) {
    
    if (document.getElementsByName(radioName).length >= 10) {
        alert("最多10条重大诉讼信息记录");
        return;
    }
    
    var newTR = document.getElementById(tableId).insertRow();
    var ftTD = newTR.insertCell();
    ftTD.align = "center";
    var sdTD = newTR.insertCell();
    sdTD.align = "center";
    var thTD = newTR.insertCell();
    thTD.align = "center";
    var foTD = newTR.insertCell();
    foTD.align = "center";
    var fiTD = newTR.insertCell();
    fiTD.align = "center";
    var siTD = newTR.insertCell();
    siTD.align = "center";
    
    ftTD.innerHTML = '<input type="radio" name="sueRadio"><input type="text" name="sueno" maxlength="60" class="input-text" style="width: 130px" onchange="setChangeFlagSue();">';
    sdTD.innerHTML = '<INPUT type="text" name="suedname" maxlength="80" class="input-text" style="width: 140px" onchange="setChangeFlagSue();">';
    thTD.innerHTML = sueCurrencySelectStr + '&nbsp;<INPUT type="text" name="judgeexecmoney" maxlength="20" onfocus="javascript:formatToNoComma(this);" onblur="javascript:this.value=commafy(this.value);" style="text-align:right;width: 90px" size="20" onchange="setChangeFlagSue();">';
    foTD.innerHTML = '<INPUT type="text" name="judgeexecdate" class="input-text" style="width: 80px" size="20" onchange="setChangeFlagSue();">';
    fiTD.innerHTML = '<textarea name="suedreason" cols="12" rows="2" wrap="VIRTUAL" onchange="setChangeFlagSue();"></textarea>';
    siTD.innerHTML = '<textarea name="execresult" size="10" cols="12" rows="2" wrap="VIRTUAL" onchange="setChangeFlagSue();"></textarea>';
}

/**
 * 增加其他大事信息的记录行
 */
function addIncident(tableId,radioName) {
    
    if (document.getElementsByName(radioName).length >= 10) {
        alert("最多10条其他大事信息记录");
        return;
    }
    
    var newTR = document.getElementById(tableId).insertRow();
    var ftTD = newTR.insertCell();
    ftTD.align = "center";
    var sdTD = newTR.insertCell();
    sdTD.align = "center";
    
    ftTD.innerHTML = '<input type="radio" name="incidentRadio"><input type="text" name="incidentno" maxlength="60" class="input-text" style="width: 220px" onchange="setChangeFlagIncident();">';
    sdTD.innerHTML = '<textarea name="incidentdesc" cols="40" rows="2" wrap="VIRTUAL" onchange="setChangeFlagIncident();"></textarea>';
}



/**
 * 本方法只用在支付信用系统
 * 校验输入的贷款卡编码校验位是否合法
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
            //alert("贷款卡编码中不能有小写字母");
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
功能：只用在支付信用  验证组织机构代码是否有效
使用：isCorpno(obj)
返回：bool
==================================================================
*/
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
		   if (financecode.charAt(j) < '0' || financecode.charAt(j) > '9') {
		     //alert("组织机构代码错误!");
		     return false;
		   }
		   c_i[j] = financecode.charCodeAt(j) - '0'.charCodeAt(0);
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
// 给日期yyyymmdd加上分隔符
function formatdate(dateName) {
   var owesForm = document.getElementsByName(dateName)[0].value;
   if(trim(owesForm).length==0)return true;
    var index=owesForm.indexOf("-");
    if(index == -1){
    var year=owesForm.substring(0,4);
    var month=owesForm.substring(4,6);
    var day=owesForm.substring(6);
    document.getElementsByName(dateName)[0].value=year+"-"+month+"-"+day;
    }
}
//若是回车自动给日期格式为yyyymmdd转化成yyyy-mm-dd
function formatdatenter(dateName) {
   if (event.keyCode == "13"){	
      var owesForm = document.getElementsByName(dateName)[0].value;
   if(trim(owesForm).length==0)return true;
    var index=owesForm.indexOf("-");
    if(index == -1){
    var year=owesForm.substring(0,4);
    var month=owesForm.substring(4,6);
    var day=owesForm.substring(6);
    document.getElementsByName(dateName)[0].value=year+"-"+month+"-"+day;
    }
    }
}



function importFinancialDataQ(sheets) {
    // 资产负债表
    
    var inputSheet = sheets.item(1);
    var desData = document.getElementById("tab1_Content").getElementsByTagName("input");
    var len = desData.length;
    var inputValue;
    var j = -1;
    var k;
    for (var i = 0; i < len; i++) {
        if (checkOdd(i)) {
            if (i%4 == 0) {
              k = i/2 - j;
              j = j + 1;
            } else {
              k = k + 31;
            }
           if(i==48)
           { k=44;}
           if(i==50 )
           { k=13;}
            if(i==90||i==94||i==98||i==102||i==106||i==110||i==114){
               inputValue = inputSheet.Cells(k-29,3).value;
            }else if (i==92||i==96||i==100||i==104||i==108||i==112||i==116){
               inputValue = inputSheet.Cells(k+31,3).value;
            }else {
              inputValue = inputSheet.Cells(k+1,3).value;//期初数
            }
        } else {
           if(i==49)
           {k=44;}
            if(i==51)
           { k=13;}          
            if(i==91||i==95||i==99||i==103||i==107||i==111||i==115){
               inputValue = inputSheet.Cells(k-29,4).value;//期末数
            }else if (i==93||i==97||i==101||i==105||i==109||i==113||i==117){
               inputValue = inputSheet.Cells(k+31,4).value;//期末数
            }else {
              inputValue = inputSheet.Cells(k+1,4).value;//期末数
            }
        }
        if (typeof inputValue == "undefined"){
            desData[i].value="";
        }else{
            desData[i].value = inputValue;
        }
    }
      inputValue=inputSheet.Cells(32,3).value;
      if (typeof inputValue == "undefined"){
           inputValue="";
      }
      desData[116].value = inputValue;
      inputValue=inputSheet.Cells(32,4).value;
      if (typeof inputValue == "undefined"){
           inputValue="";
      }
      desData[117].value = inputValue;
      
      inputValue=inputSheet.Cells(61,3).value;
      if (typeof inputValue == "undefined"){
           inputValue="";
      }
      desData[118].value = inputValue;
      inputValue=inputSheet.Cells(61,4).value;
      if (typeof inputValue == "undefined"){
           inputValue="";
      }
      desData[119].value = inputValue;
    // 利润及利润分配表
    inputSheet = sheets.item(2);
    desData = document.getElementById("tab2_Content").getElementsByTagName("input");
    len = desData.length;
    for (var i = 0; i < len; i++) {
        if (checkOdd(i)) {
            inputValue = inputSheet.Cells((i/2)+2,3).value;//上期数
        } else {
            inputValue = inputSheet.Cells((i+1)/2+1,4).value;//本期数
        }
        if (typeof inputValue == "undefined"){
            desData[i].value="";
        }else{
            desData[i].value = inputValue;
        }
    }

    // cash
    inputSheet = sheets.item(3);
    desData = document.getElementById("tab3_Content").getElementsByTagName("input");
    var count = getSheetRowsCount(inputSheet);
    for (var i=0; i <= count; i++) {
        inputValue = inputSheet.Cells(i+1,3).value;
        if (typeof inputValue == "undefined"){
            desData[i].value="";
        }else{
            desData[i].value = inputValue;
        }
        j++;
    }
}


/**
 * 贷款卡子系统概况信息、资本构成等信息、关注信息和财务报表页面数据导入的方法。
 */
function importDataFromExcelS(inObj,tabId,addArr,addArr2,changeArr) {
    var FILE_FLAG = "#*_CFCC_*#";
    // 取得输入文件路径
    if(!checkPath(inObj))  return;
    var fileDir = trim(inObj.value).replace("\\","/");
    // 取得输入数据的文件
    var oXL = new ActiveXObject("Excel.Application");
    var workbooks = oXL.WorkBooks;
    var inputBook = workbooks.Open(fileDir);
    var sheets = inputBook.Worksheets;

    var count,flagIndex;
    if (tabId == "finance") {
        count = 2;
        flagIndex = 5;
    } else {
        count = 9;
        flagIndex = 4;
    }
    if (sheets.Count > count) {
        alert("所选文件的sheet多于"+count+"个，请选择正确的文件！");
        inputBook.Close();
        return;
    }
    if (sheets.item(1).Cells(1,flagIndex).value != FILE_FLAG) {
        alert("所选文件不是指定的模板文件，请选择正确的文件！");
        inputBook.Close();
        return;
    }

    var sheet;
    var rows;
    var rowCount;
    var defaultLen;
    var msg = "<br>";
    var err = 0;
    // ---------- 财务报表页面 ----------
    if (tabId == "finance") {
        importFinancialDataS(sheets);
    }
    // ---------- 概况信息页面 ----------
    else if (tabId == "general") {
        sheet = sheets.item(1);
        rows = get("general");
        var errArr = importGeneralData(sheet,rows,msg,err);
        err = errArr[0];
        msg = errArr[1];
    }
    // ---------- 资本构成等信息页面 ----------
    else if (tabId == "capStru") {
        // 注册资本
        sheet = sheets.item(2);
        var changeFlag = false;
        if (typeof sheet.Cells(2,2).value != "undefined") {
            var optionIndex = reflectSelect(sheet.Cells(2,2).value,get("currencycode1"));
            if (optionIndex == -1) {
                msg += "Excel文件\""+sheet.name+"\"sheet中\""+sheet.Cells(2,1).value+"\"下拉列表输入数值不正确！<br>";
                err++;
            }
            changeFlag = true;
        }
        if (typeof sheet.Cells(3,2).value != "undefined") {
            get("contmoney1").value = sheet.Cells(3,2).value;
            changeFlag = true;
        }
        if (changeFlag) {if (get(changeArr[0]).value == "0")  get(changeArr[0]).value = "1";};
        // 资本构成
        sheet = sheets.item(3);
        rowCount = getSheetRowsCount(sheet);
        defaultLen = document.getElementsByName(addArr[1]).length;//addArr元素请参见addCapStru.jsp的importData()方法
        if (rowCount > defaultLen) {
            for (i = 1; i <= rowCount-defaultLen; i++)
                addCapStru(addArr[0],addArr[1],addArr[2],addArr[3]);
        }
        rows = get("capTable").rows;
        var msgArr = importStruData(sheet,rows,rowCount,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[1]).value == "0")  get(changeArr[1]).value = "1";}
        // 对外投资
        sheet = sheets.item(4);
        rows = document.getElementsByName("list")[1].rows; //对外投资是ID为list的第二个table
        msgArr = importStruData(sheet,rows,undefined,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[2]).value == "0")  get(changeArr[2]).value = "1";}
        // 高级管理人信息
        sheet = sheets.item(5);
        rows = document.getElementsByName("list")[2].rows; //高级管理人信息是ID为list的第三个table
        msgArr = importStruData(sheet,rows,undefined,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[3]).value == "0")  get(changeArr[3]).value = "1";}
        // 法人家族企业成员信息
        sheet = sheets.item(6);
        rows = document.getElementsByName("list")[3].rows; //法人家族企业成员信息是ID为list的第四个table
        msgArr = importStruData(sheet,rows,undefined,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[4]).value == "0")  get(changeArr[4]).value = "1";}
        // 集团公司
        sheet = sheets.item(7);
        if (typeof sheet.Cells(2,2).value != "undefined") {
            get("upcorpname").value = sheet.Cells(2,2).value;
            changeFlag = true;
        }
        if (typeof sheet.Cells(3,2).value != "undefined") {
            get("upcorpcardno").value = sheet.Cells(3,2).value;
            changeFlag = true;
        }
        if (typeof sheet.Cells(4,2).value != "undefined") {
            get("upcorpborrcode").value = sheet.Cells(4,2).value;
            changeFlag = true;
        }
        if (changeFlag) {if (get(changeArr[5]).value == "0")  get(changeArr[5]).value = "1";};
    }
    // ---------- 关注信息页面 ----------
    else if (tabId == "concern") {
        // 重大诉讼信息
        sheet = sheets.item(8);
        rowCount = getSheetRowsCount(sheet);
        if (rowCount > 10) rowCount = 10; // 最多取10条记录
        defaultLen = document.getElementsByName(addArr[1]).length;//addArr元素请参见addConcern.jsp的importData()方法
        if (rowCount > defaultLen) {
            for (i = 1; i <= rowCount-defaultLen; i++)
                addSue(addArr[0],addArr[1],addArr[2]);
        }
        rows = get("sueTable").rows;
        var msgArr = importStruData(sheet,rows,rowCount,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[0]).value == "0")  get(changeArr[0]).value = "1";}
        // 其他大事信息
        sheet = sheets.item(9);
        rowCount = getSheetRowsCount(sheet);
        if (rowCount > 10) rowCount = 10; // 最多取10条记录
        defaultLen = document.getElementsByName(addArr2[1]).length;//addArr2元素请参见addConcern.jsp的importData()方法
        if (rowCount > defaultLen) {
            for (i = 1; i <= rowCount-defaultLen; i++)
                addIncident(addArr2[0],addArr2[1]);
        }
        rows = get("incidentTable").rows;
        msgArr = importStruData(sheet,rows,rowCount,msg,err);
        err = msgArr[0];
        msg = msgArr[1];
        changeFlag = msgArr[2];
        if (changeFlag) {if (get(changeArr[1]).value == "0")  get(changeArr[1]).value = "1";}
    }

    inputBook.Close();
    if (err > 0)
        window.showModelessDialog("/webroot/enterprise/ImportErrMsg.jsp",msg,'dialogWidth:500px');
}


function importFinancialDataS(sheets) {
    // 资产负债表
    var inputSheet = sheets.item(1);
    var desData = document.getElementById("tab1_Content").getElementsByTagName("input");
    var len = desData.length;
    var inputValue;
    var j = 0;
    var k=0;
    for (var i = 0; i < len; i++) {
        if (checkOdd(i)) {
            if (i%4 == 0) {
              k = i/2 - j;
              j = j + 1;
            } else {
             k = k + 32;
            }
            inputValue = inputSheet.Cells(k+1,3).value;//期初数
        } else {
            inputValue = inputSheet.Cells(k+1,4).value;//期末数
        }
        if (typeof inputValue == "undefined"){
            desData[i].value ="";
        }else{
            desData[i].value = inputValue;
        }
    }
   
    // 利润及利润分配表
    inputSheet = sheets.item(2);
    desData = document.getElementById("tab2_Content").getElementsByTagName("input");
    len = desData.length;
    var j=0;
    for (var i = 0; i < len; i++) {
         if(i%3==0){
            k =i+2-2*j;
            j=j+1
         }else{
            k=k+20;
         }
        inputValue = inputSheet.Cells(k,3).value;
        if (typeof inputValue == "undefined"){
            desData[i].value ="";
        }else{
            desData[i].value = inputValue;
        }
    }
}



//新增---------------------
function checkNulToZero(data){

        if(!checkNull(data)){
           return 0.00;
        }
        return data;
}

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
            ereg=/^[1-9][0-9]{5}[1-9][0-9]{3}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;//闰年出生日期的合法性正则表达式
        } else {
            ereg=/^[1-9][0-9]{5}[1-9][0-9]{3}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;//平年出生日期的合法性正则表达式
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



function isCorpNo(financecode) {

  if (!checkstring_allSpace(financecode)) {
    alert("错误：您输入的都是空格！");
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
     alert("组织机构代码错误!");
     return false;
   }

   re = /[A-Z0-9]{8}-[A-Z0-9]/;    
   r = code.match(re);   
   if (r == null) {
	   alert("组织机构代码错误!");
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
	     alert("组织机构代码错误!");
		   return false;
	   }

	   for (i = 0; i < 10; i++) {
		   c = financecode.charAt(i);
		   if (c <= 'z' && c >= 'a') {
		     alert("组织机构代码错误!");
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
			  alert("组织机构代码错误!");
			  return false;
			}
	   }

	   s = s + w_i[0] * c_i[0];

	   if (sec_value >= 'A'.charCodeAt(0) && sec_value <= 'Z'.charCodeAt(0)) {
		   c_i[1] = sec_value + 32 - 87;
	   } else if (sec_value >= '0'.charCodeAt(0) && sec_value <= '9'.charCodeAt(0)) {
		   c_i[1] = sec_value - '0'.charCodeAt(0);
	   } else {
	     alert("组织机构代码错误!");
		   return false;
	   }

	   s = s + w_i[1] * c_i[1];
	   for (j = 2; j < 8; j++) {
		   if (financecode.charCodeAt(j) >= 'A'.charCodeAt(0) && financecode.charCodeAt(j) <= 'Z'.charCodeAt(0)) {
             c_i[j] = financecode.charCodeAt(j) + 32 - 87;
           } else if (financecode.charCodeAt(j) >= '0'.charCodeAt(0) && financecode.charCodeAt(j) <= '9'.charCodeAt(0)) {
             c_i[j] = financecode.charCodeAt(j) - '0'.charCodeAt(0);
           } else {
            alert("组织机构代码错误!");
            return false;
           }
		   s = s + w_i[j] * c_i[j];
	   }

	   c = 11 - (s % 11);

	   if (!((financecode.charAt(9) == 'X' && c == 10) ||
			 (c == 11 && financecode.charAt(9) == '0') || (c == financecode.charCodeAt(9) - '0'.charCodeAt(0)))) {
			  alert("组织机构代码错误!");
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
            alert("组织机构代码错误!");
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

function chksafe(name){
	for(var i=0; i<name.length; i++)
	{
		var charValue = name.charAt(i);
		if (!(charValue >= 'A' && charValue <= 'Z') && !(charValue>='0' && charValue<='9') && !(charValue >= 'a' && charValue <= 'z')) {
			if(!(/^[\u4e00-\u9fa5]+$/i).test(charValue)){   
				return false;
			}
		}
	}
	return true;
}