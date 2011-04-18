package br.gov.frameworkdemoiselle.cassandra;

import java.io.IOException;

import org.apache.thrift.transport.TTransportException;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import me.prettyprint.cassandra.service.CassandraClientPool;
import me.prettyprint.cassandra.service.CassandraClientPoolFactory;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.testutils.EmbeddedServerHelper;

/**
 * Base class for test cases that need access to EmbeddedServerHelper
 * 
 * @author Nate McCall (nate@vervewireless.com)
 */
public class BaseEmbededServerSetupTest {

	private static EmbeddedServerHelper embedded;

	protected CassandraClientPool pools;
	protected CassandraHostConfigurator cassandraHostConfigurator;

	/**
	 * Set embedded cassandra up and spawn it in a new thread.
	 * 
	 * @throws TTransportException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@BeforeClass
	public static void setup() throws TTransportException, IOException, InterruptedException {
		embedded = new EmbeddedServerHelper();
		embedded.setup();
	}

	@AfterClass
	public static void teardown() throws IOException {
		embedded.teardown();
		embedded = null;
	}

	protected void setupClient() {
		cassandraHostConfigurator = new CassandraHostConfigurator("127.0.0.1:9170");
		pools = CassandraClientPoolFactory.INSTANCE.createNew(cassandraHostConfigurator);
	}

}
