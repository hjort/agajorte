package com.hsiconsultoria.dao.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraColumnDAO;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.hsiconsultoria.bean.UserLine;
import com.hsiconsultoria.dao.IUserLineDAO;

public class UserLineDAO extends CassandraColumnDAO<UserLine> implements IUserLineDAO {

	public List<Long> findUserLine(String user) {
		
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
