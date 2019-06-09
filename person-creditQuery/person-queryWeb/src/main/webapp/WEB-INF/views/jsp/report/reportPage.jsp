<%@page import="cn.com.dhcc.query.creditquerycommon.util.UserUtilsForConfig"%>
<%@page import="cn.com.dhcc.creditquery.person.queryweb.util.UserAttrUtil"%>
<%@page import="cn.com.dhcc.creditquery.person.query.bo.queryconfig.CpqUserAttrBo"%>
<%@page
	import="cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
String userName = UserUtilsForConfig.getUserName(request);
CpqUserAttrBo userAttr = UserAttrUtil.findCpqUserAttrBySystemUserId(userName);
if(null == userAttr){
	userAttr = new CpqUserAttrBo();
}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<meta http-equiv="X-UA-Compatible" content="IE=10">
<!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
<title>个人信用报告（银行版）</title>
<link rel="stylesheet" href="../static/style/report/reveal/reset.css">
	<link rel="stylesheet" href="../static/style/report/reveal/main.css">
		<style type="text/css">
html, body, .popupBg {
	width: 100%;
	height: 100%;
	margin: 0;
	padding: 0;
}

html {
	overflow: hidden;
	font-family: "宋体";
}
.layui-layer.layui-layer-dialog .layui-layer-padding {
    padding-left: 75px;
}
.layui-layer.layui-layer-dialog .layui-layer-content .layui-layer-ico {
    left: 40px;
}
body {
	gn: expression_r(this.onselectstart = function(){return false;});
	*position:relative;
}
</style>
		<style type="text/css" media=print>
		@page{
	        size:  auto; 
	        margin: 0mm;  
	    }
.noPrint {
	display: none !important;
}
</style>
<script type="text/javascript" src="../static/script/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="../static/script/autoload.js"></script>
</head>
<body oncontextmenu='return false;' ondragstart='return false;'
	onselectstart='return false;' oncopy='document.selection.empty();'
	onbeforecopy='return false;' background-image = '../../static/image/error/ipandmac.png'>
	<div class="popupBg">1</div>
	<!-- <div class="popup">
		<div class="popupHead clearfix">
			<span>系统提示</span><a href="javascript:popupHide()" class="close">1</a>
		</div>
		<div class="popupContent">
			<p>右键功能被禁用</p>
		</div>
		<div class="popupContent2">
			<p>即将打印请稍后</p>
		</div>
		<div class="popupContent3">
			<p>即将保存请稍后</p>
		</div>
	</div> -->


	<div class="buttonWrap1 noPrint">
		<p class="radiusUp">1</p>
		<ul>
			<li class="btnClose"><a class="btn">关闭</a></li>
			<li class="btnPrint"><a class="btn">打印</a></li>
			<li class="btnSave"><a class="btn">保存</a></li>
			<li class="btnTop"><a class="btn">顶部</a></li>
			<li class="btnBottom"><a class="btn">底部</a></li>
		</ul>
		<p class="radiusDown">1</p>
	</div>
	<div id="reportWrap">
		<iframe id="mainIframe" src="" width="100%" height="100%"
			frameborder="no" border="0" marginwidth="0" marginheight="0"
			allowtransparency="yes" scroll="auto"></iframe>

	</div>

	<div class="buttonWrap2 noPrint">
		<table id="footertable" width="100%" cellspacing="0" cellpadding="0"
			border="0" align="center">
			<tbody>
				<tr>
					<td height="59">
						<table width="50%" cellspacing="0" cellpadding="0" border="0"
							align="center">
							<tbody>
								<tr>
									<td align="center"><label class="btnClose"> <input
											style="" value="关 闭" type="button" />
									</label></td>
									<td align="center"><label class="btnPrint"> <input
											style="" value="打 印" type="button" />
									</label></td>
									<!-- 
									 <td align="center"><label class="btnSave"> <input
											value="保 存" type="button" />
									</label></td>
									 -->
									<td align="center"><label class="btnTop"> <input
											value="顶 部" type="button" /></label></td>
									<td align="center"><label class="btnBottom"> <input
											value="底 部" type="button" /></label></td>
								</tr>
							</tbody>
						</table>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<form id="imageform" method="get" action="queryreq/showCredit">
		<input type="hidden" name="method" value="downLoadReport" /> <input
			type="hidden" name="creditId"
			value="<%=request.getAttribute("creditId") + ""%>" />
	</form>
	<script type="text/javascript" src="../static/script/pageIncludeScript.js"></script>
	<script type="text/javascript" src="../static/script/watermark.js"></script>
	<script LANGUAGE="JavaScript"> 
 	var onDoing = false;

	$(function(){
		var report = report || {} ;
			report.index = {
				init : function() {
					var t = this;
					// 判断浏览器IE版本 
					if (t.IETester() == 6 || t.IETester() == 7) { 
						$(".buttonWrap1").hide();
						$(".buttonWrap2").show();
						var h = $("#reportWrap").height()
								- $(".buttonWrap2").height();
						$("#reportWrap").css({
							"height" : h
						});
						// 绑定按钮菜单事件
						$(".buttonWrap2 input").on(
								{
									"mouseover" : function() {
										$(this).addClass("btnHover");
									},
									"mouseout" : function() {
										$(this).removeClass("btnHover");
									},
									"click" : function() {
										$(this).addClass("btnClick");
										$(this).parents("td").siblings().find(
												"input")
												.removeClass("btnClick");
									}
								});
					} else {
						$(".buttonWrap2").hide();
						$(".buttonWrap1").show();
						// 绑定按钮菜单事件
						$(".buttonWrap1").on({
							"mouseover" : t.mouseover,
							"mouseout" : t.mouseout
						});
					}
					;
					// 绑定关闭页面事件
					$(".btnClose .btn,.btnClose input")
							.on("click", t.closePage);
					// 绑定关闭提示框事件
					$(".popup a,.popupBg").on("click", t.popupHide);
					// 绑定禁止鼠标右键、Ctrl键和F12事件
					$(document).on({
						"mousedown" : t.mousedown,
						"keydown" : t.keydown
					});
					// 添加iframe内报告url
					// $("#mainIframe").attr({"src":reportUrl});
					
					// t.mainIframe($("#mainIframe"));
					// $("#mainIframe").load(t.mainIframe());
					// 调整判断iframe内容加载完毕后执行mainIframe;
					var iframe = $("#mainIframe")[0];
					iframe.src = 'showCredit?creditId=<%=request.getAttribute("creditId") + ""%>&recordId=<%=request.getAttribute("recordId") + ""%>';

					if (!t.IETester()) { 
					    iframe.onload = function(){ 
					        t.mainIframe();
					    };
					} else {
					    iframe.onreadystatechange = function(){
					        if (iframe.readyState == "complete"){ 
					   				t.mainIframe();
					        }
				    	};
					}
					$("#reportWrap").fadeIn(2000);
					// 隐藏保存按钮
					<%if ("1".equals(ConfigUtil.getSavePolicy().trim()) || "1".equals(userAttr.getSavePermit())) {%>
								$(".btnSave").hide();
						    <%}
					if ("1".equals(ConfigUtil.getPrintPolicy().trim()) || "1".equals(userAttr.getPrintPermit())) {%>
								$(".btnPrint").hide();
						    <%}%>
					},
				mainIframe : function() {
					var t = report.index;
					var body = $("#mainIframe").contents().find("BODY");
					// 去除报告表格白色背景色
					       if(body.find("td").attr("background-color") == "#ffffff"){
					       	body.find("td").removeAttr("bgcolor");
					       }   
					// body.find("td").removeAttr("bgcolor");
					// 设置水印图片
					<%if("1".equals(ConfigUtil.getWatermarkPolicy())) {%>
						var data= getAjaxData('<%=basePath %>common/getImage');
// 						var data = ["操作员001","农业发展银行北京分行","自定义内容"];
					    var str = data + "";
					    window.onresize = function() {
					        watermark.load({ watermark_txt: str, watermark_width: 200, watermark_height: 150, watermark_rows: 200,watermark_cols:20});
					    };
					    
					    watermark.load({ watermark_txt: str, watermark_width: 200, watermark_height: 150, watermark_rows: 200,watermark_cols:20 });
					    
						// body.css({
						// 	"background-image" : "url('<%=basePath %>common/getImage')"
						// }); 
						
					<%}%>
					var iframeBody = {
						oncontextmenu : 'return false;',
						ondragstart : 'return false;',
						onselectstart : 'return false;',
						oncopy : 'document.selection.empty();',
						onbeforecopy : 'return false;'
					};
					// 绑定打印事件
					$(".btnPrint .btn,.btnPrint input").on("click", t.preview);
					// 绑定保存事件
					$(".btnSave .btn,.btnSave input").on("click", t.savePage);
					// 绑定top事件
					$(".btnTop .btn,.btnTop input").on("click", function() {
						body.scrollTop(0);
					});
					// 绑定bottom事件
					$(".btnBottom .btn,.btnBottom input").on("click",
						function() {
							body.scrollTop(body[0].scrollHeight);
					});
					// 操作iframe
					body.attr(iframeBody);
					// 禁止浏览器选中文字
					body.css({
						"-moz-user-select" : "none",
						"-webkit-user-select" : "none",
						"-ms-user-select" : "none",
						"-khtml-user-select" : "none",
						"user-select" : "none"
					});
					body[0].onselectstart = body[0].ondrag = function() {
						return false;
					}
					// 绑定禁止鼠标右键、Ctrl键和F12事件
					body.on({
						"mousedown" : t.mousedown,
						"keydown" : t.keydown
					});
				},
				IETester : function(userAgent) {
					var UA = userAgent || navigator.userAgent;
					if (/msie/i.test(UA)) {
						return UA.match(/msie (\d+\.\d+)/i)[1];
					} else if (~UA.toLowerCase().indexOf('trident')
							&& ~UA.indexOf('rv')) {
						return UA.match(/rv:(\d+\.\d+)/)[1];
					}
					return false;
				},
			  	mousedown : function() {
					evt = window.event || arguments[0];
					if (evt.button == 2) {
						// $(".popupBg").show();
						// $(".popup").show();
						// $(".close").show();
						// $(".popupContent2").hide();
						// $(".popupContent3").hide();
						// $(".popupContent").show();
						layer.alert("右键功能被禁用！",{icon: 4});
						return false;
					}
				},
				keydown : function() {
					evt = window.event || arguments[0];
					k = (evt.which) || (evt.keyCode);
					// 禁止F12键
					if (k == 123) {
						k = 0; //取消按键操作
						evt.cancelBubble = true; //取消事件冒泡
						evt.returnValue = false; //阻止事件默认行为
						layer.alert("快捷键功能已被禁用！",{icon: 4});
						return false;
					} 
					// 禁止复制、保存、打印
					if (evt.ctrlKey && ((k == 67) || (k == 80) || (k == 83))) {
						if(!(navigator.userAgent.indexOf('Firefox') >= 0)){
							layer.alert("快捷键功能已被禁用！",{icon: 4});
						}
						k = 0; //取消按键操作
						evt.cancelBubble = true; //取消事件冒泡
						evt.returnValue = false; //阻止事件默认行为
						layer.alert("快捷键功能已被禁用！",{icon: 4});
						return false;
					}
				}, 
				mouseover : function() {
					$(this).stop().animate({
						"right" : "0px"
					});
				},
				mouseout : function() {
					$(this).stop().animate({
						"right" : "-80px"
					});
				},
				//原DIV打印方法 
				preview : function() {
					if(onDoing){
						return;
					}
					var printUrl ='savePrintLog?recordId=<%=request.getAttribute("recordId") + ""%>';
					onDoing = true; 
					ajaxFunc(printUrl,function(data){
						result =data;
						if(result.code == "00000000"){
						   $("#mainIframe")[0].contentWindow.focus();  
					       $("#mainIframe")[0].contentWindow.print(); 
						   onDoing = false; 
					       return;
						} else{
							layerMsg(result.msg);
							onDoing = false;
							return
						}
					});
					onDoing = false; 
				    return;
				},
				savePage : function() {
					if(onDoing){
						return;
					}
					onDoing = true; 
					var saveURl ='saveReport?creditId=<%=request.getAttribute("creditId") + ""%>&recordId=<%=request.getAttribute("recordId") + ""%>';
					window.location.href =saveURl;
					onDoing = false; 
				    return;
				},
			
				//关闭页面方法
				closePage : function() {
					var browserName = navigator.appName;
					if (browserName == "Netscape") {
						window.open('', '_parent', '');
						window.close();
					} else if (browserName == "Microsoft Internet Explorer") {
						window.opener = "whocares";
						window.close();
					}
				}
			}
			$(function() {
				report.index.init();
				//PageSetup_Null();//设置浏览器打印页眉页脚页边距
			})
		})
		
		/* 设置浏览器打印页眉页脚页边距 
  		       要该代码生效需要在浏览器手动启用ActiveX控件，步骤如下：
  		       打开ie浏览器internet选项—— 安全—— 自定义级别—— 把对没有标记为安全的activex控件进行初始化和脚本运行 设置为启用
  		*/
 		/* var hkey_path;
 		hkey_path = "HKEY_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\PageSetup\\";
		function PageSetup_Null(){   
			try{ 
				var RegWsh = new ActiveXObject("WScript.Shell");
				//页眉页脚为空
		        RegWsh.RegWrite(hkey_path + "header", "");
		        RegWsh.RegWrite(hkey_path + "footer", "");
		        //设置下、左、右、上页边距
		        RegWsh.RegWrite(hkey_path + "margin_bottom", "0");
		        RegWsh.RegWrite(hkey_path + "margin_left", "0");
		        RegWsh.RegWrite(hkey_path + "margin_right", "0");
		        RegWsh.RegWrite(hkey_path + "margin_top", "8");
			}catch(e){
				alert("ActiveX控件被禁用,请按下面步骤操作：\n1、请打开浏览器‘工具’菜单/‘选项’/‘安全’下的‘自定义级别’，\n把‘对没有标记为安全的activex控件进行初始化和脚本运行’设置为‘启用’。\n2、刷新本页 ");   
			}   
		}  */ 

		function popupHide() {
			$(".popup").hide();
			$(".popupBg").hide();
			onDoing = false;
		}
	
	</script>
	</body>
</html>
