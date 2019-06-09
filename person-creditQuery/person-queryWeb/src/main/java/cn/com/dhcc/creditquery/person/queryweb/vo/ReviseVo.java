package cn.com.dhcc.creditquery.person.queryweb.vo;

import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchivefileBo;

import java.io.Serializable;
import java.util.Date;

public class ReviseVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5300149698721714707L;
	private String archiveRevise;
	private String resultInfoId;
	private String id;
	private String archiveid;
	private Date createtime;
	private String filedescribe;
	private String filename;
	private String filepath;
	private String operator;
	private String operorg;
	private Date opertime;
	private String paperfileid;
	private String status;
	private String fileType;
	public ReviseVo() {
	}
	public String getArchiveRevise() {
		return archiveRevise;
	}
	public void setArchiveRevise(String archiveRevise) {
		this.archiveRevise = archiveRevise;
	}
	public String getResultInfoId() {
		return resultInfoId;
	}
	public void setResultInfoId(String resultInfoId) {
		this.resultInfoId = resultInfoId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getArchiveid() {
		return archiveid;
	}
	public void setArchiveid(String archiveid) {
		this.archiveid = archiveid;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getFiledescribe() {
		return filedescribe;
	}
	public void setFiledescribe(String filedescribe) {
		this.filedescribe = filedescribe;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperorg() {
		return operorg;
	}
	public void setOperorg(String operorg) {
		this.operorg = operorg;
	}
	public Date getOpertime() {
		return opertime;
	}
	public void setOpertime(Date opertime) {
		this.opertime = opertime;
	}
	public String getPaperfileid() {
		return paperfileid;
	}
	public void setPaperfileid(String paperfileid) {
		this.paperfileid = paperfileid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public CpqArchivefileBo getCpqArchivefileBo(){
		return new CpqArchivefileBo(id, archiveid, createtime, filedescribe, filename, filepath, operator, operorg, opertime, paperfileid, status,fileType);
	}
	
}
