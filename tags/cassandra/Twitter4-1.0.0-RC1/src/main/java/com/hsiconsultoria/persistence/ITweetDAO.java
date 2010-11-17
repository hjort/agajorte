package com.hsiconsultoria.persistence;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraDAO;

import com.hsiconsultoria.bean.Tweet;

/**
 * @author Rodrigo Hjort
 */
public interface ITweetDAO extends CassandraDAO<Tweet> {

	/**
	 * Given a tweet id, this gets the tweet record.
	 * 
	 * @param id
	 * @return
	 */
	Tweet findById(Long id);

	/**
	 * Given a list of tweet ids, this gets the associated tweet object for each one.
	 * 
	 * @param ids
	 * @return
	 */
	List<Tweet> findByIds(Iterable<Long> ids);

}
