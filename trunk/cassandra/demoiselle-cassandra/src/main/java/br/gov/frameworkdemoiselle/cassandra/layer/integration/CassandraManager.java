package br.gov.frameworkdemoiselle.cassandra.layer.integration;

import org.apache.log4j.Logger;

import br.gov.frameworkdemoiselle.cassandra.persistence.TypeMapping;

import com.google.common.collect.ImmutableMap;

public class CassandraManager {

	private static Logger log = Logger.getLogger(CassandraManager.class);
	
	private static final CassandraManager instance = new CassandraManager();
	
//	private Map<Class<?>, SessionFactory> sfs = new HashMap<Class<?>, SessionFactory>();
	
	// TODO: ler e armazenar configurações default
	protected boolean defaultSerializeUnknownClasses;
	protected ImmutableMap<Class<?>, TypeMapping<?>> typeMappings;

	/*
    private String hostname;
    private int port;
	private String[] nodes;

    protected String defaultKeyspace;
    protected ConsistencyLevel consistencyLevel;
    */
	
	private CassandraManager() {
		log.debug("CassandraManager created");
	}

	/*
	public SessionFactory getSessionFactory(String configName) {
		
		if (sfs.containsKey(configName)) {
			return sfs.get(configName);
		} else {
			log.debug("Creating session factory for config name \"" + configName + "\"");
			
			Configuration cfg = new AnnotationConfiguration();
			
			if (configName==null || "".equals(configName)) {
				cfg.configure();
			} else {
				String configFile = "hibernate-"+configName+".cfg.xml";
				
				log.debug("Load hibernate configuration from "+configFile);
				
				cfg.configure(configFile);
			}

			SessionFactory sessionFactory = cfg.buildSessionFactory();

			sfs.put(configName, sessionFactory);
			
			return sessionFactory;
		}
	}
	*/

	/**
	 * Returns the single instance for the handler (singleton approach).
	 * 
	 * @return CassandraManager
	 */
	public static synchronized CassandraManager getInstance() {
		return instance;
	}
	
}
