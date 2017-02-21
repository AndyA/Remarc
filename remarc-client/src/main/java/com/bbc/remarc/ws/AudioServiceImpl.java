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

package com.bbc.remarc.ws;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.bbc.remarc.db.MongoClient;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

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
