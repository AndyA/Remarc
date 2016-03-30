package com.bbc.remarc.ws;

import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.bbc.remarc.db.MongoClient;
import com.bbc.remarc.util.Configuration;
import com.bbc.remarc.util.ResourceManager;
import com.bbc.remarc.util.ResourceManager.ResourceType;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;

@Path("/audio")
public class AudioServiceImpl extends MetadataService implements AudioService {

	private static Logger log = Logger.getLogger(AudioServiceImpl.class
			.getSimpleName());

	private static final String COLLECTION_NAME = "audio";

	@Context
	private ServletContext servletContext;
	
	@GET
	@Produces("application/json")
	@Path("/metadata/{id}")
	public Response getAudioMetadata(@PathParam("id") String id) {
		log.trace("getAudioMetadata");

		DB db = MongoClient.getDB();
		DBCursor results = null;

		try {
			db.requestStart();
			results = db.getCollection(COLLECTION_NAME).find(
					BasicDBObjectBuilder.start().add("id", id).get());

			if (results.count() == 0) {
				return Response.status(404).build();
			}

		} finally {
			db.requestDone();
		}

		return Response.ok(results.next().toString()).build();
	}

	@POST
	@Consumes("application/json")
	@Produces("text/html")
	@Path("/metadata/{id}")
	public Response updateAudioMetadata(@PathParam("id") String id, HashMap<String, String> json) {
		log.trace("updateAudioMetadata");
		
		String theme = json.get("theme");
		String decade = json.get("decade");
		
		DB db = MongoClient.getDB();
		
		Response retVal = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		
		try {
			db.requestStart();
			
			//check the record exists
			DBCursor results = db.getCollection(COLLECTION_NAME).find(
					BasicDBObjectBuilder.start().add("id", id).get());
			
			if (results.count() == 1) {
			
				DBObject obj = results.next();
				
				if (theme != null) {
					obj.put("theme", theme);
				}
				if (decade != null) {
					obj.put("decade", decade);
				}
				
				db.getCollection(COLLECTION_NAME).update(BasicDBObjectBuilder.start().add("id", id).get(), obj);
				
				retVal = Response.status(Status.OK).build();
				
			}
			
		} catch (MongoException e) {
			log.error("ERROR! Update of [" + id + "] failed with:" + e);
			
		} finally {
			db.requestDone();
		}
		
		return retVal;
	}
	
	@DELETE
	@Produces("text/html")
	@Path("/metadata/{id}")
	public Response deleteAudioMetadata(@PathParam("id") String id) {
		log.trace("deleteAudioMetadata");
		
		DB db = MongoClient.getDB();
		
		Response retVal = Response.status(Status.INTERNAL_SERVER_ERROR).build();
		
		try {
			db.requestStart();
			
			//check the record exists
			DBCursor results = db.getCollection(COLLECTION_NAME).find(
					BasicDBObjectBuilder.start().add("id", id).get());
			
			if (results.count() == 1) {
			
				log.debug("found record " + id);
				
				//try to delete the files
				String resourcePath = (String)servletContext.getAttribute(Configuration.ATT_DATA_DIR);
				String contentPath = resourcePath + Configuration.CONTENT_DIR;
			
				boolean success = ResourceManager.deleteResourceForId(contentPath, ResourceType.AUDIO, id);
				if (success) {
					
					//using WriteConcern safe means we'll get an exception if this fails.
					db.getCollection(COLLECTION_NAME).remove(
							BasicDBObjectBuilder.start().add("id", id).get(), WriteConcern.SAFE);

					log.debug("mongo record deleted");
					
					retVal = Response.status(Status.OK).build();
				}
				
			} else {
				log.error("ERROR! Didn't delete [" + id + "], found more than 1 record [" + results.count() + "]");
			}
			
		} catch (MongoException e) {
			log.error("ERROR! Delete of [" + id + "] failed with:" + e);
			
		} finally {
			db.requestDone();
		}
		
		return retVal;
	}
	
	@GET
	@Produces("application/json")
	@Path("/find")
	public Response find(@QueryParam("limit") Long limit,
			@QueryParam("decade") String decade,
			@QueryParam("theme") String theme) {

		log.trace("find");

		List<DBObject> results = findMetadata(MongoClient.getDB(),
				COLLECTION_NAME, decade, theme, limit);

		return Response.ok(String.format("%s", results)).build();
	}

}
