//package cn.com.dhcc.creditquery.person.queryweb.init;
//
//import java.util.List;
//
//import org.redisson.api.RMap;
//import org.redisson.api.RedissonClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import cn.com.dhcc.credit.platform.util.RedissonUtil;
//import cn.com.dhcc.query.creditpersonquerydao.entity.modula.CpqModula;
//import cn.com.dhcc.query.creditpersonqueryservice.modula.service.CpqModulaService;
//import cn.com.dhcc.query.creditpersonqueryservice.policy.modula.CpqModulaUtil;
//
//@Component
//public class InitModula implements InitializingBean {
//
//	private static Logger log = LoggerFactory.getLogger(InitModula.class);
//
//	@Autowired
//	private CpqModulaService cpqModulaService;
//
//	private RedissonClient redis = RedissonUtil.getLocalRedisson();
//
//	@Override
//	public void afterPropertiesSet() throws Exception {
//		log.info("init CpqModula begin...");
//		RMap<String, CpqModula> modula = redis.getMap(CpqModulaUtil.MODULA_KEY);
//		log.debug("search CpqModula data by DB begin");
//		List<CpqModula> cpqModulaList = cpqModulaService.findAll();
//		log.debug("search CpqModula data by DB end, data sum = {}", cpqModulaList.size());
//		for (CpqModula cpqModula : cpqModulaList) {
//			modula.put(cpqModula.getModulaName(), cpqModula);
//		}
//		log.info("init CpqModula end...");
//	}
//
//}
