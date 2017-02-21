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

package com.bbc.remarc.db;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.bbc.remarc.util.Configuration;
import com.mongodb.DB;
import com.mongodb.Mongo;

/**
 * Singleton instance of Mongo connection pool. 
 * Initialised as ServletContextListener
 *
 * @author Peter Brock
 */
public class MongoClient {

	private static Logger log = Logger.getLogger(MongoClient.class);

	private static Mongo mongo;
	private static DB mongoDB;

	private static boolean authenticated;
	private static String username;
	private static String password;
	private static String db;
	private static String host;
	private static String portStr;
	private static Integer port;
	
	private MongoClient() {
	}

	/**
	 * Connect to mongo instance using env variables if present
	 * or fall back to local instance
	 * @return
	 * @throws UnknownHostException
	 */
	public static synchronized Mongo connect() {

		if (mongo == null) {

			log.debug("Creating new MongoDB connection");
			
			host = System.getenv(Configuration.ENV_MONGO_HOST_OPENSHIFT);
			if (host == null || "".equals(host)) {
				log.info("Not running on OpenShift. Falling back to local mongodb");

				username = System.getenv(Configuration.ENV_MONGO_USER_LOCAL);
				password = System.getenv(Configuration.ENV_MONGO_PWD_LOCAL);
				db = System.getenv(Configuration.ENV_MONGO_DB_LOCAL);
				host = System.getenv(Configuration.ENV_MONGO_HOST_LOCAL);
				portStr = System.getenv(Configuration.ENV_MONGO_PORT_LOCAL);

				if (username == null || "".equals(username)) {
					log.debug("No username found in local environment variables, assuming default");
					username = "remarc";
				}
				
				if (password == null || "".equals(password)) {
					log.debug("No password found in local environment variables, assuming default");
					password = "dementia";
				}
				
				if (db == null || "".equals(db)) {
					log.debug("No database found in local environment variables, assuming default");
					db = "remarc";
				}

				if (host == null || "".equals(host)) {
					log.debug("No host found in local environment variables, assuming default");
					host = "localhost";
				}

				if (portStr == null || "".equals(portStr)) {
					log.debug("No port found in local environment variables, assuming default");
					portStr = "27017";
				}
				
				port = Integer.decode(portStr);
				
			} else {
				
				log.info("Running on OpenShift. Configuring connection");

				portStr = System.getenv(Configuration.ENV_MONGO_PORT_OPENSHIFT);				
				port = Integer.decode(portStr);
				
				username = System.getenv(Configuration.ENV_MONGO_USER_OPENSHIFT);
				password = System.getenv(Configuration.ENV_MONGO_PWD_OPENSHIFT);				
				db = System.getenv(Configuration.ENV_MONGO_DB_OPENSHIFT);
				
			}

			try {
				mongo = new Mongo(host, port);
			} catch (UnknownHostException ex) {
				throw new RuntimeException("Failed to connect to db", ex);
			}
			
		}
		
		return mongo;
	}
	
	/**
	 * Get hold of the remarc database instance
	 * @return
	 */
	public static synchronized DB getDB() {
		
		if (mongoDB == null) {

			if (mongo == null) {

				connect();
			}

			log.debug("Retrieving database");
			mongoDB = mongo.getDB(db);

			// only authenticate if we're not already authenticated
			if (!authenticated) {

				log.debug("Not authenticated against database. Authenticating now.");

				authenticated = mongoDB.authenticate(username, password.toCharArray());

				if (!authenticated) {
					throw new RuntimeException("Failed to authenticate against db");
				}
			}

		}
		
		return mongoDB;
		
	}
	
	/**
	 * Shutdown the mongo instance
	 */
	public static void disconnect() {
		
		log.info("Disconnecting mongo client");
		if (mongo != null) {
			mongo.close();
		}
	}
}
