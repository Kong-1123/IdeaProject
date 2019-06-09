/**
 *
 */
package cn.com.dhcc.creditquery.person.reportview.service.impl;

import cn.com.dhcc.creditquery.person.query.bo.authorizemanager.CpqArchiveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryapprove.CpqApproveBo;
import cn.com.dhcc.creditquery.person.query.bo.queryflowmanager.CpqQueryRecordBo;
import cn.com.dhcc.creditquery.person.query.bo.reportview.CpqReportLogBo;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqArchiveView;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqCheckInfoView;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqReportLogView;
import cn.com.dhcc.creditquery.person.reportview.entity.CpqResultinfoView;
import cn.com.dhcc.creditquery.person.reportview.service.CpqReportViewQueryEncryptService;
import cn.com.dhcc.query.creditquerycommon.configutil.ConfigUtil;
import cn.com.dhcc.query.creditquerycommon.util.QueryEncryptImplUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 脱敏服务实现
 * @author rzd
 * @date 2019年2月26日
 */
@Slf4j
@Service
public class CpqReportViewQueryEncryptServiceImpl implements CpqReportViewQueryEncryptService {

    /**
     * @Description: 列表脱敏
     *
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> void queryEncrypt(List<T> list) {
        log.info("脱敏服务-列表脱敏服务开始...");
        String flag = ConfigUtil.getSensitiveCryto();
        if (StringUtils.equals(flag, "0")) {
            T t = list.get(0);
            if (CpqArchiveBo.class.isInstance(t)) {
                archiveQueryEncrypt((List<CpqArchiveBo>) list);
            } else if (CpqResultinfoView.class.isInstance(t)) {
                resultinfoQueryEncrypt((List<CpqQueryRecordBo>) list);
            } else if (CpqReportLogView.class.isInstance(t)) {
                reportLogQueryEncrypt((List<CpqReportLogBo>) list);
            } else if (CpqCheckInfoView.class.isInstance(t)) {
                checkInfoQueryEncrypt((List<CpqApproveBo>) list);
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
        String flag = ConfigUtil.getSensitiveCryto();
        if (StringUtils.equals(flag, "0")) {
            if (CpqArchiveView.class.isInstance(t)) {
                oneEncrypt((CpqArchiveBo) t);
            } else if (CpqResultinfoView.class.isInstance(t)) {
                oneEncrypt((CpqQueryRecordBo) t);
            } else if (CpqReportLogView.class.isInstance(t)) {
                oneEncrypt((CpqReportLogBo) t);
            } else if (CpqCheckInfoView.class.isInstance(t)) {
                oneEncrypt((CpqApproveBo) t);
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

    public void archiveQueryEncrypt(List<CpqArchiveBo> list) {
        log.info("脱敏服务-保存授权档案信息脱敏服务开始...");
        for (CpqArchiveBo cpqArchiveBo : list) {
            if (whetherIdCard(cpqArchiveBo.getCretType())) {
                cpqArchiveBo.setCretNo(QueryEncryptImplUtil.idEncrypt(cpqArchiveBo.getCretNo()));
            } else {
                cpqArchiveBo.setCretNo(QueryEncryptImplUtil.elseidEncrypt(cpqArchiveBo.getCretNo()));
            }
        }
        log.info("脱敏服务-保存授权档案信息脱敏服务完成");
    }

    public void resultinfoQueryEncrypt(List<CpqQueryRecordBo> list) {
        log.info("脱敏服务-查询结果信息脱敏服务开始...");
        for (CpqQueryRecordBo queryRecordBo : list) {
            if (whetherIdCard(queryRecordBo.getCertType())) {
                queryRecordBo.setCertNo(QueryEncryptImplUtil.idEncrypt(queryRecordBo.getCertNo()));
            } else {
                queryRecordBo.setCertNo(QueryEncryptImplUtil.elseidEncrypt(queryRecordBo.getCertNo()));
            }
        }
        log.info("脱敏服务-查询结果信息脱敏服务完成");
    }

    public void reportLogQueryEncrypt(List<CpqReportLogBo> list) {
        log.info("脱敏服务-信用报告保存记录信息脱敏服务开始...");
        for (CpqReportLogBo reportLogBo : list) {
            if (whetherIdCard(reportLogBo.getCertType())) {
                reportLogBo.setCertNo(QueryEncryptImplUtil.idEncrypt(reportLogBo.getCertNo()));
            } else {
                reportLogBo.setCertNo(QueryEncryptImplUtil.elseidEncrypt(reportLogBo.getCertNo()));
            }
        }
        log.info("脱敏服务-信用报告保存记录信息脱敏服务完成");
    }

    public void checkInfoQueryEncrypt(List<CpqApproveBo> list) {
        log.info("脱敏服务-保存审批请求记录信息脱敏服务开始...");
        for (CpqApproveBo approveBo : list) {
            if (whetherIdCard(approveBo.getCertType())) {
                approveBo.setCertNo(QueryEncryptImplUtil.idEncrypt(approveBo.getCertNo()));
            } else {
                approveBo.setCertNo(QueryEncryptImplUtil.elseidEncrypt(approveBo.getCertNo()));
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
    public void oneEncrypt(CpqReportLogBo reportLogBo) {
        log.info("脱敏服务-信用报告保存记录信息脱敏服务开始...");
        if (whetherIdCard(reportLogBo.getCertType())) {
            reportLogBo.setCertNo(QueryEncryptImplUtil.idEncrypt(reportLogBo.getCertNo()));
        } else {
            reportLogBo.setCertNo(QueryEncryptImplUtil.elseidEncrypt(reportLogBo.getCertNo()));
        }
        log.info("脱敏服务-信用报告保存记录信息脱敏服务完成");
    }

    public void oneEncrypt(CpqApproveBo approveBo) {
        log.info("脱敏服务-保存复合信息脱敏服务开始...");
        if (whetherIdCard(approveBo.getCertNo())) {
            approveBo.setCertNo(QueryEncryptImplUtil.idEncrypt(approveBo.getCertNo()));
        } else {
            approveBo.setCertNo(QueryEncryptImplUtil.elseidEncrypt(approveBo.getCertNo()));
        }
        log.info("脱敏服务-保存复合信息脱敏服务完成");
    }

    public void oneEncrypt(CpqArchiveBo archiveBo) {
        log.info("脱敏服务-保存授权档案信息脱敏服务开始...");
        if (whetherIdCard(archiveBo.getCretType())) {
            archiveBo.setCretNo(QueryEncryptImplUtil.idEncrypt(archiveBo.getCretNo()));
        } else {
            archiveBo.setCretNo(QueryEncryptImplUtil.elseidEncrypt(archiveBo.getCretNo()));
        }
        log.info("脱敏服务-保存授权档案信息脱敏服务完成");
    }

    public void oneEncrypt(CpqQueryRecordBo queryRecordBo) {
        log.info("脱敏服务-查询结果信息脱敏服务开始...");
        if (whetherIdCard(queryRecordBo.getCertType())) {
            queryRecordBo.setCertNo(QueryEncryptImplUtil.idEncrypt(queryRecordBo.getCertNo()));
        } else {
            queryRecordBo.setCertNo(QueryEncryptImplUtil.elseidEncrypt(queryRecordBo.getCertNo()));
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
