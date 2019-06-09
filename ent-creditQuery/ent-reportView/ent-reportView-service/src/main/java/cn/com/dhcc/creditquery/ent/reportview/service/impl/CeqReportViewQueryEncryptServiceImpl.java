/**
 *
 */
package cn.com.dhcc.creditquery.ent.reportview.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import cn.com.dhcc.creditquery.ent.query.bo.authorizemanager.CeqAuthorizeManagerBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryapprove.CeqApproveBo;
import cn.com.dhcc.creditquery.ent.query.bo.queryflowmanager.CeqQueryRecordBo;
import cn.com.dhcc.creditquery.ent.query.bo.reportview.CeqReportLogBo;
import cn.com.dhcc.creditquery.ent.reportview.entity.CeqCheckInfoView;
import cn.com.dhcc.creditquery.ent.reportview.entity.CeqReportLogView;
import cn.com.dhcc.creditquery.ent.reportview.entity.CeqResultinfoView;
import cn.com.dhcc.creditquery.ent.reportview.service.CeqReportViewQueryEncryptService;
import cn.com.dhcc.query.creditquerycommon.configutil.CeqConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.QueryEncryptImplUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 脱敏服务实现
 *  //TODO  若该脱敏服务需对外提供，该服务类命名需进行修改
 * @author rzd
 * @date 2019年2月26日
 */
@Slf4j
@Service
public class CeqReportViewQueryEncryptServiceImpl implements CeqReportViewQueryEncryptService {

    /**
     * @Description: 列表脱敏
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> void queryEncrypt(List<T> list) {
        log.info("脱敏服务-列表脱敏服务开始...");
        String flag = CeqConfigUtil.getSensitiveCryto();
        if (StringUtils.equals(flag, "0")) {
            T t = list.get(0);
            if (CeqAuthorizeManagerBo.class.isInstance(t)) {
                archiveQueryEncrypt((List<CeqAuthorizeManagerBo>) list);
            } else if (CeqResultinfoView.class.isInstance(t)) {
                resultinfoQueryEncrypt((List<CeqQueryRecordBo>) list);
            } else if (CeqReportLogView.class.isInstance(t)) {
                reportLogQueryEncrypt((List<CeqReportLogBo>) list);
            } else if (CeqCheckInfoView.class.isInstance(t)) {
                checkInfoQueryEncrypt((List<CeqApproveBo>) list);
            }
        }
        log.info("脱敏服务-列表脱敏服务完成");
    }

    /*
     * @Description: 明细脱敏
     *
     */
    @Override
    public <T> void queryEncrypt(T t) {
        log.info("脱敏服务-明细脱敏服务开始...");
        String flag = CeqConfigUtil.getSensitiveCryto();
        if (StringUtils.equals(flag, "0")) {
            if (CeqAuthorizeManagerBo.class.isInstance(t)) {
                oneEncrypt((CeqAuthorizeManagerBo) t);
            } else if (CeqResultinfoView.class.isInstance(t)) {
                oneEncrypt((CeqQueryRecordBo) t);
            } else if (CeqReportLogView.class.isInstance(t)) {
                oneEncrypt((CeqReportLogBo) t);
            } else if (CeqCheckInfoView.class.isInstance(t)) {
                oneEncrypt((CeqApproveBo) t);
            }
        }
        log.info("脱敏服务-明细脱敏服务完成");
    }

    /**
     * @Description: 电话号码脱敏
     *
     */
    @Override
    public String numberEncrypt(String number) {
        log.info("脱敏服务-电话号码脱敏服务开始...");
        String result;
        if (number.length() == 11) {
            result = QueryEncryptImplUtil.phoneEncrypt(number);
        } else {
            result = QueryEncryptImplUtil.telEncrypt(number);
        }
        log.info("脱敏服务-电话号码脱敏服务完成");
        return result;
    }

    public void archiveQueryEncrypt(List<CeqAuthorizeManagerBo> list) {
        log.info("脱敏服务-保存授权档案信息脱敏服务开始...");
        for (CeqAuthorizeManagerBo CeqArchiveBo : list) {
            if (whetherIdCard(CeqArchiveBo.getSignCode())) {
                CeqArchiveBo.setSignCode(QueryEncryptImplUtil.idEncrypt(CeqArchiveBo.getSignCode()));
            } else {
                CeqArchiveBo.setSignCode(QueryEncryptImplUtil.elseidEncrypt(CeqArchiveBo.getSignCode()));
            }
        }
        log.info("脱敏服务-保存授权档案信息脱敏服务完成");
    }

    public void resultinfoQueryEncrypt(List<CeqQueryRecordBo> list) {
        log.info("脱敏服务-查询结果信息脱敏服务开始...");
        for (CeqQueryRecordBo queryRecordBo : list) {
            if (whetherIdCard(queryRecordBo.getSignCode())) {
                queryRecordBo.setSignCode(QueryEncryptImplUtil.idEncrypt(queryRecordBo.getSignCode()));
            } else {
                queryRecordBo.setSignCode(QueryEncryptImplUtil.elseidEncrypt(queryRecordBo.getSignCode()));
            }
        }
        log.info("脱敏服务-查询结果信息脱敏服务完成");
    }

    public void reportLogQueryEncrypt(List<CeqReportLogBo> list) {
        log.info("脱敏服务-信用报告保存记录信息脱敏服务开始...");
        for (CeqReportLogBo reportLogBo : list) {
        	//	TODO 脱敏处理
        /*    if (whetherIdCard(reportLogBo.getCertType())) {
                reportLogBo.setCertNo(QueryEncryptImplUtil.idEncrypt(reportLogBo.getCertNo()));
            } else {
                reportLogBo.setCertNo(QueryEncryptImplUtil.elseidEncrypt(reportLogBo.getCertNo()));
            }*/
        }
        log.info("脱敏服务-信用报告保存记录信息脱敏服务完成");
    }

    public void checkInfoQueryEncrypt(List<CeqApproveBo> list) {
        log.info("脱敏服务-保存审批请求记录信息脱敏服务开始...");
        for (CeqApproveBo approveBo : list) {
            if (whetherIdCard(approveBo.getSignCode())) {
                approveBo.setSignCode(QueryEncryptImplUtil.idEncrypt(approveBo.getSignCode()));
            } else {
                approveBo.setSignCode(QueryEncryptImplUtil.elseidEncrypt(approveBo.getSignCode()));
            }
        }
        log.info("脱敏服务-保存审批请求记录信息脱敏服务完成");
    }

    /**
     * 内部逻辑一样，由于get,set方法不一致，所以不能做统一处理
     *
     * @param reportLogBo
     * @return void
     */
    public void oneEncrypt(CeqReportLogBo reportLogBo) {
        log.info("脱敏服务-信用报告保存记录信息脱敏服务开始...");
        //	TODO 脱敏处理
       /* if (whetherIdCard(reportLogBo.getCertType())) {
            reportLogBo.setCertNo(QueryEncryptImplUtil.idEncrypt(reportLogBo.getCertNo()));
        } else {
            reportLogBo.setCertNo(QueryEncryptImplUtil.elseidEncrypt(reportLogBo.getCertNo()));
        }*/
        log.info("脱敏服务-信用报告保存记录信息脱敏服务完成");
    }

    public void oneEncrypt(CeqApproveBo approveBo) {
        log.info("脱敏服务-保存复合信息脱敏服务开始...");
        if (whetherIdCard(approveBo.getSignCode())) {
            approveBo.setSignCode(QueryEncryptImplUtil.idEncrypt(approveBo.getSignCode()));
        } else {
            approveBo.setSignCode(QueryEncryptImplUtil.elseidEncrypt(approveBo.getSignCode()));
        }
        log.info("脱敏服务-保存复合信息脱敏服务完成");
    }

    public void oneEncrypt(CeqAuthorizeManagerBo archiveBo) {
        log.info("脱敏服务-保存授权档案信息脱敏服务开始...");
        if (whetherIdCard(archiveBo.getSignCode())) {
            archiveBo.setSignCode(QueryEncryptImplUtil.idEncrypt(archiveBo.getSignCode()));
        } else {
            archiveBo.setSignCode(QueryEncryptImplUtil.elseidEncrypt(archiveBo.getSignCode()));
        }
        log.info("脱敏服务-保存授权档案信息脱敏服务完成");
    }

    public void oneEncrypt(CeqQueryRecordBo queryRecordBo) {
        log.info("脱敏服务-查询结果信息脱敏服务开始...");
        if (whetherIdCard(queryRecordBo.getCreditId())) {
            queryRecordBo.setSignCode(QueryEncryptImplUtil.idEncrypt(queryRecordBo.getSignCode()));
        } else {
            queryRecordBo.setSignCode(QueryEncryptImplUtil.elseidEncrypt(queryRecordBo.getSignCode()));
        }
        log.info("脱敏服务-查询结果信息服务完成");
    }

    /**
     * 证件类型为0和7的是身份证和临时身份证，统一脱敏规则
     *
     * @param type
     * @return
     * @return boolean
     */
    private boolean whetherIdCard(String type) {
        return type.equals("10") ? true : false;
    }
}
