package br.gov.frameworkdemoiselle.helena;

public interface HelenaDAO<T> {

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
