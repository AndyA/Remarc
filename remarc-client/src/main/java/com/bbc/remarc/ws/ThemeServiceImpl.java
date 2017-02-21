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

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.bbc.remarc.db.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Path("/theme")
public class ThemeServiceImpl implements ThemeService {

	private static Logger log = Logger.getLogger(ThemeServiceImpl.class
			.getSimpleName());

	private static final String COLLECTION_NAME = "theme";
	private static final int NUM_THEMES = 8;

	@Context
	private ServletContext servletContext;
	
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
