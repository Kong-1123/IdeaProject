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
function checkCreditCode(creditCode){
	if(creditCode.length !=18){
		alert("机构信用代码长度必须18位！");
		return false;
	}
	if(!isValid(creditCode)){
		alert("机构信用代码不正确！");
		return false;
	}
	return true;
}

function checkExistCreditcode(orgcreditno,reqtype){
	//验证机构信用码是否存在
    var flag = 0;
    $.ajax({
		   type: "GET",
		   url: "RequestDispathcherAtcion.do1?method=translate&dpurl=validatorCreditcode.do?newprofessionQuery=&dpsubsys=ec",
		   data: "orgcreditno="+orgcreditno+"&reqtype="+reqtype,//机构信用码&请求类型为01，用来验证机构信用码是否存在
		   async: false,
		   success: function(msg){
		     if(msg == "true"){
		    	 flag = 1;
			 }else if(msg == "false"){
				 flag = 2;
			 }
		   },
		   error:function(req,status,errorThrown){
			   flag = 3;
		   }
		});
		if(flag == 2){
			alert("该机构信用代码在系统中不存在，请核实后重新录入。");
			return false;
		}else if(flag == 3){
			alert("到服务器验证机构信用代码出现错误，请刷新页面或重新登陆后重试。");
			return false;
		}else{
			return true;
		}
//	return true;
}


function checkExistCreditcode2(orgcreditno,reqtype){
	//验证机构信用码是否存在
    var data = "";
    $.ajax({
		   type: "GET",
		   url: "validatorCreditcode.do",
		   data: "orgcreditno="+orgcreditno+"&reqtype="+reqtype,//机构信用码&请求类型为01，用来验证机构信用码是否存在
		   async: false,
		   success: function(msg){
			 data = msg;
		   },
		   error:function(req,status,errorThrown){
			   data = 'error';
		   }
		});
	return data;
}
function checkExistCreditcode3(orgcreditno,reqtype){
	//验证机构信用码是否存在
    var flag = 0;
    $.ajax({
		   type: "GET",
		   url: "RequestDispathcherAtcion.do1?method=translate&dpurl=validatorCreditcode.do?newprofessionQuery=&dpsubsys=ec",
		   data: "orgcreditno="+orgcreditno+"&reqtype="+reqtype,//机构信用码&请求类型为01，用来验证机构信用码是否存在
		   async: false,
		   success: function(msg){
		     if(msg == "true"){
		    	 flag = 1;
			 }else if(msg == "false"){
				 flag = 2;
			 }
		   },
		   error:function(req,status,errorThrown){
			   flag = 3;
		   }
		});
		if(flag == 1){
			alert("该机构信用代码已经发放贷款卡！");
			return false;
		}else if(flag == 3){
			alert("到服务器验证机构信用代码出现错误，请刷新页面或重新登陆后重试。");
			return false;
		}else{
			return true;
		}
//	return true;
}