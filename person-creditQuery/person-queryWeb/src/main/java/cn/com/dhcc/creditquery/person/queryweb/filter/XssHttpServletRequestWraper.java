package cn.com.dhcc.creditquery.person.queryweb.filter;  
  
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringEscapeUtils;
/** 
 *  
 * @author wk 
 * @date 2015-8-6 
 */  
public class XssHttpServletRequestWraper extends HttpServletRequestWrapper {  
  
	 public XssHttpServletRequestWraper(HttpServletRequest servletRequest) {
	        super(servletRequest);
	    }

	    @Override
	    public String[] getParameterValues(String parameter) {
	        String[] values = super.getParameterValues(parameter);

	        if (values == null) {
	            return null;
	        }

	        int count = values.length;
	        String[] encodedValues = new String[count];
	        String encode;
	        for (int i = 0; i < count; i++) {
	        	encode=htmlEncode(values[i]);
	            encodedValues[i] = stripXSS(encode);
	        }

	        return encodedValues;
	    }

	    @Override
	    public String getParameter(String parameter) {
	        String value = super.getParameter(parameter);

	        return stripXSS(value);
	    }

	    @Override
	    public String getHeader(String name) {
	        String value = super.getHeader(name);
	        return stripXSS(value);
	    }

	    /*
	     * 此方法慎用，会对汉字进行转译编码
	     */
	    private String escape(String value){
	    	String value1=StringEscapeUtils.escapeHtml(value);
	    	String value2=StringEscapeUtils.escapeJavaScript(value1);
	    	String value3=StringEscapeUtils.escapeSql(value2);
	    	return value3;
	    }
	    
	    private  String htmlEncode(String source) {
	        if (source == null) {
	            return "";
	        }
	        String html = "";
	        StringBuffer buffer = new StringBuffer();
	        for (int i = 0; i < source.length(); i++) {
	            char c = source.charAt(i);
	            switch (c) {
	            case '<':
	                buffer.append("&lt;");
	                break;
	            case '>':
	                buffer.append("&gt;");
	                break;
	            case '&':
	                buffer.append("&amp;");
	                break;
	            case '"':
	                buffer.append("&quot;");
	                break;
	            case 10:
	            case 13:
	                break;
	            default:
	                buffer.append(c);
	            }
	        }
	        html = buffer.toString();
	        return html;
	    }
	    
	    private String stripXSS(String value) {
	        if (value != null) {
	            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
	            // avoid encoded attacks.
	            // value = ESAPI.encoder().canonicalize(value);

	            // Avoid null characters
	            value = value.replaceAll("", "");
	            /**
	             * at 2018-08-30,修复个人证件号码出现半角括号"()",被替换问题。
	             */
//	            value = value.replaceAll("\\(", "");
//	            value = value.replaceAll("\\)", "");

	            // Avoid anything between script tags
	            Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid anything in a src='...' type of expression
	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Remove any lonesome </script> tag
	            scriptPattern = Pattern.compile("</script>", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Remove any lonesome <script ...> tag
	            scriptPattern = Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid eval(...) expressions
	            scriptPattern = Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid expression(...) expressions
	            scriptPattern = Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid javascript:... expressions
	            scriptPattern = Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid vbscript:... expressions
	            scriptPattern = Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE);
	            value = scriptPattern.matcher(value).replaceAll("");

	            // Avoid onload= expressions
	            scriptPattern = Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
	            value = scriptPattern.matcher(value).replaceAll("");
	        }
	        return value;
	    }
  
}  