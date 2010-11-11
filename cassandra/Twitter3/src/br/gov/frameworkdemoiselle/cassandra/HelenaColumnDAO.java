package br.gov.frameworkdemoiselle.cassandra;

import java.util.List;

public abstract class HelenaColumnDAO<T> extends AbstractHelenaDAO {

	public HelenaColumnDAO() {
	}

	public void insert(final T object) {
	}

	public void delete(final T object) {
	}
	
	public List<String> getColumns(final String key) {
		return null;
	}
	
	public List<String> getColumnsBySecondary(final String key) {
		return null;
	}
	
	public List<String> getValues(final String key) {
		return null;
	}
	
}
