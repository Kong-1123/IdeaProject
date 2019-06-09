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
	
	/**
	 * 机构信用代码格式校验
	 * @param creditCode
	 * @return
	 */
	/*function checkCreditCode(creditCode){
		if(creditCode.length !=18){
			layerMsg("机构信用代码长度必须18位！");
			return false;
		}
		if(!isValid(creditCode)){
			layerMsg("机构信用代码不正确！");
			return false;
		}
		return true;
	}*/

	function checkLoanCard(){
		// 跟据中征码查询
		/*var loancard=document.getElementById("loancardCode").value;
	    if (loancard != null && loancard!='') {
		    if (trim(document.getElementById("loancardCode").value).length < 16) {
		    	layerMsg("请输入16位中征码！");
				document.getElementById("loancardCode").focus();
				return false;
			}
	   	 	if (!checkLoanCardno(document.getElementById("loancardCode").value)) {
	   	 		layerMsg("请输入正确的中征码！");
				document.getElementById("loancardCode").focus();
				return false;
			}
	   	 console.log($("#loancardCode").val());*/
	   	 var jsonData = getAjaxData("/creditenterprisequeryweb/loancardcodequery/autoCompleteInfo",{loancardCode:$("#loancardCode").val()});
	   	 if(jsonData.code !="00000000"){
	  		 layerMsg(jsonData.message);
	   	 } else {
	   		 $("#companyName").val(jsonData.companyName);
	   		 $("#creditCode").val(jsonData.creditCode);
	   		 $("#corpNo").val(jsonData.corpNo);
	   		 $("#usCreditCode").val(jsonData.usCreditCode);
	   		 console.log(jsonData);
	   	 	}
		/*}*/
	}
	
	function checkCorp(){
		// 验证组织机构代码正确性
		/*var corp=document.getElementById("corpNo").value;
		if (corp != null && corp!=''){
	        if (!isCorpNo(document.getElementById("corpNo").value)) {
	       		document.getElementById("corpNo").focus();
	            return false;
	        }
	        console.log($("#corpNo").val());*/
		   	
	        var jsonData = getAjaxData("/creditenterprisequeryweb/loancardcodequery/autoCompleteInfo",{corpNo:$("#corpNo").val()});
	        if(jsonData.code !="00000000"){
		  		 layerMsg(jsonData.message);
		  	} else {
		  		$("#companyName").val(jsonData.companyName);
		  		$("#creditCode").val(jsonData.creditCode);
		  		$("#loancardCode").val(jsonData.loancardCode);
		  		$("#usCreditCode").val(jsonData.usCreditCode);
		  		console.log(jsonData); 
		  	} 
		/*}*/
	}
	
	 function checkInput() {
			// 根据机构信用码查询
			/*var creditCode = document.getElementById("creditCode").value;
		    if (creditCode != null && creditCode!='') {
			    if(!checkCreditCode(creditCode)){
			    	document.getElementById("creditCode").focus();
					return false;
				}
				console.log($("#creditCode").val());*/
			   	var jsonData = getAjaxData("/creditenterprisequeryweb/loancardcodequery/autoCompleteInfo",{creditCode:$("#creditCode").val()});
			   	if(jsonData.code !="00000000"){
			  		 layerMsg(jsonData.message);
			  	} else {
			  		$("#companyName").val(jsonData.companyName);
			  		$("#corpNo").val(jsonData.corpNo);
			  		$("#loancardCode").val(jsonData.loancardCode);
			  		$("#usCreditCode").val(jsonData.usCreditCode);
			  		console.log(jsonData);
			  	}
			/*}*/	
	}
	 //统一社会信用编码校验
	 function CheckSocialCreditCode() {
		  /*var Code = document.getElementById("usCreditCode").value;
		  var patrn = /^[0-9A-Z]+$/;
		  //18位校验及大写校验
		  if ((Code.length != 18) || (patrn.test(Code) == false)) {
			  layerMsg("不是有效的统一社会信用编码！");
		    return false;
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
		    	layerMsg("不是有效的统一社会信用编码！");
		      return false;
		    }else{*/
		    	var jsonData = getAjaxData("/creditenterprisequeryweb/loancardcodequery/autoCompleteInfo",{usCreditCode:$("#usCreditCode").val()});
			  	if(jsonData.code !="00000000"){
			  		layerMsg(jsonData.message);
			  	} else {
			  		$("#companyName").val(jsonData.companyName);
			  		$("#corpNo").val(jsonData.corpNo);
			  		$("#loancardCode").val(jsonData.loancardCode);
			  		$("#creditCode").val(jsonData.creditCode);
			  		console.log(jsonData);
			  		}
			  /*  }
		    return true;
		  }*/
	 }