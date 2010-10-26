package com.hsiconsultoria.dao.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.HelenaSimpleDAO;

import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.IUserDAO;

public class UserDAO extends HelenaSimpleDAO<User> implements IUserDAO {

	public User findByLogin(String login) {
		return super.get(login);
	}

	public void save(User user) {
		super.insert(user);
	}

	public void remove(User user) {
		super.delete(user);
	}

	public List<User> findByLogins(Iterable<String> logins) {
		return super.get(logins);
	}

}
