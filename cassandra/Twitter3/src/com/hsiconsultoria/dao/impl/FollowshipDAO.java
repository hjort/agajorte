package com.hsiconsultoria.dao.impl;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.HelenaColumnDAO;

import com.hsiconsultoria.bean.Followship;
import com.hsiconsultoria.dao.IFollowshipDAO;

public class FollowshipDAO extends HelenaColumnDAO<Followship> implements IFollowshipDAO {

	public List<String> findFollowingsLogins(String follower) {
		
		List<String> logins = getColumns(follower);
		
		if (logins == null || logins.isEmpty())
			return null;
		
		return logins;
	}

	public List<String> findFollowersLogins(String followed) {

		List<String> logins = getColumnsBySecondary(followed);
		
		if (logins == null || logins.isEmpty())
			return null;
		
		return logins;
	}

}
