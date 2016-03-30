package com.bbc.remarc.ws;

import java.util.HashMap;

import javax.ws.rs.core.Response;

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
