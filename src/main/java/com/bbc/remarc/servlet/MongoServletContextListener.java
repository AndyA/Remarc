package com.bbc.remarc.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.bbc.remarc.db.MongoClient;
import com.bbc.remarc.util.DummyDataGenerator;

/**
 * Servlet context listener to start and shutdown 
 * mongo client instance
 * @author a171581
 *
 */
public class MongoServletContextListener implements ServletContextListener {
	
	private final static Logger log = Logger.getLogger(MongoServletContextListener.class);

	public void contextInitialized(ServletContextEvent event) {		
		log.debug("contextInitialized");			
		MongoClient.connect();		
		
		//DummyDataGenerator.saveMetaData();
		
	}
	

	public void contextDestroyed(ServletContextEvent arg0) {
		log.debug("contextDestroyed");		
		MongoClient.disconnect();
	}
 
}
