package cn.com.dhcc.creditquery.person.queryweb.util;

import cn.com.dhcc.creditquery.person.querypboc.service.CpqDicCacheService;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvExport;
import cn.com.dhcc.query.creditquerycommon.util.csv.util.CsvExportGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author lekang.liu
 * @date 2018年4月20日
 *
 */
@Component
public class CsvExportGetterImpl implements CsvExportGetter {

	@Autowired
	private CpqDicCacheService service;

	@Override
	public Map<Object, Object> getDic(Object type) {
		Map<Object, Object> dic = service.getDic(type.toString());
		return dic;
	}

	@Override
	public String dateFormater(Date date, String format) {
		DateFormat df = new SimpleDateFormat(format);
		String dateString = df.format(date);
		return dateString;
	}

	@Override
	public String customProcessing(Field field, CsvExport fieldAnno) {
		return null;
	}
}
