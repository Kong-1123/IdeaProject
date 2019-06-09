package cn.com.dhcc.creditquery.person.queryweb.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClientUtil {

	private static Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
	public static final int connTimeout = 10000;
	public static final int readTimeout = 10000;

	/**
	 * 超时时间默认10s
	 * @param url
	 * @param params
	 * @param encoding
	 * @throws ParseException
	 * @throws IOException
	 * @return String
	 */
	public static String send(String url, Map<String, String> params, String encoding) throws ParseException, IOException {
		return send(url, params, encoding, connTimeout, readTimeout);
	}

//	/**
//	 * 超时时间默认10s
//	 * @param params
//	 * @param encoding
//	 * @param httpPost
//	 * @param handler
//	 * @throws ParseException
//	 * @throws IOException
//	 * @return ResponseInfo
//	 */
//	public static ResponseInfo send(Map<String, String> params, String encoding, HttpPost httpPost, CoordinateHandler<ResponseInfo> handler) throws ParseException, IOException {
//		return send(params, encoding, httpPost, handler, connTimeout, readTimeout);
//	}

	/**
	 * 自定义超时时间
	 * @param url
	 * @param params
	 * @param encoding
	 * @param connTimeout
	 * @param readTimeout
	 * @throws ParseException
	 * @throws IOException
	 * @return String
	 */
	public static String send(String url, Map<String, String> params, String encoding, Integer connTimeout, Integer readTimeout) throws ParseException, IOException {
		String body = "";
		// 创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);
		// 设置参数
		Builder customReqConf = RequestConfig.custom();
		if (connTimeout != null) {
			customReqConf.setConnectTimeout(connTimeout);
		}
		if (readTimeout != null) {
			customReqConf.setSocketTimeout(readTimeout);
		}
		httpPost.setConfig(customReqConf.build());

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (params != null) {
			for (Entry<String, String> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		// 设置参数到请求对象中
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

		log.info("请求地址：" + url);
		log.info("请求参数：" + nvps.toString());

		// 设置header信息
		// 指定报文头【Content-type】、【User-Agent】
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPost);
		int httpCode = response.getStatusLine().getStatusCode();
		log.info("响应状态={}", httpCode);

		if (httpCode == 302) {
			Header header = response.getFirstHeader("location"); // 跳转的目标地址是在
			                                                     // HTTP-HEAD 中的
			String newuri = header.getValue(); // 这就是跳转后的地址，再向这个地址发出新申请，以便得到跳转后的信息是啥。
			log.info("重定向请求地址={}", newuri);
			httpPost = new HttpPost(newuri);
			httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
			if (nvps.size() != 0) {
				// 设置参数到请求对象中
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
			}
			response = client.execute(httpPost);
			httpCode = response.getStatusLine().getStatusCode();
			log.info("重定向后响应状态={}", httpCode);
		}
		// 获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		// 释放链接
		response.close();
		log.info("交易响应结果：{}", body);
		return body;
	}

//	/**
//	 * 自定义超时时间
//	 * @param params
//	 * @param encoding
//	 * @param httpPost
//	 * @param handler
//	 * @param connTimeout
//	 * @param readTimeout
//	 * @throws ParseException
//	 * @throws IOException
//	 * @return ResponseInfo
//	 */
//	public static ResponseInfo send(Map<String, String> params, String encoding, HttpPost httpPost, CoordinateHandler<ResponseInfo> handler, Integer connTimeout, Integer readTimeout) throws ParseException, IOException {
//		CloseableHttpClient client = HttpClients.createDefault();
//
//		// 设置参数
//		Builder customReqConf = RequestConfig.custom();
//		if (connTimeout != null) {
//			customReqConf.setConnectTimeout(connTimeout);
//		}
//		if (readTimeout != null) {
//			customReqConf.setSocketTimeout(readTimeout);
//		}
//		httpPost.setConfig(customReqConf.build());
//
//		// 装填参数
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		if (params != null) {
//			for (Entry<String, String> entry : params.entrySet()) {
//				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//			}
//		}
//		// 设置参数到请求对象中
//		httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));
//		log.info("请求参数：" + nvps.toString());
//		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
//		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//		ResponseInfo response = client.execute(httpPost, handler);
//		client.close();
//		return response;
//	}

}
