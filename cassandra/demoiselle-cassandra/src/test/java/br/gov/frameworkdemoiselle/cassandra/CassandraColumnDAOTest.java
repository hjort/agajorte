package br.gov.frameworkdemoiselle.cassandra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sample.domain.Column;
import sample.persistence.ColumnDAO;

public class CassandraColumnDAOTest extends BaseEmbededServerSetupTest {

	private static Logger log = Logger.getLogger(CassandraColumnDAOTest.class);
	
	private ColumnDAO dao;
	
	@Before
	public void setUp() throws Exception {
		dao = new ColumnDAO();
	}

	@After
	public void tearDown() throws Exception {
		dao = null;
	}

	@Test
	public void testSave() {
		Column col = createColumn();
		
		log.debug("Saving column: " + col);
		dao.save(col);
		assertTrue(true);

		List<String> cols = dao.getColumns(col.getId().toString());
		assertFalse(cols.isEmpty());
		assertEquals(1, cols.size());
		assertEquals("name", cols.get(0));
		
		dao.delete(col);
	}

	@Test
	public void testDelete() {
		Column col = createColumn();
		dao.save(col);
		assertTrue(true);

		final String key = col.getId().toString();
		List<String> values = dao.getValues(key);
		assertFalse(values.isEmpty());
		
		log.debug("Deleting column: " + col);
		dao.delete(col);
		assertTrue(true);
		
		values = dao.getValues(key);
		assertTrue(values.isEmpty());
	}

	@Test
	public void testGetColumns() {
		saveColumns();
		
		List<String> cols = dao.getColumns("2");
		assertFalse(cols.isEmpty());
		assertEquals(3, cols.size());
		assertEquals("ca", cols.get(0));
		assertTrue(cols.contains("cb"));
		 
		removeColumns();
	}

	@Test
	public void testGetColumnsBySecondary() {
		saveColumns();
		
		List<String> cols = dao.getColumnsBySecondary("ca");
		assertFalse(cols.isEmpty());
		assertEquals(9, cols.size());
		assertEquals("1", cols.get(0));
		assertTrue(cols.contains("3"));
		
		removeColumns();
	}

	@Test
	public void testGetValues() {
		saveColumns();

		List<String> values = dao.getValues("2");
		assertFalse(values.isEmpty());
		assertEquals(3, values.size());
		assertEquals("2 ca", values.get(0));
		assertTrue(values.contains("2 cb"));

		removeColumns();
	}

	private Column createColumn(int key, String name) {
		Column col = new Column();
		col.setId((long) key);
		col.setColumn(name);
		col.setValue(key + " " + name);
		return col;
	}

	private Column createColumn() {
		int key = (int) (Math.random() * 1E3);
		String name = "name";
		return createColumn(key, name);
	}
	
	private void saveColumns() {
		log.debug("Saving columns:");
		for (int i = 1; i < 10; i++) {
			for (char c = 'a'; c < 'd'; c++) {
				Column column = createColumn(i, "c" + c);
				log.debug(column);
				dao.save(column);
			}
		}
	}

	private void removeColumns() {
		for (int i = 1; i < 10; i++) {
			for (char c = 'a'; c < 'd'; c++) {
				Column column = createColumn(i, "c" + c);
				dao.delete(column);
			}
		}
	}

}
