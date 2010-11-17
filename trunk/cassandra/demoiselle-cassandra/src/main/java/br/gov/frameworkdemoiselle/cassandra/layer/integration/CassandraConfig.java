package br.gov.frameworkdemoiselle.cassandra.layer.integration;

import org.apache.cassandra.thrift.ConsistencyLevel;

import br.gov.framework.demoiselle.util.Constant;
import br.gov.framework.demoiselle.util.config.ConfigKey;
import br.gov.framework.demoiselle.util.config.ConfigType;
import br.gov.framework.demoiselle.util.config.ConfigurationLoader;
import br.gov.framework.demoiselle.util.config.IConfig;

public class CassandraConfig implements IConfig {

	private static final long serialVersionUID = 1L;

	private static CassandraConfig instance;
	
	@ConfigKey(name = "demoiselle.cassandra.server_nodes",
			type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String[] serverNodes;

	@ConfigKey(name = "demoiselle.cassandra.default_keyspace",
			type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String defaultKeyspace;

	@ConfigKey(name = "demoiselle.cassandra.default_consistency",
			type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String defaultConsistency = ConsistencyLevel.QUORUM.name();

	@ConfigKey(name = "demoiselle.cassandra.serialize_unknown",
			type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private boolean serializeUnknown;
	
	/**
	 * Returns the single instance of this configuration class.
	 * 
	 * @return	CassandraConfig
	 */
	public static synchronized CassandraConfig getInstance() {
		
		if (instance == null) {
			instance = new CassandraConfig();
			ConfigurationLoader.load(instance);
		}
		
		return instance;
	}

	public String[] getServerNodes() {
		return serverNodes;
	}

	public String getDefaultKeyspace() {
		return defaultKeyspace;
	}

	public ConsistencyLevel getDefaultConsistency() {
		return ConsistencyLevel.valueOf(defaultConsistency);
	}

	public boolean isSerializeUnknown() {
		return serializeUnknown;
	}
	
}
