package com.hsiconsultoria.dao;

import java.util.List;

import br.gov.frameworkdemoiselle.helena.HelenaDAO;

import com.hsiconsultoria.bean.UserLine;

/**
 * @author Rodrigo Hjort
 */
public interface IUserLineDAO extends HelenaDAO<UserLine> {

	/**
	 * Given a user id, get their userline (their tweets).
	 * 
	 * @param user
	 * @return
	 */
	List<Long> findUserLine(String user);

}
