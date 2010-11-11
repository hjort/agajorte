package com.hsiconsultoria.dao.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.helena.HelenaColumnDAO;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import com.hsiconsultoria.bean.TimeLine;
import com.hsiconsultoria.dao.ITimeLineDAO;

public class TimeLineDAO extends HelenaColumnDAO<TimeLine> implements ITimeLineDAO {

	public List<Long> findTimeLine(String user) {
		
		List<String> values = getValues(user);
		
		if (values == null || values.isEmpty())
			return null;
		
		List<Long> tweets = Lists.transform(values, new Function<String, Long>() {
			public Long apply(String from) {
				return Long.parseLong(from);
			}
		});
		
		return tweets;
	}

}
