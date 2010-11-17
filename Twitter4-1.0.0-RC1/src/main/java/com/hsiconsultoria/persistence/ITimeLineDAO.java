package com.hsiconsultoria.persistence;

import java.util.List;

import br.gov.frameworkdemoiselle.cassandra.CassandraDAO;

import com.hsiconsultoria.bean.TimeLine;

/**
 * @author Rodrigo Hjort
 */
public interface ITimeLineDAO extends CassandraDAO<TimeLine> {
	
	/**
	 * Given a user id, get their tweet timeline (tweets from people they follow).
	 * 
	 * @param user
	 * @return
	 */
	List<Long> findTimeLine(String user);

}
