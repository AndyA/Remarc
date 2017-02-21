/*
 * Copyright (C) 2016 BBC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bbc.remarc.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.bbc.remarc.db.MongoClient;
import com.bbc.remarc.util.DummyDataGenerator;

/**
 * Servlet context listener to start and shutdown 
 * mongo client instance
 *
 * @author Peter Brock
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
