/**
 * ���ַ�����ֵ
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
 * ��֤�������ô����Ƿ�Ϸ�
 * @param code �������뺬���һλУ��λ
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
 * �������ô����ʽУ��
 * @param creditCode
 * @return
 */
function checkCreditCode(creditCode){
	if(creditCode.length !=18){
		alert("�������ô��볤�ȱ���18λ��");
		return false;
	}
	if(!isValid(creditCode)){
		alert("�������ô��벻��ȷ��");
		return false;
	}
	return true;
}

function checkExistCreditcode(orgcreditno,reqtype){
	//��֤�����������Ƿ����
    var flag = 0;
    $.ajax({
		   type: "GET",
		   url: "RequestDispathcherAtcion.do1?method=translate&dpurl=validatorCreditcode.do?newprofessionQuery=&dpsubsys=ec",
		   data: "orgcreditno="+orgcreditno+"&reqtype="+reqtype,//����������&��������Ϊ01��������֤�����������Ƿ����
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
			alert("�û������ô�����ϵͳ�в����ڣ����ʵ������¼�롣");
			return false;
		}else if(flag == 3){
			alert("����������֤�������ô�����ִ�����ˢ��ҳ������µ�½�����ԡ�");
			return false;
		}else{
			return true;
		}
//	return true;
}


function checkExistCreditcode2(orgcreditno,reqtype){
	//��֤�����������Ƿ����
    var data = "";
    $.ajax({
		   type: "GET",
		   url: "validatorCreditcode.do",
		   data: "orgcreditno="+orgcreditno+"&reqtype="+reqtype,//����������&��������Ϊ01��������֤�����������Ƿ����
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
	//��֤�����������Ƿ����
    var flag = 0;
    $.ajax({
		   type: "GET",
		   url: "RequestDispathcherAtcion.do1?method=translate&dpurl=validatorCreditcode.do?newprofessionQuery=&dpsubsys=ec",
		   data: "orgcreditno="+orgcreditno+"&reqtype="+reqtype,//����������&��������Ϊ01��������֤�����������Ƿ����
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
			alert("�û������ô����Ѿ����Ŵ����");
			return false;
		}else if(flag == 3){
			alert("����������֤�������ô�����ִ�����ˢ��ҳ������µ�½�����ԡ�");
			return false;
		}else{
			return true;
		}
//	return true;
}