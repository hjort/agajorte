package com.hsiconsultoria.dao;

import java.util.List;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.User;

/**
 * @author Rodrigo Hjort
 */
public interface IFollowshipDAO {

	/**
	 * Adds a friendship relationship from one user to another.
	 * 
	 * @param followship
	 */
	void save(Followship followship);

	/**
	 * Removes a friendship relationship from one user to another.
	 * 
	 * @param followship
	 */
	void remove(Followship followship);

	/**
	 * Given a user login, gets the logins of the people that the user is following.
	 * 
	 * @param follower
	 * @return
	 */
	List<String> findFollowingsLogins(User follower);

	/**
	 * Given a user login, gets the logins of the people following that user.
	 * 
	 * @param followed
	 * @return
	 */
	List<String> findFollowersLogins(User followed);

}
