package com.hsiconsultoria.dao;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.HelenaDAO;

import com.hsiconsultoria.bean.Tweet;
import com.hsiconsultoria.bean.User;

/**
 * @author Rodrigo Hjort
 */
public interface ITweetDAO extends HelenaDAO<Tweet> {

	/**
	 * Given a tweet id, this gets the tweet record.
	 * 
	 * @param id
	 * @return
	 */
	Tweet findById(Long id);

	/**
	 * Saves the tweet record.
	 * 
	 * @param tweet
	 */
	void save(Tweet tweet);

	/**
	 * Removes the tweet record.
	 * 
	 * @param tweet
	 */
	void remove(Tweet tweet);

	/**
	 * Given a user id, get their userline (their tweets).
	 * 
	 * @param user
	 * @return
	 */
	List<Long> findUserLine(User user);

	/**
	 * Given a user id, get their tweet timeline (tweets from people they follow).
	 * 
	 * @param user
	 * @return
	 */
	List<Long> findTimeLine(User user);

	/**
	 * Given a list of tweet ids, this gets the associated tweet object for each one.
	 * 
	 * @param ids
	 * @return
	 */
	List<Tweet> findByIds(Iterable<Long> ids);

}
