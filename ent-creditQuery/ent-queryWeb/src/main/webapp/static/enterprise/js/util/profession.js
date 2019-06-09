     
     function showcompensatory(gcid,loancardcode) {
		var url = "?compensatoryAction.do&gcoperationId="+gcid+"&loancardcode="+loancardcode;
		url = "extraQuery?extraUrl=" + url;
		window.open(url,"","Width=800px;Height=600px,top=100px,left=200px,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=no,directories=no");
	}
	// 查看担保-贷款
	function showLoanAssureeInfo(loancardno,financecode,loancontractcode){
		var url="reportInfoWindows.do&reqFlag=2&loancardno="+loancardno+"&blockcode=ASSUREEINFO_00&loankind=1&financecode="+financecode+"&loancontractcode="+loancontractcode;
		url = "downloadQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
	}
	
	// 查看担保-贸易融资
	function showFinancingAssureeInfo(loancardno,financecode,loancontractcode){
		var url="reportInfoWindows.do&reqFlag=2&loancardno="+loancardno+"&blockcode=ASSUREEINFO_00&loankind=4&financecode="+financecode+"&loancontractcode="+loancontractcode;
		url = "downloadQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
	}
	
	// 查看担保-保理
	function showAssureeAssureeInfo(loancardno,financecode,loancontractcode){
		var url="reportInfoWindows.do&reqFlag=2&loancardno="+loancardno+"&blockcode=ASSUREEINFO_00&loankind=2&financecode="+financecode+"&loancontractcode="+loancontractcode;
		url = "downloadQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
	}
	
	// 查看担保-信用证
	function showLetterAssureeInfo(loancardno,financecode,loancontractcode){
		  var url="reportInfoWindows.do&reqFlag=2&loancardno="+loancardno+"&blockcode=ASSUREEINFO_00&loankind=5&financecode="+financecode+"&loancontractcode="+loancontractcode;
		  url = "downloadQuery?extraUrl=" + url;
		  url = encodeURI(url);
		  window.open(url,"","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
	}
		
	// 查看担保-银行承兑汇票
	function showPostalAssureeInfo(loancardno,financecode,loancontractcode){
		var url="reportInfoWindows.do&reqFlag=2&loancardno="+loancardno+"&blockcode=ASSUREEINFO_00&loankind=7&financecode="+financecode+"&loancontractcode="+loancontractcode;
		url = "downloadQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
	}
	
	// 查看担保-保函
	function showGuaranteeletterAssureeInfo(loancardno,financecode,loancontractcode){
		var url="reportInfoWindows.do&reqFlag=2&loancardno="+loancardno+"&blockcode=ASSUREEINFO_00&loankind=6&financecode="+financecode+"&loancontractcode="+loancontractcode;
		url = "downloadQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
	}
	//查看贷款明细
	function showLoanNor(){
		var obj = new Object();
		obj.name = document.getElementById("loanpaid").innerHTML;
		window.showModalDialog('ec/simplequery/profession/creditdetail.jsp',obj,"dialogWidth=800px;dialogHeight=600px");
		return;
	}
	//查看贸易融资明细
	function showFinancingNor(){
		var obj = new Object();
		obj.name = document.getElementById("financingpaid").innerHTML;
		window.showModalDialog('ec/simplequery/profession/creditdetail.jsp',obj,"dialogWidth=800px;dialogHeight=600px");
		return;
	}
	//查看保理明细
	function showAssureeNor(){
		var obj = new Object();
		obj.name = document.getElementById("assureepaid").innerHTML;
		window.showModalDialog('ec/simplequery/profession/creditdetail.jsp',obj,"dialogWidth=800px;dialogHeight=600px");
		return;
	}
	//查看承兑汇票明细
	function showPostalNor(){
		var obj = new Object();
		obj.name = document.getElementById("postalpaid").innerHTML;
		window.showModalDialog('ec/simplequery/profession/creditdetail.jsp',obj,"dialogWidth=800px;dialogHeight=600px");
		return;
	}
	//查看信用证明细
	function showLetterNor(){
		var obj = new Object();
		obj.name = document.getElementById("letterpaid").innerHTML;
		window.showModalDialog('ec/simplequery/profession/creditdetail.jsp',obj,"dialogWidth=800px;dialogHeight=600px");
		return;
	}
	//查看票据贴现明细
	function showBillNor(){
		var obj = new Object();
		obj.name = document.getElementById("billpaid").innerHTML;
		window.showModalDialog('ec/simplequery/profession/creditdetail.jsp',obj,"dialogWidth=800px;dialogHeight=600px");
		return;
	}
	
	//查看保函明细
	function showGuaranteeletterNor(){
		var obj = new Object();
		obj.name = document.getElementById("guaranteeletterpaid").innerHTML;
		window.showModalDialog('ec/simplequery/profession/creditdetail.jsp',obj,"dialogWidth=800px;dialogHeight=600px");
		return;
	}
	////////////////////////////////////查看展期弹出窗口///////////////////////////////////////////////////////////
	//查看贷款展期
	function showLoanExtensionInfo(loancardno,financecode,operid){
		var url="reportInfoWindows.do&reqFlag=3&loancardno="+loancardno+"&blockcode=EXTENSION_01&financecode="+financecode+"&operid="+operid;
		url = "downloadQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.showModalDialog(url,"","dialogWidth=800px;dialogHeight=600px");
	//	window.open(url,"","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
	}
	//查看贸易融资展期
	function showFinancingExtensionInfo(loancardno,financecode,operid){
		var url="reportInfoWindows.do&reqFlag=3&loancardno="+loancardno+"&blockcode=EXTENSION_02&financecode="+financecode+"&operid="+operid;
		url = "downloadQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
	}
	////////////////////////////////////弹出窗口查看公共信息历史24/////////////////////////////////////////////////////////
	//社会
	function showEntaccountPublicHistory(loancardcode,sistype,occureddate){
		var url="showAllHistory.do&loancardno="+loancardcode+"&sistype="+sistype+"&occureddate="+occureddate+"&type=last24month&last24MonthType=entaccount";
		url = "extraQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","Width=800px;Height=600px,top=100px,left=200px,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=no,directories=no");
	}
	//住房
	function showAccfundPublicHistory(loancardcode,occureddate){
		var url="showAllHistory.do&loancardno="+loancardcode+"&occureddate="+occureddate+"&type=last24month&last24MonthType=accfund";
		url = "extraQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","Width=800px;Height=600px,top=100px,left=200px,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=no,directories=no");
	}
	//缴费
	function showPublicPublicHistory(areacode,entaccountname,businessmancode,sistype, payinfotype){
		var url="showAllHistory.do&areacode="+areacode+"&sistype="+sistype+"&entaccountname="+entaccountname+"&type=last24month&last24MonthType=public&businessmancode="+businessmancode+"&payinfotype="+payinfotype;
		url = "extraQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","Width=800px;Height=600px,top=100px,left=200px,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=no,directories=no");
	}
	//电信缴费修改
	function showPublicHistory(loancardno,occureddate,sistype, payinfotype){
		var url="showAllHistory.do&loancardno="+loancardno+"&occureddate="+occureddate+"&sistype="+sistype+"&type=last24month&last24MonthType=public"+"&payinfotype="+payinfotype;
		url = "extraQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","Width=800px;Height=600px,top=100px,left=200px,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=no,directories=no");
	}

	//未结清正常票据贴现明细
	function showBillDetail(arg){
		var searchreason = document.getElementById("searchReason").value;
		var searchreasoncode = document.getElementById("searchReasonCode").value;
		var editiontype = document.getElementById("reportcode").value;
		var creditcode = document.getElementById("creditcode").value;
		var reportCode = document.getElementById("reportcode").value;
		var loancardcode= document.getElementById("loancardcode").value;
		var obj = new Object();
		//obj.name = document.getElementById(arg).innerHTML;
		var url1 = "includeNormal.do?loancardcode="+loancardcode+"&reportcode="+reportCode+"&kindType=3"+"&source=1"+"&toporg="+arg;
		url1 = "extraQuery?extraUrl=" + url1;
		window.showModalDialog(url1,obj,"dialogWidth=800px;dialogHeight=600px");
		var  url2 =("includeNormal.do?loancardcode="+loancardcode+"&reportcode="+reportCode+"&kindType=3"+"&source=1"+"&toporg="+arg);
		url2 = "extraQuery?extraUrl=" + url2;
		xmlHttp=AjaxGetXmlHttpObject();			
		xmlHttp.open("POST",url2,true);
		xmlHttp.onreadystatechange=ajaxStateChanged;
		xmlHttp.send(null);
		return;
	}
	//未结清正常承兑汇票明细
	function showPostalDetail(arg){
		var searchreason = document.getElementById("searchReason").value;
		var searchreasoncode = document.getElementById("searchReasonCode").value;
		var editiontype = document.getElementById("reportcode").value;
		var creditcode = document.getElementById("creditcode").value;
		var reportCode = document.getElementById("reportcode").value;
		var loancardcode= document.getElementById("loancardcode").value;
		var obj = new Object();
		var url1 ="includeNormal.do?loancardcode="+loancardcode+"&reportcode="+reportCode+"&kindType=7"+"&source=1"+"&toporg="+arg;
		url1 = "extraQuery?extraUrl=" + url1;
		window.showModalDialog(url1,obj,"dialogWidth=800px;dialogHeight=600px");
		var  url2 =("includeNormal.do?loancardcode="+loancardcode+"&reportcode="+reportCode+"&kindType=7"+"&source=1"+"&toporg="+arg);
		url2 = "extraQuery?extraUrl=" + url2;
		xmlHttp=AjaxGetXmlHttpObject();
		xmlHttp.open("POST",url2,true);
		xmlHttp.onreadystatechange=ajaxStateChanged;
		xmlHttp.send(null);
		return;
	}
	//未结清正常信用证明细
	function showLetterDetail(arg){
		var searchreason = document.getElementById("searchReason").value;
		var searchreasoncode = document.getElementById("searchReasonCode").value;
		var editiontype = document.getElementById("reportcode").value;
		var creditcode = document.getElementById("creditcode").value;
		var reportCode = document.getElementById("reportcode").value;
		var loancardcode= document.getElementById("loancardcode").value;
		var obj = new Object();
		var  url2 =("includeNormal.do?loancardcode="+loancardcode+"&reportcode="+reportCode+"&kindType=5"+"&source=1"+"&toporg="+arg);
		url2 = "extraQuery?extraUrl=" + url2;
		window.showModalDialog(url2,obj,"dialogWidth=800px;dialogHeight=600px");
		xmlHttp=AjaxGetXmlHttpObject();			
		xmlHttp.open("POST",url2,true);
		xmlHttp.onreadystatechange=ajaxStateChanged;
		xmlHttp.send(null);
		return;
	}
	//未结清正常保函明细
	function showGuaranteeletterDetail(arg){
		var searchreason = document.getElementById("searchReason").value;
		var searchreasoncode = document.getElementById("searchReasonCode").value;
		var editiontype = document.getElementById("reportcode").value;
		var creditcode = document.getElementById("creditcode").value;
		var reportCode = document.getElementById("reportcode").value;
		var loancardcode= document.getElementById("loancardcode").value;
		var obj = new Object();
		var url1 = "includeNormal.do?loancardcode="+loancardcode+"&reportcode="+reportCode+"&kindType=5"+"&source=1"+"&toporg="+arg;
		url1 = "extraQuery?extraUrl=" + url1;
		window.showModalDialog(url1,obj,"dialogWidth=800px;dialogHeight=600px");
		var  url2 =("includeNormal.do?loancardcode="+loancardcode+"&reportcode="+reportCode+"&kindType=6"+"&source=1"+"&toporg="+arg);
		url2 = "extraQuery?extraUrl=" + url2;
		xmlHttp=AjaxGetXmlHttpObject();			
		xmlHttp.open("POST",url2,true);
		xmlHttp.onreadystatechange=ajaxStateChanged;
		xmlHttp.send(null);
		return;
	}
	//查看垫款
	function showPackInfoWindow(loancardno,type,operationcode,loankindcode,financecode){
		var url="showAllHistory.do&loancardno="+loancardno+"&type="+type+"&id="+operationcode+"&loankindcode="+loankindcode+"&financecode="+financecode;
		url = "extraQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","Width=800px;Height=600px,top=100px,left=200px,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=no,directories=no");
	}
	//查看历史信息
	function showHis(loancardno,financecode,loancontractcode,type,id,loankindcode){
		var searchreason = document.getElementById("searchReason").value;
		var searchreasoncode = document.getElementById("searchReasonCode").value;
		var editiontype = document.getElementById("reportcode").value;
		var creditcode = document.getElementById("creditcode").value;
		var reportCode = document.getElementById("reportcode").value;
		var loancardcode= document.getElementById("loancardcode").value;
		var url="showAllHistory.do&loancardno="+loancardno+"&blockcode=ASSUREEINFO_00&loankindcode="+loankindcode+"&financecode="+financecode+"&loancontractcode="+loancontractcode+"&type="+type+"&id="+id+"&searchreason="+searchreason+"&searchreasoncode="+searchreasoncode+"&editiontype="+editiontype+"&creditcode="+creditcode;
	//	window.showModalDialog(url,"","dialogWidth=800px;dialogHeight=600px");
		url = "extraQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","Width=800px;Height=600px,top=100px,left=200px,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=no,directories=no");
	}
	function showWindows(loancardno,financecode,loancontractcode,type,id,loankindcode){
		var url="showAllHistory.do&loancardno="+loancardno+"&blockcode=ASSUREEINFO_00&loankind="+loankindcode+"&financecode="+financecode+"&loancontractcode="+loancontractcode+"&type="+type+"&id="+id+"&loankindcode="+loankindcode;
		url = "extraQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","Width=800px;Height=600px,top=100px,left=200px,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=no,directories=no");
	}
	function showGuaWindow(loancardno,type,loancontractcode,toporg,kind){
		var url="showAllHistory.do&loancardno="+loancardno+"&type="+type+"&id="+loancontractcode+"&toporg="+toporg+"&kind="+kind;
		url = "extraQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"","Width=800px;Height=600px,top=100px,left=200px,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=no,directories=no");
	}
	function showReport(type,code,codeType){
		var loancardcode= document.getElementById("loancardcode").value;
		var searchreason = document.getElementById("searchReason").value;
		var searchreasoncode = document.getElementById("searchReasonCode").value;
		var editiontype = document.getElementById("reportcode").value;
		var creditcode = document.getElementById("creditcode").value;
		var reportCode = document.getElementById("reportcode").value;
		if(confirm2("请在查询前确认已得到信息主体的授权。您要继续查询吗？")){
			var url = "professionAagainQuery.do&type="+type+"&code="+code+"&codeType="+codeType+"&loancardcode_s="+loancardcode;
			url = "extraQuery?extraUrl=" + url;
			window.open(url,"","top=100,left=100,width=800,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
		}else{
			return;
		}
	  }	
	// 查看信贷正常类明细
	function showNormal(loancard,editiontype,kindcode){
		var loancardcode= document.getElementById("loancardcode").value;
		var searchreason = document.getElementById("searchReason").value;
		var searchreasoncode = document.getElementById("searchReasonCode").value;
		var editiontype = document.getElementById("reportcode").value;
		var creditcode = document.getElementById("creditcode").value;
		var reportcode = document.getElementById("reportcode").value;
		var url="includeNormal.do?loancardcode="+loancardcode+"&reportcode="+editiontype+"&kindType="+kindcode+"&source=3";
		url = "extraQuery?extraUrl=" + url;
		window.showModalDialog(url,"","dialogWidth=800px,dialogHeight=80px,status=no,resizable=yes");
	}

	//资产负债表
    function showcap(loancardcode,reportyear,reporttype,reporttypesubsection,maincredit,blockcode,borrowcode){
	    var loancardno= document.getElementById("loancardcode").value;
	    var searchreason = document.getElementById("searchReason").value;
	    var searchreasoncode = document.getElementById("searchReasonCode").value;
	    var editiontype = document.getElementById("reportcode").value;
	    var creditcode = document.getElementById("creditcode").value;
	    var url = "reportInfoWindows.do&loancardno="+loancardno+"&reporttype="+reporttype+"&reporttypesubsection="+reporttypesubsection+"&reportyear="+reportyear+"&maincredit="+maincredit+"&blockcode="+blockcode+"&reqFlag=1";
	    url = "downloadQuery?extraUrl=" + url;
	    url = encodeURI(url);
	    window.open(url,"newWindow","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
    }
    //利润表
    function showprofit(loancardcode,reportyear,reporttype,reporttypesubsection,maincredit,blockcode,borrowcode){
		var loancardcode= document.getElementById("loancardcode").value;
	    var financecode= document.getElementById("financecode").value;
	    var userId = document.getElementById("userId").value;
	    var financename = document.getElementById("financename").value;
	    var searchreason = document.getElementById("searchReason").value;
	    var searchreasoncode = document.getElementById("searchReasonCode").value;
	    var editiontype = document.getElementById("reportcode").value;
	    var creditcode = document.getElementById("creditcode").value;
		var url = "reportInfoWindows.do&loancardno="+loancardcode+"&reporttype="+reporttype+"&reporttypesubsection="+reporttypesubsection+"&reportyear="+reportyear+"&maincredit="+maincredit+"&blockcode="+blockcode+"&reqFlag=1&borrowcode="+borrowcode+"&loancardcode="+loancardcode+"&financecode="+financecode+"&financename="+financename+"&userid="+userId+"&searchreason="+searchreason+"&searchreasoncode="+searchreasoncode+"&editiontype="+editiontype+"&creditcode="+creditcode;
		url = "downloadQuery?extraUrl=" + url;
		url = encodeURI(url);
		window.open(url,"newWindow","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
	}
	//现金流量表
	function showcash(loancardcode, reportyear, reporttype,reporttypesubsection, maincredit, blockcode,borrowcode) {
		  var loancardcode= document.getElementById("loancardcode").value;
	      var financecode= document.getElementById("financecode").value;
	      var userId = document.getElementById("userId").value;
	      var financename = document.getElementById("financename").value;
	      var searchreason = document.getElementById("searchReason").value;
	      var searchreasoncode = document.getElementById("searchReasonCode").value;
	      var editiontype = document.getElementById("reportcode").value;
	      var creditcode = document.getElementById("creditcode").value;
		  var url = "reportInfoWindows.do&loancardno=" + loancardcode
				+ "&reporttype=" + reporttype + "&reporttypesubsection="
				+ reporttypesubsection + "&reportyear=" + reportyear
				+ "&maincredit=" + maincredit + "&blockcode=" + blockcode+"&reqFlag=1&borrowcode="+borrowcode+"&loancardcode="+loancardcode+"&financecode="+financecode+"&financename="+financename+"&userid="+userId+"&searchreason="+searchreason+"&searchreasoncode="+searchreasoncode+"&editiontype="+editiontype+"&creditcode="+creditcode;
		  url = "downloadQuery?extraUrl=" + url;
		  url = encodeURI(url);
		 window.open(url,"","top=100,left=100,width=700,height=400,scrollbars=yes,toolbar=no,location=no,menubar=no,status=no,resizable=yes,directories=no");
	}
	
//	var printattchUrl = "";
//	var printattchObj = {};

	function showprintattch(){
		
		//TODO  发送ajax请求，进行打印附件权限管控及记录打印附件记录
		
		parent.ajaxFunc("isPrintAnnexPermit",function(result) {
    	        if(result.code == '00000000'){
    	        	printAttch();
    	        }else {
    	        	parent.layerMsg(result.msg);
    			}
    	    },
    	    function(result) {
    	        layerMsg("error:" + JSON.stringify(result));
    	    }, {recordId : parent.recordId, type:"3"});
		
		return;
	}  
	
	function printAttch(){

        var loancardcode= document.getElementById("loancardcode").value;
	    var financecode= document.getElementById("financecode").value;
	    var userId = document.getElementById("userId").value;
	    var financename = document.getElementById("financename").value;
	    var searchreason = document.getElementById("searchReason").value;
	    var searchreasoncode = document.getElementById("searchReasonCode").value;
	    var editiontype = document.getElementById("reportcode").value;
	    var creditcode = document.getElementById("creditcode").value;
	    
		var totalnum = 0;
		var loanNum = 0;
		var tradeNum = 0;
		var assureeNum = 0;
		var billNum = 0;
		var postalNum = 0;
		var letterNum = 0;
		var guaranteeNum = 0;
		//未结清正常类和已结清正常类笔数
		var total = 0;
		//未结清正常类笔数
		try {
			totalnum = document.getElementById("totalnum").value;
			totalnum = Number(totalnum);
		} catch (e) {
			totalnum = 0;
		}
		//已结清正常类贷款笔数
		try {
			loanNum = document.getElementById("loanNum").value;
			loanNum = Number(loanNum);
		} catch (e) {
			loanNum = 0;
		}
		//已结清正常类贸易融资笔数
		try {
			tradeNum = document.getElementById("tradeNum").value;
			tradeNum = Number(tradeNum);
		} catch (e) {
			tradeNum = 0;
		}
		//已结清正常类保理笔数
		try {
			assureeNum = document.getElementById("assureeNum").value;
			assureeNum = Number(assureeNum);
		} catch (e) {
			assureeNum = 0;
		}
		//已结清正常类票据贴现笔数
		try {
			billNum = document.getElementById("billNum").value;
			billNum = Number(billNum);
		} catch (e) {
			billNum = 0;
		}
		//已结清正常类承兑汇票笔数
		try {
			postalNum = document.getElementById("postalNum").value;
			postalNum = Number(postalNum);
		} catch (e) {
			postalNum = 0;
		}
		//已结清正常类信用证笔数
		try {
			letterNum = document.getElementById("letterNum").value;
			letterNum = Number(letterNum);
		} catch (e) {
			letterNum = 0;
		}
		//已结清正常类保函笔数
		try {
			guaranteeNum = document.getElementById("guaranteeNum").value;
			guaranteeNum = Number(guaranteeNum);
		} catch (e) {
			guaranteeNum = 0;
		}
		total = totalnum + loanNum + tradeNum + assureeNum + billNum + postalNum + letterNum + guaranteeNum;
		//if(total > 2000) {
			//alert("正常类业务明细笔数过多，为提高效率，请在查看‘未结清正常类业务明细’和‘查看已结清正常类业务明细’处进行查询打印。");
			//return ;
		//}
		var obj = new Object();
		var billunpaid = document.getElementById("billunpaidattch");
		var postalunpaid = document.getElementById("postalunpaidattch");
		var letterunpaid = document.getElementById("letterunpaidattch");
		var guaranteeunpaid = document.getElementById("guaranteeunpaidattch");
		obj.desc = '';
		obj.loancardcode = '';
		obj.creditCode = '';
		obj.reasonCode = '';
		obj.editiontype = '';
		//未结清
		if(billunpaid !=null && billunpaid != ''){
			obj.desc += billunpaid.innerHTML;
		}
		if(postalunpaid !=null && postalunpaid != ''){
			obj.desc += postalunpaid.innerHTML;
		}
		if(letterunpaid !=null && letterunpaid != ''){
			obj.desc += letterunpaid.innerHTML;
		}
		if(guaranteeunpaid !=null && guaranteeunpaid != ''){
			obj.desc += guaranteeunpaid.innerHTML;
		}
		obj.loancardcode = loancardcode;
		obj.creditCode = creditcode;
		obj.reasonCode = searchreasoncode;
		obj.editiontype = editiontype;

		xmlHttp=AjaxGetXmlHttpObject();
		if (xmlHttp==null){
		    alert ("您的浏览器不支持AJAX！");
		    return;
		}
		var url =("recordExpand.do&loancardno="+loancardcode
				+"&searchreasoncode="+searchreasoncode+"&editiontype="+editiontype+"&creditCode="+creditcode+"&kind=3");
		url = "extraQuery?extraUrl=" + url;
		
		
		xmlHttp.open("POST",url,true);
		xmlHttp.onreadystatechange=ajaxStateChanged;
		xmlHttp.send(null);
       
		var url2= 'includeNormal.do&loancardcode='+loancardcode+'&reportcode='+editiontype+'&source=2';
		url2 = "extraQuery?extraUrl=" + url2;
		
		parent.printattchUrl = url2;
		parent.printattchObj = obj;
		
		//弹出层
		parent.layui.use("layer",function(){
			var layer = parent.layui.layer;
			var layerOpt ={
		        type: 2  // 1：content为jquery对象，2：content为url。
		        ,title: "打印附件"
		        ,area: ["800px", "630px"]
		        ,shade: [0.3, '#333']
		        ,closeBtn:2
		        ,resize: false
		        ,maxmin: false
		        ,content:"printattch"
		        ,zIndex : 19891014
		        ,success: function(layero){
		          	layer.setTop(layero); // 置顶当前窗口
		          	// 成功弹出后回调
		        }
		       /* ,cancel:function(index){
		        	// 关闭窗口的回调
		        }*/
		    };
		    layer.open(layerOpt);
	    })
	}


   function AjaxGetXmlHttpObject(){
	  var xmlHttp=null;
	  try{
	    xmlHttp=new XMLHttpRequest();
	  }catch (e){
		  try{
		     xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
		  }catch (e){
		     xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
		  }
	  }
	  return xmlHttp;
	}
	function ajaxStateChanged(){
		if (xmlHttp.readyState == 4){
			if (xmlHttp.status == 200) { 
				return;
			}
		}
	}
	