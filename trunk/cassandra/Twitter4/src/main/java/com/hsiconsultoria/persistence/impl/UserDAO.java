package com.hsiconsultoria.persistence.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraEntityDAO;

import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.persistence.IUserDAO;

/**
 * @author Rodrigo Hjort
 */
@SuppressWarnings("serial")
public class UserDAO extends CassandraEntityDAO<User> implements IUserDAO {

	public User findByLogin(String login) {
		return get(login);
	}

	public List<User> findByLogins(Iterable<String> logins) {
		return get(logins);
	}

}
