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
	function isValid(code){
		var pattern = /[A-Z]{1}[0-9]{16}[0-9A-Z\\*]{1}/;  
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
	
	/**
	 * 机构信用代码格式校验
	 * @param creditCode
	 * @return
	 */
	function checkCreditCode(field, rules, i, options) {
		var arr = returnObject(field, options, "checkCreditCode");
		// 当前值
		var creditCode = arr[1].value;
		
		if (creditCode != null && creditCode != "") {
			if (creditCode.length != 18) {
				return arr[2].alertText1;
			}
			if (!isValid(creditCode)) {
				return arr[2].alertText2;
			}
		}
		return true;
	}
	
	
	/**
	 * 校验输入的中征码校验位是否合法
	 *
	 */
	 function checkLoanCardno(field, rules, i, options) {
		 var arr = returnObject(field, options, "checkLoanCardno");
		 var loanCardNo = arr[1].value;
		 if(loanCardNo == null || loanCardNo ==""){
			 return true;
		 }
	     if (trim(loanCardNo).length != 16)
	    	 return arr[2].alertText1;
	     
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
	        	 return arr[2].alertText2;
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
	       return arr[2].alertText3;
	     
	 }
	 
	 /**
	  * [isCorpNo 组织机构代码]
	  * @param idCard
	  * @param arr
	  * @returns
	  */
	 function isCorpNo(field, rules, i, options){
		 var arr = returnObject(field, options, "isCorpNo");
		 var idCard = arr[1].value;
	 	if (idCard == null || idCard == ""){
	 		return true;
	 	}
	 	var ws = [3, 7, 9, 10, 5, 8, 4, 2];
	 	var str = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ';
	 	var reg = /^([0-9A-Z]){8}-[0-9|X]$/;
	 	if (!reg.test(idCard)) {
	 		return arr[2].alertText;
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
	 		return arr[2].alertText;
	 	}
	 	return true;
	 }
	 
	 /**
	 ==================================================================
	 功能：校验字段是否超长，一个汉字占两个字符
	 使用：countRealLeng(obj,length,msg)
	 返回：true - 不超长；false - 超长。
	 ==================================================================
	 */
	 function registercodevalidate(field, rules, i, options) {
		 var arr = returnObject(field, options, "registercodevalidate");
		 var value = arr[1].value;
		var frgtype = arr[0].registertype.value;
		if (frgtype != null && frgtype!=''  ) {
			var lengthTemp = value.replace(/[^\x00-\xff]/g,"**").length;
			if (lengthTemp > 32) {
				return arr[2].alertText2;
			} 
		}
		return true;
	 }
	 /**
	  * 国税校验长度
	  * @param field
	  * @param rules
	  * @param i
	  * @param options
	  * @returns
	  */
	 function sdepnationaltaxcodevalidate(field, rules, i, options) {
		 var arr = returnObject(field, options, "sdepnationaltaxcodevalidate");
		 var value = arr[1].value;
		if (value != null && value != "") {
			var lengthTemp = value.replace(/[^\x00-\xff]/g, "**").length;
			if (lengthTemp > 60) {
				return arr[2].alertText;
			} else {
				return true;
			}
		}
		return true;
	 }
	 /**
	  * 地税校验长度
	  * @param field
	  * @param rules
	  * @param i
	  * @param options
	  * @returns
	  */
	 function sdeplandtaxcodevalidate(field, rules, i, options) {
		 var arr = returnObject(field, options, "sdeplandtaxcodevalidate");
		 var value = arr[1].value;
		 if (value != null && value != "") {
			 var lengthTemp = value.replace(/[^\x00-\xff]/g, "**").length;
			 if (lengthTemp > 60) {
				 return arr[2].alertText;
			 } else {
				 return true;
			 }
		 }
		 return true;
	 }

