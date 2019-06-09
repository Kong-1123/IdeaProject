/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.rules.validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.credit.platform.util.MD5;
import cn.com.dhcc.credit.platform.util.RedissonUtil;
import cn.com.dhcc.creditquery.person.querybo.queryapi.AuthorFileBo;
import cn.com.dhcc.creditquery.person.querybo.queryapi.AuthorizedBo;
import cn.com.dhcc.creditquery.person.querybo.queryapi.SingleQueryBo;
import cn.com.dhcc.creditquery.person.rules.IdCardUtil;
import cn.com.dhcc.creditquery.person.rules.SingleQueryInfoRules;
import cn.com.dhcc.platformmiddleware.vo.SystemUser;
import cn.com.dhcc.query.creditquerycommon.cache.constant.PersonCacheConstant;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.LoginValidateUtil;
import cn.com.dhcc.query.creditquerycommon.util.UserConfigUtils;
import cn.com.dhcc.query.queryapicommon.util.ZipUtils;

/**
 * 
 * @author guoshihu
 * @date 2019年1月14日
 */
public class SingleQueryInfoRulesValidator implements ConstraintValidator<SingleQueryInfoRules, SingleQueryBo> {
	private static Logger log = LoggerFactory.getLogger(SingleQueryInfoRulesValidator.class);


	private static final String AUTHOR_TYPE_NO_AUTHOR = "0";
	/**
	 * 授权编号（纸质文件编号）
	 */
	private static final String AUTHOR_TYPE_FILE_NUM = "1";
	/**
	 * 电子文件
	 */
	private static final String AUTHOR_TYPE_ELE_FILE = "2";
	/**
	 * 影像系统对应授权的url
	 */
	private static final String AUTHOR_TYPE_PHOTO_SYS_URL = "3";
	/**
	 * 纸质,电子
	 */
	private static final String AUTHOR_TYPE_ELE_NUM = "4";
	/**
	 * 同步异步返回标识 0:同步；1:异步
	 */
	private static final String ASYNC_FLAG_VALUE = "1";
	/**
	 * 文件类型 1 身份证正面;2 身份证反面;3授权书;9其他
	 */
	private static final String FILETYPE_IDCARDFRONT = "1";
	private static final String FILETYPE_IDCARDBACK = "2";
	private static final String FILETYPE_ARCHIVEFILE = "3";
	private static final String FILETYPE_OTHERFILE = "9";
	/**
	 * 证件类型-身份证
	 */
	private static final String IDTYPE_IDCARD = "10";
	/**
	 * 证件类型-港澳居民来往内地通行证
	 */
	private static final String IDTYPE_HKMACIDPASS = "5";
	/**
	 * 证件类型-台湾同胞来往内地通行证
	 */
	private static final String IDTYPE_TAIWANIDPASS = "6";
	/**
	 * 参数---授权文件个数以及最小数量
	 */
	private final static String ARCHIVETYPE = "archive.FileClass";
	/**
	 * 参数---授权文件最大个数
	 */
	private final static String ARCHIVENUM = "archiveNum";
	/**
	 * 页面文件类型 ------ 身份证正面
	 */
	private static String PAGE_FILETYPE_IDCARDFRONT = "101";
	/**
	 * 页面文件类型------- 身份证反面
	 */
	private static String PAGE_FILETYPE_IDCARDBACK = "102";
	/**
	 * 页面文件类型--------授权书
	 */
	private static String PAGE_FILETYPE_ARCHIVEFILE = "103";
	/**
	 * 页面文件类型--------其他
	 */
	private static String PAGE_FILETYPE_OTHERFILE = "200";

	private static final String SERIALVERSIONUID = "serialVersionUID";

	/**
	 * 正则（被查询人姓名）
	 */
	private static final String NAME_REGEX = "[\\u4E00-\\u9FA5·s_a-zA-Z.s][\\s\\u4E00-\\u9FA5·s_a-zA-Z.s]*[\\u4E00-\\u9FA5·s_a-zA-Z.s]*";

	/**
	 * 校验结果提示信息
	 */
	private static final String QUERIERNAME = "QuerierName只能填写中文汉字与英文字母;";
	
	
	private RedissonClient redis = RedissonUtil.getLocalRedisson();

	@Override
	public void initialize(SingleQueryInfoRules constraintAnnotation) {

	}

	@Override
	public boolean isValid(SingleQueryBo value, ConstraintValidatorContext context) {
		boolean flag = true;
		StringBuffer messageTemplate = new StringBuffer();
		if (null != value) {
			idCardValid(messageTemplate, value);

			boolean matches = Pattern.matches(NAME_REGEX, value.getQuerierName());//被查询人姓名校验
			if (!matches) {
				messageTemplate.append(QUERIERNAME);
			}
			AuthorizedBo authorizedBo = value.getAuthorizedBo();
			String syncFlag = value.getSyncFlag();
			if (!checkObjAllFieldsIsNull(authorizedBo)) {
				Date authBeginDate = value.getAuthorizedBo().getAuthorBeginDate();
				Date authEndDate = value.getAuthorizedBo().getAuthorEndDate();
				String authorType = value.getAuthorizedBo().getAuthorType();
				if (StringUtils.isBlank(authorType)) {
					messageTemplate.append("授权类型不能为空|");
				} else {
					if (!AUTHOR_TYPE_NO_AUTHOR.equals(authorType)) {
						if (null != authBeginDate && null != authEndDate) {
							if (authBeginDate.after(authEndDate)) {
								messageTemplate.append("授权起始日期必须小于等于授权结束日期|");
							}
						} else {
							messageTemplate.append("授权起始日期或授权结束日期不能为空|");
						}
					}
					if (AUTHOR_TYPE_FILE_NUM.equals(authorType)) {
						String authorNum = authorizedBo.getAuthorNum();
						if (StringUtils.isBlank(authorNum)) {
							messageTemplate.append("授权类型为授权编号时,授权编号不能为空|");
						}
					} else if (AUTHOR_TYPE_ELE_FILE.equals(authorType)) {
						eleHandle(value, messageTemplate, authorizedBo);
					} else if (AUTHOR_TYPE_PHOTO_SYS_URL.equals(authorType)) {
						String archiveUrl = authorizedBo.getAuthorizedURL();
						if (StringUtils.isBlank(archiveUrl)) {
							messageTemplate.append("授权类型为影像系统对应授权的url时,影像系统url不能为空|");
						}
					} else if(AUTHOR_TYPE_ELE_NUM.equals(authorType)) {
						String authorNum = authorizedBo.getAuthorNum();
						if (StringUtils.isBlank(authorNum)) {
							messageTemplate.append("授权类型为授权编号时,授权编号不能为空|");
						}
						eleHandle(value, messageTemplate, authorizedBo);
					}
				}
			}

			if (null != syncFlag && ASYNC_FLAG_VALUE.equals(syncFlag)) {
				String asyncQueryFlag = value.getAsyncQueryFlag();
				if (StringUtils.isBlank(asyncQueryFlag)) {
					messageTemplate.append("同步异步标识为异步时,异步调用查询流程配置不能为空|");
				}
			}
			if (StringUtils.isBlank(messageTemplate.toString())) {
				flag = true;
			} else {
				tip(context, messageTemplate.toString());
				flag = false;
			}
		}
		return flag;
	}

	/**
	 * 处理电子文件校验
	 * @param value
	 * @param messageTemplate
	 * @param authorizedBo
	 * @author yuzhao.xue
	 * @date 2019年3月14日
	 */
	private void eleHandle(SingleQueryBo value, StringBuffer messageTemplate, AuthorizedBo authorizedBo) {
		List<AuthorFileBo> fileList = authorizedBo.getAuthorizedFileBo().getAuthorfile();
		if (CollectionUtils.isNotEmpty(fileList)) {
			// 校验文件数量及类型是否符合配置要求
			validFileNum(fileList, value, messageTemplate);
			for (AuthorFileBo file : fileList) {
				String fileType = file.getFileType();
				String fileContent = file.getFileContent();
				String fileFormat = file.getFileFormat();
				String url = file.getUrl();
				String md5 = file.getMd5();
				String contentMd5 = MD5.getMd5String(fileContent);
				if (StringUtils.isBlank(url)) {
					if (!md5.equals(contentMd5)) {
						messageTemplate.append("MD5内容与文件内容不匹配|");
					}
					if (StringUtils.isNotBlank(fileType)) {
						if (StringUtils.isBlank(fileContent)) {
							messageTemplate.append("当含有档案文件的时候文件内容不能为空|");
							continue;
						}
						if (StringUtils.isBlank(ZipUtils.gunzip(fileContent))) {
							messageTemplate.append("当含有档案文件的时候文件内容必须经过加密转码|");
							continue;
						}
						if (FILETYPE_IDCARDFRONT.equals(fileType)) {
							valiImageSize(fileFormat, fileContent, messageTemplate,
									"身份证正面电子文件大小不能超过10M|");
						} else if (FILETYPE_IDCARDBACK.equals(fileType)) {
							valiImageSize(fileFormat, fileContent, messageTemplate,
									"身份证反面电子文件大小不能超过10M|");
						} else if (FILETYPE_ARCHIVEFILE.equals(fileType)) {
							valiImageSize(fileFormat, fileContent, messageTemplate,
									"授权书电子文件大小不能超过10M|");
						} else if (FILETYPE_OTHERFILE.equals(fileType)) {
							valiImageSize(fileFormat, fileContent, messageTemplate, "电子文件大小不能超过10M|");
						}
					}
				}
			}
		} else {
			messageTemplate.append("授权类型为电子文件时,授权文件信息不能为空|");
		}
	}

	private void tip(ConstraintValidatorContext context, String messageTemplate) {
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation();
	}

	private void valiImageSize(String fileFormat, String fileContent, StringBuffer messageTemplate,
			String errorMessage) {
		String extension = "jpg,png,gif,bmp,pdf";
		String spiltStr = "=";
		int maxSize = 10;
		fileContent = ZipUtils.gunzip(fileContent);
		String base64 = new sun.misc.BASE64Encoder().encodeBuffer(fileContent.getBytes());
		String format = base64.substring(11, 13);
		String str = base64.substring(22);
		if (str.indexOf(spiltStr) > 0) {
			int index = str.indexOf(spiltStr);
			str = str.substring(0, index);
		}
		int length = str.length();
		int size = (length - (length / 8) * 2) / 1024 / 1024;
		if (size > maxSize) {
			messageTemplate.append(errorMessage);
		}
		if (!extension.contains(fileFormat)) {
			messageTemplate.append("文件格式只能为JPG、PNG、GIF、BMP、PDF|");
		}
		/*
		 * if(!fileFormat.equals(format)){ if (null == messageTemplate) {
		 * messageTemplate = new StringBuffer(); } messageTemplate.append("字符串与文件格式不符");
		 * }
		 */

	}

	/**
	 * 根据证件类型判断证件号码是否符合规则
	 * 
	 * @param messageTemplate
	 * @param value
	 * @author guoshihu
	 * @date 2019年2月14日
	 */
	public static void idCardValid(StringBuffer messageTemplate, SingleQueryBo value) {
		log.info("idCardValid ---value", value);

		String idType = value.getQuerierCertype();
		String idNum = value.getQuerierCertno();
		if (StringUtils.isBlank(idNum) || StringUtils.isBlank(idType)) {
			return;
		}
		if(idNum.length()>18) {
			messageTemplate.append("被查询人证件号码长度不能超过18位|");
			return;
		}
		// 身份证号码校验
		if (idType.equals(IDTYPE_IDCARD)) {
			if (!IdCardUtil.checkCardStrict(idNum)) {
				messageTemplate.append("公民身份证号码无效|");
			}
		} else if (idType.equals(IDTYPE_HKMACIDPASS)) {
			// 港澳居民往来大陆通行证”的证件号码应符合如下规则：
			// 长度必须为 9 位或 11 位；
			// 第一位为字母“H”或“M”，后 8 位或 10 位必须为数字。
			if (!IdCardUtil.hkmacidpassValidator(idNum)) {
				messageTemplate.append("港澳居民往来大陆通行证无效|");
			}
		} else if (idType.equals(IDTYPE_TAIWANIDPASS)) {
			// 台湾居民往来大陆通行证”的证件号码应符合如下规则：
			if (!IdCardUtil.taiwanIdpassValidator(idNum)) {
				messageTemplate.append("台湾居民往来大陆通行证无效|");
			}
		}
	}

	/**
	 * 判断上传电子文件数量是否符合参数要求的最大,最小数量 并判断上传文件类型是否符合要求
	 * 
	 * @param fileList
	 * @param value
	 * @param messageTemplate
	 * @author guoshihu
	 * @date 2019年2月17日
	 */
	private void validFileNum(List<AuthorFileBo> fileList, SingleQueryBo value, StringBuffer messageTemplate) {
		log.info("validFileNum --------start");
		List<String> fileTypeList = new ArrayList<>();
		try {
			String queryUserOrg = getQueryUserOrg(value.getQueryUser());
			String rootDeptCode = UserConfigUtils.getRootDeptCode(queryUserOrg);
			String archives = ConfigUtil.getArchiveFileClass(rootDeptCode);
			String[] configList = archives.split(",");
			int fileNum = fileList.size();
			int configMinNum = configList.length;
			String num = ConfigUtil.getMaxArchiveFileNum(rootDeptCode);
			int maxFileNum = configMinNum + Integer.valueOf(num);
			log.info("validFileNum filenum={},configMinNum={},maxFileNum={}",fileNum,configMinNum,maxFileNum);
			if (fileNum < configMinNum) {
				messageTemplate.append("电子文件数量不能低于").append(configMinNum).append("|");
			} else if (fileNum > maxFileNum) {
				messageTemplate.append("电子文件数量不能高于").append(maxFileNum).append("|");
			}
			for (AuthorFileBo file : fileList) {
				String fileType = file.getFileType();
				fileTypeList.add(fileType);
			}
			for (String config : configList) {
				if (PAGE_FILETYPE_IDCARDFRONT.equals(config)) {
					if (!fileTypeList.contains(FILETYPE_IDCARDFRONT)) {
						messageTemplate.append("上传的电子文件中必须包含身份证正面|");
					}
				} else if (PAGE_FILETYPE_IDCARDBACK.equals(config)) {
					if (!fileTypeList.contains(FILETYPE_IDCARDBACK)) {
						messageTemplate.append("上传的电子文件中必须包含身份证反面|");
					}
				} else if (PAGE_FILETYPE_ARCHIVEFILE.equals(config)) {
					if (!fileTypeList.contains(FILETYPE_ARCHIVEFILE)) {
						messageTemplate.append("上传的电子文件中必须包含授权书|");
					}
				} else if (PAGE_FILETYPE_OTHERFILE.equals(config)) {
					if (!fileTypeList.contains(FILETYPE_OTHERFILE)) {
						messageTemplate.append("上传的电子文件中必须包含其他类型文件|");
					}
				}
			}
			fileTypeList = null;
			configList = null;
		} catch (Exception e) {
			log.error("查询用户信息失败", e);
		}
	}

	/**
	 * 返回用户所属机构
	 *
	 * @param userName
	 * @return
	 * @author guoshihu
	 * @date 2019年1月16日
	 */
	private String getQueryUserOrg(String userName) {
		log.info("getQueryUserOrg userName={}", userName);
		String userDeptcode = "";
		try {
			SystemUser user = LoginValidateUtil.findUserByUserName(userName);
			userDeptcode = user.getOrgId();
		} catch (Exception e) {
			log.error("getQueryUserOrg error:", e);
		}
		log.info("getQueryUserOrg result={}", userDeptcode);
		return userDeptcode;
	}

	public static boolean checkObjAllFieldsIsNull(AuthorizedBo authorized) {
		if (null == authorized) {
			return true;
		}
		try {
			for (Field f : authorized.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (f.getName().equals(SERIALVERSIONUID)) {
					continue;
				}
				if (f.getName().equals("authorizedFile")) {
					List<AuthorFileBo> list = authorized.getAuthorizedFileBo().getAuthorfile();
					if (CollectionUtils.isNotEmpty(list)) {
						for (AuthorFileBo authorFile : list) {
							if (!checkObjAllFieldsIsNull(authorFile)) {
								return false;
							}
						}
						continue;
					}
				}
				if (f.get(authorized) != null && StringUtils.isNotBlank(f.get(authorized).toString())) {
					return false;
				}

			}
		} catch (Exception e) {
			log.error("校验授权参数是否为空出现异常", e);
		}

		return true;
	}

	public static boolean checkObjAllFieldsIsNull(AuthorFileBo authorFile) throws Exception {
		if (null == authorFile) {
			return true;
		}
		for (Field f : authorFile.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (f.getName().equals(SERIALVERSIONUID)) {
				continue;
			}
			if (f.get(authorFile) != null && StringUtils.isNotBlank(f.get(authorFile).toString())) {
				return false;
			}

		}
		return true;
	}

}
