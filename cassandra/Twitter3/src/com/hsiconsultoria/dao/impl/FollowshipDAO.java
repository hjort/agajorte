package com.hsiconsultoria.dao.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.HelenaSimpleDAO;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.bean.User;
import com.hsiconsultoria.dao.IFollowshipDAO;

public class FollowshipDAO extends HelenaSimpleDAO<Followship> implements IFollowshipDAO {

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
