/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.ent.authorizemanager.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.com.dhcc.creditquery.ent.authorizemanager.dao.CeqAuthorizeFileDao;
import cn.com.dhcc.creditquery.ent.authorizemanager.entity.CeqAuthorizeFile;
import cn.com.dhcc.creditquery.ent.authorizemanager.service.CeqAuthorizeFileService;
import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeFileBo;
import cn.com.dhcc.platform.filestorage.info.StorageRequest;
import cn.com.dhcc.platform.filestorage.info.StorageResponse;
import cn.com.dhcc.platform.filestorage.service.FileStorageService;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.CeqConstants;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 授权管理服务-授权档案文件管理服务实现
 * @author sjk
 * @date 2019年3月19日
 */
@Slf4j
@Service
@Transactional(value = "transactionManager")
public class CeqAuthorizeFileServiceImpl implements CeqAuthorizeFileService {

private final static String SUCESS_CODE="000000";
	
	@Autowired
	private CeqAuthorizeFileDao ceqAuthorizeFileDao;
	@Autowired
	private FileStorageService fileStorageService;
	
	@Override
	public void create(CeqAuthorizeFileBo ceqAuthorizeFileBo) {
		log.info("CeqAuthorizeFileServiceImpl method : create request begin,ceqAuthorizeFileBo={}",ceqAuthorizeFileBo);
		CeqAuthorizeFile ceqAuthorizeFile = null;
		try {
			if(StringUtils.isNotBlank(ceqAuthorizeFileBo.getPaperFileId())) {//存入纸质档案
				ceqAuthorizeFile = ClassCloneUtil.copyObject(ceqAuthorizeFileBo, CeqAuthorizeFile.class);
				ceqAuthorizeFileDao.save(ceqAuthorizeFile);
			}
			if(ceqAuthorizeFileBo.getCommonsMultipartFile() != null) {
				ceqAuthorizeFileBo = authorizeFileWeb(ceqAuthorizeFileBo);
			}
		} catch (Exception e) {
			log.error("create ceqAuthorizeFile Failure,error={}",e);
		}
		log.info("CeqAuthorizeFileServiceImpl method : create response end");
	}

	
	/**
	 * 请求来源于前置系统界面
	 * @param ceqAuthorizefileBo
	 * @author sjk
	 * @date 2019年3月5日
	 */
	private CeqAuthorizeFileBo authorizeFileWeb (CeqAuthorizeFileBo ceqAuthorizefileBo) throws IOException {
		CeqAuthorizeFile ceqAuthorizeFile = null;
		StorageRequest storageRequest = authorizFileRequest(ceqAuthorizefileBo);
		StorageResponse storageResponse = fileStorageService.saveFile(storageRequest);//档案文件存储
		if(!storageResponse.getErrorCode().equals(SUCESS_CODE)) {//如果上传失败
			ceqAuthorizefileBo.setStorageResponse(storageResponse);
			return ceqAuthorizefileBo;
		}
		ceqAuthorizefileBo.setStorageResponse(storageResponse);
		ceqAuthorizefileBo.setFilePath(storageResponse.getUri());
		ceqAuthorizefileBo.setPaperFileId(null);
		ceqAuthorizeFile = ClassCloneUtil.copyObject(ceqAuthorizefileBo, CeqAuthorizeFile.class);
		ceqAuthorizeFile = ceqAuthorizeFileDao.save(ceqAuthorizeFile);
		ceqAuthorizefileBo = ClassCloneUtil.copyObject(ceqAuthorizeFile, CeqAuthorizeFileBo.class);
		return ceqAuthorizefileBo;
	}
	
	/**
	 * 请求来源于api
	 */
	@Override
	public void authorizeFileInterface(List<CeqAuthorizeFileBo> ceqAuthorizefileBoList) {
		log.info("CeqAuthorizeFileServiceImpl method : createList request begin,ceqAuthorizefileBoList={}",ceqAuthorizefileBoList);
		try {
			for (CeqAuthorizeFileBo ceqAuthorizefileBo : ceqAuthorizefileBoList) {
				//纸质档案与影像档案，不需要进行文件类型转换，直接存储即可
				if(StringUtils.isNotBlank(ceqAuthorizefileBo.getPaperFileId()) || StringUtils.isNotBlank(ceqAuthorizefileBo.getImageSysUrl())) {//存入纸质档案
					CeqAuthorizeFile ceqAuthorizeFile = ClassCloneUtil.copyObject(ceqAuthorizefileBo, CeqAuthorizeFile.class);
					ceqAuthorizeFileDao.save(ceqAuthorizeFile);
				}else if(StringUtils.isNotBlank(ceqAuthorizefileBo.getFilePath())) {
					//filePath不为空，代表文件已保存在了外部路径上。直接存储即可
					ceqAuthorizefileBo = fileTypeConversion(ceqAuthorizefileBo);//文件类型转换
					CeqAuthorizeFile ceqAuthorzeFile = ClassCloneUtil.copyObject(ceqAuthorizefileBo, CeqAuthorizeFile.class);
					ceqAuthorizeFileDao.save(ceqAuthorzeFile);
				}else if(null !=ceqAuthorizefileBo.getStorageRequest()) {
					//电子档案不为空，需将传入的档案文件信息进行存储，并保存路径
					StorageRequest storageRequest = ceqAuthorizefileBo.getStorageRequest();
					storageRequest.setFileName(ceqAuthorizefileBo.getFileName());
					StorageResponse storageResponse = fileStorageService.saveFile(storageRequest);
					if(!storageResponse.getErrorCode().equals(SUCESS_CODE)) {//如果上传失败
						ceqAuthorizefileBo.setStorageResponse(storageResponse);
						log.error("CeqAuthorizeFileServiceImpl method : authorizeFileApi response end storageResponse={}",storageResponse);
					}
					//封装数据信息 并入库
					//文件存储的url
					String uri = storageResponse.getUri();
					ceqAuthorizefileBo.setFilePath(uri);
					String fileName = storageResponse.getFileName();
					if(StringUtils.isNotBlank(fileName)) {
						ceqAuthorizefileBo.setFileName(fileName);
					}
					ceqAuthorizefileBo.setPaperFileId(null);
					ceqAuthorizefileBo = fileTypeConversion(ceqAuthorizefileBo);//文件类型转换
					CeqAuthorizeFile ceqAuthorizeFile = ClassCloneUtil.copyObject(ceqAuthorizefileBo, CeqAuthorizeFile.class);
					ceqAuthorizeFileDao.save(ceqAuthorizeFile);
				}
				
			}
		} catch (Exception e) {
			log.error("createList CeqAuthorzeFile Failure,error={}",e);
		}
		log.info("CeqAuthorizeFileServiceImpl method : createList response end");
	}

	@Override
	public int update(CeqAuthorizeFileBo ceqAuthorizefileBo) {
		log.info("CeqAuthorizeFileServiceImpl method : update request begin,ceqAuthorizefileBo={}",ceqAuthorizefileBo);
		int successFlag = 0;
		try {
			CeqAuthorizeFile ceqAuthorizeFile = ClassCloneUtil.copyObject(ceqAuthorizefileBo, CeqAuthorizeFile.class);
			ceqAuthorizeFileDao.save(ceqAuthorizeFile);
		} catch (Exception e) {
			log.error("update CeqAuthorizeFile Failure,error={}",e);
			successFlag = -1;
		}
		log.info("CeqAuthorizeFileServiceImpl method : update response end");
		return successFlag;
	}

	@Override
	public int deleteById(String ceqAuthorizeFileId) {
		log.info("CeqAuthorizeFileServiceImpl method : deleteById request begin,ceqAuthorizeFileId={}",ceqAuthorizeFileId);
		int successFlag = 0;
		try {
			ceqAuthorizeFileDao.delete(ceqAuthorizeFileId);
		} catch (Exception e) {
			log.error("deleteById CeqAuthorizeFile Failure,error={}",e);
			successFlag = -1;
		}
		log.info("CeqAuthorizeFileServiceImpl method : deleteById response end");
		return successFlag;
	}

	@Override
	public int deleteByAuthorizeId(String ceqAuthorizeId) {
		log.info("CeqAuthorizeFileServiceImpl method : deleteByAuthorizeId request begin,ceqAuthorizeId={}",ceqAuthorizeId);
		int successFlag = 0;
		try {
			ceqAuthorizeFileDao.deleteByAuthorizeId(ceqAuthorizeId);
		} catch (Exception e) {
			log.error("deleteByAuthorizeId CeqAuthorizeFile Failure,error={}",e);
			successFlag = -1;
		}
		log.info("CeqAuthorizeFileServiceImpl method : deleteByAuthorizeId response end");
		return successFlag;
	}

	@Override
	public List<CeqAuthorizeFileBo> findCeqAuthorizeFileByAuthorizeId(String ceqAuthorizeId) {
		log.info("CeqAuthorizeFileServiceImpl method : findCeqAuthorizeFileByAuthorizeId request begin,ceqAuthorizeId={}",ceqAuthorizeId);
		List<CeqAuthorizeFileBo> ceqAuthorizefileBoList = null;
		try {
			List<CeqAuthorizeFile> ceqAuthorizeFileList = ceqAuthorizeFileDao.findByAuthorizeId(ceqAuthorizeId);
			ceqAuthorizefileBoList = ClassCloneUtil.copyIterableObject(ceqAuthorizeFileList, CeqAuthorizeFileBo.class);
		} catch (Exception e) {
			log.error("findCeqAuthorizeFileByAuthorizeId CeqAuthorzeFile Failure,error={}",e);
		}
		log.info("CeqAuthorizeFileServiceImpl method : findCeqAuthorizeFileByAuthorizeId response end");
		return ceqAuthorizefileBoList;
	}

	@Override
	public CeqAuthorizeFileBo findCeqAuthorizeFileByAuthorizeFileId(String ceqAuthorizeFileId) {
		log.info("CeqAuthorizeFileServiceImpl method : findCeqAuthorizeFileByAuthorizeFileId request begin,ceqAuthorizeFileId={}"
				,ceqAuthorizeFileId);
		CeqAuthorizeFileBo ceqAuthorizefileBo = null;
		try {
			CeqAuthorizeFile ceqAuthorizeFile = ceqAuthorizeFileDao.findOne(ceqAuthorizeFileId);
			ceqAuthorizefileBo = ClassCloneUtil.copyObject(ceqAuthorizeFile, CeqAuthorizeFileBo.class);
		} catch (Exception e) {
			log.error("findCeqAuthorizeFileByAuthorizeFileId CeqAuthorzeFile Failure,error={}",e);
		}
		log.info("CeqAuthorizeFileServiceImpl method : findCeqAuthorizeFileByAuthorizeFileId response end");
		return ceqAuthorizefileBo;
	}

	
	/**
	 * 封装文件存储请求参数
	 * @param ceqAuthorizeFileBo
	 * @return
	 * @author sjk
	 * @date 2019年2月26日
	 */
	private StorageRequest authorizFileRequest (CeqAuthorizeFileBo ceqAuthorizeFileBo) throws IOException {
		StorageRequest request = new StorageRequest();
		//将授权文件流转换为string
		String fileName = ceqAuthorizeFileBo.getCommonsMultipartFile().getOriginalFilename();//获取文件名称
		ceqAuthorizeFileBo.setFileName(fileName);
		String split[] = fileName.split("\\\\");
		fileName = split[split.length-1];
		String afterLast = StringUtils.substringAfterLast(fileName, ".");//获取文件后缀
		request.setSerialNumber(ceqAuthorizeFileBo.getSerialNumber());
		MultipartFile commonsMultipartFile = ceqAuthorizeFileBo.getCommonsMultipartFile();
		byte[] fileBytes = commonsMultipartFile.getBytes();
		request.setContentBytes(fileBytes);//档案内容
		request.setFileType(afterLast);//后缀
		String archiveStorePath = CeqConfigUtil.getSystemWorkPath();//取得系统参数获取根路径
		request.setRootUri(archiveStorePath);
		request.setBussModelEN(CeqConstants.BUSSMODELEN_AM);
		request.setSourceSystem(CeqConstants.SOURCESYSTEM_QE);
		request.setEncrypt(true);//不进行加密
		request.setCompression(true);//不进行加压
		String systemStorageType = CeqConfigUtil.getSystemStorageType();
		if(StringUtils.isNotBlank(systemStorageType)) {
			request.setStorageType(systemStorageType);
		}
		return request;
	}
	
	/**
	 *  文件类型转换
	 * @param ceqAuthorizefileBo
	 * @return
	 * @author sjk
	 * @date 2019年3月14日
	 */
	private CeqAuthorizeFileBo fileTypeConversion(CeqAuthorizeFileBo ceqAuthorizefileBo) {
		String fileType = ceqAuthorizefileBo.getFileType();
		if(StringUtils.isNotBlank(fileType)) {
			if(fileType.equals(CeqConstants.FILE_TYPE_1)) {
				ceqAuthorizefileBo.setFileType(CeqConstants.FILE_TYPE_101);
			}else if(fileType.equals(CeqConstants.FILE_TYPE_2)) {
				ceqAuthorizefileBo.setFileType(CeqConstants.FILE_TYPE_102);
			}else if(fileType.equals(CeqConstants.FILE_TYPE_3)) {
				ceqAuthorizefileBo.setFileType(CeqConstants.FILE_TYPE_103);
			}else if(fileType.equals(CeqConstants.FILE_TYPE_4)) {
				ceqAuthorizefileBo.setFileType(CeqConstants.FILE_TYPE_105);
			}else if(fileType.equals(CeqConstants.FILE_TYPE_9)) {
				ceqAuthorizefileBo.setFileType(CeqConstants.FILE_TYPE_200);
			}
		}
		return ceqAuthorizefileBo;
	}
	
}
