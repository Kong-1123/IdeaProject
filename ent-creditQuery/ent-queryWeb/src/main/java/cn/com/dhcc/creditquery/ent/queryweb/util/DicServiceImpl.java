package cn.com.dhcc.creditquery.ent.queryweb.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.com.dhcc.creditquery.ent.querypboc.service.CeqDicCacheService;
import cn.com.dhcc.query.creditquerycommon.util.excle.util.Dic;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DicServiceImpl implements Dic {

	@Autowired
	private CeqDicCacheService service;

	@Override
	public Map<Object, Object> getDic(Object type) {
		Map<Object, Object> dic = service.getDic(type.toString());
		return dic;
	}

	
	@Override
	public String getDicName(String dicType, String dicCode) {
		log.info("getDicName params dicType = {} , dicCode = {}",dicType,dicCode);
		Map<Object, Object> dicMap = service.getDic(dicType);
		String dicName = (String) dicMap.get(dicCode);
		log.info("getDicName result dicName = ",dicName);
		return dicName;
	}
}
