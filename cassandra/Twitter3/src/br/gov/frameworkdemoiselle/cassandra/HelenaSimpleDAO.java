package br.gov.frameworkdemoiselle.cassandra;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.log4j.Logger;

public abstract class HelenaSimpleDAO<T> extends AbstractHelenaDAO {

	private static Logger log = Logger.getLogger(HelenaSimpleDAO.class);
	
	private Class<T> clz;
	
//	private final SerializeUnknownClasses serializationPolicy;
//	private final ImmutableMap<Class<?>, TypeMapping<?>> typeMappings;
	
	@SuppressWarnings("unchecked")
	protected HelenaSimpleDAO() {
        clz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        log.debug("Instantiating HelenaSimpleDAO with " + clz.getSimpleName());
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
