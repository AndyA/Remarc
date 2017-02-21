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

import java.util.HashMap;

import javax.ws.rs.core.Response;

/**
 * Service that exposes endpoints to perform CRUD operations with image resources.
 *
 * @author Peter Brock
 * @author Mark Fearnley
 */
public interface ImageService {
	
	/**
	 * Retrieve image metadata
	 * @param id identifier
	 * @return image metadata (JSON)
	 */
	public Response getImageMetadata(String id);
	
	/**
	 * Deletes image metadata (and associated resources)
	 * @param id identifier
	 * @return HTTP response
	 */
	public Response deleteImageMetadata(String id);
	
	/**
	 * Updates image metadata
	 * @param id identifier
	 * @param json JSON request data parsed into HashMap
	 * @return HTTP response
	 */
	public Response updateImageMetadata(String id, HashMap<String, String> json);
	
	/**
	 * Search for random image metadata  
	 * @param limit maximum to return
	 * @param decade decade of image
	 * @param theme theme category of image
	 * @return collection of metadata as JSON 
	 */
	public Response find(Long limit,
			String decade,
			String theme);
			
}
