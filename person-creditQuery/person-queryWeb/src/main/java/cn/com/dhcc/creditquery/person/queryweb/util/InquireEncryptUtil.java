package cn.com.dhcc.creditquery.person.queryweb.util;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.QueryEncryptImplUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 对征信报告进行脱敏的工具类
 * @author 文朝波
 * @date 2018年10月30日
 */
@Slf4j
public class InquireEncryptUtil {

	/**
	 * @Description: 对证件号码进行脱敏
	 * @param htmlPage
	 */
	public static String idEncrypt(String htmlPage) {
		log.info("idcard Encrypt begin");
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
		log.info("idcard Encrypt end");
		return htmlPage;
	}

	/**
	 * @Description: 对电话手机号码进行脱敏
	 * @param htmlPage
	 */
	public static String numberEncrypt(String htmlPage) {
		String flag = ConfigUtil.getNumberEncrypt();
		if (StringUtils.equals(flag, "0")) {

			Document doc = Jsoup.parse(htmlPage);
			Elements elements = doc.select("tr:contains(性别)+tr").select("div");
			if (!elements.isEmpty() && elements.size() > 4) {
				for (int i = 3; i < 6; i++) {
					String tel = elements.get(i).child(0).text().trim();

					if (null == tel || StringUtils.equals(tel, ""))
						break;

					String update = QueryEncryptUtil.numberEncrypt(tel);
					elements.get(i).child(0).text(update);
				}
			}
			// 配偶电话号码
			Elements el = doc.select("tr:contains(联系电话)+tr").select("div");
			if (!el.isEmpty() && el.size() > 3) {
				String tel2 = el.get(4).child(0).text().trim();
				if (!(null == tel2 || StringUtils.equals(tel2, ""))) {
					String update = QueryEncryptUtil.numberEncrypt(tel2);
					el.get(4).child(0).text(update);
				}
			}
			return doc.html();
		}
		return htmlPage;
	}

	/**
	 * @Description: 对地址进行脱敏
	 * @param htmlPage
	 */
	public static String addressEncrypt(String htmlPage) {
		String flag = ConfigUtil.getAddressEncrypt();
		if (StringUtils.equals(flag, "0")) {

			Document doc = Jsoup.parse(htmlPage);
			Elements elements = doc.select("tr:contains(通讯地址)+tr");

			// 通讯地址，户籍地址
			for (int i = 0; i < 2; i++) {
				doAddressEncrypt(elements, i);
			}

			Elements el = doc.select("tr:contains(居住地址)+tr");
			doAddressEncrypt(el, 1);
			Elements el2 = doc.select("tr:contains(单位地址)+tr");
			doAddressEncrypt(el2, 2);

			return doc.html();
		}
		return htmlPage;
	}

	/**
	 * @Description: 对地址进行脱敏的具体实现
	 * @param el
	 * @param i
	 * @return void
	 */
	public static void doAddressEncrypt(Elements el, int i) {
		if (!el.isEmpty()) {
			Elements elements = el.select("div");
			if (!elements.isEmpty() && elements.size() > i) {
				String address = elements.get(i).child(0).text().trim();
				if (!(null == address || StringUtils.equals(address, ""))) {
					String update = QueryEncryptImplUtil.addressEncrypt(address);
					elements.get(i).child(0).text(update);
				}
			}
		}
	}

	/**
	 * @Description: 对证件号脱敏的具体实现
	 * @param el
	 * @param first
	 * @param second
	 * @return void
	 */
	public static void doIdEncrypt(Elements el, int first, int second) {
		if (!el.isEmpty()) {
			Elements elements = el.select("div");
			if (!elements.isEmpty() && elements.size() > second) {
				String certType = elements.get(first).child(0).text().trim();
				String certNo = elements.get(second).child(0).text().trim();
				if (!(null == certType || StringUtils.equals(certType, ""))) {
					if (!(null == certNo || StringUtils.equals(certNo, ""))) {
						if (StringUtils.equals("身份证", certType) || StringUtils.equals("临时身份证", certType)) {
							String update = QueryEncryptImplUtil.idEncrypt(certNo);
							elements.get(second).child(0).text(update);
						} else {
							String update = QueryEncryptImplUtil.elseidEncrypt(certNo);
							elements.get(second).child(0).text(update);

						}
					}
				}
			}
		}
	}

	/**
	 * @Description: 对查询失败或查无此人的征信报告进行证件号脱敏
	 * @param el
	 * @param first
	 * @param second
	 * @return void
	 */
	public static void failIdEncrypt(Elements el, int first, int second) {
		if (!el.isEmpty()) {
			Elements elements = el.select("td");
			if (!elements.isEmpty() && elements.size() > second) {
				String certType = elements.get(first).child(0).text().trim();
				String certNo = elements.get(second).child(0).text().trim();
				if (!(null == certType || StringUtils.equals(certType, ""))) {
					if (!(null == certNo || StringUtils.equals(certNo, ""))) {
						if (StringUtils.equals("身份证", certType) || StringUtils.equals("临时身份证", certType)) {
							String update = QueryEncryptImplUtil.idEncrypt(certNo);
							elements.get(second).child(0).text(update);
						} else {
							String update = QueryEncryptImplUtil.elseidEncrypt(certNo);
							elements.get(second).child(0).text(update);

						}
					}
				}
			}
		}
	}
}
