/*
 * The MIT License
 *
 * Copyright (c) 2010 Marcus Thiesen (marcus@thiesen.org)
 *
 * This file is part of HelenaORM.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.gov.frameworkdemoiselle.cassandra.internal;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import br.gov.frameworkdemoiselle.cassandra.CassandraException;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;

public class TypeConverter {

	private static final byte[] EMPTY_BYTES = new byte[0];

	private final ImmutableMap<Class<?>, TypeMapping<?>> mappings;
	private final boolean serializeUnknown;

	public TypeConverter(final ImmutableMap<Class<?>, TypeMapping<?>> typeMappings, final boolean serializeUnknown) {
		this.serializeUnknown = serializeUnknown;
		this.mappings = typeMappings;
	}

	public byte[] convertValueObjectToByteArray(final Object propertyValue) {
		if (propertyValue == null) {
			return EMPTY_BYTES;
		}
		if (mappings.containsKey(propertyValue.getClass())) {
			return mappings.get(propertyValue.getClass())
					.toBytes(propertyValue);
		}
		if (Enum.class.isAssignableFrom(propertyValue.getClass())) {
			return stringToBytes(((Enum<?>) propertyValue).name());
		}
		if (propertyValue instanceof Serializable && serializeUnknown) {
			return serialize(propertyValue);
		}
		throw new CassandraException("Cannot map " + propertyValue.getClass() + 
				" instance to byte array, either implement serializable or create a custom type mapping!");
	}

	private byte[] serialize(final Object propertyValue) {
		try {
			
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final ObjectOutputStream oout = new ObjectOutputStream(out);

			oout.writeObject(propertyValue);
			oout.close();

			return out.toByteArray();
		} catch (final IOException e) {
			throw new CassandraException("Unable to serialize object of type " + propertyValue.getClass(), e);
		}
	}

	public String bytesToString(final byte[] bytes) {
		return (String) mappings.get(String.class).fromBytes(bytes);
	}

	public byte[] stringToBytes(final String string) {
		return mappings.get(String.class).toBytes(string);
	}

	public Object convertByteArrayToValueObject(final Class<?> returnType, final byte[] value) {
		if (mappings.containsKey(returnType)) {
			return returnType.cast(mappings.get(returnType).fromBytes(value));
		}
		if (returnType.isEnum()) {
			return makeEnumInstance(returnType, value);
		}
		if (Serializable.class.isAssignableFrom(returnType)) {
			return returnType.cast(deserialize(value));
		}
		throw new CassandraException("Cannot handle type " + returnType.getClass() + 
				", maybe you have getters and setters with different types? Otherwise, add a type mapping.");
	}

	private Enum<?> makeEnumInstance(final Class<?> returnType, final byte[] value) {
		try {

			final Method method = returnType.getMethod("valueOf", String.class);
			return (Enum<?>) method.invoke(returnType, bytesToString(value));

		} catch (final SecurityException e) {
			throw new CassandraException(e);
		} catch (final NoSuchMethodException e) {
			throw new CassandraException(e);
		} catch (final IllegalArgumentException e) {
			throw new CassandraException(e);
		} catch (final IllegalAccessException e) {
			throw new CassandraException(e);
		} catch (final InvocationTargetException e) {
			throw new CassandraException(e);
		}
	}

	private Object deserialize(final byte[] value) {
		final ByteArrayInputStream in = new ByteArrayInputStream(value);
		try {

			final ObjectInputStream oin = new ObjectInputStream(in);
			final Object retval = oin.readObject();

			oin.close();

			return retval;
		} catch (final IOException e) {
			throw new CassandraException(e);
		} catch (final ClassNotFoundException e) {
			throw new CassandraException(e);
		}
	}

	public Function<String, byte[]> toByteArrayFunction() {
		return new Function<String, byte[]>() {
			public byte[] apply(final String arg0) {
				return stringToBytes(arg0);
			}
		};
	}

}