package com.hsiconsultoria.persistence.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraColumnDAO;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.hsiconsultoria.bean.TimeLine;
import com.hsiconsultoria.persistence.ITimeLineDAO;

@SuppressWarnings("serial")
public class TimeLineDAO extends CassandraColumnDAO<TimeLine> implements ITimeLineDAO {

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
