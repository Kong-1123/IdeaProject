<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>printReport</title>
<script type="text/javascript" src="<%=basePath%>report/script/jquery.min.js"></script>
<style type="text/css">
	.clearfix:after{content:".";display:block;height:0;clear:both;visibility:hidden}
	.clearfix{*+height:1%;}
	*{
		margin: 0px;
		padding: 0px;
	}
	body{
		position: relative;
		overflow-x: hidden;
	}
	img{
		display:block;
		position: relative;
	}
	.printBtn{
		width: 100%;
		height: 50px;
		color:#333;
      	font-size:16px;
      	font-family: "宋体";
		line-height:50px;
		background-color: #fff;
		border-bottom: 1px solid #ccc; 
		position: absolute;
		top: 0;
	}
	.printBtn input{
		display:block;
      	width:100px;
      	height:30px;
      	border:1px solid #333;
      	background-color:#fff;
      	float: right;
      	margin: 10px 30px 0px 0px;
	}
	.printBtn input.btnHover{
      color:#79BBCB;
      border-color:#79BBCB;
    }
    .printBtn p{
    	font-size: 12px;
    	color: #79BBCB;
    	float: right;
    	margin-right: 30px;
    }
    .printContent{
      text-align: center;
      width: 1024px;
      margin: 0 auto;
      padding-top: 50px; 
    }
    .printTop a{
    	display: none;
    	position:absolute;
    	right: 50px;
    	bottom: 50px;
    	padding: 14px 24px; 
    	padding: 14px 14px\9;
    	*padding: 14px 24px;
		border :1px solid #ddd;
		text-indent: -9999px;
		background: #fff url(report/images/top_hover.png) no-repeat center center;
    }
</style>
<style media="print" type="text/css"> 
	.noprint{visibility:hidden} 
</style>
</head>
<body>

	<div class="printContent">
		<%
		    List<Long> idlist = (List<Long>) request.getAttribute("ids");

				    for (int i = 0; i < idlist.size(); i++) {
						long id = idlist.get(i);
		%>
		<img id="printImg"
			src="<%=basePath%>QpCreditReport.do?method=getReportImageToShow&id=<%=id%>&random=<%=Math.random()%>"
			alt="" />
		<%
		    }
		%>
	</div>
	<div class="printBtn clearfix noprint">
		<input type="button" value="打 印" />
		<p>报告完成加载，请点击打印按钮</p>
	</div>
	<div class="printTop noprint">
		<a href="javascript:void(0);">向上返回</a>
	</div>
	<script LANGUAGE="JavaScript">
		$(function() {
			$(".printBtn input").on({
				"mouseover" : function() {
					$(this).addClass("btnHover");
				},
				"mouseout" : function() {
					$(this).removeClass("btnHover");
				},
				"click" : function() {
					window.print();
				}
			});
			$(".printTop a").show().on("click", function() {
				$(document).scrollTop(0);
			})
		})
		window.dialogArguments.popupHide();
	</script>
</body>
</html>