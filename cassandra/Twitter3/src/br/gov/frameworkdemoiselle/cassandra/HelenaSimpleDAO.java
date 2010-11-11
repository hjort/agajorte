package br.gov.frameworkdemoiselle.cassandra;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import me.prettyprint.cassandra.dao.Command;
import me.prettyprint.cassandra.model.HectorException;
import me.prettyprint.cassandra.service.Keyspace;

import org.apache.cassandra.thrift.Column;
import org.apache.cassandra.thrift.ColumnParent;
import org.apache.cassandra.thrift.ColumnPath;
import org.apache.cassandra.thrift.SlicePredicate;
import org.apache.cassandra.thrift.SuperColumn;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.thiesen.helenaorm.HelenaRuntimeException;
import org.thiesen.helenaorm.MarshalledObject;
import org.thiesen.helenaorm.TypeConverter;
import org.thiesen.helenaorm.annotations.HelenaBean;
import org.thiesen.helenaorm.annotations.KeyProperty;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableSet.Builder;

public abstract class HelenaSimpleDAO<T> extends AbstractHelenaDAO<T> {

	private static Logger log = Logger.getLogger(HelenaSimpleDAO.class);
	
	protected HelenaSimpleDAO() {
		
        log.debug("Instantiating HelenaSimpleDAO with " + clz.getSimpleName());
        
        if (!clz.isAnnotationPresent(HelenaBean.class)) {
            throw new IllegalArgumentException("Trying to get a HelenaDAO for a class that is not mapped with @HelenaBean");
        }
        
        final HelenaBean annotation = clz.getAnnotation(HelenaBean.class);

		typeConverter = new TypeConverter(typeMappings, serializationPolicy);
		propertyDescriptors = PropertyUtils.getPropertyDescriptors(clz);

        columnFamily = annotation.columnFamily();
        keyspace = annotation.keyspace();

        if (annotation.consistency() != null) {
        	consistencyLevel = annotation.consistency();
        }
        
        fields = new HashMap<String, Field>();
        for (Field field : clz.getDeclaredFields()) {
        	fields.put(field.getName(), field);
        	if (field.isAnnotationPresent(KeyProperty.class)) {
        		keyField = field;
        	}
		}
        
		final Builder<byte[]> setBuilder = ImmutableSet.<byte[]> builder();
		for (final PropertyDescriptor descriptor : propertyDescriptors) {
			setBuilder.add(typeConverter.stringToBytes(descriptor.getName()));
			if (isKeyProperty(descriptor)) {
				keyPropertyDescriptor = descriptor;
			}
			if (isSuperColumnProperty(descriptor)) {
				superColumnPropertyDescriptor = descriptor;
			}
		}
		columnNames = ImmutableList.copyOf(setBuilder.build());

		if (keyField == null && keyPropertyDescriptor == null) {
			throw new HelenaRuntimeException("Could not find key of class "
					+ clz.getName() + ", did you annotate with @KeyProperty");
		}
	}

	public void save(final T object) {

        final MarshalledObject marshalledObject = MarshalledObject.create();

		for (final PropertyDescriptor d : propertyDescriptors) {
			if (isReadWrite(d)) {
				try {
					
					final String name = d.getName();
					final byte[] value = typeConverter.convertValueObjectToByteArray(
							PropertyUtils.getProperty(object, name));

					if (isKeyProperty(d)) {
						marshalledObject.setKey(value);
					} else if (isSuperColumnProperty(d)) {
						marshalledObject.setSuperColumn(value);
					} else if (!isTransient(d)) {
						marshalledObject.addValue(name, value);
					}

				} catch (final NoSuchMethodException e) {
					throw new HelenaRuntimeException(e);
				} catch (final IllegalAccessException e) {
					throw new HelenaRuntimeException(e);
				} catch (final InvocationTargetException e) {
					throw new HelenaRuntimeException(e);
				}
			}
        }

		if (marshalledObject.getKey() == null || marshalledObject.getKey().length == 0) {
			throw new HelenaRuntimeException("Key is null, can't store object");
		}

		store(marshalledObject);
	}

	private void store(final MarshalledObject marshalledObject) {

		final byte[] idColumn = marshalledObject.getKey();
		final List<Column> columnList = Lists.newLinkedList();
		final long timestamp = System.currentTimeMillis();

		for (final Map.Entry<String, byte[]> property : marshalledObject.getEntries()) {
			columnList.add(toColumn(property, timestamp));
		}

		final Map<String, List<Column>> columnMap;
		final Map<String, List<SuperColumn>> superColumnMap;

		if (marshalledObject.isSuperColumnPresent()) {
			final SuperColumn superColumn = new SuperColumn(marshalledObject.getSuperColumn(), columnList);
			superColumnMap = ImmutableMap.<String, List<SuperColumn>> of(columnFamily, ImmutableList.of(superColumn));
			columnMap = null;
		} else {
			columnMap = ImmutableMap.<String, List<Column>> of(columnFamily, columnList);
			superColumnMap = null;
		}

		try {
			execute(new Command<Void>() {
				public Void execute(final Keyspace ks) throws HectorException {
					ks.batchInsert(typeConverter.bytesToString(idColumn),
							columnMap, superColumnMap);
					return null;
				}
			});
		} catch (final Exception e) {
			throw new HelenaRuntimeException(e);
		}
	}

	private Column toColumn(final Entry<String, byte[]> property, final long timestamp) {
		return new Column(typeConverter.stringToBytes(property.getKey()), property.getValue(), timestamp);
	}
    
	public void delete(final T object) {
		delete(getKeyFrom(object));
	}

	private String getKeyFrom(final T object) {
		try {
			return typeConverter.bytesToString(typeConverter.convertValueObjectToByteArray(
					PropertyUtils.getProperty(object, keyPropertyDescriptor.getName())));
		} catch (final IllegalAccessException e) {
			throw new HelenaRuntimeException(e);
		} catch (final InvocationTargetException e) {
			throw new HelenaRuntimeException(e);
		} catch (final NoSuchMethodException e) {
			throw new HelenaRuntimeException(e);
		}
	}

	public void delete(final String key) {
		try {
			execute(new Command<Void>() {
				public Void execute(final Keyspace ks) throws HectorException {
					ks.remove(key, new ColumnPath(columnFamily));
					return null;
				}
			});
		} catch (final Exception e) {
			throw new HelenaRuntimeException(e);
		}
	}

	public T get(final String key) {

		final ColumnParent parent = makeColumnParent();
		final SlicePredicate predicate = makeSlicePredicateWithAllPropertyColumns();

		try {
			return execute(new Command<T>() {
				public T execute(final Keyspace ks) throws HectorException {
					try {
						final List<Column> slice = ks.getSlice(key, parent,predicate);
						if (Iterables.isEmpty(slice)) {
							return null;
						}
						return applyColumns(key, slice);
					} catch (final HectorException e) {
						return null;
					}
				}
			});
		} catch (final Exception e) {
			throw new HelenaRuntimeException(e);
		}
	}

	private T applyColumns(final String key, final Iterable<Column> slice) {
		try {
			final T newInstance = clz.newInstance();

			PropertyUtils.setProperty(newInstance, keyPropertyDescriptor.getName(),
					typeConverter.convertByteArrayToValueObject(
							keyPropertyDescriptor.getReadMethod().getReturnType(),
							typeConverter.stringToBytes(key)));

			for (final Column c : slice) {
				final String name = typeConverter.bytesToString(c.name);
				if (PropertyUtils.isWriteable(newInstance, name)) {
					final PropertyDescriptor propertyDescriptor = PropertyUtils.getPropertyDescriptor(newInstance, name);
					final Class<?> returnType = propertyDescriptor.getReadMethod().getReturnType();
					PropertyUtils.setProperty(newInstance, name,
							typeConverter.convertByteArrayToValueObject(returnType, c.value));
				}
			}

			return newInstance;

		} catch (final InstantiationException e) {
			throw new HelenaRuntimeException("Could not instantiate " + clz.getName(), e);
		} catch (final IllegalAccessException e) {
			throw new HelenaRuntimeException("Could not instantiate " + clz.getName(), e);
		} catch (final InvocationTargetException e) {
			throw new HelenaRuntimeException(e);
		} catch (final NoSuchMethodException e) {
			throw new HelenaRuntimeException(e);
		}
	}

	private List<T> applyColumns(final String key, final List<SuperColumn> slice) {
		final ImmutableList.Builder<T> listBuilder = ImmutableList.builder();
		for (final SuperColumn superColumn : slice) {
			final T object = applyColumns(key, superColumn.getColumns());
			applySuperColumnName(object, superColumn.getName());
			listBuilder.add(object);
		}
		return listBuilder.build();
	}

	private void applySuperColumnName(final T object, final byte[] value) {
		final Class<?> returnType = superColumnPropertyDescriptor.getReadMethod().getReturnType();
		try {
			PropertyUtils.setProperty(object, superColumnPropertyDescriptor.getName(),
					typeConverter.convertByteArrayToValueObject(returnType, value));
		} catch (final IllegalAccessException e) {
			throw new HelenaRuntimeException(e);
		} catch (final InvocationTargetException e) {
			throw new HelenaRuntimeException(e);
		} catch (final NoSuchMethodException e) {
			throw new HelenaRuntimeException(e);
		}
	}

	public List<T> get(final Iterable<String> keys) {
		final ColumnParent parent = makeColumnParent();
		final SlicePredicate predicate = makeSlicePredicateWithAllPropertyColumns();
		try {
			return execute(new Command<List<T>>() {
				public List<T> execute(final Keyspace ks) throws HectorException {
					final Map<String, List<Column>> slice = ks.multigetSlice(
							ImmutableList.copyOf(keys), parent, predicate);
					return convertToList(slice);
				}
			});
		} catch (final Exception e) {
			throw new HelenaRuntimeException(e);
		}
	}

	public List<T> getRange(final String keyStart, final String keyEnd, final int amount) {
		final ColumnParent parent = makeColumnParent();
		final SlicePredicate predicate = makeSlicePredicateWithAllPropertyColumns();
		try {
			return execute(new Command<List<T>>() {
				@SuppressWarnings("deprecation")
				public List<T> execute(final Keyspace ks) throws HectorException {
					final Map<String, List<Column>> slice = ks.getRangeSlice(
							parent, predicate, keyStart, keyEnd, amount);
					return convertToList(slice);
				}
			});
		} catch (final Exception e) {
			throw new HelenaRuntimeException(e);
		}
	}

	private SlicePredicate makeSlicePredicateWithAllPropertyColumns() {
		final SlicePredicate predicate = new SlicePredicate();
		predicate.setColumn_names(columnNames);
		return predicate;
	}

	private ColumnParent makeColumnParent() {
		final ColumnParent parent = new ColumnParent();
		parent.setColumn_family(columnFamily);
		return parent;
	}

	private List<T> convertToList(final Map<String, List<Column>> slice) {
		final ImmutableList.Builder<T> listBuilder = ImmutableList.<T> builder();
		for (final Map.Entry<String, List<Column>> entry : slice.entrySet()) {
			listBuilder.add(applyColumns(entry.getKey(), entry.getValue()));
		}
		return listBuilder.build();
	}

	public List<T> get(final String key, final Iterable<String> columns) {

		final ColumnParent parent = makeColumnParent();
		final SlicePredicate predicate = makeSlicePredicateWithColumns(columns);

		try {
			return execute(new Command<List<T>>() {
				public List<T> execute(final Keyspace ks) throws HectorException {
					try {
						final List<SuperColumn> slice = ks.getSuperSlice(key, parent, predicate);
						return applyColumns(key, slice);
					} catch (final HectorException e) {
						return null;
					}
				}
			});
		} catch (final Exception e) {
			throw new HelenaRuntimeException(e);
		}
	}
	
	private SlicePredicate makeSlicePredicateWithColumns(final Iterable<String> columns) {
		final SlicePredicate predicate = new SlicePredicate();
		predicate.setColumn_names(ImmutableList.copyOf(Iterables.transform(
				columns, typeConverter.toByteArrayFunction())));
		return predicate;
	}

}
