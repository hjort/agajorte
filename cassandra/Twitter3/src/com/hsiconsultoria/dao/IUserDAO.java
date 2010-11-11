package com.hsiconsultoria.dao;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.HelenaDAO;

import com.hsiconsultoria.bean.User;

/**
 * @author Rodrigo Hjort
 */
public interface IUserDAO extends HelenaDAO<User> {

	/**
	 * Given a user login, this gets the user record.
	 * 
	 * @param login
	 * @return
	 */
	User findByLogin(String login);

	/**
	 * Saves the user record.
	 * 
	 * @param user
	 */
//	void save(User user);

	/**
	 * Removes the user record.
	 * 
	 * @param user
	 */
//	void remove(User user);

	/**
	 * Given a list of user logins, this gets the associated user object for each one.
	 * 
	 * @param logins
	 * @return
	 */
	List<User> findByLogins(Iterable<String> logins);

}
