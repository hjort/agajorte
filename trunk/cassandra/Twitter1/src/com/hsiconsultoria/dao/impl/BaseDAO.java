package com.hsiconsultoria.dao.impl;

import org.apache.cassandra.thrift.Cassandra;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.hsiconsultoria.dao.IBaseDAO;

public abstract class BaseDAO implements IBaseDAO {

	private static TTransport tr = null;

	/**
	 * Open up a new connection to the Cassandra Database.
	 * 
	 * @return the Cassandra Client
	 */
	private static Cassandra.Client setupConnection() {
		try {
			tr = new TSocket("localhost", 9160);
			TProtocol proto = new TBinaryProtocol(tr);
			Cassandra.Client client = new Cassandra.Client(proto);
			tr.open();
			return client;
		} catch (TTransportException e) {
			throw new RuntimeException("Error opening the connection: ", e);
		}
	}

	/**
	 * Close the connection to the Cassandra Database.
	 */
	private static void closeConnection() {
		try {
			tr.flush();
			tr.close();
		} catch (TTransportException e) {
			throw new RuntimeException("Error closing the connection: ", e);
		}
	}

	protected Cassandra.Client client;

	@Override
	public void startup() {
		this.client = setupConnection();
	}

	@Override
	public void shutdown() {
		closeConnection();
	}

}
