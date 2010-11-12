package br.gov.frameworkdemoiselle.cassandra;

import br.gov.framework.demoiselle.core.layer.IPersistenceController;

public interface CassandraDAO<T> extends IPersistenceController {

	/**
	 * Saves the given object into the data store.
	 * 
	 * @param object
	 */
	void save(T object);
	
	/**
	 * Removes the given object from the data store.
	 * 
	 * @param object
	 */
	void delete(T object);

}
