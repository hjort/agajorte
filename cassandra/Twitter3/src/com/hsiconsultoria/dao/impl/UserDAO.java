package com.hsiconsultoria.dao.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.helena.HelenaSimpleDAO;

import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.IUserDAO;

public class UserDAO extends HelenaSimpleDAO<User> implements IUserDAO {

	public User findByLogin(String login) {
		return get(login);
	}

	public List<User> findByLogins(Iterable<String> logins) {
		return get(logins);
	}

}
