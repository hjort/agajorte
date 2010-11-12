package com.hsiconsultoria.persistence;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraDAO;

import com.hsiconsultoria.bean.Followship;

/**
 * @author Rodrigo Hjort
 */
public interface IFollowshipDAO extends CassandraDAO<Followship> {

	/**
	 * Adds a friendship relationship from one user to another.
	 * 
	 * @param followship
	 */
//	void save(Followship followship);

	/**
	 * Removes a friendship relationship from one user to another.
	 * 
	 * @param followship
	 */
//	void remove(Followship followship);

	/**
	 * Given a user login, gets the logins of the people that the user is following.
	 * 
	 * @param follower
	 * @return
	 */
	List<String> findFollowingsLogins(String follower);

	/**
	 * Given a user login, gets the logins of the people following that user.
	 * 
	 * @param followed
	 * @return
	 */
	List<String> findFollowersLogins(String followed);

}
