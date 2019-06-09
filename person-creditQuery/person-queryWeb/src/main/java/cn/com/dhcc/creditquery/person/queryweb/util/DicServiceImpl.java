package cn.com.dhcc.creditquery.person.queryweb.util;

import cn.com.dhcc.creditquery.person.querypboc.service.CpqDicCacheService;
import cn.com.dhcc.query.creditquerycommon.util.excle.util.Dic;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class DicServiceImpl implements Dic {

	@Autowired
	private CpqDicCacheService service;

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
