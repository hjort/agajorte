package br.gov.frameworkdemoiselle.cassandra;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.dao.Command;
import me.prettyprint.cassandra.service.CassandraClient;

import org.apache.cassandra.thrift.ConsistencyLevel;
import org.thiesen.helenaorm.SerializeUnknownClasses;
import org.thiesen.helenaorm.TypeConverter;
import org.thiesen.helenaorm.TypeMapping;
import org.thiesen.helenaorm.annotations.ColumnProperty;
import org.thiesen.helenaorm.annotations.KeyProperty;
import org.thiesen.helenaorm.annotations.SuperColumnProperty;
import org.thiesen.helenaorm.annotations.Transient;
import org.thiesen.helenaorm.annotations.ValueProperty;
import org.thiesen.helenaorm.mappings.IntegerTypeMapping;
import org.thiesen.helenaorm.mappings.LongTypeMapping;
import org.thiesen.helenaorm.mappings.StringTypeMapping;
import org.thiesen.helenaorm.mappings.URITypeMapping;
import org.thiesen.helenaorm.mappings.UUIDTypeMapping;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

public abstract class AbstractHelenaDAO<T> {

	protected Class<T> clz;
	
	protected SerializeUnknownClasses serializationPolicy;
	protected ImmutableMap<Class<?>, TypeMapping<?>> typeMappings;

    private String hostname;
    private int port;
	private String[] nodes;

    protected String keyspace;
    protected String columnFamily;

    protected ConsistencyLevel consistencyLevel = CassandraClient.DEFAULT_CONSISTENCY_LEVEL;

    protected PropertyDescriptor[] propertyDescriptors;
    protected ImmutableList<byte[]> columnNames;
    protected PropertyDescriptor keyPropertyDescriptor;
    protected PropertyDescriptor superColumnPropertyDescriptor;

    protected Map<String, Field> fields;
    protected Field keyField;

    protected TypeConverter typeConverter;
    
    private static final Map<Class<?>, TypeMapping<?>> DEFAULT_TYPES = ImmutableMap.<Class<?>, TypeMapping<?>>of(
            String.class, new StringTypeMapping(),
            UUID.class, new UUIDTypeMapping(),
            Long.class, new LongTypeMapping(),
            Integer.class, new IntegerTypeMapping(),
            URI.class, new URITypeMapping()
    );

	@SuppressWarnings("unchecked")
	public AbstractHelenaDAO() {
		
        clz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        
        // TODO: read type mappings from somewhere
        typeMappings = ImmutableMap.<Class<?>, TypeMapping<?>>builder().putAll(DEFAULT_TYPES)./*putAll(mappings).*/build();

        initialize();
	}

	private void initialize() {
		
		// TODO: read settings from somewhere (i.e., properties file)
		hostname = "localhost";
		port = 9160;
	}
	
	protected boolean isKeyProperty(final PropertyDescriptor d) {
		return safeIsAnnotationPresent(d, KeyProperty.class);
	}

    protected boolean isColumnProperty(final PropertyDescriptor d) {
		return safeIsAnnotationPresent(d, ColumnProperty.class);
	}

    protected boolean isValueProperty(final PropertyDescriptor d) {
		return safeIsAnnotationPresent(d, ValueProperty.class);
	}

	protected boolean isTransient(final PropertyDescriptor d) {
		return safeIsAnnotationPresent(d, Transient.class);
	}

	protected boolean isSuperColumnProperty(final PropertyDescriptor descriptor) {
		return safeIsAnnotationPresent(descriptor, SuperColumnProperty.class);
	}

	private boolean safeIsAnnotationPresent(final PropertyDescriptor d, final Class<? extends Annotation> annotation) {
		return nullSafeAnnotationPresent(annotation, fields.get(d.getName()))
				|| nullSafeAnnotationPresent(annotation, d.getReadMethod())
				|| nullSafeAnnotationPresent(annotation, d.getWriteMethod());
	}

	private boolean nullSafeAnnotationPresent(final Class<? extends Annotation> annotation, final Method method) {
		return (method != null && method.isAnnotationPresent(annotation));
	}

	private boolean nullSafeAnnotationPresent(final Class<? extends Annotation> annotation, final Field field) {
		return (field != null && field.isAnnotationPresent(annotation));
	}

	protected boolean isReadWrite(final PropertyDescriptor d) {
		return (d.getReadMethod() != null && d.getWriteMethod() != null);
	}

	protected <V> V execute(final Command<V> command) throws Exception {
    	if (hostname != null) {
    		return command.execute(hostname, port, keyspace);
    	} else if (nodes != null) {
    		return command.execute(nodes, keyspace, consistencyLevel);
    	} else {
    		throw new HelenaException("One of these must be set: hostname and port or an array of nodes.");
    	}
    }

}
