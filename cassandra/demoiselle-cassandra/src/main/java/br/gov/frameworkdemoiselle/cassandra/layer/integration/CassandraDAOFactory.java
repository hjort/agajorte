package br.gov.frameworkdemoiselle.cassandra.layer.integration;

import org.apache.log4j.Logger;

import br.gov.framework.demoiselle.core.layer.integration.IFactory;
import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.gov.framework.demoiselle.core.layer.integration.InjectionContext;
import br.gov.framework.demoiselle.util.layer.integration.GenericFactory;
import br.gov.frameworkdemoiselle.cassandra.CassandraDAO;
import br.gov.frameworkdemoiselle.cassandra.CassandraException;

public class CassandraDAOFactory extends GenericFactory implements IFactory<CassandraDAO<?>> {

	private static Logger log = Logger.getLogger(CassandraDAOFactory.class);
	
	public CassandraDAO<?> create(InjectionContext ctx) {
		
		Injection injection = ctx.getInjection();
		
		// TODO: ...
		/*
		String configName = "";
		if (!injection.name().equals("")) {
			configName = injection.name();
		} else {
			if (ctx.getClassType().isAnnotationPresent(DefaultConfigName.class)) {
				DefaultConfigName pu = ctx.getClassType().getAnnotation(DefaultConfigName.class);
				configName = pu.name();
			} else if (ctx.getClassType().getPackage().isAnnotationPresent(DefaultConfigName.class)) {
				DefaultConfigName pu = ctx.getClassType().getPackage().getAnnotation(DefaultConfigName.class);
				configName = pu.name();
			}
		}
		
		SessionFactory sessionFactory = ConfigurationHandler.getInstance().getSessionFactory(configName);
		*/
		
//		log.debug("Creating " + Session.class.getName() + (configName.equals("")?"":" for config name " + configName));
		log.debug("Creating " + CassandraDAO.class.getName() + " for injection " + injection.toString());
		
		/*
		return createWithLazyCreateProxy(CassandraDAOProxy.class, 
				new Class[] {SessionFactory.class, String.class }, 
				new Object[] {sessionFactory, configName});
		*/
		
		String className = null;
//		className = conventionForClassName(ctx, );
		/*
		if (config.hasDAO()) {
			className = conventionForClassName(ctx, config.getRegexDAO(), config.getReplaceDAO());
		} else {
			className = conventionForClassName(ctx, config.getRegex(), config.getReplace());
		}
		*/
		
		try {
			log.debug("Creating DAO from class "+ className);
			Class<?> clazz = Class.forName(className);
			return (CassandraDAO<?>) createWithLazyCreateProxy(clazz);
		} catch (Exception e) {
			throw new CassandraException("Could not instantiate class \"" + className + "\"", e);
		}
		
		// TODO: instantiate a proxy class instead
//		return createWithLazyCreateProxy(CassandraEntityDAOProxy.class);
	}

}
