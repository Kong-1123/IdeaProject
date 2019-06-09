package cn.com.dhcc.creditquery.ent.reportview.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.base.Objects;

import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description: 对征信报告进行脱敏的工具类
 * @author 文朝波
 * @date 2018年10月30日
 */

@Slf4j
public class ReportEncryptUtil {

	private static final String IDCARD = "身份证";
	private static final String IDCARD_TEMP = "临时身份证";
	private static final String SIGNCODE = "中征码";
	private static final String UNIFORMSOCIALCREDCODE = "统一社会信用代码";
	private static final String ORGINSTCODE = "组织机构代码";
	private static final String ORG_CREDIT_CODE = "机构信用代码";
	private static final String GS_REGI_CODE = "纳税人识别号(国税)";
	private static final String DS_REGI_CODE = "纳税人识别号(地税)";
	private static final String ENABLE_FLAG = "0";
	private static final String REGISTER_ADDR = "登记地址";
	private static final String BUSINESS_ADDR = "办公/经营地址";
	private static final String IDENTIFICATION = "身份标识";
	private static final String CAPITAL = "注册资本及主要出资人信息";
	private static final String UPORG = "上级机构";
	private static final String OWNER = "实际控制人";
	private static final String MEMBER = "主要组成人员信息";

	/**
	 * 对身份标识进行脱敏
	 * 
	 * @param htmlPage
	 * @return
	 */
	public static String identifyInfoEncrypt(String htmlPage) {
		log.info("identifyInfoEncrypt start!");
		String idEncryptFlag = CeqConfigUtil.getSensitiveCryto();
		if (Objects.equal(ENABLE_FLAG, idEncryptFlag)) {
			Document doc = Jsoup.parse(htmlPage);
			/**
			 * 身份标识
			 */
			Elements identityCodeEles = getElementsByDiv(doc, IDENTIFICATION);
			identityCodeEncrypt(identityCodeEles, 0, 1, 1);
			/**
			 * 注册资本信息
			 */
			Elements captilEle = getElementsByDiv(doc, CAPITAL);
			identityTagEncrypt(captilEle, 2, 3, 5);
			/**
			 * 上级机构信息
			 */
			Elements upOrgEle = getElementsByDiv(doc, UPORG);
			identityTagEncrypt(upOrgEle, 2, 3, 5);
			/**
			 * 主要组成人员信息
			 */
			Elements memberEle = getElementsByDiv(doc, MEMBER);
			identityTagEncrypt(memberEle, 2, 3, 4);
			/**
			 * 实际控制人信息
			 */
			Elements ownerEle = getElementsByDiv(doc, OWNER);
			identityTagEncrypt(ownerEle, 1, 2, 4);
			String content = doc.html();
			log.info("identifyInfoEncrypt end!");
			return content;
		}
		return htmlPage;
	}

	/**
	 * 对地址进行脱敏
	 * 
	 * @param htmlPage
	 * @return
	 */
	public static String addressInfoEncrypt(String htmlPage) {
		log.info("addressInfoEncrypt start!");
		String addrEncryptFlag = CeqConfigUtil.getAddressEncrypt();
		if (Objects.equal(ENABLE_FLAG, addrEncryptFlag)) {
			Document doc = Jsoup.parse(htmlPage);
			Elements registerEle = getElementsBySelector(doc, REGISTER_ADDR);
			addressEncrypt(registerEle, 0, 1, REGISTER_ADDR);
			addressEncrypt(registerEle, 0, 1, BUSINESS_ADDR);
			String content = doc.html();
			return content;
		}
		log.info("addressInfoEncrypt end!");
		return htmlPage;
	}

	/**
	 * 身份标识处理
	 * 
	 * @param identityCodeEles
	 * @param first
	 * @param second
	 * @param size
	 */
	private static void identityCodeEncrypt(Elements identityCodeEles, int first, int second, int size) {
		for (Element element : identityCodeEles) {
			Elements addrEles = element.select("th");
			Elements eles = element.select("td");
			if (eles.size() == size) {
				String thType = addrEles.get(first).text().trim();
				encryptInfoByType(second - 1, eles, thType);
			}
		}
	}
	
	/**
	 * 对具体信息处理
	 * @param second
	 * @param eles
	 * @param thType
	 */
	private static void encryptInfoByType(int second, Elements eles, String thType) {
		if (Objects.equal(SIGNCODE, thType)) {
			String context = eles.get(second).text().trim();
			context = ViewEncryptUtil.loancardCodeEncrypt(context);
			eles.get(second).text(context);
		}
		if (Objects.equal(UNIFORMSOCIALCREDCODE, thType)) {
			String context = eles.get(second).text().trim();
			context = ViewEncryptUtil.usCreditCodeEncrypt(context);
			eles.get(second).text(context);
		}
		if (Objects.equal(ORG_CREDIT_CODE, thType)) {
			String context = eles.get(second).text().trim();
			context = ViewEncryptUtil.creditCodeEncrypt(context);
			eles.get(second).text(context);
		}
		if (Objects.equal(ORGINSTCODE, thType)) {
			String context = eles.get(second).text().trim();
			context = ViewEncryptUtil.corpNoEncrypt(context);
			eles.get(second).text(context);
		}
		if (Objects.equal(GS_REGI_CODE, thType) || Objects.equal(DS_REGI_CODE, thType)) {
			String context = eles.get(second).text().trim();
			context = ViewEncryptUtil.taxEncrypt(context);
			eles.get(second).text(context);
		}
		if (Objects.equal(IDCARD, thType)||Objects.equal(IDCARD_TEMP, thType)) {
			String context = eles.get(second).text().trim();
			context = ViewEncryptUtil.idEncrypt(context);
			eles.get(second).text(context);
		}
	}
	
	/**
	 * 对除身份标识和地址信息的内容脱敏
	 * @param identityCodeEles
	 * @param first
	 * @param second
	 * @param size
	 */
	private static void identityTagEncrypt(Elements identityCodeEles, int first, int second, int size) {
		Elements trEles = identityCodeEles.select("tr");
		for (Element element : trEles) {
			Elements eles = element.select("td");
			if (eles.size() == size) {
				String thType = eles.get(first).text().trim();
				encryptInfoByType(second, eles, thType);
			}
		}
	}

	/**
	 * 对地址脱敏进行处理，elements地址元素段 first 地址类型列 second 地址信息列
	 * 
	 * @param elements
	 * @param first
	 * @param second
	 */
	private static void addressEncrypt(Elements elements, int first, int second, String type) {
		for (Element element : elements) {
			Elements addrEles = element.select("th");
			Elements eles = element.select("td");
			if (eles.size() == second) {
				String thType = addrEles.get(first).text().trim();
				if (Objects.equal(type, thType)) {
					String address = eles.get(second - 1).text().trim();
					address = ViewEncryptUtil.addressEncrypt(address);
					eles.get(second - 1).text(address);
				}
			}
		}

	}

	/**
	 * @Description:根据关键字选择要脱敏的信息段
	 * @param htmlPage
	 */
	private static Elements getElementsBySelector(Document doc, String content) {
		log.info("getElementsBySelector start,selector = {}", content);
		Elements elements = doc.select("table:has(tr>th:contains(" + content + "))").select("tr");
		log.info("getElementsBySelector end");
		return elements;
	}


	private static Elements getElementsByDiv(Document doc, String content) {
		log.info("getElementsByDiv start!");
		Elements elements = doc.select("div:contains(" + content + ")+table").select("tr");
		log.info("getElementsByDiv end!");
		return elements;
	}

	
}
