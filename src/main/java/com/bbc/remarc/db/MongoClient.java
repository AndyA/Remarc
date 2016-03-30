package com.bbc.remarc.db;

import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.bbc.remarc.util.Configuration;
import com.mongodb.DB;
import com.mongodb.Mongo;

/**
 * Singleton instance of Mongo connection pool. 
 * Initialised as ServletContextListener
 * @author a171581
 *
 */
public class MongoClient {

	private static Logger log = Logger.getLogger(MongoClient.class);

	private static Mongo mongo;
	
	private static boolean local;
	
	private static boolean authenticated;

	private MongoClient() {
	}

	/**
	 * Connect to mongo instance using env variables if present
	 * or fall back to local instance
	 * @return
	 * @throws UnknownHostException
	 */
	public static Mongo connect() {

		if (mongo == null) {

			log.debug("Creating new MongoDB connection");
			
			String host = System.getenv(Configuration.ENV_MONGO_HOST_OPENSHIFT);
			String mongoport = "27017";
						
			int port = Integer.decode(mongoport);

			if (host == null || "".equals(host)) {
				log.info("Not running on OpenShift. Falling back to local mongodb");
				local = true;
			} else {

				log.debug("Retrieved host from OpenShift");

				mongoport = System.getenv("OPENSHIFT_MONGODB_DB_PORT");				
				port = Integer.decode(mongoport);
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
	public static DB getDB() {
		
		if (mongo == null) {
			
			connect();
		}
		
		String user = System.getenv(Configuration.ENV_MONGO_USER_OPENSHIFT);
		String password = System.getenv(Configuration.ENV_MONGO_PWD_OPENSHIFT);				
		String db = System.getenv(Configuration.ENV_MONGO_DB_OPENSHIFT);
		
		if (local) {
			user = System.getenv(Configuration.ENV_MONGO_USER_LOCAL);
			password = System.getenv(Configuration.ENV_MONGO_PWD_LOCAL);
			db = System.getenv(Configuration.ENV_MONGO_DB_LOCAL);
			
			if (db == null || "".equals(db)) {
				log.debug("No database found in environment variables, assuming \"remarc\"");
				db = "remarc";
			}
		}
		
		log.debug("Retrieving database [" + db + "]");
		DB mongoDB = mongo.getDB(db);
		
		//only authenticate if we're not already authenticated and not running locally (local auth OFF)
		if (!authenticated && !local) {
			
			log.debug("Not authenticated against database and not running locally. Authenticating now.");
			authenticated = mongoDB.authenticate(user, password.toCharArray());
			
			if (!authenticated) {
				throw new RuntimeException("Failed to authenticate against db");
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
