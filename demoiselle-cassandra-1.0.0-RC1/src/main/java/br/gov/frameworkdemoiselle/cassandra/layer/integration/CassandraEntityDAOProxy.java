package br.gov.frameworkdemoiselle.cassandra.layer.integration;

import me.prettyprint.cassandra.dao.Command;

import org.apache.log4j.Logger;

import br.gov.frameworkdemoiselle.cassandra.CassandraDAO;

public class CassandraEntityDAOProxy<T> implements CassandraDAO<T> {

	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(CassandraEntityDAOProxy.class);

	// TODO: ...
	public <V> V execute(Command<V> command) throws Exception {
		log.debug("CassandraEntityDAOProxy.execute()");
		return null;
	}

	public void delete(T object) {
	}

	public void save(T object) {
	}

}
