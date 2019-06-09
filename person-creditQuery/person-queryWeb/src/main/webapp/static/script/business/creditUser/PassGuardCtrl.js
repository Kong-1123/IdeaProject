//Windows IE32
var PGEdit_IE32_CLASSID="2AB566D4-7DC9-40E4-88A6-2C5043FC19F2";
var PGEdit_IE32_CAB="";
var PGEdit_IE32_EXE="PBCCRCPassGuardIE.exe";
var PGEdit_IE32_VERSION="1.0.0.3";
//Windows IE64
var PGEdit_IE64_CLASSID="2AB566D4-7DC9-40E4-88A6-2C5043FC19F2";
var PGEdit_IE64_CAB="";
var PGEdit_IE64_EXE="PBCCRCPassGuardX64.exe";
var PGEditt_IE64_VERSION="1.0.0.3";
//Windows 非IE
var PGEdit_FF="PBCCRCPassGuardFF.exe";
var PGEdit_FF_VERSION="1.0.0.3";
//Win10Edge / Chrome42+
var PGEdit_Edge="PBCCRCPassGuardEdge.exe";
var PGEdit_Edge_Mac="";
var PGEdit_Edge_VERSION="1.0.0.3";
//Mac OS
var PGEdit_MacOs="";
var PGEdit_MacOs_VERSION="";
//Win10Edge / Chrome42+环境下用到的一些变量
var CIJSON = {"interfacetype":0,"data":{"switch":3}};//检查控件是否安装
var ICJSON = {"interfacetype":0,"data":{"switch":2}};//实例化控件窗口
var INCJSON = {"interfacetype":1,"data":{}};//初始化控件参数
var OPJSON = {"interfacetype":0,"data":{"switch":0}};//开启控件保护
var XTJSON = {"interfacetype":0,"data":{"switch":5}};//心跳监测
var CPJSON = {"interfacetype":0,"data":{"switch":1}};//关闭控件保护
var OUTJSON = {"interfacetype":2,"data":{}};//获取值类json串
var CLPJSON = {"interfacetype":0,"data":{"switch":4}};//清空密码
//心跳监听变量、集合、本地服务地址、日志开关、全局检测安装变量
var isInstalled = -1,objVersion = "",interv,urls,logFlag = true,onceInterv={},iterArray=[];
var inFlag = {},datac,RZCIJSON;//控制是否能输入
var license = "UkxmWndLV2pIdm5sMVBUNUdUQ21halNQSCtud25FeXhRZHBzdjJ4V3JVVFFxd3lnWFRsUWk3ZkQrR0tHbzhGMDhRZ1N0YlZ1SmNLMWxkMmZzVjh5d2ZVWkowMjZCaU9GemdWa1RYNE5qRFZ2cXVUL0Z2eXJyNGJOWG1rQThxcWRmc0x4U3hHcE4zRkRvU0xmbzJjYmN0WGlPSndTUzFUSnMwYXVaUUt2blhjPXsiaWQiOjAsInR5cGUiOiJ0ZXN0IiwicGxhdGZvcm0iOjQsIm5vdGJlZm9yZSI6IjIwMTgwNjAzIiwibm90YWZ0ZXIiOiIyMDE4MDkwMyJ9";
var PGEdit_Update="1";//非IE控件是否强制升级 1强制升级,0不强制升级
if(navigator.userAgent.indexOf("MSIE")<0){navigator.plugins.refresh();}//非IE需要刷新plugins数组
var pge = function (option) {
	this.settings = {};
	if(option){
		this.settings = option;
	}
    this.settings.pgeUrls = "https://windows10.microdone.cn";
    this.pgeDownText = "请点此安装控件";
    this.settings.pgePort = "5826";
    this.osBrowser = this.checkOsBrowser();
    if(isInstalled == -1) isInstalled = this.checkInstall();
    this.pgeVersion = this.getVersion();
};
    
pge.prototype.apiVersion = "2016.07.10001",//js脚本版本号
/**输入接口**/
//给控件设置随机因子
pge.prototype.pwdSetSk = function(s) {
	if (isInstalled) {
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3 || this.osBrowser==6 || this.osBrowser==8) {
				control.input1 = s;
			} else if (this.osBrowser==2) {
				control.input(1,s);
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				INCJSON = {"interfacetype" : 1,"data" : {}};
				INCJSON.id = id , INCJSON.data.aeskey = s;
				datac = this.getEnStr(this.settings.pgeRZRandNum,INCJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				this.pwdGetData(RZCIJSON);
			}
		} catch (err) {	}
	}
}
//设置授权license
pge.prototype.setLicense = function(){
	if(isInstalled){
		var control = document.getElementById(this.settings.pgeId);
		if(this.osBrowser == 1 || this.osBrowser == 3){
			control.license = license;
		}
	}
}
/**输出接口**/
//获得密码长度
pge.prototype.pwdLength = function() {
	var code = 0;
	if (!isInstalled) {
		code = 0;
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.output3;
			} else if (this.osBrowser==2) {
				code = control.output(3);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output3();
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 3,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
		} catch (err) {	code = 0; }
	}
	return code;
}
//获得密码的Hash值
pge.prototype.pwdHash = function() {
	var code = 0;
	if (!isInstalled) {
		code = 0;
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.output2;
			} else if (this.osBrowser==2) {
				code = control.output(2);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output2();
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 2,OUTJSON.data.encrypttype = 1;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
		} catch (err) {code = 0;}
	}
	return code;
}
//判断密码是否是简单密码
pge.prototype.pwdSimple = function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.output44;
			} else if (this.osBrowser==2) {
				code = control.output(13);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output10();
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 13,OUTJSON.data.encrypttype = 1;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
		} catch (err) {code = '';}
	}
	return code;
}		
//判断密码是否匹配正则表达式二
pge.prototype.pwdValid = function() {
	var code = 1;
	if (!isInstalled) {
		code = 1;
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				if(control.output1) code = control.output5;
			} else if (this.osBrowser==2) {
				code = control.output(5);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output5();
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 5,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
		} catch (err) {	code = 1; }
	}
	return code;
}
//获得密码AES密文
pge.prototype.pwdResult = function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.output1;
			} else if (this.osBrowser==2) {
				code = control.output(7);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output1();
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				OUTJSON.id = this.settings.pgeWindowID,OUTJSON.data.datatype = 7,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}							
		} catch (err) {	code = '';}
	}
	return code;
}

//获得密码AES密文
pge.prototype.pwdResultRSA = function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				control.input8 = this.settings.pgeCert;
				code = control.output28;
			} else if (this.osBrowser==2) {
				control.input(800,this.settings.pgeCert);
				code = control.output(801);
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				OUTJSON.id = this.settings.pgeWindowID,OUTJSON.data.datatype = 801,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}							
		} catch (err) {	code = '';}
	}
	return code;
}
//获得计算机Mac信息密文
pge.prototype.machineNetwork =  function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.GetIPMacList();
			} else if (this.osBrowser==2) {
				code = control.output(9);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output7(0);
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 9,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
		} catch (err) { code = ''; }
	}
	return code;
}
//获得计算机硬盘信息密文
pge.prototype.machineDisk = function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.GetNicPhAddr(1);
			} else if (this.osBrowser==2) {
				code = control.output(11);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output7(2);
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 11,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
		} catch (err) {	code = ''; }
	}
	return code;
}
//获得计算机cpu信息密文
pge.prototype.machineCPU = function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.GetNicPhAddr(2);
			} else if (this.osBrowser==2) {
				code = control.output(10);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output7(1);
			} else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 10,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
		} catch (err) {	code = '';}
	}
	return code;
}
//获得计算机信息列表
pge.prototype.machineList = function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.output58;
			} else if (this.osBrowser==2) {
				code = control.output(15);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output20();
			} else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 15,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
		} catch (err) {	code = '';}
	}
	return code;
}



//获得国密密文
pge.prototype.pwdResultSM = function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				control.input7 = this.settings.pgeCertX + "|" + this.settings.pgeCertY;
				code = control.output105;
			} else if (this.osBrowser==2) {
				control.input(200,this.settings.pgeCertX + "|" + this.settings.pgeCertY);
				code = control.output(900);
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				OUTJSON.id = this.settings.pgeWindowID,OUTJSON.data.datatype = 900,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
			if(code){
				code = code.toUpperCase();
			}
		} catch (err) {	code = '';}
	}
	return code;
}

//获得计算机Mac信息密文
pge.prototype.machineNetworkSM =  function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.GetNicPhAddrex(0);
			} else if (this.osBrowser==2) {
				code = control.output(903);
				if(code){
					code = code.toUpperCase();
				}
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output7(0);
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 903,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
			if(code){
				code = code.toUpperCase();
			}
		} catch (err) { code = ''; }
	}
	return code;
}
//获得计算机硬盘信息密文
pge.prototype.machineDiskSM = function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.GetNicPhAddrex(1);
			} else if (this.osBrowser==2) {
				code = control.output(902);
				if(code){
					code = code.toUpperCase();
				}
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output7(2);
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 902,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
			if(code){
				code = code.toUpperCase();
			}
		} catch (err) {	code = ''; }
	}
	return code;
}
//获得计算机cpu信息密文
pge.prototype.machineCPUSM = function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.GetNicPhAddrex(2);
			} else if (this.osBrowser==2) {
				code = control.output(901);
				if(code){
					code = code.toUpperCase();
				}
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output7(1);
			} else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 901,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
			if(code){
				code = code.toUpperCase();
			}
		} catch (err) {	code = '';}
	}
	return code;
}
//获得计算机信息列表
pge.prototype.machineListSM = function() {
	var code = '';
	if (!isInstalled) {
		code = '';
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.GetNicPhAddrex(3);
			} else if (this.osBrowser==2) {
				code = control.output(904);
				if(code){
					code = code.toUpperCase();
				}
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output20();
			} else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 904,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
			if(code){
				code = code.toUpperCase();
			}
		} catch (err) {	code = '';}
	}
	return code;
}
//获得密码的强度(强度的规则可调整)
pge.prototype.pwdStrength = function() {
	var code = 0;
	if (!isInstalled) {
		code = 0;
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if(this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 3,OUTJSON.data.encrypttype = 0,datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				var len = this.pwdGetData(RZCIJSON);
				OUTJSON.data.datatype = 4,OUTJSON.data.encrypttype =2,datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				var num = this.pwdGetData(RZCIJSON);
				OUTJSON.data.datatype = 4,OUTJSON.data.encrypttype = 1,datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				var zoom = this.pwdGetData(RZCIJSON);
			} else if (this.osBrowser==1 || this.osBrowser==3) {
				var len = control.output3;
				var num = control.output4;
				var zoom = control.output54;
			} else if (this.osBrowser==2 ) {
				var len = control.output(3);
				var num = control.output(4);
				var zoom = control.output(4,1);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				var len = control.get_output3();
				var num = control.get_output4();
				var zoom = control.get_output16();
			}
			if(len < 6){
				code = 0;
			}else if(num == 1 && len >= 6){
				code = 1; //弱
			}else if(num == 2 && len >= 6){
				code = 2; //中
			}else if(num == 3 && len >= 6){
				code = 3; //强
			}
		} catch (err) {	code ="";}
	}
	return code;
}
//获得控件版本号
pge.prototype.getVersion = function() {
	try {
		if (this.osBrowser==1 || this.osBrowser==3) {
			var comActiveX = new ActiveXObject("PBCCRCPassGuardX.PassGuard.1"); 
			return comActiveX.output35;
		}else if(this.osBrowser == 2 || this.osBrowser == 6 || this.osBrowser == 8 ){
			var arr = new Array(),pge_version;
	    	var pge_info = navigator.plugins['PBCCRC'].description;
			if(pge_info.indexOf(":") > 0){
				arr = pge_info.split(":");
				pge_version = arr[1];
			}else{
				pge_version = "";
			}
		} else if (this.osBrowser == 10 || this.osBrowser == 11){
			if(isInstalled == -1 || !isInstalled) return;
			var id = this.settings.pgeWindowID;
			OUTJSON.id = id,OUTJSON.data.datatype = 12,OUTJSON.data.encrypttype = 0;
			datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
			RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
			pge_version = this.pwdGetData(RZCIJSON);
		}
		return pge_version;
	}catch(err){ return "";}					
}
//获得密码字符类型个数
pge.prototype.pwdNum = function() {
	var code = 0;
	if (!isInstalled) {
		code = 0;
	}else{
		try {
			var control = this.pwdGetEById(this.settings.pgeId);
			if (this.osBrowser==1 || this.osBrowser==3) {
				code = control.output4;
			} else if (this.osBrowser==2) {
				code = control.output(4);
			}else if (this.osBrowser==6 || this.osBrowser==8) {
				code = control.get_output4();
			}else if (this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID;
				OUTJSON.id = id,OUTJSON.data.datatype = 4,OUTJSON.data.encrypttype = 0;
				datac = this.getEnStr(this.settings.pgeRZRandNum,OUTJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				code = this.pwdGetData(RZCIJSON);
			}
		} catch (err) { code = 0;}
	}
	return code;
}
/**公共方法**/
//检测操作系统和浏览器信息
pge.prototype.checkOsBrowser = function() {
	var userosbrowser;
	var regStr_chrome = /chrome\/[\d.]+/gi ;
	var regStr_firefox =  /firefox\/[\d.]+/gi ;
	if((navigator.platform =="Win32") || (navigator.platform =="Windows")){
		if(navigator.userAgent.indexOf("MSIE")>0 || navigator.userAgent.indexOf("msie")>0 || navigator.userAgent.indexOf("Trident")>0 || navigator.userAgent.indexOf("trident")>0){
			if(navigator.userAgent.indexOf("ARM")>0){
				userosbrowser=9; // win8 RAM Touch
				this.pgeditIEExe="";
			}else{
				userosbrowser=1;// windows32ie32
				this.pgeditIEClassid=PGEdit_IE32_CLASSID;
				this.pgeditIECab=PGEdit_IE32_CAB;
				this.pgeditIEExe=PGEdit_IE32_EXE;
			}
		}else if(navigator.userAgent.indexOf("Edge")>0){
			userosbrowser = 10;
			this.pgeditFFExe = PGEdit_Edge;
		}else if(navigator.userAgent.indexOf("Chrome")>0){
			var chromeVersion = navigator.userAgent.match(regStr_chrome).toString();
			chromeVersion = parseInt(chromeVersion.replace(/[^0-9.]/gi,""));
			if(chromeVersion >= 42){
				userosbrowser = 10;
				this.pgeditFFExe = PGEdit_Edge;
			}else{
				userosbrowser=2;
				this.pgeditFFExe = PGEdit_FF;
			}
		}else if(navigator.userAgent.indexOf("Firefox")>0){
			var firefoxVersion = navigator.userAgent.match(regStr_firefox).toString();
			firefoxVersion = parseInt(firefoxVersion.replace(/[^0-9.]/gi,""));
			if(firefoxVersion >= 51){
				userosbrowser = 10;
				this.pgeditFFExe = PGEdit_Edge;
			}else{
				userosbrowser=2;
				this.pgeditFFExe = PGEdit_FF;
			}
		}else{
			userosbrowser=2; // windowsff
			this.pgeditFFExe=PGEdit_FF;
		}
	}else if((navigator.platform=="Win64")){
		if((navigator.userAgent.indexOf("Windows NT 6.2")>0 || navigator.userAgent.indexOf("windows nt 6.2")>0) && navigator.userAgent.indexOf("Firefox") == -1){		
			userosbrowser=1;// windows32ie32
			this.pgeditIEClassid=PGEdit_IE32_CLASSID;
			this.pgeditIECab=PGEdit_IE32_CAB;
			this.pgeditIEExe=PGEdit_IE32_EXE;						
		}else if(navigator.userAgent.indexOf("MSIE")>0 || navigator.userAgent.indexOf("msie")>0 || navigator.userAgent.indexOf("Trident")>0 || navigator.userAgent.indexOf("trident")>0){
			userosbrowser=3;//windows64ie64
			this.pgeditIEClassid=PGEdit_IE64_CLASSID;
			this.pgeditIECab=PGEdit_IE64_CAB;
			this.pgeditIEExe=PGEdit_IE64_EXE;			
		}else if(navigator.userAgent.indexOf("Edge")>0 || navigator.userAgent.indexOf("Firefox")){
			userosbrowser = 10;
			this.pgeditFFExe = PGEdit_Edge;
		}else if(navigator.userAgent.indexOf("Chrome")>0){
			var chromeVersion = navigator.userAgent.match(regStr_chrome).toString();
			chromeVersion = parseInt(chromeVersion.replace(/[^0-9.]/gi,""));
			if(chromeVersion >= 42){
				userosbrowser = 10;
				this.pgeditFFExe = PGEdit_Edge;
			}else{
				userosbrowser = 2;
				this.pgeditFFExe = PGEdit_FF;
			}
		}else{
			userosbrowser=2;
			this.pgeditFFExe=PGEdit_FF;
		}
	}else if(navigator.userAgent.indexOf("Macintosh")>0){
		if(navigator.userAgent.indexOf("Safari")>0 && (navigator.userAgent.indexOf("Version/5.1")>0 || navigator.userAgent.indexOf("Version/5.2")>0 || navigator.userAgent.indexOf("Version/6")>0)){
			userosbrowser=8;//macos Safari 5.1 more
			this.pgeditFFExe=PGEdit_MacOs;
		}else if(navigator.userAgent.indexOf("Firefox")>0 || navigator.userAgent.indexOf("Chrome")>0){
			var chromeVersion = navigator.userAgent.match(regStr_chrome);
			if( chromeVersion != null){
				chromeVersion = chromeVersion.toString();
				chromeVersion = parseInt(chromeVersion.replace(/[^0-9.]/gi,""));
				if(chromeVersion >= 42){
					userosbrowser = 11;
					this.pgeditFFExe = PGEdit_Edge_Mac;
				}else{
					userosbrowser=6;
					this.pgeditFFExe = PGEdit_MacOs;		
				}
			}else{
				userosbrowser=6;// macos
				this.pgeditFFExe = PGEdit_MacOs;
			}
		}else if(navigator.userAgent.indexOf("Opera")>=0 && (navigator.userAgent.indexOf("Version/11.6")>0 || navigator.userAgent.indexOf("Version/11.7")>0)){
			userosbrowser=6;//macos
			this.pgeditFFExe=PGEdit_MacOs;						
		}else if(navigator.userAgent.indexOf("Safari")>=0){
			userosbrowser=6;//macos
			this.pgeditFFExe=PGEdit_MacOs;			
		}else{
			userosbrowser=0;//macos
			this.pgeditFFExe="";
		}
	}
	return userosbrowser;
}
//根据不同的浏览器生成控件标签代码
pge.prototype.getpgeHtml = function() {
	if (this.osBrowser==1 || this.osBrowser==3) {
		return '<span id="'+this.settings.pgeId+'_pge" class="'+this.settings.passLoginStyle+'"><OBJECT ID="' + this.settings.pgeId + '" CLASSID="CLSID:' + this.pgeditIEClassid + '" codebase="' 
		        +this.settings.pgePath+ this.pgeditIECab + '" onkeydown="if(13==event.keyCode || 27==event.keyCode)'+this.settings.pgeOnkeydown+';" tabindex="'+this.settings.pgeTabindex+'" class="' + this.settings.pgeClass + '">' 
		        + '<param name="edittype" value="'+ this.settings.pgeEdittype + '"><param name="maxlength" value="' + this.settings.pgeMaxlength +'"><param name="input58" value="'+this.settings.pgeOnfocus+'"><param name="input59" value="'+this.settings.pgeOnblur+'">' 
				+ '<param name="input2" value="'+ this.settings.pgeEreg1 + '"><param name="input3" value="'+ this.settings.pgeEreg2 + '"></OBJECT></span>'
				+ '<span id="'+this.settings.pgeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;display:none;"><a href="'+this.settings.pgePath+this.pgeditIEExe+'">'+this.pgeDownText+'</a></span>';
	} else if (this.osBrowser==2) {
		var ff = "";
		if(navigator.userAgent.indexOf("SE 2.X") > -1 || navigator.userAgent.indexOf("OPR") > -1){
			ff = "this.focus();";
		}
		var pgeOcx='<embed onmouseover="'+ff+'" onfocus="'+this.settings.pgeOnfocus+'" input_0 ="'+license+'" onblur="'+this.settings.pgeOnblur+'" ID="' + this.settings.pgeId + '"  maxlength="'+this.settings.pgeMaxlength+'" input_2="'+this.settings.pgeEreg1+'" input_3="'+this.settings.pgeEreg2+'" edittype="'+this.settings.pgeEdittype+'" type="application/PBCCRCpass-guard-x" tabindex="'+this.settings.pgeTabindex+'" class="' + this.settings.pgeClass + '" ';
		if(this.settings.pgeOnkeydown!=undefined && this.settings.pgeOnkeydown!="") pgeOcx+=' input_1013="'+this.settings.pgeOnkeydown+'"';
		if(this.settings.tabCallback!=undefined && this.settings.tabCallback!="") pgeOcx+=' input_1009="setTimeout(function(){document.getElementById(\''+this.settings.tabCallback+'\').focus();},10);"';
		if(navigator.userAgent.indexOf("Chrome") > -1 || navigator.userAgent.indexOf("Safari") > -1){
			if(this.settings.pgeOnfocus!=undefined && this.settings.pgeOnfocus!="") pgeOcx+=' input_1010="'+this.settings.pgeOnfocus+'"';
			if(this.settings.pgeOnblur!=undefined && this.settings.pgeOnblur!="") pgeOcx+=' input_1011="'+this.settings.pgeOnblur+'"';
		}
		if(this.settings.pgeFontName!=undefined && this.settings.pgeFontName!="") pgeOcx+=' FontName="'+this.settings.pgeFontName+'"';
		if(this.settings.pgeFontSize!=undefined && this.settings.pgeFontSize!="") pgeOcx+=' FontSize='+Number(this.settings.pgeFontSize)+'';					
		pgeOcx+='/>';
		return pgeOcx;
	} else if (this.osBrowser == 10 ){
		var obj =this;
		this.checkInstall(1,function(obj){
			if(!isInstalled || isInstalled == -1) isInstalled = true;
			var id = obj.settings.pgeId;
			var winId = obj.settings.pgeWindowID;
			if((obj.getConvertVersion(objVersion)<obj.getConvertVersion(PGEdit_Edge_VERSION))&&PGEdit_Update=="1"){
				var winPath = obj.settings.pgePath+obj.pgeditFFExe;
				var inputs = document.querySelectorAll(".winA");
				for ( var int = 0; int < inputs.length; int++) {
					inputs[int].innerHTML = ("请点此升级控件");
					inputs[int].setAttribute("href",winPath);
				}
			}else{
				document.querySelector("#"+id+"_down").parentNode.innerHTML = '<input type="text" onfocus="pgeCtrl.openProt(\''+winId+'\',this.id);pgeCtrl.setCX(this);'+obj.settings.pgeOnfocus+'" autocomplete="off" onkeydown="pgeCtrl.setSX(event,\''+obj.settings.pgeOnkeydown+'\',this);" onclick = "pgeCtrl.setCX(this)" onblur = "pgeCtrl.closeProt(\''+winId+'\',this.id);'+obj.settings.pgeOnblur+';" id = "'+id+'" style="ime-mode:disabled" tabindex="2" class="' + obj.settings.pgeClass + '"/>';
				var o = obj.pwdGetEById(id);
				if(obj.osBrowser == 10 || obj.osBrowser == 11){
					o = obj.pwdGetEById(id)
					if(o != null){
						if(obj.osBrowser == 11){
							obj.pwdGetEById(id).type="text";
							o.onkeypress = function(e){
								var chrTyped, chrCode=0, evt = e ? e : event,chrCode = evt.which;
								var x = String.fromCharCode(chrCode);
								var maxlength = parseInt(obj.settings.pgeMaxlength);
								if(chrCode>=32 && chrCode<=126){
									if(this.value.length > (maxlength-1)) return false;
									var reg1 = obj.settings.pgeEreg1.replace("*","");
									reg1 = new RegExp(reg1);
									if(reg1.test(x)) this.value+='*';
									return false;
								}else return true;
							}
							o.onkeydown = function(e){
								var chrTyped, chrCode=0, evt=e?e:event;
								chrCode = evt.which;
								var x = String.fromCharCode(chrCode);
								if(chrCode == 13){
									this.blur();
									eval("("+obj.settings.pgeOnkeydown+")");
								}else if(chrCode>=37 && chrCode<=40) return false;
								 else return true;
							}
						}
						if(obj.osBrowser == 10){
							o.onkeypress = function(){ return inFlag[winId].flag; }
						}
					}
					obj.instControl(winId,obj);
				}
			}
		});
		return '<div id="'+this.settings.pgeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;line-height:25px;"><a class="winA" href="'+this.settings.pgePath+this.pgeditFFExe+'">'+this.pgeDownText+'</a></div>';
	} else {
		return '<div id="'+this.settings.pgeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;">暂不支持此浏览器</div>';
	}				
}
//用document.write()绘制控件标签代码
pge.prototype.generate = function() {
	if (this.osBrowser==10) {return document.write(this.getpgeHtml());}
	if (!isInstalled) {
		return document.write(this.getDownHtml());
	}else{
		if(this.osBrowser==1){
			if(this.getConvertVersion(this.pgeVersion)<this.getConvertVersion(PGEdit_IE32_VERSION) && PGEdit_Update==1){
				this.setDownText();
				return document.write(this.getDownHtml());
			} 
		} else if(this.osBrowser==3){	
			if(this.getConvertVersion(this.pgeVersion)<this.getConvertVersion(PGEditt_IE64_VERSION) && PGEdit_Update==1){
				this.setDownText();
				return document.write(this.getDownHtml());
			}
		} else if(this.osBrowser==2){
			if(this.getConvertVersion(this.pgeVersion)<this.getConvertVersion(PGEdit_FF_VERSION) && PGEdit_Update==1){
				this.setDownText();
				return document.write(this.getDownHtml());	
			}
		} else if (this.osBrowser==6 || this.osBrowser==8) {
			if(this.getConvertVersion(this.pgeVersion)<this.getConvertVersion(PGEdit_MacOs_VERSION)&& PGEdit_Update==1){
				this.setDownText();
				return document.write(this.getDownHtml());	
			}
		}
		return document.write(this.getpgeHtml());
	}
}
//返回绘制控件标签代码
pge.prototype.load = function() {		
	if (this.osBrowser==10) {return this.getpgeHtml();}
	if (!isInstalled) {
		return this.getDownHtml();
	}else{
		if(this.osBrowser==1){
			if(this.getConvertVersion(this.pgeVersion)<this.getConvertVersion(PGEdit_IE32_VERSION) && PGEdit_Update==1){
				this.setDownText();
				return this.getDownHtml();
			} 
		}else if(this.osBrowser==3){
			if(this.getConvertVersion(this.pgeVersion)<this.getConvertVersion(PGEditt_IE64_VERSION) && PGEdit_Update==1){
				this.setDownText();
				return this.getDownHtml();
			} 		
		}else if(this.osBrowser==2){  
			if(this.getConvertVersion(this.pgeVersion)<this.getConvertVersion(PGEdit_FF_VERSION) && PGEdit_Update==1){
				this.setDownText();
				return this.getDownHtml();	
			}
		} else if (this.osBrowser==6 || this.osBrowser==8) {
			if(this.getConvertVersion(this.pgeVersion)<this.getConvertVersion(PGEdit_MacOs_VERSION)&& PGEdit_Update==1){
				this.setDownText();
				return this.getDownHtml();	
			}
		}
		return this.getpgeHtml();
	}
}
//返回提示下载的标签代码
pge.prototype.getDownHtml = function() {
	if (this.osBrowser==1 || this.osBrowser==3) {
		return '<div id="'+this.settings.pgeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;line-height:28px;"><a href="'+this.settings.pgePath+this.pgeditIEExe+'">'+this.pgeDownText+'</a></div>';
	} else if (this.osBrowser==2 || this.osBrowser == 10) {
		return '<div id="'+this.settings.pgeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;line-height:25px;"><a href="'+this.settings.pgePath+this.pgeditFFExe+'">'+this.pgeDownText+'</a></div>';
	} else {
		return '<div id="'+this.settings.pgeId+'_down" class="'+this.settings.pgeInstallClass+'" style="text-align:center;">暂不支持此浏览器</div>';
	}			
}
//清空密码
pge.prototype.pwdClear = function() {
	if (isInstalled) {
		try{
            if(this.osBrowser == 10 || this.osBrowser == 11){
				var id = this.settings.pgeWindowID,inputID = this.settings.pgeId;
				this.pwdGetEById(inputID).value = ("");
				CLPJSON.id = id,datac = this.getEnStr(this.settings.pgeRZRandNum,CLPJSON);
				RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
				this.pwdGetData(RZCIJSON);
            }else{
				var control = this.pwdGetEById(this.settings.pgeId);
				control.ClearSeCtrl();
            }
        }catch(err){}
	}
}
//检测控件是否已安装
pge.prototype.checkInstall = function(s,callf) {
	try {
		if (this.osBrowser == 1 || this.osBrowser == 3) {
			var comActiveX = new ActiveXObject("PBCCRCPassGuardX.PassGuard.1");
			return true;
		} else if (this.osBrowser==2 || this.osBrowser==6 || this.osBrowser==8) {
			var arr = new Array(),pge_version;
			var pge_info=navigator.plugins['PBCCRC'].description;
			if(pge_info.indexOf(":")>0){
				arr = pge_info.split(":");
				pge_version = arr[1];
				if (pge_version != "") return true;
			}else{
				pge_version = "";
				return false;
			}
		} else if ((this.osBrowser == 10) && s == 1){
			var obj = this,id = this.settings.pgeWindowID;
			if(!this.settings.pgeRZRandNum) return -1;
			CIJSON.id = this.settings.pgeWindowID,datac = this.getEnStr(this.settings.pgeRZRandNum,CIJSON);
			RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
			urls = this.settings.pgeUrls+":"+this.settings.pgePort;
			if(logFlag) console.log(urls);
			jsonp( {time : 7000,url : urls,
				data : {"str" : JSON.stringify(RZCIJSON),"type":"check","jsoncallback":"cb"},
				callback : "jsoncallback",
				success : function(x) {
					objVersion = x.data;
					if(!!callf) callf(obj);
				},
				fail : function(jqXHR, textStatus, errorThrown) {
					if(obj.settings.pgePort < 5830){
						obj.settings.pgePort++;
						obj.checkInstall(1, callf);
					}else{isInstalled = false;return;}
				}
			});
		}
	}catch(err){return false;}
}
//发同步ajax请求
pge.prototype.pwdGetData = function(datas){
	var d = 0;
	var str = JSON.stringify(datas).replace(/\+/g,"%2B");
	Ajax.request( {
		timeout : 1000,
		url : urls,
		type : 'GET',
		async: false,
		data : {
			"jsoncallback" : "cb",
			"str" : str
		},
		success : function(x) {
			x = x.responseText.substr("cb(".length);
			x = x.substring(0, x.length-1);
			x = JSON.parse(x);
			d = x.data;
			return d;
		},
		error : function(x){
			d = -1;
		}
	});
	return d;
}
//将控件版本号转换成整形值，用于比较
pge.prototype.getConvertVersion = function(version) {
	try {
		if(version==undefined || version==""){
			return 0;
		}else{
			var flag = false,m = "";
			if(this.osBrowser == 1 || this.osBrowser == 3){
				if(version.indexOf(",")> -1) flag = true;
			}
			if(flag) m = version.split(",");
			else m=version.split(".");
			var v=parseInt(m[0]*1000)+parseInt(m[1]*100)+parseInt(m[2]*10)+parseInt(m[3]);
			return v;
		}
	}catch(err){return 0;}			
}
//获得当前时间毫秒，主要用于防止缓存
pge.prototype.pwdGetTime = function() {
	return new Date().getTime();
}
//通过id获取页面元素
pge.prototype.pwdGetEById = function(id) {
	return document.getElementById(id);
}
//加密通信数据
pge.prototype.getEnStr = function (sKey,jsonStr) {
	var neiKey = [ 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x1A, 0x2A, 0x2B,
			0x2C, 0x2D, 0x2E, 0x2F, 0x3A, 0x3B, 0x11, 0x22, 0x33, 0x44, 0x55,
			0x66, 0x77, 0x1A, 0x2A, 0x2B, 0x2C, 0x2D, 0x2E, 0x2F, 0x3A, 0x3B ];
	var fkey = "";
	var lx = "";
	for ( var i = 0; i < sKey.length; i++) {
		lx = String.fromCharCode(sKey[i].charCodeAt(0) ^ neiKey[i]);
		fkey += lx;
	}
	var hexKey = CryptoJS.enc.Utf8.parse(fkey);
	var enStr = CryptoJS.AES.encrypt(JSON.stringify(jsonStr), hexKey, {
		mode : CryptoJS.mode.ECB,
		padding : CryptoJS.pad.Pkcs7
	});
	return enStr.toString();
}
//设置提示文字
pge.prototype.setDownText = function(){
	if(this.pgeVersion!=undefined && this.pgeVersion!=""){
			this.pgeDownText="请点此升级控件";
	}
}
//初始化密码框属性
pge.prototype.pgInitialize = function(){
	if(isInstalled){
		if(this.osBrowser==1 || this.osBrowser==3){ 
			this.pwdGetEById(this.settings.pgeId).style.display = "";
		}
		
	}else{
		this.pwdGetEById(this.settings.pgeId+'_pge').style.display = "none";	
		if(this.osBrowser==1 || this.osBrowser==3){
			this.pwdGetEById(this.settings.pgeId+'_down').style.display = "";
		}	
	}
}
/**Win10Edge控件相关接口**/
//当按enter键时，提交表单
pge.prototype.setSX = function(e,m,o){
	var keynum,va = this.pwdGetEById(o.id),len = va.value.length,maxlen=this.settings.pgeMaxlength+1;
	if(window.event){//IE
		keynum = e.keyCode
	}else if(e.which) {//FF
		keynum = e.which
	}
	if(keynum == 13){
		o.blur();
		eval("("+m+")");
	}
}
//控制不能从密码框中间编辑
pge.prototype.setCX = function(ctrl) {
	var caretPos = 0,len = ctrl.value.length;
	if (document.selection) {//IE
		var sel = document.selection.createRange();
		sel.moveStart('character', -ctrl.value.length);
		caretPos = sel.text.length;
	}else if (ctrl.selectionStart || ctrl.selectionStart == '0'){//FF
		caretPos = ctrl.selectionStart;
	}
	if (caretPos <= len) {
		if (ctrl.setSelectionRange) {//设置光标位置函数 FF
			setTimeout(function(){
				ctrl.setSelectionRange(len, len);
			},1);
		} else if (ctrl.createTextRange) {//IE
			var range = ctrl.createTextRange();
			range.collapse(true);
			range.moveEnd('character', len);
			range.moveStart('character', len);
			range.select();
		}
	}
}
//实例化密码控件窗口
pge.prototype.instControl = function(id,obj) {
	ICJSON.id = id,datac = this.getEnStr(this.settings.pgeRZRandNum,ICJSON);
	RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
	jsonp( {url : urls,
		data : {"str" : JSON.stringify(RZCIJSON),type:"inst","jsoncallback":"cb"},
		callback : "jsoncallback",
		success : function(x) {
			if(logFlag)  console.log("id:"+id);
			if(logFlag)  console.log("x.data:"+x.data+",x.code:"+x.code);
			if (x.code == 0) {
				if(logFlag)  console.info("实例化成功");
				obj.initControl(id);
			}
		}
	});
	//初始化是否能输入
	inFlag[id] = {"flag":false};
}
//初始化密码控件窗口参数
pge.prototype.initControl = function(id) {
	INCJSON.id = id,INCJSON.data.edittype = this.settings.pgeEdittype,INCJSON.data.maxlength = this.settings.pgeMaxlength;
	INCJSON.data.reg1 = this.settings.pgeEreg1,INCJSON.data.reg2 = this.settings.pgeEreg2;INCJSON.data.sm2xyhexkey = this.settings.pgeCertX + "|" + this.settings.pgeCertY;
	if(this.osBrowser == 10) INCJSON.data.lic = {"liccode":license,"url":"aHR0cCUzQS8vYS5wYmNjcmMub3JnLmNuLw=="};
	datac = this.getEnStr(this.settings.pgeRZRandNum,INCJSON);
	RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
	jsonp( {url : urls,
		data : {"str" : JSON.stringify(RZCIJSON),"type" : "init","jsoncallback" : "cb"},
		callback : "jsoncallback",
		success : function(x) {
			if(logFlag) console.log("id:"+id);
			if(logFlag) console.log("x.data:"+x.data+",x.code:"+x.code);
			if (x.code == 0) {
				if(logFlag) console.info("设置参数成功");
				inFlag[id] = {"flag":false};
			} else {
				if(logFlag) console.info("data:" + x.data);
			}
		}
	});
	//初始化对应id心跳值
	onceInterv[id]="";
}
//开启密码控件保护
pge.prototype.openProt = function(id,inputID) {
	//控制不让输入,待开启保护后才让输入
	inFlag[id].flag = false,OPJSON.id = id,datac = this.getEnStr(this.settings.pgeRZRandNum,OPJSON);
	RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
	jsonp( {url : urls,
		data : {"str" : JSON.stringify(RZCIJSON),"jsoncallback" : "cb","type" : "open prot"},
		callback : "jsoncallback",
		success : function(x) {
			if(logFlag) console.log("成功开启密码控件保护");
			if(logFlag) console.log("x.data:"+x.data+",x.code:"+x.code);
			inFlag[id].flag = true;//控制让输入
		}
	});
	if(typeof onceInterv[id] == "string"){//监听焦点切出
		for(var i = 0;i < iterArray.length;i++){
			window.clearInterval(iterArray[i]);
		}
		onceInterv[id] = window.setInterval("pgeCtrl.intervlOut(\""+id+"\",\""+inputID+"\")",800);	
		iterArray.push(onceInterv[id]);
	}
}
//心跳监测
pge.prototype.intervlOut = function(id,inputID) {
	XTJSON.id = id,datac = this.getEnStr(this.settings.pgeRZRandNum,XTJSON);
	RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
	jsonp( {url : urls,
		data : {"str" : JSON.stringify(RZCIJSON),"jsoncallback" : "cb"},
		callback : "jsoncallback",
		success : function(x) {
			 if(logFlag) console.log(id+"的长度："+len);
			 if(logFlag) console.log("x.data(长度)："+x.data+",x.code:"+x.code);
			 code = parseInt(x.data);
			 var va = pgeCtrl.pwdGetEById(inputID), len = va.value.length,y = "",i = 0;
			 if(logFlag){ 
				 if(logFlag) console.log(id+"的长度："+len);
				 if(logFlag) console.log("x.data(长度)："+ code);
			 }
			 for( ; i < code; i++) y += "*";
			 va.value = y;
		}
	});
}
//关闭密码控件保护
pge.prototype.closeProt = function(id,inputID) {
	CPJSON.id = id,datac = this.getEnStr(this.settings.pgeRZRandNum,CPJSON);
	RZCIJSON = {"rankey":this.settings.pgeRZRandNum,"datab":this.settings.pgeRZDataB,"datac":datac};
	jsonp( {url : urls,
		data : {"str" : JSON.stringify(RZCIJSON),"jsoncallback" : "cb"},
		callback : "jsoncallback",
		success : function(x) {
			if(logFlag) console.log("关闭密码控件保护成功");
			if(logFlag) console.log("x.data:"+x.data+",x.code:"+x.code);
			inFlag[id].flag = false;
		}
	});
	if(typeof onceInterv[id] == "number"){
		 for(var i = 0;i < iterArray.length;i++){
			 window.clearInterval(iterArray[i]);
		 }
		 onceInterv[id] = "";
	}
}
var pgeCtrl = new pge();//公共对象，跟本地服务交互时会用到
///////////////////////////////////////////////////////////
Ajax = function() {  
    function request(opt) {  
        function fn() {  
        }  
        var url = opt.url || "";  
        var async = opt.async !== false, method = opt.method || 'GET', data = opt.data  
                || null, success = opt.success || fn, error = opt.failure  
                || fn;  
        method = method.toUpperCase();  
        if (method == 'GET' && data) {  
            var args = "";  
            if(typeof data == 'string'){  
                //alert("string")  
            args = data;  
            }else if(typeof data == 'object'){  
                //alert("object")  
                var arr = new Array();  
                for(var k in data){  
                    var v = data[k];  
                    arr.push(k + "=" + v);  
                }  
                args = arr.join("&");  
            }  
        url += (url.indexOf('?') == -1 ? '?' : '&') + args;  
            data = null;  
        }  
        var xhr = window.XMLHttpRequest ? new XMLHttpRequest()  
                : new ActiveXObject('Microsoft.XMLHTTP');  
        xhr.onreadystatechange = function() {  
            _onStateChange(xhr, success, error);  
        };  
        xhr.open(method, url, async);  
        if (method == 'POST') {  
            xhr.setRequestHeader('Content-type',  
                    'application/x-www-form-urlencoded;');  
        }  
        xhr.send(data);  
        return xhr;  
    }  
    function _onStateChange(xhr, success, failure) {  
        if (xhr.readyState == 4) {  
            var s = xhr.status;  
            if (s >= 200 && s < 300) {  
                success(xhr);  
            } else {  
                failure(xhr);  
            }  
        } else {  
        }  
    }  
    return {  
        request : request  
    };  
}();
function jsonp(options) {
    options = options || {};
    if (!options.url || !options.callback) {
        throw new Error("参数不合法");
    }
    var callbackName = ('jsonp_' + Math.random()).replace(".", "");
    var oHead = document.getElementsByTagName('head')[0];
    options.data[options.callback] = callbackName;
    var params = formatParams(options.data);
    var oS = document.createElement('script');
    oHead.appendChild(oS);

    //创建jsonp回调函数
    window[callbackName] = function (json) {
        oHead.removeChild(oS);
        clearTimeout(oS.timer);
        window[callbackName] = null;
        options.success && options.success(json);
    };

    //发送请求
    oS.src = options.url + '?' + params;

    //超时处理
    if (options.time) {
        oS.timer = setTimeout(function () {
            window[callbackName] = null;
            oHead.removeChild(oS);
            options.fail && options.fail({ message: "超时" });
        }, options.time);
    }
};

//格式化参数
function formatParams(data) {
    var arr = [];
    for (var name in data) {
        arr.push(name + '=' + encodeURIComponent(data[name]));
    }
    return arr.join('&');
}
///////////////////////////////////////////////////////////