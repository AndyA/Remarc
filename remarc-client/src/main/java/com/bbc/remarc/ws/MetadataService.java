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
import java.util.Random;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

/**
 * Base service class for metadata
 * Common find() logic defined here
 * @author a171581
 *
 */
public abstract class MetadataService {

	/**
	 * Core find method for retrieving metadata for 
	 * media 
	 * @param db mongodb connection
	 * @param dbCollection collection name
	 * @param decade decade (optional)
	 * @param theme theme (optional)
	 * @param limit limit (required)
	 * @return results
	 */
	protected List<DBObject> findMetadata(DB db, 
			final String dbCollection,
			String decade, String theme, Long limit) {
		DBObject searchFilter = null;

		if (limit == null) {
			throw new WebApplicationException(Response
					.status(HttpURLConnection.HTTP_BAD_REQUEST)
					.entity("limit parameter is mandatory").build());
		}

		List<DBObject> results = new ArrayList<DBObject>();

		try {

			db.requestStart();

			if (decade != null || theme != null) {
				searchFilter = new BasicDBObject();
			}

			if (decade != null) {
				searchFilter.put("decade", decade);
			}

			if (theme != null) {
				searchFilter.put("theme", theme);
			}

			results = findRandom(db, limit, searchFilter, dbCollection);
		} finally {
			db.requestDone();
		}
		
		return results;
	}
	
	/**
	 * Core random find service for entities that have metadata
	 * 
	 * @param db
	 *            db instance
	 * @param limit
	 *            number of records to find
	 * @param filter
	 *            find records that match this filter. Can be null for no
	 *            filtering
	 * @param collectionName
	 *            the database collection name
	 * @return collection of found DBObjects
	 */
	protected List<DBObject> findRandom(DB db, long limit, DBObject filter,
			final String collectionName) {

		List<DBObject> results = new ArrayList<DBObject>();

		try {
			db.requestStart();
			Random random = new Random();
			DBCollection dbCollection = db.getCollection(collectionName);
			
			long maxRecords = dbCollection.count();
			
			if (limit > maxRecords) {
				limit = maxRecords;
			}

			for (int i = 0; i < limit; i++) {
	
				boolean foundUnique = false;

				int numChecked = 0;
				
				while (!foundUnique) {

					if (numChecked == limit) {
						break;
					}

					int randomNumber = random.nextInt((int) dbCollection.count());
					
					DBObject randomRecord = null;

					if (filter == null) {
						randomRecord = dbCollection.find().limit(-1)
								.skip(randomNumber).next();
					} else {

						DBCursor cursor = dbCollection.find(filter).limit(-1);

						if (i >= cursor.count()) {
							// we dont have enough records from the filter to
							// match the limit
							break;
						}

						randomNumber = random.nextInt((int) cursor.count());

						randomRecord = cursor.skip(randomNumber).next();
					}

					foundUnique = resultsContains(randomRecord, results) == false;

					if (foundUnique) {
						randomRecord.removeField("_id");
						results.add(randomRecord);
					}
				}
			}
		} finally {
			db.requestDone();
		}
		return results;
	}

	/**
	 * Determine if db object already exists in the provided collection. 
	 * @param newObj object to check 
	 * @param results collection to determine if newObj exists
	 * @return true if it does false if not.
	 */
	private boolean resultsContains(DBObject newObj, List<DBObject> results) {
		
		boolean result = false;
		
		for (DBObject obj : results) {			
			if (obj.get("id").equals(newObj.get("id"))) {
				return true;
			}
		}
		
		return result;
	}

}
