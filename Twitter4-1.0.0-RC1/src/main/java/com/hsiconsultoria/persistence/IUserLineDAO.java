package com.hsiconsultoria.persistence;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraDAO;

import com.hsiconsultoria.bean.UserLine;

/**
 * @author Rodrigo Hjort
 */
public interface IUserLineDAO extends CassandraDAO<UserLine> {

	/**
	 * Given a user id, get their userline (their tweets).
	 * 
	 * @param user
	 * @return
	 */
	List<Long> findUserLine(String user);

}
