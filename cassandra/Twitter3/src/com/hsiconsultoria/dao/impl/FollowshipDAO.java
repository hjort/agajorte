package com.hsiconsultoria.dao.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.HelenaColumnDAO;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.IFollowshipDAO;

public class FollowshipDAO extends HelenaColumnDAO<Followship> implements IFollowshipDAO {

	public void save(Followship followship) {
	}

	public void remove(Followship followship) {
	}

	public List<String> findFollowingsLogins(User followingUser) {
		return null;
	}

	public List<String> findFollowersLogins(User followed) {
		return null;
	}

}
