/*
package cn.com.dhcc.creditquery.person.queryweb.util;

import java.util.List;

import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.dhcc.credit.platform.util.RedissonUtil;

public class AuthorityUtil {

    private static Logger log = LoggerFactory.getLogger(AuthorityUtil.class);

    private static RedissonClient redis = RedissonUtil.getLocalRedisson();
    */
/*
     *
     *  从REDIS中获取当前用户的所有权限
     * @return
     *//*


    private static List<String> getAllAuthority() {
        RList<String> allLinkList = redis.getList(Constants.KEY_ALL_LINK);
        return allLinkList;
    }


    */
/**
     * 判断是否具有某个权限
     *
     * @param url：权限页面访问url
     * @return
     *//*

    public static boolean haveAuthority(String url) {
        boolean flag = false;
        try {
            String substring = url.substring(url.lastIndexOf("/") + 1, url.length());
            if (substring.equals("list")) {
                url = url.substring(0, url.lastIndexOf("/"));
            }
            flag = getAllAuthority().contains(url);
        } catch (Exception e) {
            log.error("获取权限出现异常!" + e.getMessage());
        }
        return flag;
    }

    */
/*
     *
     * 判断是否具有档案管理权限
     * @return
     *//*

    public static boolean haveArchiveAuthority() {
        boolean flag = false;
        try {
            flag = haveAuthority(Constants.AUTHORITY_ARCHIVE);
        } catch (Exception e) {
            log.error("获取权限出现异常!" + e.getMessage());
        }
        return flag;
    }

    */
/*
     *
     * 判断是否具有审批权限
     * @return
     *//*

    public static boolean haveCheckInfoAuthority() {
        boolean flag = false;
        try {
            flag = haveAuthority(Constants.AUTHORITY_CHECKINFO);
        } catch (Exception e) {
            log.error("获取权限出现异常!" + e.getMessage());
        }
        return flag;
    }

    */
/**
     * 判断是否具有单笔查询权限
     *
     * @return
     *//*

    public static boolean haveSingleQueryAuthority() {
        boolean flag = false;
        try {
            flag = haveAuthority(Constants.AUTHORITY_SINGLEQUERY);
        } catch (Exception e) {
            log.error("获取权限出现异常!" + e.getMessage());
        }
        return flag;
    }

    */
/**
     * 判断是否具有批量查询权限（批量功能尚未开发，该方法暂未实现）
     *
     * @return
     *//*

    public static boolean haveBatchQueryAuthority() {
        // 批量功能尚未开发该方法暂未实现
        boolean flag = false;
        try {
            flag = haveAuthority(Constants.AUTHORITY_QUERY);
        } catch (Exception e) {
            log.error("获取权限出现异常!" + e.getMessage());
        }
        return flag;
    }

    */
/**
     * 判断是否同时具有单笔查询或批量查询权限
     *
     * @return
     *//*


    public static boolean haveQueryAuthority() {
        boolean singleFlag = false;
        boolean batchFlag = false;
        try {
            singleFlag = haveSingleQueryAuthority();
            batchFlag = haveBatchQueryAuthority();
        } catch (Exception e) {
            log.error("获取权限出现异常!" + e.getMessage());
        }
        return singleFlag && batchFlag;
    }

}
*/
