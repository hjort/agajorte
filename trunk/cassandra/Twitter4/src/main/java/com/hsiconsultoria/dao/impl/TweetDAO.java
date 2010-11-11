package com.hsiconsultoria.dao.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraSimpleDAO;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.dao.ITweetDAO;

public class TweetDAO extends CassandraSimpleDAO<Tweet> implements ITweetDAO {

	public Tweet findById(Long id) {
		return get(id.toString());
	}

	public List<Tweet> findByIds(Iterable<Long> ids) {
		return get(Iterables.transform(ids, Functions.toStringFunction()));
	}

}
