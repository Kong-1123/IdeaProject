<%@ page contentType="text/html; charset=GBK"%>
<%@ page import="java.util.*"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>��ҵ���ñ���</title>
<style type="text/css">
<!--
.style2 {
	font-size: 18px;
	font-weight: bold;
}
-->
.tdStyle{
	  height:25px;
	  font-size: 12px;
	  align:left;
 	  background-color:"#87bff7";
}
.tdStyle1{
	  height:25px;
	  font-size: 12px;
	  align:left;
}

<!--
.black{
    color:black;
    font-size: 12;
}

-->
</style>
<link href="<html:rewrite page='/css/common_report.css'/>"	rel="stylesheet" type="text/css">
</head>
<body onload="Load()">
	<br>
	<form action="" name="form1"></form>
	<table width="300px" align='center'>
		<tr><td align='center'>
		<input type='button' class="input-button" value="�� ӡ"  onclick='javascript:window.print();'/>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<input type='button' class="input-button" value="�� ��"  onclick='javascript:window.close();'/>
		</td></tr>
	</table>
</body>
</html>
<script type="text/javascript">
function Load()
{   
    
	var obj = window.dialogArguments;
	
	if(obj == null || obj == '' || obj == 'undefined'){
		window.document.form1.innerHTML ="<div align='center'><b>û�и�ҵ�����������ϸ��Ϣ</b></div>";	
		return;
	}
	var arg = obj.name;
	if(arg == null || arg == '' || arg == 'undefined'){
		window.document.form1.innerHTML ="<div align='center'><b>û�и�ҵ�����������ϸ��Ϣ</b></div>";
		return;
	}
	
	window.document.form1.innerHTML =arg;


}

</script>


