// 校验自定义方法（可复用）

 /** [checkCard 身份证校验]
 * 
 * @param {[Object]} field [当前校验元素] @param {[Object]} rules [当前元素所含所有校验] @param
 * {[Number]} i [当前自定义校验在html中的index] @param {[Object]} options [校验框架对象] @return
 * {[Boolean/String]} [验证通过返回ture,否则返回提示信息]
 */
/*function checkCard(field, rules, i, options) {
	var arr = returnObject(field, options, "checkCard");
	console.log(arr);
	// 身份类型-自然人
	var brerTypePerson = 1;
	// 获得身份类型
	var brerType = arr[0].brerType;
	var brerTypeValue;
	
	if (brerType != undefined) {
		brerTypeValue = brerType.value;
	}
	if (brerType == undefined || brerTypeValue == brerTypePerson) {
		// 证件号码
		var idCard = arr[1].value;
		// 证件号码的长度
		var idCardLength = idCard.length;
		// 取到Id
		var idType = arr[0].idType.value;
		// 校验身份证件
		if (idType == 0) {
			// 身份证校验的正则表达式
			var isIdCard = checkIdCard(field,idCard);
			if (isIdCard == false) {
				return arr[2].alertText2;
			} else {
				return true;
			}
		}
		if (idType == 5) {
			var isHkmacidpass = hkmacidpassValidator(idCard);
			if (isHkmacidpass == false) {
				return arr[2].alertText3;
			} else {
				return true;
			}
		}
		// 护照代码
		if (idType == 2) {
			return passportValidator(arr, idCard, idCardLength);
		}
		// 台湾居民往来大陆通行证代码

		if (idType == 6) {
			var idTypeRocValidatorpass = idTypeRocValidator(idCard);
			if (idTypeRocValidatorpass == false) {
				return arr[2].alertText5;
			} else {
				return true;
			}
		}
	} else {
		return true;
	}
}*/

function checkCard(field, rules, i, options) {
	var arr = returnObject(field, options, "checkCard");
	//console.log(arr);
	// 证件号码
	var idCard = arr[1].value;
	// 证件号码的长度
	var idCardLength = idCard.length;
	// 取到证件类型
	var idType = arr[0].idType;
	if(!idType){
		return true;
	}
	idType = arr[0].idType.value;
	// 校验身份证件
	if (idType == 0) {
		// 身份证校验的正则表达式
		var isIdCard = checkIdCard(field,idCard);
		if (isIdCard == false) {
			return arr[2].alertText2;
		} else {
			return true;
		}
	}else if (idType == 5) {
		var isHkmacidpass = hkmacidpassValidator(idCard);
		if (isHkmacidpass == false) {
			return arr[2].alertText3;
		} else {
			return true;
		}
	}else if (idType == 6) {
		// 台湾居民往来大陆通行证代码
		var idTypeRocValidatorpass = idTypeRocValidator(idCard);
		if (idTypeRocValidatorpass == false) {
			return arr[2].alertText5;
		} else {
			return true;
		}
	}else{
		return true;
	}
}

function checkThreeOrg(field, rules, i, options) {
	var arr = returnObject(field, options,"orgCode");
	// 证件号码
	var idCard = arr[1].value;
	// 证件类型
	var idType = arr[0].idType.value;
		// 中征码（原贷款卡编码）
		if (idType == 10) {
			return  checkLoanCardnorules(idCard, arr);
		}
		// 组织机构代码
		if (idType == 30) {
			return isCorpNo(idCard,arr);
		}
		// 统一社会信用代码
		if (idType == 20) {
		
			return  isCCode(idCard, arr);
		}

}

/**
 * [passportValidator 护照]
 * 
 * @param {[type]}
 *            arr [系统对象]
 * @param {[type]}
 *            idCard [证件号码]
 * @param {[type]}
 *            idCardLength [证件号码长度]
 * @return {[type]} [证件号码合法时返回true，证件号码不合法时返回提示信息]
 */
function passportValidator(arr, idCard, idCardLength) {
	var validatMsg;
	if (idCardLength > 3 && idCardLength <= 12) {
		var projectName = getProjectName(location.href);
		ajaxFunc(projectName + "common/dic?type=PD_NATION", function(data) {
			var index = idCard.substring(0, 3);
			var jsonValue = data[index];
			if (jsonValue == undefined) {
				validatMsg = arr[2].alertText4;
			} else {
				validatMsg = true;
			}
		}, function(data) {
			layerMsg("请求出错！");
		});
		return validatMsg;
	} else {
		validatMsg = arr[2].alertText4;
		return validatMsg;
	}
}

/**
 * [idTypeRocValidator 台湾居民往来大陆通行证]
 * 
 * @param {[type]}
 *            arr [系统对象]
 * @param {[type]}
 *            idCard [证件号码]
 * @param {[type]}
 *            idCardLength [证件号码长度]
 * @return {[type]} [证件号码合法时返回true，证件号码不合法时返回提示信息]
 */
function idTypeRocValidator(idCard){
	 var re1 = /^([0-9]{8}|[0-9]{10}[0-9A-Za-z]?[0-9A-Za-z]?)$/;
     var re2 = /^[0-9]{10}\([A-Za-z]\)$/;
     var re3 = /^[0-9]{10}\([0-9]{2}\)$/;
     if(re1.test(idCard)||re2.test(idCard)||re3.test(idCard)){
     	return true;
     }else{
     	return false;
     }
}
/*function idTypeRocValidator(arr, idCard, idCardLength) {
	var isNumber = /^[0-9]*$/;
	if (idCardLength == 8) {
		if (!isNumber.test(idCard)) {
			return false;
		}else{
			return true;
		}
	}
	if (idCardLength >= 10 && idCardLength <= 14) {
		var beforeNum = idCard.substring(0,10);
		var afterNum = idCard.substring(10);
		if (!isNumber.test(beforeNum)) {
			return false;
		}else{
			if (idCardLength == 13) {
				var rex13 = /^\([0-9]\)$/;
				if (!rex13.test(afterNum)) {
					return false;
				}else{
					return true;
				}
			}else if(idCardLength == 14){
				var rex14 = /^\([A-Za-z]{2,3}\)$/;
				if (!rex14.test(afterNum)) {
					return false;
				}else{
					return true;
				}
			}else{
				return true;
			}
		}
	} else {
		console.log("证件号码的长度不合法");
		return false;
	}
};*/

/**
 * [getParityBit 身份证号码15位转18位第18位号码算法]
 * @param  {[String]} id17     [前17位身份证号码]
 * @param  {[Array]} powers    [加权数组]
 * @param  {[Array]} parityBit [第18位校检码数组]
 * @return {[type]}            [身份证号第18位号码]
 */
function getParityBit(id17,powers,parityBit){
	     /*加权 */
	     var power = 0;
	     for(var i=0;i<17;i++){
	       power += parseInt(id17.charAt(i),10) * parseInt(powers[i]);
	     }              
	     /*取模*/ 
	     var mod = power % 11;
	     return parityBit[mod];
}

/**
 * [idLen15To18 身份证号码15位转18位]
 * @param  {[String]} idcard 	[页面取值输入的身份证号码]
 * @return {[Boolean/String]}   [18位身份证号码或false]
 */
 function idLen15To18(field,idcard){
 	  /*每位加权因子*/
	  var powers = ["7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2"];
	  
	  /*第18位校检码*/
	  var parityBit = ["1","0","X","9","8","7","6","5","4","3","2"];
  
	   if(idcard.length==15){
	       var id17 = idcard.substring(0,6) + '19' + idcard.substring(6);
	       var id18 = getParityBit(id17,powers,parityBit);
	       if(!field.hasClass("idCard15")){
	       		field.addClass("idCard15");
	       }
	       return id17 + id18;
	   }else if(idcard.length==18){
	       return idcard;
	   }else{
	       return false;
	   }
  }


/**
 * [checkIdCard 校验身份证]
 * 
 * @param {[type]}
 *            idcard [证件号码]
 * @return {[type]} [根据大系统的身份证校验代码判断 15位身份证只校验长度 每一位必须为整数 18位弱校验]
 */
function checkIdCard(field,idcard) {

	if (idcard.length > 0 && idcard != null) {
		// 15位身份证号转换18位
		idcard = idLen15To18(field,idcard);
		if (!idcard) {
			return false;
		}
		var re = /^\d{17}([0-9]|X)$/;
		var idcard, Y, JYM;
		var S, M;
		var idcard_array = new Array();
		idcard_array = idcard.split("");
		// 先判断18位身份证是否通过基本校验： 如长度 ，每一位是否为整数 以及18位最后一位是否为"X"
		if (!re.test(idcard)) {
			return false;
		}
		// 再判断18身份证的出生日期与校验位

		// 判断出生日期是否合法
		var date = idcard.substr(6, 4) + idcard.substr(10, 2)
				+ idcard.substr(12, 2);

		if (isDate(date)) {

			// 计算校验位
			S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10])) * 7
					+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
					* 9
					+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
					* 10
					+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
					* 5
					+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
					* 8
					+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
					* 4
					+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
					* 2 + parseInt(idcard_array[7]) * 1
					+ parseInt(idcard_array[8]) * 6 + parseInt(idcard_array[9])
					* 3;
			Y = S % 11;
			M = "F";
			JYM = "10X98765432";

			// 判断校验位是否合法
			M = JYM.substr(Y, 1);
			if (M == idcard_array[17]) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	} else {
		return false;
	}
};
/**
 * isDate getMaxDay isNumber 身份证校验用到的三个函数 isDate 判断生日的合法性 getMaxDay 取得天的最大数
 * 包括平年和闰年 isNumber 校验整数
 */
function isDate(date) {
	var fmt = "yyyyMMdd";
	var yIndex = fmt.indexOf("yyyy");
	if (yIndex == -1)
		return false;
	var year = date.substring(yIndex, yIndex + 4);
	var mIndex = fmt.indexOf("MM");
	if (mIndex == -1)
		return false;
	var month = date.substring(mIndex, mIndex + 2);
	var dIndex = fmt.indexOf("dd");
	if (dIndex == -1)
		return false;
	var day = date.substring(dIndex, dIndex + 2);
	if (!isNumber(year) || year > "2100" || year < "1900")
		return false;
	if (!isNumber(month) || month > "12" || month < "01")
		return false;
	if (day > getMaxDay(year, month) || day < "01")
		return false;
	return true;

}
function getMaxDay(year, month) {
	if (month == 4 || month == 6 || month == 9 || month == 11)
		return "30";
	if (month == 2)
		if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0)
			return "29";
		else
			return "28";
	return "31";
}
function isNumber(s) {
	var regu = "^[0-9]+$";
	var re = new RegExp(regu);
	if (s.search(re) != -1) {
		return true;
	} else {
		return false;
	}
}
/*
 * function checkIdCard(idcard) { var re = /(^\d{15}$)|(^\d{17}([0-9]|X|x)$)/;
 * var idcard, Y, JYM; var S, M; var idcard_array = new Array();
 * //console.log("idcard：：：：：" + JSON.stringify(idcard)); idcard_array =
 * idcard.split(""); if (idcard.length > 0 && idcard != null) { //
 * 先判断15或18位身份证的基本校验 如长度 每一位是否为整数 以及18位最后一位是否为X if (!re.test(idcard)) { return
 * false; } return true; } };
 */

/**
 * [hkmacidpassValidator 校验港澳居民往来大陆通行证]
 * 
 * @param {[type]}
 *            idcard [证件号码]
 * @return {[type]} [description]
 */
function hkmacidpassValidator(idcard) {
	var re = /^[HM]{1}([0-9]{10}|[0-9]{8})$/;
	if(!re.test(idcard)){
		return false;	
	} else {
		return true;
	}
}
/*function hkmacidpassValidator(idcard) {
	var idCardLength = idcard.length;
	if (idCardLength == 9 || idCardLength == 11) {
		var first_index = idcard.substr(0, 1);
		if (first_index == 'M' || first_index == 'H') {
			var isNumber = /^[0-9]*$/;
			var last_index = idcard.substring(1);
			console.log(last_index);
			if (isNumber.test(last_index)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	} else {
		return false;
	}
};*/

/**
 * [checkNwBussNum 标识变更校验新业务号与旧业务号不能相同]
 */
function checkNwBussNum(field, rules, i, options) {
	var arr = returnObject(field, options, "checkNwBussNum");
	// 新业务号
	var nwBussNum = arr[1].value;
	// 原业务号
	var odBussNum = arr[0].odBussNum.value;
	if (nwBussNum == odBussNum) {
		return arr[2].alertText;
	} else {
		return true;
	}
}


/**
 * [orgCode 组织机构校验]
 * 
 * @param {[type]}
 *            field [description]
 * @param {[type]}
 *            rules [description]
 * @param {[type]}
 *            i [description]
 * @param {[type]}
 *            options [description]
 * @return {[type]} [description]
 */
function orgCode(field, rules, i, options) {
	console.log("---------------orgCode-------------");
	var arr = returnObject(field, options, "orgCode");
	console.log("---------------orgCode-------------"+arr);
	// 身份类型-组织机构
	var brerOrg = 2;
	var brerType = "";
	// 获得校验身份类型
	if (arr[0].brerType != undefined) {
		brerType = arr[0].brerType.value;
	}
	/*else{
		return false;
	}*/
	// 证件号码
	var idCard = arr[1].value;
	// 证件类型
	var idType = arr[0].idType.value;
	if (brerType == brerOrg || brerType == "") {
		// 中征码（原贷款卡编码）
		if (idType == 10) {
			return checkLoanCardnorules(idCard, arr);
		}
		// 组织机构代码
		if (idType == 30) {
			return isCorpNo(idCard, arr);
		}
		// 统一社会信用代码
		if (idType == 20) {

			return isCCode(idCard, arr);
		}

	}
}
/*中征码校验  (企业征信监管)*/
function orgLoanCard(field, rules, i, options) {
	var arr = returnObject(field, options, "orgLoanCard");
	var idCard = arr[1].value;
	return checkLoanCardno(idCard, arr);	
}
/*机构信用代码校验  (企业征信监管)*/
function checkCreditCode(field, rules, i, options) {
	var arr = returnObject(field, options, "checkCreditCode");
	var idCard = arr[1].value;
	return checkCreditCard(idCard, arr);	
}
/*组织机构代码校验   (企业征信监管)*/
function checkCorpNo(field, rules, i, options) {
	var arr = returnObject(field, options, "checkCorpNo");
	var idCard = arr[1].value;
	return isCorpNocode(idCard, arr);	
}
/*统一社会信用代码校验   (企业征信监管)*/
function checkisCCode(field, rules, i, options) {
	var arr = returnObject(field, options, "checkisCCode");
	var idCard = arr[1].value;
	return isCCodeNo(idCard, arr);	
}
/**
 * [checkLoanCardno 中证码]
 * 
 * @param {[type]}
 *            loanCardNo [description]
 * @return {[type]} [description]
 */
/*校验输入的中征码校验位是否合法*/
 function checkLoanCardno(loanCardNo,arr) { 
     if (trim(loanCardNo).length != 16)
    	 return arr[2].alertText10;
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
        	return arr[2].alertText10;
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
    	return arr[2].alertText10;
 }
 
	/**
	 * 求字符的数值
	 */
	function  char2num(a) {
		if (a == '*')
			return 36;
		if (a >= 0 && a <= 9) {
			return a;
		} else{
			//a.replace("/[^\d]/g,");
			return (a.charCodeAt(0) - 55);
		}
		
	}
 /**
	 * 验证机构信用代码是否合法
	 * @param code 整个代码含最后一位校验位
	 * @return 
	 */
	var pattern = /[A-Z]{1}[0-9]{16}[0-9A-Z\\*]{1}/;  
	function isValid(code){
		if (code.length != 18 || !pattern.exec(code))
			return false;
		var m = 36;
		var s = m;
		for(var i=0; i<=code.length-2; i++){
			s = (parseInt(s) + parseInt(char2num(code.toUpperCase().substring(i,i+1))))%m;
			if(s==0)s=m;
			s = (s*2)%(parseInt(m)+1);
		}
		return (parseInt(s)+parseInt(char2num(code.substring(code.length-1,code.length))))%m == 1 ;
	}
	function checkCreditCard(creditCode,arr){
		if(creditCode.length !=18){
			return arr[2].alertText1;
		}
		if(!isValid(creditCode)){
			return arr[2].alertText2;
		}
		return true;
	}
/**
	 * [isCorpNocode 组织机构代码]
	 * @param idCard
	 * @param arr
	 * @returns
	 */
	function isCorpNocode(idCard, arr){
		var idCard = trim(idCard);
		if (idCard == ""){
			return true;
		}
		var ws = [3, 7, 9, 10, 5, 8, 4, 2];
		var str = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
		var reg = /^([0-9A-Z]){8}-[0-9|X]$/;
		if (!reg.test(idCard)) {
			return arr[2].alertText30;
		}
		var sum = 0;
		for (var i = 0; i < 8; i++) {
		sum += str.indexOf(idCard.charAt(i)) * ws[i];
		}
		var c9 = 11 - (sum % 11);
		if(c9==10){
		   c9='X';
		}else if(c9==11){
		   c9='0';
		}
		if(c9 != idCard.charAt(9)){
			return arr[2].alertText30;
		}
		return true;
	}
/**
	 * [isCCodeNo 统一社会信用代码]
	 * @param code
	 * @param arr
	 * @returns
	 */
	function isCCodeNo(Code, arr) {
		  var patrn = /^[0-9A-Z]+$/;
		  //18位校验及大写校验
		  if ((Code.length != 18) || (patrn.test(Code) == false)) {
			  return arr[2].alertText30;
		  }
		  else {
		    var Ancode;//统一社会信用代码的每一个值
		    var Ancodevalue;//统一社会信用代码每一个值的权重 
		    var total = 0;
		    var weightedfactors = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];//加权因子 
		    var str = '0123456789ABCDEFGHJKLMNPQRTUWXY';
		    //不用I、O、S、V、Z 
		    for (var i = 0; i < Code.length - 1; i++) {
		      Ancode = Code.substring(i, i + 1);
		      Ancodevalue = str.indexOf(Ancode);
		      total = total + Ancodevalue * weightedfactors[i];
		      //权重与加权因子相乘之和 
		    }
		    var logiccheckcode = 31 - total % 31;
		    if (logiccheckcode == 31) {
		       logiccheckcode = 0;
		    }
		    var Str = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,T,U,W,X,Y";
		    var Array_Str = Str.split(',');
		    logiccheckcode = Array_Str[logiccheckcode];
		    var checkcode = Code.substring(17, 18);
		    if (logiccheckcode != checkcode) {
		    	return arr[2].alertText30;
		    }
		    return true;
		  }
	 }

 /**
  * [checkLoanCardnorules 中证码]
  * 
  * @param {[type]}
  *            loanCardNo [description]
  * @return {[type]} [description]
  */
function checkLoanCardnorules(loanCardNo, arr) {
	if (trim(loanCardNo).length != 16)
		return arr[2].alertText10;
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
			return arr[2].alertText10;
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
		var tempValue = checkCode.substring(j, j + 1);
		if (tempValue >= "A" && tempValue <= "Z") {
			checkValue[j] = tempValue.charCodeAt() - 55;
		} else {
			checkValue[j] = tempValue;
		}

		totalValue = totalValue + weightValue[j] * checkValue[j];
	}

	for (var j = 3; j < 14; j++) {
		checkValue[j] = checkCode.substring(j, j + 1);
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
		return arr[2].alertText10;
}

/**
 * [isCCode 统一社会信用代码码]
 * 
 * @param {[type]}
 *            creditCode [description]
 * @return {Boolean} [description]
 */
//function isCCode(Code,arr){
//	var patrn = /^[0-9A-Z]+$/;
//	// 18位校验及大写校验
//	if ((Code.length != 18) || (patrn.test(Code) == false)){ 
//		return arr[2].alertText20;
//	}else{ 
//　　　　var Ancode;// 统一社会信用代码的每一个值
// 　　　 var Ancodevalue;// 统一社会信用代码每一个值的权重
//　　　　var total = 0; 
//　　　　var weightedfactors = [1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28];// 加权因子
//　　　　var str = '0123456789ABCDEFGHJKLMNPQRTUWXY';
//　　　　// 不用I、O、S、V、Z
//　　　　for (var i = 0; i < Code.length - 1; i++) 
//　　　　{
// 　　　　Ancode = Code.substring(i, i + 1); 
//　　　　Ancodevalue = str.indexOf(Ancode); 
//　　　　total = total + Ancodevalue * weightedfactors[i];
//　　　　// 权重与加权因子相乘之和
//　　　　}
// 　　　　var logiccheckcode = 31 - total % 31;
//　　　　if (logiccheckcode == 31)
//　　　　{
//　　　　　　logiccheckcode = 0;
//　　　　}
//　　　　var Str = "0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,J,K,L,M,N,P,Q,R,T,U,W,X,Y";
//　　　　var Array_Str = Str.split(',');
//　　　　logiccheckcode = Array_Str[logiccheckcode];
//
//
//　　　　 var checkcode = Code.substring(17, 18);
//　　　　 if (logiccheckcode != checkcode){ 
//　　　　　　return arr[2].alertText20;
// 　　　　}
// 		 return true;
// 　　} 
//}

//  	function isCCode(creditCode,arr) { 
//  		var codelength = creditCode.length; //校验机构信用码长度为18位 
//  		if (codelength != 18) { 
//  			return arr[2].alertText20; 
//  		} // 取前17位
//  		var codes = (trim(creditCode).substring(0, 17)).toUpperCase(); 
//  		var codesArr = toArray(codes); // 取第18位 var valid = trim(creditCode).substring(17, 18);
//  		var m = 36; 
//  		var p = m; 
//  		var s = 0;
//  		for (i = 0; i < codesArr.length; i++) { 
//  			s = p % (m + 1) +char2num(codesArr[i]); 
//  			if (s % m == 0) { 
//  				p = 72; 
//  			} else {
//  				p = s % m  2; 
//  			} 
//  		}
//  		s = (37 - p) % 36;
//  		if (s < 0) 
//  			s += 37; 
//  		if (s == 36) 
//  			s = 0;
//  		var v = num2char(s);
//  		if (valid != v) { 
//  			return arr[2].alertText20; 
//  		}
//  		return true; 
//  	};
 

/**
 * [isCorpNo 组织机构代码]
 * @param idCard
 * @param arr
 * @returns
 */
function isCorpNo(idCard, arr){
	var idCard = trim(idCard);
	if (idCard == ""){
		return true;
	}
	var ws = [3, 7, 9, 10, 5, 8, 4, 2];
	var str = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	var reg = /^([0-9A-Z]){8}-[0-9|X]$/;
	if (!reg.test(idCard)) {
		return arr[2].alertText30;
	}

	var sum = 0;
	for (var i = 0; i < 8; i++) {
	sum += str.indexOf(idCard.charAt(i)) * ws[i];
	}
	var c9 = 11 - (sum % 11);
	if(c9==10){
	   c9='X';
	}else if(c9==11){
	   c9='0';
	}
	if(c9 != idCard.charAt(9)){
		return arr[2].alertText30;
	}
	return true;
}

///**
// * [isCorpNo 组织机构代码]
// * 
// * @param {[type]}
// *            financecode [description]
// * @param {[type]}
// *            arr [description]
// * @return {Boolean} [description]
// */
//function isCorpNo(financecode, arr) {
//	var financecode = trim(financecode);
//	if (financecode == "")
//		return true;
//
//	if (financecode == "00000000-0") {
//		return arr[2].alertText30;
//	}
//
//	re = /[A-Z0-9]{8}-[A-Z0-9]/;
//	r = financecode.match(re);
//	if (r == null) {
//		//console.log(arr[2].alertText30);
//		return arr[2].alertText30;
//	}
//	return true;
//};




/**
 * 
 * 
 * [trim 将数据去掉空格]
 * 
 * @param {[type]}
 *            val [description]
 * @return {[type]} [description]
 */
function trim(val) {
	var str = val + "";
	if (str.length == 0)
		return str;
	var re = /^\s*/;
	str = str.replace(re, '');
	re = /\s*$/;
	return str.replace(re, '');
};



/**
 * [校验长度小于15(不包括逗号)]
 */
function lt15(field, rules, i, options) {
	var arr = returnObject(field, options, "lt15");
	// 当前值
	var current = arr[1].value;
	
	if (current != "") {
		var replace = current.replace(/,/g, '');
		var length = replace.length;
		if (length > 15) {
			return arr[2].alertText;
		}
	}
	return true;
}