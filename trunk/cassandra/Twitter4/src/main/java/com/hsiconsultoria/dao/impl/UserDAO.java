package com.hsiconsultoria.dao.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraEntityDAO;

import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.IUserDAO;

public class UserDAO extends CassandraEntityDAO<User> implements IUserDAO {

	private static final long serialVersionUID = 1L;

	public User findByLogin(String login) {
		return get(login);
	}

	public List<User> findByLogins(Iterable<String> logins) {
		return get(logins);
	}

}
