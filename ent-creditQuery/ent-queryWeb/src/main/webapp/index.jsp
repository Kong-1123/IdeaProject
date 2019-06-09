<%@ page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	
	<link rel="stylesheet" type="text/css" href="css/my.css">
	
  </head>
  
  <body>
   
	
<div id="header_index"><h1><a href="http://www.w3cschool.cn/index-4.html" title="W3CSchool 在线教程">W3CSchool 在线教程</a></h1></div>
	
<div id="navfirst">

<ul id="menu">

<li id="h"><a href="http://www.w3cschool.cn/h.html" title="HTML 教程">HTML 教程</a></li>

<li id="x"><a href="http://www.w3cschool.cn/x.html" title="XML 教程">XML 教程</a></li>

<li id="b"><a href="http://www.w3cschool.cn/b.html" title="浏览器脚本">浏览器脚本</a></li>

<li id="s"><a href="http://www.w3cschool.cn/s.html" title="服务器脚本">服务器脚本</a></li>

<li id="d"><a href="http://www.w3cschool.cn/d.html" title="dot net 教程">dot net 教程</a></li>

<li id="m"><a href="http://www.w3cschool.cn/m.html" title="多媒体教程">多媒体教程</a></li>

<li id="w"><a href="http://www.w3cschool.cn/w.html" title="建站手册">建站手册</a></li>

</ul>

</div>


<div id="navsecond">

<h2>HTML教程</h2>

<ul>

<li><a href="http://www.w3cschool.cn/index-5.html" title="HTML 教程">HTML</a></li>

<li><a href="http://www.w3cschool.cn/index-6.html" title="XHTML 教程">XHTML</a></li>

<li><a href="http://www.w3cschool.cn/index-7.html" title="CSS 教程">CSS</a></li>

<li><a href="http://www.w3cschool.cn/index-8.html" title="TCP/IP 教程">TCP/IP</a></li>

</ul>



<h2>XML教程</h2>

<ul>

<li><a href="http://www.w3cschool.cn/index-9.html" title="XML 教程">XML</a></li>

<li><a href="http://www.w3cschool.cn/index-10.html" title="DTD教程">DTD</a></li>

<li><a href="http://www.w3cschool.cn/index-11.html" title="XML DOM 教程">XML DOM</a></li>

<li><a href="http://www.w3cschool.cn/xsl_languages.html" title="XSL 语言">XSL</a></li>

<li><a href="http://www.w3cschool.cn/index-12.html" title="XSLT 教程">XSLT</a></li>

<li><a href="http://www.w3cschool.cn/index-13.html" title="XSL-FO 教程">XSL-FO</a></li>

<li><a href="http://www.w3cschool.cn/index-14.html" title="XPath 教程">XPath</a></li>

<li><a href="http://www.w3cschool.cn/index-15.html" title="XQuery 教程">XQuery</a></li>

<li><a href="http://www.w3cschool.cn/index-16.html" title="XLink 教程">XLink</a></li>

<li><a href="http://www.w3cschool.cn/index-16.html" title="XPointer 教程">XPointer</a></li>

<li><a href="http://www.w3cschool.cn/index-17.html" title="Schema 教程">Schema</a></li>

<li><a href="http://www.w3cschool.cn/index-18.html" title="XForms 教程">XForms</a></li>

<li><a href="http://www.w3cschool.cn/index-19.html" title="SOAP 教程">SOAP</a></li>

<li><a href="http://www.w3cschool.cn/index-20.html" title="WSDL 教程">WSDL</a></li>

<li><a href="http://www.w3cschool.cn/index-21.html" title="RDF 教程">RDF</a></li>

<li><a href="http://www.w3cschool.cn/index-22.html" title="RSS 教程">RSS</a></li>

<li><a href="http://www.w3cschool.cn/index-23.html" title="WAP 教程">WAP</a></li>

<li><a href="http://www.w3cschool.cn/index-24.html" title="Web Services 教程">Web Services</a></li>

</ul>



<h2>浏览器脚本</h2>

<ul>

<li><a href="http://www.w3cschool.cn/index-25.html" title="JavaScript 教程">JavaScript</a></li>

<li><a href="http://www.w3cschool.cn/index-26.html" title="HTML DOM 教程">HTML DOM</a></li>

<li><a href="http://www.w3cschool.cn/index-27.html" title="DHTML 教程">DHTML</a></li>

<li><a href="http://www.w3cschool.cn/index-28.html" title="VBScript 教程">VBScript</a></li>

<li><a href="http://www.w3cschool.cn/index-29.html" title="AJAX 教程">AJAX</a></li>

<li><a href="http://www.w3cschool.cn/index-30.html" title="jQuery 教程">jQuery</a></li>

<li><a href="http://www.w3cschool.cn/index-31.html" title="E4X 教程">E4X</a></li>

<li><a href="http://www.w3cschool.cn/index-32.html" title="WMLScript 教程">WMLScript</a></li>

</ul>



<h2>服务器脚本</h2>

<ul>

<li><a href="http://www.w3cschool.cn/index-33.html" title="SQL 教程">SQL</a></li>

<li><a href="http://www.w3cschool.cn/index-34.html" title="ASP 教程">ASP</a></li>

<li><a href="http://www.w3cschool.cn/index-35.html" title="ADO 教程">ADO</a></li>

<li><a href="http://www.w3cschool.cn/index-36.html" title="PHP 教程">PHP</a></li>

</ul>



<h2>.NET(dotnet)</h2>

<ul>

<li><a href="http://www.w3cschool.cn/index-37.html" title="Microsoft.NET 教程">.NET Microsoft</a></li>

<li><a href="http://www.w3cschool.cn/index-38.html" title="ASP.NET 教程">.NET ASP</a></li>

<li><a href="http://www.w3cschool.cn/index-39.html" title=".NET Mobile 教程">.NET Mobile</a></li>

</ul>



<h2>多媒体</h2>

<ul>

<li><a href="http://www.w3cschool.cn/index-40.html" title="Media 教程">Media</a></li>

<li><a href="http://www.w3cschool.cn/index-41.html" title="SMIL 教程">SMIL</a></li>

<li><a href="http://www.w3cschool.cn/index-42.html" title="SVG 教程">SVG</a></li>

</ul>



<h2>建站手册</h2>

<ul>

<li><a href="http://www.w3cschool.cn/index-43.html" title="网站构建">网站构建</a></li>

<li><a href="http://www.w3cschool.cn/index-44.html" title="万维网联盟 (W3C)">万维网联盟 (W3C)</a></li>

<li><a href="http://www.w3cschool.cn/index-45.html" title="浏览器信息">浏览器信息</a></li>

<li><a href="http://www.w3cschool.cn/index-46.html" title="网站品质">网站品质</a></li>

<li><a href="http://www.w3cschool.cn/index-47.html" title="语义网">语义网</a></li>

<li><a href="http://www.w3cschool.cn/index-48.html" title="职业规划">职业规划</a></li>

<li><a href="http://www.w3cschool.cn/index-49.html" title="网站主机教程">网站主机</a></li>

</ul>



<h2><a href="http://www.w3cschool.cn/index-50.html" title="关于 W3CSchool" id="link_about">关于 W3CSchool</a></h2>

<h2><a href="http://www.w3cschool.cn/about_helping.html" title="帮助 W3CSchool" id="link_help">帮助 W3CSchool</a></h2>

</div>



<div id="maincontent">



<div id="w3">

<h2>领先的 Web 技术教程 - 全部免费</h2>

<p>在W3CSchool，你可以找到你所需要的所有的网站建设教程。</p>

<p>从基础的HTML到XHTML，乃至进阶的XML、SQL、数据库、多媒体和WAP。</p>

<p><strong>从左侧的菜单选择你需要的教程！</strong></p>

</div>



<div class="idea">

<img src="./W3CSchool_files/site_photoref.jpg" alt="完整的网站技术参考手册">

<h3>完整的网站技术参考手册</h3>

<p>我们的参考手册涵盖了网站技术的方方面面。</p>

<p>其中包括W3C的标准技术：HTML、XHTML、CSS、XML 。以及其他的技术，诸如JavaScript、PHP、ASP、SQL等等。</p>

</div>



<div class="idea">

<img src="./W3CSchool_files/site_photoexa.jpg" alt="在线实例测试工具">

<h3>在线实例测试工具</h3>

<p>在W3CSchool，我们提供上千个实例。</p>

<p>通过使用我们的在线编辑器，你可以编辑这些例子，并对代码进行实验。</p>

</div>



<div class="idea">

<img src="./W3CSchool_files/site_photoqe.jpg" alt="快捷易懂的学习方式">

<h3>快捷易懂的学习方式</h3>

<p>一寸光阴一寸金，因此，我们为您提供快捷易懂的学习内容。</p>

<p>在这里，您可以通过一种易懂的便利的模式获得您需要的任何知识。</p>

</div>



<div>

<h3>从何入手？</h3>

<p>什么是一个Web建设者需要学习的知识呢？</p>

<p>W3CSchool将为您回答这个问题，在您成为专业Web开发者的路上助一臂之力，从而更好地应对未来的挑战。</p>

<p>如果您是初学者，请您阅读《<a href="http://www.w3cschool.cn/index-51.html" title="网站构建初级教程">网站构建初级教程</a>》。</p>

<p>如果您是开发者，请您阅读《<a href="http://www.w3cschool.cn/index-43.html" title="网站构建">网站构建高级教程</a>》。</p>

</div>



<div>

<h3>W3CSchool 新闻</h3>

<p>

<span><a href="http://www.w3cschool.cn/browsers_safari.html">苹果公司发布 Safari 5 浏览器</a></span>

&nbsp;&nbsp;&nbsp;

<span><a href="http://www.w3cschool.cn/index-52.html">W3C 发布 HTML 5 工作草案</a></span>

</p>

</div>





<div>

<h3>W3CSchool 更新信息</h3>



<p>

与 W3C 中国办事处合作开发的<a href="http://www.w3cschool.cn/index-53.html" title="W3C 术语表和词典">术语表</a>已经开始测试，目前以字母 A 开头的条目已上线，敬请批评指正。

</p>



<p>参考手册：<a href="http://www.w3cschool.cn/jquery_reference.html">jQuery 参考手册</a></p>

</div>





<div>

<h3>W3CSchool 友情链接</h3>

<p class="partner"><a href="http://www.news.cn/internet/">新华网 - 互联网频道</a> &nbsp;&nbsp;&nbsp; <a href="http://www.chinaw3c.org/">W3C 中国办事处</a></p>

</div>



</div>





<div id="sidebar">



<div id="searchui">

<form method="get" id="searchform" action="http://www.google.com/search">

<p><label for="searched_content">Search:</label></p>

<p><input type="hidden" name="sitesearch" value="www.W3CSchool.cn"></p>

<p>

<input type="text" name="as_q" class="box" id="searched_content" title="在此输入搜索内容。">

<input type="submit" value="Go" class="button" title="搜索！">

</p>

</form>

</div>



<h2>参考手册</h2>

<ul>

<li><a href="http://www.w3cschool.cn/index-54.html" title="HTML 4.01/XHTML 1.0 参考手册">HTML 4.01</a></li>

<li><a href="http://www.w3cschool.cn/index-54.html" title="HTML 4.01/XHTML 1.0 参考手册">XHTML 1.0</a></li>

<li><a href="http://www.w3cschool.cn/html5_reference.html" title="HTML 5 参考手册">HTML 5</a></li>

<li><a href="http://www.w3cschool.cn/css_reference.html" title="CSS2 参考手册">CSS 2.0</a></li>

<li><a href="http://www.w3cschool.cn/js_reference.html" title="JavaScript 参考手册">JavaScript</a></li>

<li><a href="http://www.w3cschool.cn/jquery_reference.html" title="jQuery 参考手册">jQuery</a></li>

<li><a href="http://www.w3cschool.cn/vbscript_ref_functions.html" title="VBScript 函数">VBScript</a></li>

<li><a href="http://www.w3cschool.cn/htmldom_reference.html" title="HTML DOM 参考手册">HTML DOM</a></li>

<li><a href="http://www.w3cschool.cn/xmldom_reference.html" title="XML DOM 参考手册">XML DOM</a></li>

<li><a href="http://www.w3cschool.cn/asp_ref.html" title="ASP 参考手册">ASP</a></li>

<li><a href="http://www.w3cschool.cn/ado_reference.html" title="ADO 参考手册">ADO</a></li>

<li><a href="http://www.w3cschool.cn/aspnet_reference.html" title="ASP.NET 参考手册">ASP.NET</a></li>

<li><a href="http://www.w3cschool.cn/php_ref.html" title="PHP 参考手册">PHP 5.1</a></li>

<li><a href="http://www.w3cschool.cn/xsl_w3celementref.html" title="XSLT 元素参考手册">XSLT 1.0</a></li>

<li><a href="http://www.w3cschool.cn/xpath_functions.html" title="XPath、XQuery 以及 XSLT 函数">XPath 2.0</a></li>

<li><a href="http://www.w3cschool.cn/xslfo_reference.html" title="XSL-FO 参考手册">XSL-FO</a></li>

<li><a href="http://www.w3cschool.cn/wml_reference.html" title="WML 参考手册">WML 1.1</a></li>

<li><a href="http://www.w3cschool.cn/html_ref_colornames.html" title="HTML 颜色名">HTML 颜色</a></li>

<li><a href="http://www.w3cschool.cn/index-53.html" title="W3C 术语表和词典">W3C 术语表</a></li>

</ul>



<h2>字符集</h2>

<ul>

<li><a href="http://www.w3cschool.cn/html_ref_charactersets.html" title="HTML 字符集">HTML 字符集</a></li>

<li><a href="http://www.w3cschool.cn/html_ref_ascii.html" title="HTML ASCII 参考手册">HTML ASCII</a></li>

<li><a href="http://www.w3cschool.cn/html_ref_entities.html" title="HTML ISO-8859-1 参考手册">HTML ISO-8859-1</a></li>

<li><a href="http://www.w3cschool.cn/html_ref_symbols.html" title="HTML 4.01 符号实体">HTML 符号</a></li>

</ul>



<h2>实例/案例</h2>

<ul>

<li><a href="http://www.w3cschool.cn/html_examples.html">HTML 实例</a></li>

<li><a href="http://www.w3cschool.cn/csse_examples.html">CSS 实例</a></li>

<li><a href="http://www.w3cschool.cn/xmle_examples.html">XML 实例</a></li>

<li><a href="http://www.w3cschool.cn/jseg_examples.html">JavaScript 实例</a></li>

<li><a href="http://www.w3cschool.cn/jsrf_examples.html">JavaScript 对象实例</a></li>

<li><a href="http://www.w3cschool.cn/hdom_examples.html">HTML DOM 实例</a></li>

<li><a href="http://www.w3cschool.cn/xdom_examples.html">XML DOM 实例</a></li>

<li><a href="http://www.w3cschool.cn/dhtm_examples.html">DHTML 实例</a></li>

<li><a href="http://www.w3cschool.cn/ajax_examples.html">AJAX 实例</a></li>

<li><a href="http://www.w3cschool.cn/vbst_examples.html">VBScript 实例</a></li>

<li><a href="http://www.w3cschool.cn/aspe_examples.html">ASP 实例</a></li>

<li><a href="http://www.w3cschool.cn/adoe_examples.html">ADO 实例</a></li>

<li><a href="http://www.w3cschool.cn/svg_examples.html">SVG 实例</a></li>

<li><a href="http://www.w3cschool.cn/wap_demo.html" title="WAP 演示">WAP 实例</a></li>

</ul>



<h2>测验/考试</h2>

<ul>

<li><a href="http://www.w3cschool.cn/html_quiz.html">HTML 测验</a></li>

<li><a href="http://www.w3cschool.cn/xhtml_quiz.html">XHTML 测验</a></li>

<li><a href="http://www.w3cschool.cn/css_quiz.html">CSS 测验</a></li>

<li><a href="http://www.w3cschool.cn/xml_quiz.html">XML 测验</a></li>

<li><a href="http://www.w3cschool.cn/js_quiz.html">JavaScript 测验</a></li>

<li><a href="http://www.w3cschool.cn/sql_quiz.html">SQL 测验</a></li>

<li><a href="http://www.w3cschool.cn/asp_quiz.html">ASP 测验</a></li>

<li><a href="http://www.w3cschool.cn/php_quiz.html">PHP 测验</a></li>

</ul>



<h2>代码验证</h2>

<ul>

<li><a href="http://www.w3cschool.cn/site_validate.html" title="网页验证">验证HTML</a></li>

<li><a href="http://www.w3cschool.cn/site_validate.html" title="网页验证">验证CSS</a></li>

<li><a href="http://www.w3cschool.cn/site_validate.html" title="网页验证">验证XHTML</a></li>

<li><a href="http://www.w3cschool.cn/site_validate.html" title="网页验证">验证XML</a></li>

<li><a href="http://www.w3cschool.cn/site_validate.html" title="网页验证">验证WML</a></li>

</ul>



</div>





<div id="footer">

<p>W3CSchool提供的内容仅用于培训。我们不保证内容的正确性。通过使用本站内容随之而来的风险与本站无关。  W3CSchool 简体中文版的所有内容仅供测试，对任何法律问题及风险不承担任何责任。QQ:602648598。</p>

</div>


  </body>
</html>
