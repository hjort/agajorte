package br.gov.frameworkdemoiselle.cassandra;

import org.apache.cassandra.thrift.ConsistencyLevel;

import me.prettyprint.cassandra.dao.Command;
import me.prettyprint.cassandra.service.CassandraClient;

public abstract class AbstractHelenaDAO {

    private String hostname;
    private int port;
	private String[] nodes;

    private String keyspace;
    private String columnFamily;

	private ConsistencyLevel consistencyLevel = CassandraClient.DEFAULT_CONSISTENCY_LEVEL;

	/*
    public AbstractHelenaDAO(String keyspace, String columnFamily,
			ConsistencyLevel consistencyLevel, String hostname, int port, String[] nodes) {
		this.keyspace = keyspace;
		this.columnFamily = columnFamily;
		this.consistencyLevel = consistencyLevel;
		this.hostname = hostname;
		this.port = port;
		this.nodes = nodes;
	}

    public AbstractHelenaDAO(String keyspace, String columnFamily,
			ConsistencyLevel consistencyLevel, String hostname, int port) {
    	this(keyspace, columnFamily, consistencyLevel, hostname, port, null);
	}

    public AbstractHelenaDAO(String keyspace, String columnFamily,
			ConsistencyLevel consistencyLevel, String[] nodes) {
    	this(keyspace, columnFamily, consistencyLevel, null, 0, nodes);
	}
	*/
	
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
