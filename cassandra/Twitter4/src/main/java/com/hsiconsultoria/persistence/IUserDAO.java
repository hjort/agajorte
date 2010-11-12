package com.hsiconsultoria.persistence;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraDAO;

import com.hsiconsultoria.bean.User;

/**
 * @author Rodrigo Hjort
 */
public interface IUserDAO extends CassandraDAO<User> {

	/**
	 * Given a user login, this gets the user record.
	 * 
	 * @param login
	 * @return
	 */
	User findByLogin(String login);

	/**
	 * Given a list of user logins, this gets the associated user object for each one.
	 * 
	 * @param logins
	 * @return
	 */
	List<User> findByLogins(Iterable<String> logins);

}
