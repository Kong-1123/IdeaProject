package cn.com.dhcc.creditquery.person.reportview.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Objects;

import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 对征信报告进行脱敏的工具类
 * @author 文朝波
 * @date 2018年10月30日
 */

@Slf4j
public class ReportEncryptUtil {
	private final static String DEFAULT_VALUE = "--";
	/**
	 * @Description: 对证件号码进行脱敏
	 * @param htmlPage
	 */
	public static String idEncrypt(String htmlPage) {
		log.info("idcard Encrypt begin!");
		String flag = ConfigUtil.getSensitiveCryto();
		if (StringUtils.equals(flag, "0")) {
			Document doc = Jsoup.parse(htmlPage);

			Elements elements = doc.select("tr:contains(被查询者姓名)+tr");
			if (elements.isEmpty()) {
				Elements element = doc.select("tr:contains(姓名)+tr");
				failIdEncrypt(element, 1, 2);
			} else {
				doIdEncrypt(elements, 1, 2);
			}
			// 配偶证件信息
			Elements el = doc.select("tr:contains(姓名)+tr");
			doIdEncrypt(el, 6, 7);
			htmlPage = doc.html();

		}
		log.info("idcard Encrypt end!");
		return htmlPage;
	}

	/**
	 * @Description: 对电话手机号码进行脱敏
	 * @param htmlPage
	 */
	public static String numberEncrypt(String htmlPage) {
		log.info("numberEncrypt begin!");
		String flag = ConfigUtil.getNumberEncrypt();
		if (StringUtils.equals(flag, "0")) {

			Document doc = Jsoup.parse(htmlPage);
			Elements elements = doc.select("table:has(tbody>tr>th:contains(手机号码))").select("tr");
			if (!elements.isEmpty() && elements.size() > 1) {
				for (int i = 1; i < elements.size(); i++) {
					String tel = elements.get(i).child(1).text().trim();

					if (!isNotDefaultValue(tel))
						break;

					String update = ViewEncryptUtil.numberEncrypt(tel);
					elements.get(i).child(1).text(update);
				}
			}
			// 配偶电话号码
			Elements el = doc.select("table:has(tbody>tr>th:contains(联系电话))").select("tr");
			if (!el.isEmpty() && el.size() > 1) {
				String tel2 = el.get(1).child(4).text().trim();
				if (isNotDefaultValue(tel2)) {
					String update = ViewEncryptUtil.numberEncrypt(tel2);
					el.get(1).child(4).text(update);
				}
			}
			/**
			 * 对单位电话进行脱敏
			 */
			Elements worksEls = doc.select("table:has(tbody>tr>th:contains(单位电话)").select("tr");
			for (int i = 1; i < worksEls.size(); i++) {
				doTelEncryptMulti(worksEls.get(i),4);
			}
			/**
			 * 对住宅电话进行脱敏
			 */
			Elements homeEls = doc.select("table:has(tbody>tr>th:contains(住宅电话)").select("tr");
			for (int i = 1; i < homeEls.size(); i++) {
				doTelEncryptMulti(homeEls.get(i),2);
			}
			return doc.html();
		}
		log.info("numberEncrypt end");
		return htmlPage;
	}

	/**
	 * @Description: 对地址进行脱敏
	 * @param htmlPage
	 */
	public static String addressEncrypt(String htmlPage) {
		log.info("addressEncrypt begin!");
		String flag = ConfigUtil.getAddressEncrypt();
		if (StringUtils.equals(flag, "0")) {
			Document doc = Jsoup.parse(htmlPage);
			Elements elements = doc.select("tr:contains(通讯地址)+tr");

			// 通讯地址
			for (int i = 0; i < 2; i++) {
				ReportEncryptUtil.doAddressEncrypt(elements, i);
			}
			// 居住地址
			Elements el = doc.select("table:has(tbody>tr:contains(居住地址))").select("tr");
			for (int i = 1; i < el.size(); i++) {
				ReportEncryptUtil.doAddressEncryptMulti(el.get(i), 1);
			}
			// 单位地址
			Elements el2 = doc.select("table:has(tbody>tr:contains(单位地址))").select("tr");
			for (int i = 1; i < el2.size(); i++) {
				ReportEncryptUtil.doAddressEncryptMulti(el2.get(i), 3);
			}

			return doc.html();
		}
		log.info("addressEncrypt end!");
		return htmlPage;
	}

	/**
	 * @Description: 对地址进行脱敏的具体实现
	 * @param el
	 * @param i
	 * @return void
	 */
	public static void doAddressEncrypt(Elements el, int i) {
		log.info("doAddressEncrypt begin!");
		if (!el.isEmpty()) {
			Elements elements = el.select("td");
			if (!elements.isEmpty() && elements.size() > i) {
				String address = elements.get(i).text().trim();
				if (isNotDefaultValue(address)) {
					String update = ViewEncryptUtil.addressEncrypt(address);
					elements.get(i).text(update);
				}
			}
		}
		log.info("doAddressEncrypt end!");
	}

	/**
	 * 对单位地址和居住地址进行加密
	 * 
	 * @param el
	 * @param i
	 */
	public static void doAddressEncryptMulti(Element el, int i) {
		log.info("doAddressEncryptMulti begin!");
		if (null != el) {
			Elements elements = el.select("td");
			if (!elements.isEmpty() && elements.size() > i) {
				String address = elements.get(i).text().trim();
				if (isNotDefaultValue(address)) {
					String update = ViewEncryptUtil.addressEncrypt(address);
					elements.get(i).text(update);
				}
			}
		}
		log.info("doAddressEncryptMulti end!");
	}

	/**
	 * 对用户单位电话和住宅电话进行加密
	 * 
	 * @param el
	 * @param i
	 */
	public static void doTelEncryptMulti(Element el, int i) {
		log.info("doTelEncryptMulti begin!");
		if (null != el) {
			Elements elements = el.select("td");
			if (!elements.isEmpty() && elements.size() > i) {
				String tel = elements.get(i).text().trim();
				if (isNotDefaultValue(tel)) {
					String update = ViewEncryptUtil.numberEncrypt(tel);
					elements.get(i).text(update);
				}
			}
		}
		log.info("doTelEncryptMulti end!");
	}

	/**
	 * @Description: 对证件号脱敏的具体实现
	 * @param el
	 * @param first
	 * @param second
	 * @return void
	 */
	public static void doIdEncrypt(Elements el, int first, int second) {
		log.info("doIdEncrypt begin!");
		if (!el.isEmpty()) {
			Elements elements = el.select("td");
			if (!elements.isEmpty() && elements.size() > second) {
				String certType = elements.get(first).text().trim();
				String certNo = elements.get(second).text().trim();
				if (!StringUtils.isBlank(certType)) {
					if (isNotDefaultValue(certNo)) {
						if (StringUtils.equals("身份证", certType) || StringUtils.equals("临时身份证", certType)) {
							String update = ViewEncryptUtil.idEncrypt(certNo);
							elements.get(second).text(update);
						} else {
							String update = ViewEncryptUtil.elseidEncrypt(certNo);
							elements.get(second).text(update);

						}
					}
				}
			}
		}
		log.info("doIdEncrypt end!");
	}

	/**
	 * @Description: 对查询失败或查无此人的征信报告进行证件号脱敏
	 * @param el
	 * @param first
	 * @param second
	 * @return void
	 */
	public static void failIdEncrypt(Elements el, int first, int second) {
		log.info("failIdEncrypt begin!");
		if (!el.isEmpty()) {
			Elements elements = el.select("td");
			if (!elements.isEmpty() && elements.size() > second) {
				String certType = elements.get(first).child(0).text().trim();
				String certNo = elements.get(second).child(0).text().trim();
				if (!StringUtils.isBlank(certType)) {
					if (isNotDefaultValue(certNo)) {
						if (StringUtils.equals("身份证", certType) || StringUtils.equals("临时身份证", certType)) {
							String update = ViewEncryptUtil.idEncrypt(certNo);
							elements.get(second).child(0).text(update);
						} else {
							String update = ViewEncryptUtil.elseidEncrypt(certNo);
							elements.get(second).child(0).text(update);

						}
					}
				}
			}
		}
		log.info("failIdEncrypt end!");
	}
	
	/**
	 * 	如果需要脱敏的值是默认值{空（""或null）或者等于--},不进行处理。
	 *	如果是默认值则为true
	 * @param text
	 * @return
	 */
	private static boolean isNotDefaultValue(String text) {
		log.info("isNotDefaultValue start!");
		boolean isDefault = true;
		if(StringUtils.isBlank(text) || Objects.equal(text, DEFAULT_VALUE)) {
			isDefault = false;
		}
		log.info("isNotDefaultValue end! isDefault = {}",isDefault);
		return isDefault;
	}
}
