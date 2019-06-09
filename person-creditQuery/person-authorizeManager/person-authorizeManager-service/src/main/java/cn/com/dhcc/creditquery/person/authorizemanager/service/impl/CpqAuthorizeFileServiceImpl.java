/**
 *  Copyright (c)  2018-2028 DHCC, Inc.
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of DHCC, 
 *  Inc. ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the license agreement you entered into with DHCC.
 */
package cn.com.dhcc.creditquery.person.authorizemanager.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import cn.com.dhcc.creditquery.person.authorizemanager.dao.CpqAuthorizeFileDao;
import cn.com.dhcc.creditquery.person.authorizemanager.entity.CpqArchiveFile;
import cn.com.dhcc.creditquery.person.authorizemanager.service.CpqAuthorizeFileService;
import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchivefileBo;
import cn.com.dhcc.platform.filestorage.info.StorageRequest;
import cn.com.dhcc.platform.filestorage.info.StorageResponse;
import cn.com.dhcc.platform.filestorage.service.FileStorageService;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.ClassCloneUtil;
import cn.com.dhcc.query.creditquerycommon.util.Constants;
import lombok.extern.slf4j.Slf4j;
/**
 * 授权管理服务-授权档案文件管理服务实现类
 * @author sjk
 * @date 2019年2月22日
 */
@Slf4j
@Service
public class CpqAuthorizeFileServiceImpl implements CpqAuthorizeFileService{
	
	private final static String SUCESS_CODE="000000";
	
	@Autowired
	private CpqAuthorizeFileDao cpqAuthorizeFileDao;
	@Autowired
	private FileStorageService fileStorageService;
	
	@Override
	public void create(CpqArchivefileBo cpqArchivefileBo) {
		log.info("CpqAuthorizeFileServiceImpl method : create request begin,cpqArchivefileBo={}",cpqArchivefileBo);
		CpqArchiveFile cpqArchiveFile = null;
		try {
			if(StringUtils.isNotBlank(cpqArchivefileBo.getPaperfileid())) {//存入纸质档案
				cpqArchiveFile = ClassCloneUtil.copyObject(cpqArchivefileBo, CpqArchiveFile.class);
				cpqAuthorizeFileDao.save(cpqArchiveFile);
			}
			if(cpqArchivefileBo.getCommonsMultipartFile() != null) {
				cpqArchivefileBo = authorizeFileWeb(cpqArchivefileBo);
			}
		} catch (Exception e) {
			log.error("create CpqArchivefile Failure,error={}",e);
		}
		log.info("CpqAuthorizeFileServiceImpl method : create response end");
	}

	@Override
	public void createList(List<CpqArchivefileBo> cpqArchivefileBoList) {
		log.info("CpqAuthorizeFileServiceImpl method : createList request begin,cpqArchivefileBoList={}",cpqArchivefileBoList);
		try {
			for (CpqArchivefileBo cpqArchivefileBo : cpqArchivefileBoList) {
				//纸质档案与影像档案，不需要进行文件类型转换，直接存储即可
				if(StringUtils.isNotBlank(cpqArchivefileBo.getPaperfileid()) || StringUtils.isNotBlank(cpqArchivefileBo.getImageSysUrl())) {//存入纸质档案
					CpqArchiveFile cpqArchiveFile = ClassCloneUtil.copyObject(cpqArchivefileBo, CpqArchiveFile.class);
					cpqAuthorizeFileDao.save(cpqArchiveFile);
				}else if(StringUtils.isNotBlank(cpqArchivefileBo.getFilepath())) {
					//filePath不为空，代表文件已保存在了外部路径上。直接存储即可
					cpqArchivefileBo = fileTypeConversion(cpqArchivefileBo);//文件类型转换
					CpqArchiveFile cpqArchiveFile = ClassCloneUtil.copyObject(cpqArchivefileBo, CpqArchiveFile.class);
					cpqAuthorizeFileDao.save(cpqArchiveFile);
				}else if(null !=cpqArchivefileBo.getStorageRequest()) {
					//电子档案不为空，需将传入的档案文件信息进行存储，并保存路径
					StorageRequest storageRequest = cpqArchivefileBo.getStorageRequest();
					storageRequest.setFileName(cpqArchivefileBo.getFilename());
					StorageResponse storageResponse = fileStorageService.saveFile(storageRequest);
					if(!storageResponse.getErrorCode().equals(SUCESS_CODE)) {//如果上传失败
						cpqArchivefileBo.setStorageResponse(storageResponse);
						log.error("CpqAuthorizeFileServiceImpl method : authorizeFileApi response end storageResponse={}",storageResponse);
					}
					//封装数据信息 并入库
					//文件存储的url
					String uri = storageResponse.getUri();
					cpqArchivefileBo.setFilepath(uri);
					String fileName = storageResponse.getFileName();
					if(StringUtils.isNotBlank(fileName)) {
						cpqArchivefileBo.setFilename(fileName);
					}
					cpqArchivefileBo.setPaperfileid(null);
					cpqArchivefileBo = fileTypeConversion(cpqArchivefileBo);//文件类型转换
					CpqArchiveFile archiveFile = ClassCloneUtil.copyObject(cpqArchivefileBo, CpqArchiveFile.class);
					cpqAuthorizeFileDao.save(archiveFile);
				}
				
			}
		} catch (Exception e) {
			log.error("createList CpqArchivefile Failure,error={}",e);
		}
		log.info("CpqAuthorizeFileServiceImpl method : createList response end");
	}
	
	/**
	 * 请求来源于前置系统界面
	 * @param cpqArchivefileBo
	 * @return
	 * @author sjk
	 * @date 2019年3月5日
	 */
	private CpqArchivefileBo authorizeFileWeb (CpqArchivefileBo cpqArchivefileBo) throws IOException {
		CpqArchiveFile cpqArchiveFile = null;
		StorageRequest storageRequest = authorizFileRequest(cpqArchivefileBo);
		StorageResponse storageResponse = fileStorageService.saveFile(storageRequest);//档案文件存储
		if(!storageResponse.getErrorCode().equals(SUCESS_CODE)) {//如果上传失败
			cpqArchivefileBo.setStorageResponse(storageResponse);
			return cpqArchivefileBo;
		}
		cpqArchivefileBo.setStorageResponse(storageResponse);
		cpqArchivefileBo.setFilepath(storageResponse.getUri());
		cpqArchivefileBo.setPaperfileid(null);
		cpqArchiveFile = ClassCloneUtil.copyObject(cpqArchivefileBo, CpqArchiveFile.class);
		cpqArchiveFile = cpqAuthorizeFileDao.save(cpqArchiveFile);
		cpqArchivefileBo = ClassCloneUtil.copyObject(cpqArchiveFile, CpqArchivefileBo.class);
		return cpqArchivefileBo;
	}
	
	
	
	@Override
	public int update(CpqArchivefileBo cpqArchivefileBo) {
		log.info("CpqAuthorizeFileServiceImpl method : update request begin,cpqArchivefileBo={}",cpqArchivefileBo);
		int successFlag = 0;
		try {
			CpqArchiveFile cpqArchiveFile = ClassCloneUtil.copyObject(cpqArchivefileBo, CpqArchiveFile.class);
			cpqAuthorizeFileDao.save(cpqArchiveFile);
		} catch (Exception e) {
			log.error("update CpqArchivefile Failure,error={}",e);
			successFlag = -1;
		}
		log.info("CpqAuthorizeFileServiceImpl method : update response end");
		return successFlag;
	}

	@Override
	public int deleteById(String cpqArchiveFileId) {
		log.info("CpqAuthorizeFileServiceImpl method : deleteById request begin,cpqArchiveFileId={}",cpqArchiveFileId);
		int successFlag = 0;
		try {
			cpqAuthorizeFileDao.deleteById(cpqArchiveFileId);
		} catch (Exception e) {
			log.error("deleteById CpqArchivefile Failure,error={}",e);
			successFlag = -1;
		}
		log.info("CpqAuthorizeFileServiceImpl method : deleteById response end");
		return successFlag;
	}

	@Override
	public int deleteByArchiveId(String cpqArchiveId) {
		log.info("CpqAuthorizeFileServiceImpl method : deleteByArchiveId request begin,cpqArchiveId={}",cpqArchiveId);
		int successFlag = 0;
		try {
			cpqAuthorizeFileDao.deleteByArchiveId(cpqArchiveId);
		} catch (Exception e) {
			log.error("deleteByArchiveId CpqArchivefile Failure,error={}",e);
			successFlag = -1;
		}
		log.info("CpqAuthorizeFileServiceImpl method : deleteByArchiveId response end");
		return successFlag;
	}

	@Override
	public List<CpqArchivefileBo> findCpqArchivefilesByArchiveId(String cpqArchiveId) {
		log.info("CpqAuthorizeFileServiceImpl method : findCpqArchivefilesByArchiveId request begin,cpqArchiveId={}",cpqArchiveId);
		List<CpqArchivefileBo> cpqArchivefileBoList = null;
		try {
			List<CpqArchiveFile> cpqArchiveFileList = cpqAuthorizeFileDao.findByArchiveId(cpqArchiveId);
			cpqArchivefileBoList = ClassCloneUtil.copyIterableObject(cpqArchiveFileList, CpqArchivefileBo.class);
		} catch (Exception e) {
			log.error("findCpqArchivefilesByArchiveId CpqArchivefile Failure,error={}",e);
		}
		log.info("CpqAuthorizeFileServiceImpl method : findCpqArchivefilesByArchiveId response end");
		return cpqArchivefileBoList;
	}

	@Override
	public CpqArchivefileBo findCpqArchivefileByArchiveFileId(String cpqArchiveFileId) {
		log.info("CpqAuthorizeFileServiceImpl method : findCpqArchivefileByArchiveFileId request begin,cpqArchiveFileId={}"
				,cpqArchiveFileId);
		CpqArchivefileBo cpqArchivefileBo = null;
		try {
			Optional<CpqArchiveFile> cpqArchiveFile = cpqAuthorizeFileDao.findById(cpqArchiveFileId);
			cpqArchivefileBo = ClassCloneUtil.copyObject(cpqArchiveFile, CpqArchivefileBo.class);
		} catch (Exception e) {
			log.error("findCpqArchivefileByArchiveFileId CpqArchivefile Failure,error={}",e);
		}
		log.info("CpqAuthorizeFileServiceImpl method : findCpqArchivefileByArchiveFileId response end");
		return cpqArchivefileBo;
	}

	/**
	 * 封装文件存储请求参数
	 * @param cpqArchivefileBo
	 * @return
	 * @author sjk
	 * @date 2019年2月26日
	 */
	private StorageRequest authorizFileRequest (CpqArchivefileBo cpqArchivefileBo) throws IOException {
		StorageRequest request = new StorageRequest();
		//将授权文件流转换为string
		String fileName = cpqArchivefileBo.getCommonsMultipartFile().getOriginalFilename();//获取文件名称
		cpqArchivefileBo.setFilename(fileName);
		String split[] = fileName.split("\\\\");
		fileName = split[split.length-1];
		String afterLast = StringUtils.substringAfterLast(fileName, ".");//获取文件后缀
		request.setSerialNumber(cpqArchivefileBo.getSerialNumber());
		MultipartFile commonsMultipartFile = cpqArchivefileBo.getCommonsMultipartFile();
		byte[] fileBytes = commonsMultipartFile.getBytes();
		request.setContentBytes(fileBytes);//档案内容
		request.setFileType(afterLast);//后缀
		String archiveStorePath = ConfigUtil.getSystemWorkPath();//取得系统参数获取根路径
		request.setRootUri(archiveStorePath);
		request.setBussModelEN(Constants.BUSSMODELEN_AM);
		request.setSourceSystem(Constants.SOURCESYSTEM_QP);
		request.setEncrypt(true);//不进行加密
		request.setCompression(true);//不进行加压
		String systemStorageType = ConfigUtil.getSystemStorageType();
		if(StringUtils.isNotBlank(systemStorageType)) {
			request.setStorageType(systemStorageType);
		}
		return request;
	}

	/**
	 *   文件类型转换
	 * @param cpqArchivefileBo
	 * @return
	 * @author DHC-S
	 * @date 2019年3月14日
	 */
	private CpqArchivefileBo fileTypeConversion(CpqArchivefileBo cpqArchivefileBo) {
		String fileType = cpqArchivefileBo.getFileType();
		if(StringUtils.isNotBlank(fileType)) {
			if(fileType.equals(Constants.FILE_TYPE_1)) {
				cpqArchivefileBo.setFileType(Constants.FILE_TYPE_101);
			}else if(fileType.equals(Constants.FILE_TYPE_2)) {
				cpqArchivefileBo.setFileType(Constants.FILE_TYPE_102);
			}else if(fileType.equals(Constants.FILE_TYPE_3)) {
				cpqArchivefileBo.setFileType(Constants.FILE_TYPE_103);
			}else if(fileType.equals(Constants.FILE_TYPE_9)) {
				cpqArchivefileBo.setFileType(Constants.FILE_TYPE_200);
			}
		}
		return cpqArchivefileBo;
	}
	
}
