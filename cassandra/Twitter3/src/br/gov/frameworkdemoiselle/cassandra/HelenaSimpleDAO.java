package br.gov.frameworkdemoiselle.cassandra;

import java.util.List;

public abstract class HelenaSimpleDAO<T> {

	public HelenaSimpleDAO() {
	}

	public void insert(final T object) {
	}

	public void delete(final T object) {
	}

	public void delete(final String key) {
	}

	public T get(final String key) {
		return null;
	}

	public List<T> get(final Iterable<String> keys) {
		return null;
	}

	public List<T> getRange(final String keyStart, final String keyEnd,
			final int amount) {
		return null;
	}

	public List<T> get(final String key, final Iterable<String> columns) {
		return null;
	}
	
}
