package sample.domain;

import br.gov.frameworkdemoiselle.cassandra.annotation.CassandraColumn;
import br.gov.frameworkdemoiselle.cassandra.annotation.ColumnProperty;
import br.gov.frameworkdemoiselle.cassandra.annotation.KeyProperty;
import br.gov.frameworkdemoiselle.cassandra.annotation.ValueProperty;

@CassandraColumn(columnFamily = "Standard1", secondaryColumnFamily = "Standard2")
public class Column {

	@KeyProperty
	private Long id;
	
	@ColumnProperty
	private String column;
	
	@ValueProperty
	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Column [id=" + id + ", column=" + column + ", value=" + value + "]";
	}
	
}
