<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>

<!DOCTYPE html>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<title>东华软件系统平台</title>
<link rel="shortcut icon" href="static/image/favicon.dx.ico"
	type="image/x-icon" />
<%
String message = (String)request.getAttribute("IPOrMACmsg");
if(null == message){
	message = "";
}
%>
<style>
html, body {
	width: 100%;
	height: 100%;
	margin: 0;
	overflow: hidden;
}

.error-con {
	width: 603px;
	height: 193px;
	margin: 40px auto;
	vertical-align: middle;
	text-indent: -9999px;
	background: #fff url(${ctx}/static/image/error/ipandmac.png) no-repeat
		center center;
}

.message {
	text-align: center;
	color:#00FF00;
}
</style>
</head>
<body>
	<div class="error-con"></div>
	<div class="message">
  		<h3><%=message %></h3>
	</div>
</body>
</html>
