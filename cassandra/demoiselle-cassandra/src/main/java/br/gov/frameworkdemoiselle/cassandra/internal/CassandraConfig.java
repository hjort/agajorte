package br.gov.frameworkdemoiselle.cassandra.internal;

import br.gov.framework.demoiselle.util.Constant;
import br.gov.framework.demoiselle.util.config.ConfigKey;
import br.gov.framework.demoiselle.util.config.ConfigType;
import br.gov.framework.demoiselle.util.config.ConfigurationLoader;
import br.gov.framework.demoiselle.util.config.IConfig;

public class CassandraConfig implements IConfig {

	private static final long serialVersionUID = 1L;

	private static CassandraConfig instance;
	
	@ConfigKey(name = "component.demoiselle.cassandra.server_nodes",
			type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String[] serverNodes;

	@ConfigKey(name = "component.demoiselle.cassandra.default_keyspace",
			type = ConfigType.PROPERTIES, resourceName = Constant.FRAMEWORK_CONFIGURATOR_FILE)
	private String defaultKeyspace;

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
	
}
