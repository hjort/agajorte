package com.hsiconsultoria.dao.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.HelenaSimpleDAO;

import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.ITweetDAO;

public class TweetDAO extends HelenaSimpleDAO<Tweet> implements ITweetDAO {

	public Tweet findById(Long id) {
		return null;
	}

	public void save(Tweet tweet) {
	}

	public void remove(Tweet tweet) {
	}

	public List<Tweet> findByIds(Iterable<Long> ids) {
		return null;
	}

	public List<Long> findUserLine(User user) {
		return null;
	}

	public List<Long> findTimeLine(User user) {
		return null;
	}

}
