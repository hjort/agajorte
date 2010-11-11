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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import br.gov.frameworkdemoiselle.cassandra.CassandraException;

import com.google.common.collect.Maps;

public class MarshalledObject {

	private byte[] key;
	private byte[] superColumn;
	private final Map<String, byte[]> values = Maps.newHashMap();

	public static MarshalledObject create() {
		return new MarshalledObject();
	}

	public void setKey(final byte[] value) {
		key = value;
	}

	public void addValue(final String name, final byte[] value) {
		if (values.put(name, value) != null) {
			throw new CassandraException("Property with name " + name
					+ " had already" + " a value, overwriting is illegal");
		}
	}

	public byte[] getKey() {
		return key;
	}

	public Set<Map.Entry<String, byte[]>> getEntries() {
		return Collections.unmodifiableSet(values.entrySet());
	}

	public void setSuperColumn(final byte[] superColumn) {
		this.superColumn = superColumn;
	}

	public byte[] getSuperColumn() {
		return superColumn;
	}

	public boolean isSuperColumnPresent() {
		return superColumn != null;
	}

}
