package br.gov.frameworkdemoiselle.cassandra.mapping;

public class FloatTypeMapping extends AbstractStringBasedTypeMapping<Float> {

	protected String asString(final Float value) {
		return value.toString();
	}

	protected Float fromString(final String string) {
		return Float.valueOf(string);
	}

}
