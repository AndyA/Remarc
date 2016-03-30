package com.bbc.remarc.ws;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.bbc.remarc.db.MongoClient;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Path("/theme")
public class ThemeServiceImpl implements ThemeService {

	private static Logger log = Logger.getLogger(ThemeServiceImpl.class
			.getSimpleName());

	private static final String COLLECTION_NAME = "theme";
	private static final int NUM_THEMES = 10;

	@Context
	private ServletContext servletContext;
	
	
	@PUT
	@Consumes("application/json")
	@Produces("text/html")
	public Response updateThemes(List<String> json) {
		log.trace("updateAudioMetadata");
		
		if (json.size() != NUM_THEMES) {
			throw new WebApplicationException(Response
					.status(HttpURLConnection.HTTP_BAD_REQUEST)
					.entity("list of themes must contain 10 elements exactly").build());
		}
			
		DB db = MongoClient.getDB();
		db.requestStart();
		
		//clear the theme database down
		DBCollection theme = db.getCollection(COLLECTION_NAME);
		theme.remove(new BasicDBObject()); 

		//re-insert all themes
		for (String themeName : json) {
				
			theme.insert(BasicDBObjectBuilder.start()
				.add("name", themeName)
				.get());
		}
			
		db.requestDone();
		
		return Response.ok().build();
	}
	
	@GET
	@Produces("application/json")
	public Response find() {
		
		List<DBObject> results = new ArrayList<DBObject>();

		DB db = MongoClient.getDB();
		db.requestStart();
		
		DBCollection theme = db.getCollection(COLLECTION_NAME);
		DBCursor cursor = theme.find().limit(NUM_THEMES);
		
		if (cursor.length() != NUM_THEMES) {
			throw new WebApplicationException(Response
					.status(HttpURLConnection.HTTP_INTERNAL_ERROR)
					.entity("unexpected number of themes returned").build());
		}
		
		results = cursor.toArray();
		
		db.requestDone();

		return Response.ok(String.format("%s", results)).build();
	}
}
