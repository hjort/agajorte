package com.hsiconsultoria.persistence.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraEntityDAO;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.persistence.ITweetDAO;

/**
 * @author Rodrigo Hjort
 */
@SuppressWarnings("serial")
public class TweetDAO extends CassandraEntityDAO<Tweet> implements ITweetDAO {

	public Tweet findById(Long id) {
		return get(id.toString());
	}

	public List<Tweet> findByIds(Iterable<Long> ids) {
		return get(Iterables.transform(ids, Functions.toStringFunction()));
	}

}
