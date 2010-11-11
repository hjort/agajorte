package com.hsiconsultoria.dao;

import java.util.List;

import br.gov.frameworkdemoiselle.helena.HelenaDAO;

import com.hsiconsultoria.bean.TimeLine;

/**
 * @author Rodrigo Hjort
 */
public interface ITimeLineDAO extends HelenaDAO<TimeLine> {
	
	/**
	 * Given a user id, get their tweet timeline (tweets from people they follow).
	 * 
	 * @param user
	 * @return
	 */
	List<Long> findTimeLine(String user);

}
